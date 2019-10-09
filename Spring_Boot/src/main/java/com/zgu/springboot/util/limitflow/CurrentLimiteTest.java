package com.zgu.springboot.util.limitflow;

import org.springframework.web.client.RestTemplate;

public class CurrentLimiteTest {
    public static void main(String[] args) {
        final String limitUrlPath = "http://localhost:8080/zgu/v1.0/user/getUserInfo";
        test(limitUrlPath);
    }

    private static void test(String limitUrlPath) {
        Thread[] requesters = new Thread[10];

        for (int i = 0; i < requesters.length; i++) {
            requesters[i] = new Thread(new Requester(limitUrlPath));
            requesters[i].start();
        }
    }
}

class Requester implements Runnable {
    private final String urlPath;
    private final RestTemplate restTemplate = new RestTemplate();

    public Requester(String urlPath) {
        this.urlPath = urlPath;
    }

    @Override
    public void run() {
        String response = restTemplate.getForObject(urlPath, String.class);
        System.out.println("response: " + response);
    }
}