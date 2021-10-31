package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.CollectableEntity;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.response.models.ItemResponse;

public class Inventory {
    private List<CollectableEntity> collectables = new ArrayList<>();

    /**
     * @param c collectable to add
     * @return true if the inventory has changed as a result of this operation
     */  
    public boolean add(CollectableEntity c) {
        if (c instanceof Key && hasKey()) {
            // Player cannot pickup a second key
            return false;
        } 
        if (c instanceof Bomb) {
            // Player cannot pickup bomb already placed
            Bomb bomb = (Bomb) c;
            System.out.println(bomb.toString());
            if (bomb.getIsPlaced() == true) {
                System.out.println("It was placed");
                return false;
            }
        } 
        System.out.println("Ran that bomb is Picked Up");
        return this.collectables.add(c);
    }

    /**
     * @param c collectable to remove
     * @return true if the collectable was in the inventory
     */
    public boolean remove(CollectableEntity c) {
        return this.collectables.remove(c);
    }

    public List<ItemResponse> asItemResponses() {
        List<ItemResponse> outputListItemResponses = new ArrayList<ItemResponse>();
        for (CollectableEntity item : collectables) {
            String id = item.getId();
            String type = item.getTypeAsString();
            ItemResponse currItemResponse = new ItemResponse(id, type);
            outputListItemResponses.add(currItemResponse);
        }
        return outputListItemResponses;
    }
    

    public List<CollectableEntity> getCollectables() {
        return this.collectables;
    }

    public boolean hasKey() {
        return collectables.stream().anyMatch(c -> c instanceof Key);
    }

}
