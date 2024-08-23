package com.manqin.service1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;


/**
 * 此示例中 http://gs-spring-boot-k8s:8081 在开发环境中无法访问
 * 需要修改 host 文件添加 127.0.0.1 gs-spring-boot-k8s
 */
@RestController
@RequestMapping("DnsTest")
public class DnsTestController {

    @Value("${spring.application.name}")
    private String serviceName;


    private WebClient webClient = WebClient.create();

    @GetMapping("hello")
    public String hello() {
        return "Hello this is "+serviceName+"!";
    }

    @GetMapping("requestService1")
    public Mono<String> requestPod1Service1() {
        System.out.println("request service1");
        return webClient.get().uri("http://gs-spring-boot-k8s:8081")
                .retrieve()
                .toEntity(String.class)
                .map(entity -> {
                    List<String> hostnames = entity.getHeaders().get("k8s-host");
                    String host = hostnames == null ? "null" :hostnames.get(0);
                    return "Hello " + entity.getBody() + " from " + host;
                });
    }

    @GetMapping("requestService2")
    public Mono<String> requestPod1Service2() {
        System.out.println("request service2");
        return webClient.get().uri("http://gs-spring-boot-k8s:8082")
                .retrieve()
                .toEntity(String.class)
                .map(entity -> {
                    List<String> hostnames = entity.getHeaders().get("k8s-host");
                    String host = hostnames == null ? "null" :hostnames.get(0);
                    return "Hello " + entity.getBody() + " from " + host;
                });
    }


    @GetMapping("getHostName")
    public Mono<String> getHostName(String uri){
        System.out.println("getHostName()-->"+" uri:"+uri);
        return webClient.get().uri("http://"+uri)
                .retrieve()
                .toEntity(String.class)
                .map(entity -> {
                    List<String> hostnames = entity.getHeaders().get("k8s-host");
                    String host = hostnames == null ? "null" :hostnames.get(0);
                    return "Hello " + entity.getBody() + " from " + host;
                });
    }

    public static void main(String[] args) {
        Mono<String> mono = new DnsTestController().getHostName("localhost:8081/DnsTest/hello");
        System.out.println(mono.block());
        while (true){

        }
    }

}
