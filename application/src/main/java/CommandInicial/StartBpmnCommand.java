package CommandInicial;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class StartBpmnCommand implements ICommand {

    private String intencao;


}
