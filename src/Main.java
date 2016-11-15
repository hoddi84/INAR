import java.util.ArrayList;

/**
 * Created by hoddi84 on 15.11.2016.
 */
public class Main {


    public static void main(String args[]) {

        Merchant merchant = new Merchant();
        Player player1 = new Player(Races.Human, PlayerActions.Buy);
        Player player2 = new Player(Races.Troll, PlayerActions.Steal);
        Player player3 = new Player(Races.Human, PlayerActions.Steal);
        Player player4 = new Player(Races.Troll, PlayerActions.Buy);


        // beginning Q-value table.
        for (StateActionValue element : merchant.Q) {
            System.out.println(element);
        }

        // testing 1 2 3 HOUSTON
        // run for 30 iterations.
        // meeting a human who buys and steals half the time.
        // 90% chance to choose maxQvalue action, 10% chance for random.
        // pre-initialized values are 3.0 for LetIn, and 2.0 for ThrowOut.
        // since LetIn is higher, is should choose LetIn action 90% of the time.
        for (int i = 0; i < 30; i++) {

            merchant.MeetPlayer(player1);

            for (StateActionValue element : merchant.Q) {
                System.out.println(element);
            }

            merchant.MeetPlayer(player3);

            for (StateActionValue element : merchant.Q) {
                System.out.println(element);
            }
        }
    }
}
