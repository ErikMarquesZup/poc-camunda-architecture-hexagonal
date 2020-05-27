package CommandInicial;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class ValidationCommand implements ICommand {

    private long cpf;

}