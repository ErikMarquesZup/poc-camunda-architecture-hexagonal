package br.com.hexagonal.architecture.journey.request;

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
    private String uuid;

}