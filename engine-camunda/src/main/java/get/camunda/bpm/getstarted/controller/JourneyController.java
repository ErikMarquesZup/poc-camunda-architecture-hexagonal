package get.camunda.bpm.getstarted.controller;

import get.camunda.bpm.getstarted.request.JourneyStartRequest;
import get.camunda.bpm.getstarted.service.ProcessInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class JourneyController implements Journey {

    @Autowired
    private ProcessInstanceService processInstanceService;

    @Override
    public ResponseEntity<String> start(JourneyStartRequest requestStart) throws IOException {
        processInstanceService.startProcessInstance(requestStart.getBpmnInstance());
        return ResponseEntity.ok("");
    }
}
