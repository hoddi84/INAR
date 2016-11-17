import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by hoddi84 on 15.11.2016.
 */
public class Merchant {

    //ArrayList<StateActionValue> Q = Helper.CreateQ(); // Q-value struct.
    //ArrayList<StateActionValue> R = Helper.CreateR(); // R-value struct.
    ArrayList<StateActionValue> Q = new ArrayList<>();
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
        if (Q.isEmpty()) {
            AddPlayerToQ(player);
            //Q.get(0).value = 0.0;
            //Q.get(1).value = 0.0;
        }
        for (int i = 0; i < Q.size(); i++) {
            if (player.raceType.equals(Q.get(i).raceType)) {
                System.out.println("I have met this race: " + player.raceType);
                // merchant has this race in memory, finding appropriate action to execute.
                if (merchantActions(player) == MerchantActions.LetIn) {
                    System.out.println("I let you in");
                    updateQ(player,MerchantActions.LetIn);
                    break;
                }
                {
                    System.out.println("I throw you out");
                    updateQ(player,MerchantActions.ThrowOut);
                    break;
                }
            }
            else {
                System.out.println("I have not met this race: " + player.raceType);
                // merchant has not met this race, calculate the races score and add to Q.
                AddPlayerToQ(player);
                if (merchantActions(player) == MerchantActions.LetIn) {
                    System.out.println("I let you in");
                    updateQ(player,MerchantActions.LetIn);
                    break;
                }
                {
                    System.out.println("I throw you out");
                    updateQ(player,MerchantActions.ThrowOut);
                    break;
                }
            }
        }
    }

    public void AddPlayerToQ(Player player) {
        // double[] contains scores for MerchantActions, e.g. LetIn = double[0]
        double[] score = QPartialPlayerScoreEachAction(player);
        StateActionValue state1act1 = new StateActionValue(player.raceType, MerchantActions.LetIn, score[0]);
        StateActionValue state1act2 = new StateActionValue(player.raceType, MerchantActions.ThrowOut, score[1]);
        Q.add(state1act1);
        Q.add(state1act2);
    }
/*
    public void MeetPlayer(Player player) {

        System.out.println("\nI met: " + player);
        if (merchantActions(player) == MerchantActions.LetIn) {
            System.out.println("I let you in");
            updateQ(player,MerchantActions.LetIn);
        }
        else {
            System.out.println("I throw you out");
            updateQ(player,MerchantActions.ThrowOut);
        }
    }
*/
    // assuming all players are in here with their values pre-calculated.
    public MerchantActions maxQaction(Player player) {
        double maxValue = Integer.MIN_VALUE;
        MerchantActions action = MerchantActions.NULL;
        for (int i = 0; i < Q.size(); i++) {
            if (player.raceType.equals(Q.get(i).raceType)) {
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
            double R = getRfromPlayer(player);
            double Q = getQ(player, merchantActions);
            double Qmax = getQmax(player);
            double newQvalue = Q + alpha*(R + gamma*Qmax - Q);
            setQ(player, merchantActions, newQvalue);
        }
    }

    // set the new Q value for the corresponding player race and merchant action.
    public void setQ(Player player, MerchantActions merchantActions, double newQvalue) {
        for (int i = 0; i < Q.size(); i++) {
            if (player.raceType.equals(Q.get(i).raceType)) {
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
            if (player.raceType.equals(Q.get(i).raceType)) {
                if (merchantActions.equals(Q.get(i).merchantActions)) {
                    Qval = Q.get(i).value;
                }
            }
        }
        return Qval;
    }

    // Get the highest Q-value from all possible actions for a given player.
    public double getQmax(Player player) {
        MerchantActions maxAction = maxQaction(player);
        double QmaxVal = Integer.MIN_VALUE;
        for (int i = 0; i < Q.size(); i++) {
            if (player.raceType.equals(Q.get(i).raceType)) {
                if (maxAction.equals(Q.get(i).merchantActions)) {
                    QmaxVal = Q.get(i).value;
                }
            }
        }
        return QmaxVal;
    }

    // not using currently, since we always want the highest Q-value for all actions.
    // when calculating maxQ(a) in the Q-Learning algorithm.
    public double getQnext(Player player, MerchantActions merchantActions) {
        double Qnext = getQ(player, merchantActions);
        Qnext += player.actionScore;
        return Qnext;
    }

    // get the R, immediate reward, from the player's action score.
    public double getRfromPlayer(Player player) {
        double Rval = player.actionScore;
        return Rval;
    }

    // get the R value from the created table.
    // not using, we are using getRfromPlayer().
    /*
    public double getR(Player player, MerchantActions merchantActions) {
        double Rval = 0;
        for (int i = 0; i < R.size(); i++) {
            if (player.raceType.equals(R.get(i).raceType)) {
                if (merchantActions.equals(R.get(i).merchantActions)) {
                    Rval = R.get(i).value;
                }
            }
        }
        return Rval;
    }
    */

    public double[] QPartialPlayerScoreEachAction(Player player) {
        HashMap<String, Integer> map = new HashMap<>(QPartialPlayerMatches(player));
        double totalAttributes = map.get("total");
        int counter = 0;
        int counter2 = 0;
        double[] accumulatedScore = {0.0, 0.0};
        for (StateActionValue element : Q) {
            if (element.merchantActions.equals(MerchantActions.LetIn)) {
                double scoreAttributes = 0;
                ArrayList<String> matches = new ArrayList<>(QMatchedAttributes(element, player));
                if (!matches.isEmpty()) {
                    counter++;
                }
                for (String match : matches) {
                    scoreAttributes += map.get(match);
                }
                accumulatedScore[0] += (scoreAttributes / totalAttributes) * element.value;
            }
            if (element.merchantActions.equals(MerchantActions.ThrowOut)) {
                double scoreAttributes = 0;
                ArrayList<String> matches = new ArrayList<>(QMatchedAttributes(element, player));
                if (!matches.isEmpty()) {
                    counter2++;
                }
                for (String match : matches) {
                    scoreAttributes += map.get(match);
                }
                accumulatedScore[1] += (scoreAttributes / totalAttributes) * element.value;
            }
        }
        accumulatedScore[0] = accumulatedScore[0]/counter;
        accumulatedScore[1] = accumulatedScore[1]/counter2;
        return accumulatedScore;
    }

    // compare a player to a StateActionValue attributes and list the attributes they have in common.
    public ArrayList<String> QMatchedAttributes(StateActionValue stateActionValue, Player player) {
        ArrayList<String> list = new ArrayList<>();
        for (String x : stateActionValue.features) {
            for (String y : player.features) {
                if (y.equals(x)) {
                    list.add(x);
                }
            }
        }
        return list;
    }

    // Q does not contain this player.
    // Check if Q contains a partial match of attributes and return how many matches and what matches.
    public HashMap<String, Integer> QPartialPlayerMatches(Player player) {
        int preValue = 0;
        int total = 0;
        HashMap<String,Integer> map = new HashMap<>();
        for (StateActionValue el : Q) {
            for (int i = 0; i < el.features.size(); i++) {
                for (int k = 0; k < player.features.size(); k++) {
                    if (player.features.get(k).equals(el.features.get(i))) {
                        // check if map contains key [player feature]
                        if (map.containsKey(player.features.get(k))) {
                            preValue = map.get(player.features.get(k));
                            map.replace(player.features.get(k), preValue, preValue+1);
                            total++;
                        }
                        else {
                            map.put(player.features.get(k), 1);
                            total++;
                        }
                    }
                }
            }
        }
        map.put("total", total);
        return map;
    }


}
