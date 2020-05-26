package br.com.hexagonal.architecture.journey.controller;

import br.com.hexagonal.architecture.journey.request.JourneyStartRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

public interface Journey {

    @PostMapping("/start")
    @Consumes("application/json")
    @Produces("application/json")
    ResponseEntity<String> start(@RequestBody JourneyStartRequest requestStart);

}
