package CommandInicial;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@Log
@AllArgsConstructor
public class StartBpmnCommandHandler implements ICommandHandler {

    private StartBpmnCommand startBpmnCommand;

    @Override
    public void handle() {

        String msg = "### Iniciando jornada com a intenção = " + startBpmnCommand.getIntencao();
        log.info(msg);

    }
}
