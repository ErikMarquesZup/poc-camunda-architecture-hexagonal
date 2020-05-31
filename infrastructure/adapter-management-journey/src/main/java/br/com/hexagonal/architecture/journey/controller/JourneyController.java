package br.com.hexagonal.architecture.journey.controller;

import br.com.hexagonal.architecture.journey.request.JourneyStartRequest;
import br.com.hexagonal.architecture.journey.rocksdb.kv.exception.FindFailedException;
import br.com.hexagonal.architecture.journey.service.StartJourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/journey")
public class JourneyController implements Journey {

    @Autowired
    private StartJourneyService startJourneyUseCase;

    @Override
    public ResponseEntity<String> start(JourneyStartRequest requestStart) throws IOException, FindFailedException {
        return ResponseEntity.ok(startJourneyUseCase.start(requestStart));
    }

    @PostMapping("/{processInstanceId}/complete/{taskId}")
    public ResponseEntity<String> complete(@PathVariable String processInstanceId, @PathVariable String taskId) throws IOException, FindFailedException {
        return ResponseEntity.ok(startJourneyUseCase.completeTask(processInstanceId, taskId));
    }
}
