import java.util.HashMap;

/**
 * Created by hoddi84 on 15.11.2016.
 */
public class Main {

    public static void main(String args[]) {

        Merchant merchant = new Merchant();

        Player player1 = new Player(RaceType.Human, PlayerActions.Buy);
        Player player2 = new Player(RaceType.Goblin, PlayerActions.Buy);
        Player player3 = new Player(RaceType.Orc, PlayerActions.Steal);
        Player player4 = new Player(RaceType.Troll, PlayerActions.Buy);
        Player player5 = new Player(RaceType.Elf, PlayerActions.Buy);
        Player player6 = new Player(RaceType.Goblin, PlayerActions.Sell);
        Player player7 = new Player(RaceType.Human, PlayerActions.Steal);

        PrintQ(merchant);

        merchant.MeetPlayer(player5);

        PrintQ(merchant);

        merchant.MeetPlayer(player1);

        PrintQ(merchant);

        merchant.MeetPlayer(player1);

        PrintQ(merchant);


        for (FeatureCount x : merchant.featureCountList) {
            System.out.println(x);
        }

        for (String x : merchant.featureList) {
            System.out.println(x);
        }


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
