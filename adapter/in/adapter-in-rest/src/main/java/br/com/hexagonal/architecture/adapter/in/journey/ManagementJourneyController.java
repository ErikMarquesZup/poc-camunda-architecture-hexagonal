package br.com.hexagonal.architecture.adapter.in.journey;

import br.com.hexagonal.architecture.usecase.port.in.journey.ManagementJourneyUseCase;
import br.com.hexagonal.architecture.usecase.port.in.journey.request.JourneyStartRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/journey")
public class ManagementJourneyController {

    @Autowired
    private ManagementJourneyUseCase managementJourneyUseCase;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/start", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> start(JourneyStartRequest requestStart) throws IOException {
        return ResponseEntity.ok(managementJourneyUseCase.execute(requestStart));
    }

//    @PostMapping("/{processInstanceId}/complete/{taskId}")
//    public ResponseEntity<String> complete(@PathVariable String processInstanceId, @PathVariable String taskId) throws IOException, FindFailedException {
//        return ResponseEntity.ok(startJourneyUseCase.completeTask(processInstanceId, taskId));
//    }
}
