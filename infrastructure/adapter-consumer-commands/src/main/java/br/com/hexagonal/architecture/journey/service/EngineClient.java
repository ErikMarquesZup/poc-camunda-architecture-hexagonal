package br.com.hexagonal.architecture.journey.service;

import br.com.hexagonal.architecture.journey.request.JourneyStartRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

@FeignClient(url="localhost:8080", name="camunda")
public interface EngineClient {

    @PostMapping(value = "engine/journey/start", headers = {"Content-Type:application/json"})
    ResponseEntity<String> start(@RequestBody JourneyStartRequest requestStart) throws IOException;
}
