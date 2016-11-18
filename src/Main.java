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

        merchant.MeetPlayer(player1);

        PrintQ(merchant);

        merchant.MeetPlayer(player1);

        PrintQ(merchant);

        merchant.MeetPlayer(player7);

        PrintQ(merchant);

        merchant.MeetPlayer(player2);

        PrintQ(merchant);

        merchant.MeetPlayer(player2);

        PrintQ(merchant);

        merchant.MeetPlayer(player5);

        PrintQ(merchant);

        merchant.MeetPlayer(player5);

        PrintQ(merchant);

        merchant.MeetPlayer(player3);

        PrintQ(merchant);

        merchant.MeetPlayer(player3);

        PrintQ(merchant);

        merchant.MeetPlayer(player3);

        PrintQ(merchant);

        HashMap<String, Double> map = new HashMap<>(Helper.CalculateIndividialAttrScore(merchant, player4));
        System.out.println(map);


        // TODO
        // add merchant messages.
        // make merchant choose random actions when the highest are equal instead of the last equal highest action
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
