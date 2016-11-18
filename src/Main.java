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

        merchant.MeetPlayer(player6);

        PrintQ(merchant);

        merchant.MeetPlayer(player7);

        PrintQ(merchant);

        HashMap<String,Integer> hash = merchant.QPartialPlayerMatches(player5);
        System.out.println(hash);
        HashMap<String, Double> map = new HashMap<>(Helper.CalculateIndividialAttrScore(merchant, player5));
        System.out.println(map);



        //HashMap<String,Integer> hash = new HashMap<>(Helper.QPartialPlayerMatches(merchant, player6));
        //System.out.println(hash);


        //double[] d = Helper.QPartialPlayerScoreEachAction(merchant, player6);
        //System.out.println(d[0] + " " +  d[1]);


        // TODO
        // change from racial bias to feature bias, by using the helper functions to replace some in Merchant.java.
        // replace the reward R, with the corresponding player action score as the immediate reward, R.
        // fix the score function w.r.t. merchant actions before adding to Q.
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
