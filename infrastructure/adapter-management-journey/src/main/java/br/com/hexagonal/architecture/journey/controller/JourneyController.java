package br.com.hexagonal.architecture.journey.controller;

import br.com.hexagonal.architecture.journey.request.JourneyStartRequest;
import br.com.hexagonal.architecture.journey.service.StartJourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public class JourneyController implements Journey {

    @Autowired
    private StartJourneyService startJourneyUseCase;

    @Override
    public ResponseEntity<String> start(JourneyStartRequest requestStart) {
        return ResponseEntity.ok(startJourneyUseCase.execute(requestStart));
    }
}
