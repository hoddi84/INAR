import java.util.ArrayList;

/**
 * Created by hoddi84 on 15.11.2016.
 */
public class Helper {

    public static ArrayList<StateActionValue> CreateR() {

        ArrayList<StateActionValue> list = new ArrayList<>();
        StateActionValue state1act1 = new StateActionValue(Races.Human, MerchantActions.LetIn, 1.0);
        StateActionValue state1act2 = new StateActionValue(Races.Human, MerchantActions.ThrowOut, 0.0);
        StateActionValue state2act1 = new StateActionValue(Races.Troll, MerchantActions.LetIn, 1.0);
        StateActionValue state2act2 = new StateActionValue(Races.Troll, MerchantActions.ThrowOut, 0.0);
        list.add(state1act1);
        list.add(state1act2);
        list.add(state2act1);
        list.add(state2act2);
        return list;
    }

    public static ArrayList<StateActionValue> CreateQ() {

        ArrayList<StateActionValue> list = new ArrayList<>();
        StateActionValue state1act1 = new StateActionValue(Races.Human, MerchantActions.LetIn, 3.0);
        StateActionValue state1act2 = new StateActionValue(Races.Human, MerchantActions.ThrowOut, 2.0);
        StateActionValue state2act1 = new StateActionValue(Races.Troll, MerchantActions.LetIn, 0.0);
        StateActionValue state2act2 = new StateActionValue(Races.Troll, MerchantActions.ThrowOut, 0.0);
        list.add(state1act1);
        list.add(state1act2);
        list.add(state2act1);
        list.add(state2act2);
        return list;
    }
}
