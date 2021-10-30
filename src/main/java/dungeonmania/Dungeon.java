package dungeonmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.battlestrategies.BattleStrategy;
import dungeonmania.battlestrategies.NormalBattleStrategy;
import dungeonmania.entities.movings.Player;
import dungeonmania.entities.movings.Spider;
import dungeonmania.entities.movings.ZombieToast;
import dungeonmania.entities.statics.Boulder;
import dungeonmania.entities.statics.Exit;
import dungeonmania.entities.statics.Portal;
import dungeonmania.entities.statics.Wall;
import dungeonmania.entities.statics.ZombieToastSpawner;
import dungeonmania.entities.CollectableEntity;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.ConsumableEntity;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.collectables.consumables.InvincibilityPotion;
import dungeonmania.entities.collectables.Armour;
import dungeonmania.entities.collectables.Key;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.goal.Goal;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.lang.System;

public class Dungeon {
    private String id;
    private DungeonMap dungeonMap;
    private GameMode mode;
    private Goal goal;
    private Player player;
    private String name;
    private List<CollectableEntity> collectables = new ArrayList<CollectableEntity>();
    private List<Entity> occupants = new ArrayList<Entity>();

    private PriorityQueue<BattleStrategy> battleStrategies;

    public static int nextDungeonId = 1;

    public Dungeon(String name, GameMode mode, DungeonMap dungeonMap, Goal goal) {
        this.name = name;
        this.mode = mode;
        this.dungeonMap = dungeonMap;
        this.goal = goal;
        this.id = "dungeon-" + Dungeon.nextDungeonId;
        this.player = null;

        this.battleStrategies = new PriorityQueue<BattleStrategy>(5, (a, b) -> a.getPrecendence() - b.getPrecendence());
        this.battleStrategies.add(new NormalBattleStrategy(0));

        Dungeon.nextDungeonId++;
    }

    /**
     * Creates a Dungeon instance from the JSON file's content
     */
    public static Dungeon fromJSONObject(String name, GameMode mode, JSONObject obj) {
        
        Goal goal = Goal.fromJSONObject(obj);

        DungeonMap map = new DungeonMap(obj);

        Dungeon dungeon = new Dungeon(name, mode, map, goal);

        JSONArray entities = obj.getJSONArray("entities");
        Player player = null;

        for (int i = 0; i < entities.length(); i++) {
            JSONObject entity = entities.getJSONObject(i);
            int x = entity.getInt("x");
            int y = entity.getInt("y");
            String type = entity.getString("type");

            // TODO: probably need a builder pattern here
            // for now, i just handle walls and player
            Entity occupant;
            Cell cell = map.getCell(x, y);
            if (Objects.equals(type, Wall.STRING_TYPE)) {
                occupant = new Wall(dungeon, cell.getPosition());
            } else if (Objects.equals(type, Exit.STRING_TYPE)) {
                occupant = new Exit(dungeon, cell.getPosition());
            } else if (Objects.equals(type, ZombieToastSpawner.STRING_TYPE)) {
                occupant = new ZombieToastSpawner(dungeon, cell.getPosition());
            } else if (Objects.equals(type, ZombieToast.STRING_TYPE)) {
                occupant = new ZombieToast(dungeon, cell.getPosition());
            } else if (Objects.equals(type, Treasure.STRING_TYPE)) {
                occupant = new Treasure(dungeon, cell.getPosition());
            } else if (Objects.equals(type, Arrow.STRING_TYPE)) {
                occupant = new Arrow(dungeon, cell.getPosition());
            } else if (Objects.equals(type, Wood.STRING_TYPE)) {
                occupant = new Wood(dungeon, cell.getPosition());
            } else if (Objects.equals(type, Sword.STRING_TYPE)) {
                occupant = new Sword(dungeon, cell.getPosition());
            } else if (Objects.equals(type, Armour.STRING_TYPE)) {
                occupant = new Armour(dungeon, cell.getPosition());
            } else if (Objects.equals(type, Key.STRING_TYPE)) {
                occupant = new Key(dungeon, cell.getPosition(), entity.getInt("key"));
            } else if (Objects.equals(type, Boulder.STRING_TYPE)) {
                occupant = new Boulder(dungeon, cell.getPosition());
            } else if (Objects.equals(type, Spider.STRING_TYPE)) {
                occupant = Spider.spawnSpider(dungeon);
            } else if (Objects.equals(type, InvincibilityPotion.STRING_TYPE)) {
                occupant = new InvincibilityPotion(dungeon, cell.getPosition());
            } else if (Objects.equals(type, Player.STRING_TYPE)) {
                player = new Player(dungeon, cell.getPosition());
                occupant = player;
            } else if (Objects.equals(type, Portal.STRING_TYPE)) {
                String colour = entity.getString("colour");

                Portal portal = new Portal(dungeon, cell.getPosition(), colour);
                // Check if there is another portal of the same colour
                Portal correspondingPortal = existsPortal(colour, map);

                if (correspondingPortal != null) {
                    portal.setCorrespondingPortal(correspondingPortal);
                    correspondingPortal.setCorrespondingPortal(portal);
                }
                occupant = portal;
            } 
            else {
                throw new Error("unhandled entity type: " + type);
            }
            dungeon.addOccupant(occupant);
            cell.addOccupant(occupant);
        }

        if (player == null) {
            throw new Error("the player's position wasn't specified");
        }

        dungeon.setPlayer(player);

        return dungeon;
    }

