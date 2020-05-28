package br.com.hexagonal.architecture.journey.controller;

import br.com.hexagonal.architecture.journey.request.JourneyStartRequest;
import br.com.hexagonal.architecture.journey.service.StartJourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/journey")
public class JourneyController implements Journey {

    @Autowired
    private StartJourneyService startJourneyUseCase;

    @Override
    public ResponseEntity<String> start(JourneyStartRequest requestStart) throws IOException {
        return ResponseEntity.ok(startJourneyUseCase.execute(requestStart));
    }
}
