package CommandInicial;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class LogCommand implements ICommand {

    private String processInstanceId;
    private String message;


}
