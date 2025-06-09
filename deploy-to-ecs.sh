#!/bin/bash

set -e

# Set environment variables (update if needed)
AWS_REGION="us-east-1"
ECR_REPO_NAME="dubey-portfolio-ecr-repo"
CLUSTER_NAME="dubey-portfolio-ecs-cluster"
TASK_DEF_NAME="dubey-portfolio-task-def"
SERVICE_NAME="dubey-portfolio-ecs-service"
CONTAINER_NAME="dubey-portfolio-container"
IMAGE_TAG="latest"
DESIRED_COUNT=1

# === Fetch AWS Account ID ===
AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)

# === Get default VPC ID ===
VPC_ID=$(aws ec2 describe-vpcs \
  --filters "Name=isDefault,Values=true" \
  --query "Vpcs[0].VpcId" \
  --region "$AWS_REGION" \
  --output text)

echo "Default VPC ID: $VPC_ID"

# === Get two subnet IDs from default VPC ===
SUBNET_IDS=($(aws ec2 describe-subnets \
  --filters "Name=vpc-id,Values=$VPC_ID" \
  --query "Subnets[*].SubnetId" \
  --region "$AWS_REGION" \
  --output text))

SUBNET_ID_1=${SUBNET_IDS[0]}
SUBNET_ID_2=${SUBNET_IDS[1]}

echo "Subnets: $SUBNET_ID_1, $SUBNET_ID_2"

# === Get the default Security Group from the default VPC ===
SG_ID=$(aws ec2 describe-security-groups \
  --filters "Name=vpc-id,Values=$VPC_ID" "Name=group-name,Values=default" \
  --query "SecurityGroups[0].GroupId" \
  --region "$AWS_REGION" \
  --output text)


echo "Security Group ID: $SG_ID"

# === Ensure ECS Task Execution Role Exists ===
ROLE_NAME="ecsTaskExecutionRole"
EXECUTION_ROLE_ARN="arn:aws:iam::${AWS_ACCOUNT_ID}:role/${ROLE_NAME}"

ROLE_EXISTS=$(aws iam get-role --role-name $ROLE_NAME --query "Role.Arn" --output text 2>/dev/null || echo "none")
if [[ "$ROLE_EXISTS" == "none" ]]; then
  echo "Creating IAM Role for ECS Task Execution..."

  cat > trust-policy.json <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "ecs-tasks.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOF

  aws iam create-role \
    --role-name $ROLE_NAME \
    --assume-role-policy-document file://trust-policy.json

  aws iam attach-role-policy \
    --role-name $ROLE_NAME \
    --policy-arn arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy

  echo "Waiting for IAM role propagation..."
  sleep 10

  rm -f trust-policy.json
else
  echo "IAM Role already exists: $EXECUTION_ROLE_ARN"
fi

# === Step 1: Create ECR repository ===
echo "Creating ECR repository..."
aws ecr create-repository \
  --repository-name "$ECR_REPO_NAME" \
  --region "$AWS_REGION" || echo "ECR repo may already exist."

# === Step 2: Create ECS Cluster ===
echo "Creating ECS Cluster..."
aws ecs create-cluster \
  --cluster-name "$CLUSTER_NAME" \
  --region "$AWS_REGION" || echo "Cluster may already exist."

# === Step 3: Create ECS Task Definition JSON ===
echo "Generating ECS Task Definition JSON..."
cat > task-def.json <<EOL
{
  "family": "$TASK_DEF_NAME",
  "executionRoleArn": "$EXECUTION_ROLE_ARN",
  "networkMode": "awsvpc",
  "requiresCompatibilities": ["FARGATE"],
  "cpu": "256",
  "memory": "512",
  "containerDefinitions": [
    {
      "name": "$CONTAINER_NAME",
      "image": "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO_NAME}:${IMAGE_TAG}",
      "essential": true,
      "portMappings": [
        {
          "containerPort": 8080,
          "protocol": "tcp"
        }
      ]
    }
  ]
}
EOL

echo "Task definition saved to task-def.json"

# === Register ECS Task Definition ===
echo "Registering ECS Task Definition..."
aws ecs register-task-definition \
  --cli-input-json file://task-def.json \
  --region "$AWS_REGION"

# === Create ECS Service ===
echo "Creating ECS Service..."
aws ecs create-service \
  --cluster "$CLUSTER_NAME" \
  --service-name "$SERVICE_NAME" \
  --task-definition "$TASK_DEF_NAME" \
  --desired-count "$DESIRED_COUNT" \
  --launch-type FARGATE \
  --network-configuration "awsvpcConfiguration={subnets=[$SUBNET_ID_1,$SUBNET_ID_2],securityGroups=[$SG_ID],assignPublicIp=ENABLED}" \
  --region "$AWS_REGION"

echo "✅ Deployment complete!"
echo "📦 Task definition JSON stored in: task-def.json"
