package DealNoDealGame;

import java.util.Map;

public class Banker extends GameEntity implements BankerDecide{

    public Banker(String name) {
        super(name);
    }
    
    @Override
    public double bankerOffer(Map<Integer, Case> cases, Case playerCase) {

        //looks at hashmap and checks how many closed cases. 
        //returns average prize amount of all closed cases. 
        double offer = 0.0;
        int count = 0;

        for (Case c : cases.values()) {
            if (!c.getIsOpen() && c != playerCase) {
                offer += c.getMoney();
                count++;
            }
        }

        if (count == 0) {
            return 0;
        }

        double average = offer / count;
        return Math.round(average);
    }
}
