package CommandInicial;

public class CreateJourneyController {

    public static void main(String[] args) {

        CreateJourneyUseCase createJourneyAdapter = new CreateJourneyUseCase();

        try {
            createJourneyAdapter.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
