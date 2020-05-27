package CommandInicial;

import java.util.ArrayList;
import java.util.List;

public class CreateJourneyUseCase {

    void execute() throws Exception {

        List<ICommandHandler> comandos = new ArrayList<>();

        comandos.add(new LogCommandHandler(new LogCommand("12345", "minha mensagem")));

        // bdAdapter.validaCpfExistente(111);
        comandos.add(new ValidationCommandHandler(new ValidationCommand(111)));

        // camudandaAdapter.iniciaJornada("");
        comandos.add(new StartBpmnCommandHandler(new StartBpmnCommand("cobranded")));
        comandos.add(new StartBpmnCommandHandler(new StartBpmnCommand("flex")));

        for (ICommandHandler comando : comandos) {
            comando.handle();
        }

    }

}