    private void addOccupant(Entity occupant) {
        this.occupants.add(occupant);
    }

    /**
     * Picks Up the Collectable Entities that Are in the Player's Square
     * Runs Every Tick, After the Player Has Moved
     * If any collectables are in the player's square this function will remove
     * the collectable item from the cell and add it to the player's inventory.
     */
    private void pickupCollectableEntities() {
        dungeonMap.flood();

        //Retreiving Player's Cell
        Cell playerCell = dungeonMap.getPlayerCell();
        if (playerCell == null) {
            return;
        }

        //Check if Collectibles in the Player's Cell
        if (playerCell.getOccupants() == null) {
            return;
        }
        List<Entity> playerCellOccupants = playerCell.getOccupants();
        for (Entity occupant : playerCellOccupants) {
            if (occupant instanceof CollectableEntity) {
                CollectableEntity collectableOccupant = (CollectableEntity)occupant;
                //Add To Collectables Inventory
                this.collectables.add(collectableOccupant);
                //Remove the Collectable From the Current Cell
                playerCell.removeOccupant(occupant);

            }
        }
    }

    public void tick(String itemUsed, Direction movementDirection)
            throws IllegalArgumentException, InvalidActionException {

        assert this.battleStrategies.size() > 0;

        // PROBLEM: if we call tick as we iterate through the cells' entities
        // certain entities could get updated twice if they move down or left
        // SOLUTION: make a list of all the entities on the dungeonMap
        //           and *only* then call tick on them all

        this.player.handleMoveOrder(movementDirection);
        
        dungeonMap.allEntities().stream().forEach(entity -> entity.tick());
        

        pickupCollectableEntities();

        // TODO: figure out order
        if (itemUsed != null) {
            CollectableEntity item = collectables.stream()
                .filter(e -> e.getId().equals(itemUsed))
                .findFirst().orElse(null);

            // Error checking
            if (item == null) throw new InvalidActionException("Item can't be found");
            if (item instanceof ConsumableEntity) {
                ConsumableEntity useable = (ConsumableEntity) item;
                useable.use();
                collectables.remove(useable);
            } else {
                throw new IllegalArgumentException("Item not usable");
            }
        }
        

        long spiderPopulation = this.dungeonMap.allEntities().stream()
            .filter(e -> e instanceof Spider).count();
        if (spiderPopulation < Spider.MAX_SPIDERS) {
            Spider.spawnSpider(this);
        }

        this.battleStrategies.peek().findAndPerformBattles(this);
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Goal getGoal() {
        return this.goal;
    }

    public String getGoalAsString() {
        return this.goal.asString();
    }

    /**
     * this should only be called exactly once, after the dungeon has been
     * constructed.
     * 
     * This method is just there so the Dungeon constructor doesn't have to look
     * for the player in the entire map
     * @param player
     */
    private void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Create an entity response object for each entity in the dungeon
     * @return list of entity responses
     */
    public List<EntityResponse> getEntitiesResponse() {
        ArrayList<EntityResponse> entities = new ArrayList<>();

        for (int y = 0; y < this.dungeonMap.getHeight(); y++) {
            for (int x = 0; x < this.dungeonMap.getWidth(); x++) {
                Cell cell = this.dungeonMap.getCell(x, y);
                for (Entity entity : cell.getOccupants()) {
                    entities.add(new EntityResponse(
                        entity.getId(),
                        entity.getTypeAsString(),
                        new Position(x, y, entity.getLayerLevel().getValue()),
                        entity.isInteractable()
                    ));
                }
            }
        }
        return entities;
    }

    // Check if a portal exists on the map with a specified colour
    public static Portal existsPortal(String colour, DungeonMap map) {
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                Cell cell = map.getCell(x,y);
                Portal portal = cell.hasPortal();
                if (portal != null && portal.getColour().equals(colour)) {
                    return portal;
                }
            }
        }
        return null;
    }

    public GameMode getGameMode() {
        return this.mode;
    }

    public DungeonMap getMap() {
        return dungeonMap;
    }

    /**
     * Returns the Inventory in the form of a list of
     * ItemResponse instances. 
     */
    public List<ItemResponse> getInventoryAsItemResponse() {
        List<ItemResponse> outputListItemResponses = new ArrayList<ItemResponse>();
        for (CollectableEntity item : collectables) {
            String id = item.getId();
            String type = item.getTypeAsString();
            ItemResponse currItemResponse = new ItemResponse(id, type);
            outputListItemResponses.add(currItemResponse);
        }
        return outputListItemResponses;
    }

    public List<Entity> getOccupants() {
        return this.occupants;
    }
}
