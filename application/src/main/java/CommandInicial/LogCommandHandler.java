package CommandInicial;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@Log
@AllArgsConstructor
public class LogCommandHandler implements ICommandHandler {

    private LogCommand logCommand;

    @Override
    public void handle() {

        String msg = "### Id=" + logCommand.getProcessInstanceId() + ", mensagem = " + logCommand.getMessage();
        log.info(msg);

    }
}
