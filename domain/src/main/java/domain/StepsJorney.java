package domain;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class StepsJorney {

   private String instanceProcessId;
   private String cpf;
   private String taskId;
   private String task;
   /*
   task --> Iniciada,
    Completou Task1,
    Completou Task2,
    Completou Taskn, ... ,
    finalizou -> Represetar pelo tópico de eventos conforme avançarmos na solução
    */

}
