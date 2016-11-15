import java.util.ArrayList;

/**
 * Created by hoddi84 on 15.11.2016.
 */
public class Main {

    public static void main(String args[]) {

        Merchant merchant = new Merchant();
        Player player1 = new Player(Races.Human, PlayerActions.Buy);
        Player player2 = new Player(Races.Human, PlayerActions.Sell);
        Player player3 = new Player(Races.Human, PlayerActions.Leave);
        Player player4 = new Player(Races.Human, PlayerActions.Steal);

        PrintQ(merchant);

        merchant.MeetPlayer(player1);

        PrintQ(merchant);

        merchant.MeetPlayer(player2);

        PrintQ(merchant);

        merchant.MeetPlayer(player4);

        PrintQ(merchant);

        merchant.MeetPlayer(player3);

        PrintQ(merchant);


    }

    static void PrintQ(Merchant merchant) {
        for (StateActionValue element : merchant.Q) {
            System.out.println(element);
        }
    }
}
