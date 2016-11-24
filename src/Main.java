import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hoddi84 on 15.11.2016.
 */
public class Main {

    public static void main(String args[]) {

        Merchant merchant = new Merchant();

        Player player1 = new Player(RaceType.Human, PlayerActions.Buy);
        Player player2 = new Player(RaceType.Goblin, PlayerActions.Buy);
        Player player3 = new Player(RaceType.Orc, PlayerActions.Buy);
        Player player4 = new Player(RaceType.Troll, PlayerActions.Buy);
        Player player5 = new Player(RaceType.Elf, PlayerActions.Buy);
        Player player6 = new Player(RaceType.Goblin, PlayerActions.Sell);
        Player player7 = new Player(RaceType.Human, PlayerActions.Steal);
        Player player8 = new Player(RaceType.Human, PlayerActions.Leave);

        PrintQ(merchant);

        for (int i = 0; i < 100; i++) {

            merchant.MeetPlayer(player8);
            PrintQ(merchant);
            merchant.MeetPlayer(player6);
            PrintQ(merchant);
            merchant.MeetPlayer(player3);
            PrintQ(merchant);
        }

        PrintQ(merchant);

        // TODO
        // add merchant messages.
        // need to rethink the R-value from throwing out players.
        // .....
    }

    static void PrintQ(Merchant merchant) {
        System.out.println("");
        for (StateActionValue element : merchant.Q) {
            System.out.println(element);
        }
        System.out.println("");
    }
}
