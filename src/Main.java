import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hoddi84 on 15.11.2016.
 */
public class Main {

    public static void main(String args[]) {

        Merchant merchant = new Merchant();
        Player player1 = new Player(Races.Human, PlayerActions.Buy);
        Player player2 = new Player(Races.Human, PlayerActions.Buy);
        Player player3 = new Player(Races.Troll, PlayerActions.Steal);
        Player player4 = new Player(Races.Troll, PlayerActions.Buy);

        Player player6 = new Player(Races.Goblin, PlayerActions.Sell);

        HashMap<String,Integer> hash = new HashMap<>(Helper.QPartialPlayerMatches(merchant, player6));
        System.out.println(hash);

        double d = Helper.QPartialPlayerScore(merchant, player6);
        System.out.println(d);


        // TODO
        // change from racial bias to feature bias, by using the helper functions to replace some in Merchant.java.
        // replace the reward R, with the corresponding player action score as the immediate reward, R.
        // .....
    }

    static void PrintQ(Merchant merchant) {
        for (StateActionValue element : merchant.Q) {
            System.out.println(element);
        }
    }
}
