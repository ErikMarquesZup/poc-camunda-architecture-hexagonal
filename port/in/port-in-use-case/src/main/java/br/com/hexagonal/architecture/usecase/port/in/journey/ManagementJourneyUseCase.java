package br.com.hexagonal.architecture.usecase.port.in.journey;

import br.com.hexagonal.architecture.usecase.port.in.journey.request.JourneyStartRequest;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

public interface ManagementJourneyUseCase {

    String execute(@RequestBody JourneyStartRequest requestStart) throws IOException;

}
