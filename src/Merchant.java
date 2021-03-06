import com.sun.org.apache.xalan.internal.utils.FeatureManager;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;

import java.util.*;

/**
 * Created by hoddi84 on 15.11.2016.
 */
public class Merchant {

    ArrayList<StateActionValue> Q = new ArrayList<>();
    double alpha = 0.1; // learning rate.
    double gamma = 0.9; // discount factor.
    ArrayList<PlayerActionsValue> actionList = new ArrayList<>(Helper.GetActionList());
    ArrayList<FeatureCount> featureCountList = new ArrayList<>();
    ArrayList<String> featureList = new ArrayList<>();

    // returns the merchant action.
    // 10% chance for a random action, 90% chance to choose MaxQ action.
    public MerchantActions merchantActions(Player player) {
        Random rnd = new Random();
        int move = rnd.nextInt(10);
        if (move == 0) {
            // 10% chance for random move. Two moves possible.
            System.out.println("I chose a random action to perform (10%)");
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
            System.out.println("I chose my best action to perform (90%)");
            return maxQaction(player);
        }
    }


    public void MeetPlayer(Player player) {
        // we meet a player, need a method to store all player actions.
        // trying out UpdatePlayerActionList()
        UpdatePlayerActionList(player, actionList);

        if (Q.isEmpty()) {
            System.out.println("Hello, friend");
            System.out.println("You are the first person I have met, welcome " + player.raceType);
            AddPlayerToQ(player);
            if (merchantActions(player) == MerchantActions.LetIn) {
                System.out.println("I let him in, and his action was to " + player.playerActions);
                updateQ(player, MerchantActions.LetIn);
                return;
            }
            {
                System.out.println("I threw him out");
                updateQ(player, MerchantActions.ThrowOut);
                return;
            }
        }

        for (int i = 0; i < Q.size(); i++) {
            if (player.raceType.equals(Q.get(i).raceType)) {
                MerchantMessage.getMerchantMessage(CalculateIndividualAttrScore(player));
                System.out.println("I have met this race: " + player.raceType);
                // merchant has this race in memory, finding appropriate action to execute.
                if (merchantActions(player) == MerchantActions.LetIn) {
                    System.out.println("I let him in, and his action was to " + player.playerActions);
                    updateQ(player, MerchantActions.LetIn);
                    return;
                }
                else {
                    System.out.println("I threw him out");
                    updateQ(player, MerchantActions.ThrowOut);
                    return;
                }
            }
        }
        MerchantMessage.getMerchantMessage(CalculateIndividualAttrScore(player));
        System.out.println("I have not met this race: " + player.raceType);
        // merchant has not met this race, calculate the races score and add to Q.
        AddPlayerToQ(player);
        if (merchantActions(player) == MerchantActions.LetIn) {
            System.out.println("I let him in, and his action was to " + player.playerActions);
            updateQ(player,MerchantActions.LetIn);
        }
        else
        {
            System.out.println("I throw you out");
            updateQ(player,MerchantActions.ThrowOut);
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

    // return the merchant action which corresponds to the highest Q-value
    // which is the best actions for the merchant to perform.
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

    // updates a player's Q-value based on the merchants action, using the players action as the immediate reward.
    // when throwing players out, we must choose which reward to give since the player won't be able to perform
    // an action since he was thrown out.
    public void updateQ(Player player, MerchantActions merchantActions) {

        if (merchantActions.equals(MerchantActions.ThrowOut)) {
            double R = ReturnExpectedRValue(player); // need to rethink this value.
            double Q = getQ(player, merchantActions);
            double Qmax = getQmax(player);
            double newQvalue = Q + alpha*(R + gamma*Qmax - Q);
            double Qdiff = newQvalue - Q;
            setQ(player, merchantActions, newQvalue);
        }
        else {
            double R = getRfromPlayer(player);
            double Q = getQ(player, merchantActions);
            double Qmax = getQmax(player);
            double newQvalue = Q + alpha*(R + gamma*Qmax - Q);
            double Qdiff = newQvalue - Q;
            setQ(player, merchantActions, newQvalue);
            UpdateQAllTable(merchantActions, player, Qdiff);

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

    // update the Q valeus when we meet another player w.r.t. his Q score difference.
    // e.g. we update the whole Q table except for the Q values for the race of player we just met,
    // since our playr should have his values updated by the updateQ() method.
    public void UpdateQAllTable(MerchantActions merchantActions, Player player, double value) {
        HashMap<String, Integer> listPartialMatches = new HashMap<>(QPartialPlayerMatches(player));
        // find common attributes to player
        for (int i = 0; i < Q.size(); i++) {
            double accAttr = 0.0;
            double score = 0.0;
            double total = listPartialMatches.get("total");
            if (Q.get(i).merchantActions.equals(merchantActions) && !Q.get(i).raceType.equals(player.raceType)) {
                ArrayList<String> listMatches = new ArrayList<>(QMatchedAttributes(Q.get(i), player));
                for (int k = 0; k < listMatches.size(); k++) {
                    score = listPartialMatches.get(listMatches.get(k));
                    accAttr += score;
                }
                Q.get(i).value += (accAttr/total)*value;
            }
        }
    }

    // returns and counts a list of all race attributes and their corresponding
    // counts of player actions.
    // e.g. [Large, Green, Pointy] steal: 3 buy: 0 sell: 0 leave: 0
    public void UpdatePlayerActionList(Player player, ArrayList<PlayerActionsValue> actionList) {

        for (PlayerActionsValue x : actionList) {
            if (player.raceType.equals(x.raceType)) {
                x.UpdateActionValues(player.playerActions);
            }
        }
        UpdateFeatureCuntList();
    }

    // updates a list of all attributes and their corresponding
    // counts of player actions
    // e.g. Large buy: 1 sell: 0 steal: 3 leave: 0
    public void UpdateFeatureCuntList() {

        featureCountList.clear();
        // get the features.
        for (PlayerActionsValue x : actionList) {
            for (String y : x.features) {
                if (featureList.contains(y)) {
                }
                else {
                    featureList.add(y);
                }
            }
        }

        for (String x : featureList) {
            FeatureCount newCount = new FeatureCount(x);
            featureCountList.add(newCount);
        }

        for (PlayerActionsValue x : actionList) {
            for (String y : x.features) {
                for (FeatureCount count : featureCountList) {
                    if (y.equals(count.feature)) {
                        count.buy += x.actionList.get(PlayerActions.Buy);
                        count.sell += x.actionList.get(PlayerActions.Sell);
                        count.steal += x.actionList.get(PlayerActions.Steal);
                        count.leave += x.actionList.get(PlayerActions.Leave);
                    }
                }
            }
        }
    }

    // a pre-made hash map with hardcoded player actions and their reward.
    // returns the best expected score for R, immediate reward
    // when a merchant throws out a player.
    // we use this when we calculate the expected immediate reward for the merchant
    // when he throws out a player and the player does not have a chance to make an action.
    public double ReturnExpectedRValue(Player player) {
        HashMap<String, Integer> countMap = new HashMap<>();
        int highest = 0;
        int total = 0;
        String bestAction = "";
        countMap.put("buy",0);
        countMap.put("sell",0);
        countMap.put("steal",0);
        countMap.put("leave",0);
        for (String x : player.features) {
            for (int i = 0; i < featureCountList.size(); i++) {
                if (x.equals(featureCountList.get(i).feature)) {
                    int oldBuy = countMap.get("buy");
                    int oldSell = countMap.get("sell");
                    int oldSteal = countMap.get("steal");
                    int oldLeave = countMap.get("leave");
                    int buy = featureCountList.get(i).buy + oldBuy;
                    int sell = featureCountList.get(i).sell + oldSell;
                    int steal = featureCountList.get(i).steal + oldSteal;
                    int leave = featureCountList.get(i).leave + oldLeave;
                    countMap.replace("buy", oldBuy, buy);
                    countMap.replace("sell", oldSell, sell);
                    countMap.replace("steal", oldSteal, steal);
                    countMap.replace("leave", oldLeave, leave);
                }
            }
        }
        for (Map.Entry<String, Integer> map : countMap.entrySet()) {
            total += map.getValue();
            if (map.getValue() > highest) {
                highest = map.getValue();
                bestAction = map.getKey();
            }
        }
        // these are the immediate rewards R, the merchant won't be getting those
        // since he threw out a player, therefore they should perhaps be the negated values
        // of what he might have gotten.
        if (bestAction == "buy") {
            return 2.0*(countMap.get("buy")/total);
        }
        else if (bestAction == "sell") {
            return 1.0*(countMap.get("sell")/total);
        }
        else if (bestAction == "steal") {
            return -3.0*((double)countMap.get("steal")/(double)total);
        }
        else {
            return 0.0;
        }
    }

    // find the individual score for the attributes of a approaching player from previously known
    // player attributes from Q.
    public HashMap<String, Double> CalculateIndividualAttrScore(Player player) {
        HashMap<String, Integer> map = new HashMap<>(QPartialPlayerMatches(player));
        HashMap<String, Double> newMap = new HashMap<>();
        for (int i = 0; i < Q.size(); i++) {
            double newScore = 0.0;
            for (int k = 0; k < Q.get(i).features.size(); k++) {
                if (map.containsKey(Q.get(i).features.get(k))) {
                    newScore = Q.get(i).value / Q.get(i).features.size();
                    if (newMap.containsKey(Q.get(i).features.get(k))) {
                        double oldScore = newMap.get(Q.get(i).features.get(k));
                        double nextScore = newScore + oldScore;
                        newMap.replace(Q.get(i).features.get(k), oldScore, nextScore);
                    }
                    else {
                        newMap.put(Q.get(i).features.get(k), newScore);
                    }

                }
            }
        }
        // iterate the newMap and divide by total attribute occurrences from map.
        Iterator it = newMap.entrySet().iterator();
        for (Map.Entry<String, Double> newMapElement : newMap.entrySet()) {
            for (Map.Entry<String, Integer> mapElement : map.entrySet()) {
                if (newMapElement.getKey().equals(mapElement.getKey())) {
                    double oldScore = newMapElement.getValue();
                    double newScore = oldScore/mapElement.getValue();
                    newMapElement.setValue(newScore);
                }
            }
        }
        // iterate through newMap to find the highest valued attribute
        // and it's corresponding string.
        double highest = -Double.MAX_VALUE;
        String highestAttribute = "";
        for (Map.Entry<String, Double> element : newMap.entrySet()) {
            if (element.getValue() > highest) {
                highest = element.getValue();
                highestAttribute = element.getKey();
            }
        }
        // return only highest valued string for now.
        newMap.clear();
        newMap.put(highestAttribute, highest);
        return newMap;
    }
}
