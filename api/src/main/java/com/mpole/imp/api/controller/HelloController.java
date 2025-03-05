package com.mpole.imp.api.controller;


import com.mpole.imp.api.dto.Response;
import com.mpole.imp.api.dto.type.Status;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
public class HelloController {

    @GetMapping("/hello"  )
    public Mono<Response> hello() {
        return Mono.just(new Response( Status.SUCCESS, "Hello, World!" ));
    }

    @GetMapping("/error"  )
    public Mono<Response> error() {
        return Mono.error(new RuntimeException("what the!!"));
    }


}
