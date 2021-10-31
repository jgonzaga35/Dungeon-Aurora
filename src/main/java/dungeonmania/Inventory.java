package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.CollectableEntity;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.response.models.ItemResponse;

public class Inventory {
    private List<CollectableEntity> collectables = new ArrayList<>();

    /**
     * @param c collectable to add
     * @return true if the inventory has changed as a result of this operation
     */
    public boolean add(CollectableEntity c) {
        return this.collectables.add(c);
    }

    /**
     * @param c collectable to remove
     * @return true if the collectable was in the inventory
     */
    public boolean remove(CollectableEntity c) {
        return this.collectables.remove(c);
    }
    /**
     * Removes one treasure from the inventory.
     * 
     * @return true if the inventory had a coin removed
     */
    public boolean pay() {
        Treasure coin = (Treasure) collectables.stream().filter(c -> c instanceof Treasure)
            .findFirst().orElse(null);

        collectables.remove(coin);

        return coin != null;
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
}
