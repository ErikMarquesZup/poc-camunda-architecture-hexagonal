package br.com.hexagonal.architecture.usecase.port.in.journey.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JourneyStartRequest {

    private String bpmnInstance;
    private String cpf;

}