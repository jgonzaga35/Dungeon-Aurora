package dungeonmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import dungeonmania.battlestrategies.BattleStrategy.BattleDirection;
import dungeonmania.entities.CollectableEntity;
import dungeonmania.entities.collectables.*;
import dungeonmania.entities.collectables.buildables.*;
import dungeonmania.entities.collectables.consumables.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.DungeonMap;

public class Inventory {
    private List<CollectableEntity> collectables = new ArrayList<>();

    /**
     * Can't pick up more than one key
     * 
     * @param c collectable to add
     * @return true if the player was able to pick the collectable up
     */
    public boolean add(CollectableEntity c) {
        if (c instanceof Key && hasKey()) {
            // Player cannot pickup a second key
            return false;
        } 
        if (c instanceof Bomb) {
            // Player cannot pickup bomb already placed
            Bomb bomb = (Bomb) c;
            if (bomb.getIsPlaced() == true) {
                return false;
            }
        } 
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
     * Removes one of the given class from the inventory.
     * 
     * @return true if the inventory had something removed
     */
    public boolean pay(List<Class<? extends CollectableEntity>> cost) {
        List<CollectableEntity> price = new ArrayList<>();

        System.out.println("Cost check 2");
        cost.stream().forEach(t -> {
            CollectableEntity item = (CollectableEntity) collectables.stream()
                .filter(c -> t.isInstance(c))
                .findFirst().orElse(null);
            System.out.println("Cost Check");
            System.out.println("Cost:");
            System.out.println(t);
            System.out.println("Item Added to Price:");
            System.out.println(item);
            price.add(item);
        });
        
        //.filter(c -> t.isInstance(c))
        //.filter(c -> c.getClass().equals(t))

        if (price.stream().anyMatch(i -> i == null)) return false;
        
        price.stream().filter(e -> !(e instanceof SunStone)).forEach(i -> collectables.remove(i));

        return true;
    }

    public void build(String buildable) throws InvalidActionException {
        List<CollectableEntity> items;
        switch (buildable) {
            case "shield":
                items = buildable(Shield.RECIPES);
                if (items == null) throw new InvalidActionException("not enough resources to build " + buildable);
                collectables.removeAll(items);
                collectables.add(new Shield(null, null));
                return;
            case "sceptre":
                items = buildable(Sceptre.RECIPES);
                if (items == null) throw new InvalidActionException("not enough resources to build " + buildable);
                collectables.removeAll(items);
                collectables.add(new Sceptre(null, null));
                return;
            case "bow":
                items = buildable(Bow.RECIPES);
                if (items == null) throw new InvalidActionException("not enough resources to build " + buildable);
                collectables.removeAll(items);
                collectables.add(new Bow(null, null));
                return;
            default:
                throw new IllegalArgumentException("unknown buildable: " + buildable);
        }
    }

    public List<CollectableEntity> buildable(List<List<String>> recipes) {
        for (List<String> recipe : recipes) {
            List<CollectableEntity> items = findItems(recipe);
            if (items != null) return items;
        }
        return null;
    }

    /**
     * 
     * @return true if the inventory contains a sceptre
     */
    public boolean hasSceptre() {
        Sceptre sceptre = (Sceptre) collectables.stream().filter(c -> c instanceof Sceptre)
            .findFirst().orElse(null);

        return sceptre != null;
    }

    public List<String> getBuildables() {
        List<String> buildables = new ArrayList<>();
        if (buildable(Bow.RECIPES) != null) buildables.add(Bow.STRING_TYPE);
        if (buildable(Sceptre.RECIPES) != null) buildables.add(Sceptre.STRING_TYPE);
        if (buildable(Shield.RECIPES) != null) buildables.add(Shield.STRING_TYPE);

        return buildables;
    }

    /**
     * Use the item specified by the id.
     * Raise an InvalidArgumentException if the item can't be used.
     * Raise an InvalidActionException if the item is not in the inventory.
     * 
     * @param entityId to be used
     * @return the entity used.
     * 
     */
    public CollectableEntity useItem(String entityId) throws IllegalArgumentException, InvalidActionException {

        // find item
        CollectableEntity itemUsed = collectables.stream()
            .filter(c -> c.getId().equals(entityId))
            .findFirst().orElse(null);

        if (itemUsed == null) throw new InvalidActionException("Item not in inventory");

        if (!(itemUsed instanceof Potion) && !(itemUsed instanceof Bomb)) throw new IllegalArgumentException("Item not useable");
        
        if (itemUsed instanceof Potion) {
            Potion potionDrunk = (Potion) itemUsed;
            potionDrunk.drink();
        }

        collectables.remove(itemUsed);
        
        return itemUsed;
    }

    /**
     * finds the items from the inventory
     * @param itemsStringType list of String
     * @return null if inventory does not contain all the items, else return the list of items
     */
    public List<CollectableEntity> findItems(List<String> itemsStringType) {
        List<CollectableEntity> found = new ArrayList<>();
        for (String itemStringType : itemsStringType) {
            Optional<CollectableEntity> itemOpt = this.collectables.stream()
                // find an item of the right type that isn't already used
                .filter(item -> ((item.getTypeAsString().equals(itemStringType) || 
                (item.getTypeAsString().equals(SunStone.STRING_TYPE) && itemStringType == Treasure.STRING_TYPE))) && !found.contains(item))
                .findFirst();

            if (itemOpt.isEmpty()) return null;
            else found.add(itemOpt.get());
        }
        return found;
    }
    //.filter(item -> item.getTypeAsString().equals(itemStringType) && !found.contains(item))
    /**
     * decreases the items' durability
     * @param d
     */
    public void usedItemsForBattle(BattleDirection d) {
        List<CollectableEntity> deadItems = new ArrayList<>();
        for (CollectableEntity item : this.collectables) {
            if (item instanceof BattleItem) {
                BattleItem bitem = (BattleItem) item;
                bitem.usedForBattleRound(d);
                if (bitem.getDurability() <= 0) {
                    deadItems.add(item);
                }
            }
        }
        this.collectables.removeAll(deadItems);
    }

    /**
     * @param bitem battle item
     * @param d direction
     * @return true if the battle item is dead
     */
    public boolean usedItemForBattle(BattleItem bitem, BattleDirection d) {
        bitem.usedForBattleRound(d);
        if (bitem.getDurability() <= 0) {
            assert bitem instanceof CollectableEntity;
            this.collectables.remove((CollectableEntity) bitem);
            return true;
        }
        return false;
    }

    /**
     * See the list below for what is classified as a weapon
     * @return a weapon, or null
     */
    public BattleItem getOneWeapon() {
        // we could add another layer (make a super class Weapon, but this is
        // good enough)
        List<Class<? extends BattleItem>> weapons = List.of(
            Sword.class,
            Bow.class
        );
        return (BattleItem) this.collectables.stream().filter(e -> weapons.contains(e.getClass())).findFirst().orElse(null);
    }

    /**
     * Total bonus added by the inventory in the specified direction 
     *
     * Notice that attack damage adds, but defence coefficients multiply.
     * 
     * @param d battle direction
     * @return total bonus
     */
    public float totalBonus(BattleDirection d) {
        float bonus = 1;
        if (d == BattleDirection.ATTACK) {
            bonus = 0;
        } else if (d == BattleDirection.DEFENCE) {
            bonus = 1;
        }
        for (CollectableEntity item : this.collectables) {
            if (item instanceof BattleItem) {
                BattleItem bitem = (BattleItem) item;
                if (d == BattleDirection.ATTACK) {
                    bonus += bitem.getAttackDamageBonus();
                } else if (d == BattleDirection.DEFENCE) {
                    bonus *= bitem.getDefenceCoefBonus();
                }
            }
        }
        return bonus;
    }

    /**
     * @usage for example, {@code inventory.itemsOfType(Shield).forEach(shield -> foobar)}
     * @param <T> type
     * @param type type
     * @return Stream of CollectableEntities
     */
    public <T extends CollectableEntity> Stream<T> itemsOfType(Class<T> type) {
        return this.collectables.stream().filter(e -> type.isInstance(e)).map(e -> {
            @SuppressWarnings("unchecked") T t = (T)e; // bruh
            return t;
        });
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
