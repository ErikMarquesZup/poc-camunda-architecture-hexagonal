package br.com.hexagonal.architecture.journey.controller;

import br.com.hexagonal.architecture.journey.request.JourneyStartRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

public interface Journey {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/start", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> start(@RequestBody JourneyStartRequest requestStart) throws IOException;

}
