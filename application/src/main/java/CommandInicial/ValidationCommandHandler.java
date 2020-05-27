package CommandInicial;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@Log
@AllArgsConstructor
public class ValidationCommandHandler implements ICommandHandler {

    private ValidationCommand validationCommand;

    @Override
    public void handle() throws Exception {

        if (validationCommand.getCpf() == 111) {
            log.severe("###### !!!!! CPF não pode ser 111");
            throw new Exception("##### Deu ruim");
        }

        String msg = "### CPF " + validationCommand.getCpf() + " válido!";
        log.info(msg);

    }
}
