package dungeonmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.Spider;
import dungeonmania.entities.movings.Player;
import dungeonmania.entities.movings.ZombieToast;
import dungeonmania.entities.statics.Exit;
import dungeonmania.entities.statics.Wall;
import dungeonmania.entities.statics.ZombieToastSpawner;
import dungeonmania.entities.CollectableEntity;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.collectables.Armour;
import dungeonmania.entities.collectables.Key;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.lang.System;

public class Dungeon {
    private String id;
    private DungeonMap dungeonMap;
    private GameMode mode;
    private Goals goals;
    private Player player;
    private String name;
    private List<CollectableEntity> collectables = new ArrayList<CollectableEntity>();

    private int spiderPopulation;

    public static int nextDungeonId = 1;

    public Dungeon(String name, GameMode mode, DungeonMap dungeonMap, Goals goals) {
        this.name = name;
        this.mode = mode;
        this.dungeonMap = dungeonMap;
        this.goals = goals;
        this.id = "dungeon-" + Dungeon.nextDungeonId;
        this.player = null;

        Dungeon.nextDungeonId++;
    }

    /**
     * Creates a Dungeon instance from the JSON file's content
     */
    public static Dungeon fromJSONObject(String name, GameMode mode, JSONObject obj) {
        Goals goals = Goals.fromJSONObject(obj);

        DungeonMap map = new DungeonMap(obj);

        Dungeon dungeon = new Dungeon(name, mode, map, goals);

        JSONArray entities = obj.getJSONArray("entities");
        Player player = null;

        for (int i = 0; i < entities.length(); i++) {
            JSONObject entity = entities.getJSONObject(i);
            int x = entity.getInt("x");
            int y = entity.getInt("y");
            String type = entity.getString("type");

            // TODO: probably need a builder pattern here
            // for now, i just handle walls and player
            Cell cell = map.getCell(x, y);
            if (Objects.equals(type, Wall.STRING_TYPE)) {
                cell.addOccupant(new Wall(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, Exit.STRING_TYPE)) {
                cell.addOccupant(new Exit(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, ZombieToastSpawner.STRING_TYPE)) {
                cell.addOccupant(new ZombieToastSpawner(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, ZombieToast.STRING_TYPE)) {
                cell.addOccupant(new ZombieToast(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, Treasure.STRING_TYPE)) {
                cell.addOccupant(new Treasure(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, Arrow.STRING_TYPE)) {
                cell.addOccupant(new Arrow(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, Wood.STRING_TYPE)) {
                cell.addOccupant(new Wood(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, Sword.STRING_TYPE)) {
                cell.addOccupant(new Sword(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, Armour.STRING_TYPE)) {
                cell.addOccupant(new Armour(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, Key.STRING_TYPE)) {
                cell.addOccupant(new Key(dungeon, cell.getPosition(), entity.getInt("key")));
            } /*else if (Objects.equals(type, Door.STRING_TYPE)) {
                cell.addOccupant(new Door(dungeon, cell.getPosition()));
            } */
             else if (Objects.equals(type, Player.STRING_TYPE)) {
                player = new Player(dungeon, cell.getPosition());
                cell.addOccupant(player);
            } else {
                throw new Error("unhandled entity type: " + type);
            }
        }

        if (player == null) {
            throw new Error("the player's position wasn't specified");
        }

        dungeon.setPlayer(player);

        return dungeon;
    }

    private void pickupCollectableEntities() {
        dungeonMap.flood();

        System.out.println("Ran pickup function");
        //Retreiving Player's Cell
        Cell playerCell = dungeonMap.getPlayerCell();
        if (playerCell == null) {
            System.out.println("Nothing in Player Cell");
            return;
        }

        //Check if Collectibles in the Player's Cell
        if (playerCell.getOccupants() == null) {
            System.out.println("No Occupants in Cell");
            return;
        }
        List<Entity> playerCellOccupants = playerCell.getOccupants();
        System.out.println("Some Occupants");
        System.out.println("Occupants in Cell:");
        System.out.println(playerCellOccupants.toString());
        System.out.println(dungeonMap.toString());
        for (Entity occupant : playerCellOccupants) {
            System.out.println("Ran pickup for loop");
            System.out.println(occupant.toString());
            if (occupant instanceof CollectableEntity) {
                System.out.println("Attempted to Add to Add Collectable v2");
                //CollectableEntity collectableOccupant = new CollectableEntity();
                CollectableEntity collectableOccupant = (CollectableEntity)occupant;
                System.out.println(collectableOccupant);
                this.collectables.add(collectableOccupant);
                System.out.println("Added to Collectables");
            }
        }
    }

    public void tick(String itemUsed, Direction movementDirection)
            throws IllegalArgumentException, InvalidActionException {

        // PROBLEM: if we call tick as we iterate through the cells' entities
        // certain entities could get updated twice if they move down or left
        // SOLUTION: make a list of all the entities on the dungeonMap
        //           and *only* then call tick on them all

        this.player.handleMoveOrder(movementDirection);
        
        dungeonMap.allEntities().stream().forEach(entity -> entity.tick());
        

        pickupCollectableEntities();
        
        if (spiderPopulation < Spider.MAX_SPIDERS) {
            spiderPopulation++;
            Spider.spawnSpider(this);
        }
        
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getGoalsAsString() {
        return this.goals.asString();
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

    public GameMode getGameMode() {
        return this.mode;
    }

    public DungeonMap getMap() {
        return dungeonMap;
    }

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
}
