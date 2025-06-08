package com.dubey.dubey_porfolio_java.service;

import java.util.Map;

public interface  DubeyService {
     String getHello();

    String postHello(Map<String, String> body);

    String putHello(Map<String, String> body);

    String deleteHello();

    String patchHello(Map<String, String> body);
}
