package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.CollectableEntity;
import dungeonmania.entities.collectables.consumables.Potion;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Key;
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
        } else return this.collectables.add(c);
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

    /**
     * Use the item specified by the id.
     * Raise an InvalidArgumentException if the item can't be used.
     * Raise an InvalidActionException if the item is not in the inventory.
     * 
     * @param entityId to be used
     * 
     */
    public void useItem(String entityId) throws IllegalArgumentException, InvalidActionException {

        // find item
        CollectableEntity itemUsed = collectables.stream()
            .filter(c -> c.getId().equals(entityId))
            .findFirst().orElse(null);

        if (itemUsed == null) throw new InvalidActionException("Item not in inventory");

        //TODO: add bomb
        if (!(itemUsed instanceof Potion)) throw new IllegalArgumentException("Item not useable");
        
        if (itemUsed instanceof Potion) {
            Potion potionDrunk = (Potion) itemUsed;
            potionDrunk.drink();
        }

        //TODO: add bomb

        collectables.remove(itemUsed);
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
