import java.util.ArrayList;
import java.util.Random;

/**
 * Created by hoddi84 on 15.11.2016.
 */
public class Merchant {

    ArrayList<StateActionValue> Q = Helper.CreateQ(); // Q-value struct.
    ArrayList<StateActionValue> R = Helper.CreateR(); // R-value struct.
    double alpha = 0.1; // learning rate.
    double gamma = 0.9; // discount factor.

    // returns the merchant action.
    // 10% chance for a random action, 90% chance to choose MaxQ action.
    public MerchantActions merchantActions(Player player) {
        Random rnd = new Random();
        int move = rnd.nextInt(10);
        if (move == 0) {
            // 10% chance for random move. Two moves possible.
            int randomMove = rnd.nextInt(2);
            if (randomMove == 0) {
                return MerchantActions.LetIn;
            }
            else {
                return MerchantActions.ThrowOut;
            }
        }
        else {
            // 90% chance to choose maxQ action.
            System.out.println("90%");
            return maxQaction(player);
        }
    }

    public void MeetPlayer(Player player) {
        System.out.println("\nI met: " + player);
        if (merchantActions(player) == MerchantActions.LetIn) {
            System.out.println("I let you in"); // immediate reward is 1.
            updateQ(player,MerchantActions.LetIn);
        }
        else {
            System.out.println("I throw you out"); // immediate reward is 0.
            updateQ(player,MerchantActions.ThrowOut);
        }
    }

    // assuming all players are in here with their values pre-calculated.
    public MerchantActions maxQaction(Player player) {
        double maxValue = Integer.MIN_VALUE;
        MerchantActions action = MerchantActions.NULL;
        for (int i = 0; i < Q.size(); i++) {
            if (player.race.equals(Q.get(i).playerRace)) {
                if (maxValue < Q.get(i).value) {
                    maxValue = Q.get(i).value;
                    action = Q.get(i).merchantActions;
                }
            }
        }
        return action;
    }

    public void updateQ(Player player, MerchantActions merchantActions) {

        if (merchantActions.equals(MerchantActions.ThrowOut)) {
            System.out.println("Throw out not changing Q value");
        }
        else {
            double R = getR(player, merchantActions);
            double Q = getQ(player, merchantActions);
            double Qnext = getQnext(player, merchantActions);
            double newQvalue = Q + alpha*(R + gamma*Qnext - Q);
            setQ(player, merchantActions, newQvalue);
        }
    }

    // set the new Q value for the corresponding player race and merchant action.
    public void setQ(Player player, MerchantActions merchantActions, double newQvalue) {
        for (int i = 0; i < Q.size(); i++) {
            if (player.race.equals(Q.get(i).playerRace)) {
                if (merchantActions.equals(Q.get(i).merchantActions)) {
                    Q.get(i).value = newQvalue;
                }
            }
        }
    }

    // get the Q value for a given player race and a given merchant action.
    public double getQ(Player player, MerchantActions merchantActions) {
        double Qval = 0;
        for (int i = 0; i < Q.size(); i++) {
            if (player.race.equals(Q.get(i).playerRace)) {
                if (merchantActions.equals(Q.get(i).merchantActions)) {
                    Qval = Q.get(i).value;
                }
            }
        }
        return Qval;
    }

    public double getQnext(Player player, MerchantActions merchantActions) {
        double Qnext = getQ(player, merchantActions);
        Qnext += player.actionScore;
        return Qnext;
    }

    public double getR(Player player, MerchantActions merchantActions) {
        double Rval = 0;
        for (int i = 0; i < R.size(); i++) {
            if (player.race.equals(R.get(i).playerRace)) {
                if (merchantActions.equals(R.get(i).merchantActions)) {
                    Rval = R.get(i).value;
                }
            }
        }
        return Rval;
    }



}
