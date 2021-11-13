package dungeonmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.GenerateMaze.BCell;
import dungeonmania.battlestrategies.BattleStrategy;
import dungeonmania.battlestrategies.BattleStrategy.BattleDirection;
import dungeonmania.battlestrategies.NoBattleStrategy;
import dungeonmania.battlestrategies.NormalBattleStrategy;
import dungeonmania.entities.CollectableEntity;
import dungeonmania.entities.collectables.Anduril;
import dungeonmania.entities.MovingEntity;
import dungeonmania.entities.collectables.Armour;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.BattleItem;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.OneRing;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.collectables.buildables.Bow;
import dungeonmania.entities.collectables.buildables.Shield;
import dungeonmania.entities.collectables.consumables.HealthPotion;
import dungeonmania.entities.collectables.consumables.InvincibilityPotion;
import dungeonmania.entities.collectables.consumables.InvisibilityPotion;
import dungeonmania.entities.collectables.consumables.Potion;
import dungeonmania.entities.movings.Assassin;
import dungeonmania.entities.movings.Hydra;
import dungeonmania.entities.movings.Mercenary;
import dungeonmania.entities.movings.Player;
import dungeonmania.entities.movings.Spider;
import dungeonmania.entities.movings.ZombieToast;
import dungeonmania.entities.statics.Boulder;
import dungeonmania.entities.statics.Door;
import dungeonmania.entities.statics.Exit;
import dungeonmania.entities.statics.FloorSwitch;
import dungeonmania.entities.statics.LightBulb;
import dungeonmania.entities.statics.Portal;
import dungeonmania.entities.statics.Swamp;
import dungeonmania.entities.statics.SwitchDoor;
import dungeonmania.entities.statics.Wall;
import dungeonmania.entities.statics.Wire;
import dungeonmania.entities.statics.ZombieToastSpawner;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.goal.ExitGoal;
import dungeonmania.goal.Goal;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Dungeon {
    private String id;
    private DungeonMap dungeonMap;
    private GameMode mode;
    private Goal goal;
    private Player player;
    private String name;
    private Inventory inventory = new Inventory();
    private List<Potion> activePotions = new ArrayList<>();
    private PriorityQueue<BattleStrategy> battleStrategies;

    public static int nextDungeonId = 1;
    
    private int tickCount = 0;

    private boolean hadEnemiesAtStartOfDungeon = false;

    /**
     * make sure to seed before each test
     */
    private Random r;

    public Dungeon(Random r, String name, GameMode mode, DungeonMap dungeonMap, Goal goal) {
        this.name = name;
        this.mode = mode;
        this.dungeonMap = dungeonMap;
        this.goal = goal;
        this.id = "dungeon-" + Dungeon.nextDungeonId;
        this.player = null;

        this.r = r;

        this.battleStrategies = new PriorityQueue<BattleStrategy>(5, (a, b) -> b.getPrecedence() - a.getPrecedence());
        if (mode == GameMode.PEACEFUL) {
            this.battleStrategies.add(new NoBattleStrategy(0));
        } else {
            this.battleStrategies.add(new NormalBattleStrategy(0));
        }

        Dungeon.nextDungeonId++;
    }

    /**
     * All source of randomness should come from here, so that we can seed it.
     * 
     * to seed: dungeon.getRandom().setSeed(1);
     */
    public Random getRandom() {
        return this.r;
    } 

    /**
     * Creates a Dungeon instance from the JSON file's content
     */
    public static Dungeon fromJSONObject(Random r, String name, GameMode mode, JSONObject obj) {
        
        Goal goal = Goal.fromJSONObject(obj);

        DungeonMap map = new DungeonMap(obj);

        Dungeon dungeon = new Dungeon(r, name, mode, map, goal);

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
            } else if (Objects.equals(type, FloorSwitch.STRING_TYPE)) {
                cell.addOccupant(new FloorSwitch(dungeon, cell.getPosition(), getLogicString(entity)));
            } else if (Objects.equals(type, ZombieToastSpawner.STRING_TYPE)) {
                cell.addOccupant(new ZombieToastSpawner(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, ZombieToast.STRING_TYPE)) {
                cell.addOccupant(new ZombieToast(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, Treasure.STRING_TYPE)) {
                cell.addOccupant(new Treasure(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, OneRing.STRING_TYPE)) {
                cell.addOccupant(new OneRing(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, Arrow.STRING_TYPE)) {
                cell.addOccupant(new Arrow(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, Wood.STRING_TYPE)) {
                cell.addOccupant(new Wood(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, Anduril.STRING_TYPE)) {
                cell.addOccupant(new Anduril(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, Sword.STRING_TYPE)) {
                cell.addOccupant(new Sword(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, Armour.STRING_TYPE)) {
                cell.addOccupant(new Armour(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, Bomb.STRING_TYPE)) {
                cell.addOccupant(new Bomb(dungeon, cell.getPosition(), false, getLogicString(entity)));
            } else if (Objects.equals(type, Key.STRING_TYPE)) {
                cell.addOccupant(new Key(dungeon, cell.getPosition(), entity.getInt("id")));
            } else if (Objects.equals(type, Door.STRING_TYPE)) {
                cell.addOccupant(new Door(dungeon, cell.getPosition(), entity.getInt("id")));
            } else if (Objects.equals(type, Boulder.STRING_TYPE)) {
                cell.addOccupant(new Boulder(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, Swamp.STRING_TYPE)) {
                cell.addOccupant(new Swamp(dungeon, cell.getPosition(), entity.getInt("movement_factor")));
            } else if (Objects.equals(type, Spider.STRING_TYPE)) {
                cell.addOccupant(new Spider(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, InvincibilityPotion.STRING_TYPE)) {
                cell.addOccupant(new InvincibilityPotion(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, HealthPotion.STRING_TYPE)) {
                cell.addOccupant(new HealthPotion(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, InvisibilityPotion.STRING_TYPE)) {
                cell.addOccupant(new InvisibilityPotion(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, Mercenary.STRING_TYPE)) {
                cell.addOccupant(new Mercenary(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, Assassin.STRING_TYPE)) {
                cell.addOccupant(new Assassin(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, Hydra.STRING_TYPE)) {
                cell.addOccupant(new Hydra(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, Player.STRING_TYPE)) {
                player = new Player(dungeon, cell.getPosition());
                cell.addOccupant(player);
            } else if (Objects.equals(type, Portal.STRING_TYPE)) {
                String colour = entity.getString("colour");

                Portal portal = new Portal(dungeon, cell.getPosition(), colour);
                // Check if there is another portal of the same colour
                Portal correspondingPortal = existsPortal(colour, map);

                if (correspondingPortal != null) {
                    portal.setCorrespondingPortal(correspondingPortal);
                    correspondingPortal.setCorrespondingPortal(portal);
                }
                cell.addOccupant(portal);
            } else if (Objects.equals(type, LightBulb.STRING_TYPE + LightBulb.ON)) {
                LightBulb lBulb = new LightBulb(dungeon, cell.getPosition(), getLogicString(entity));
                lBulb.activate();
                cell.addOccupant(lBulb);
            } else if (Objects.equals(type, LightBulb.STRING_TYPE + LightBulb.OFF)) {
                cell.addOccupant(new LightBulb(dungeon, cell.getPosition(), getLogicString(entity)));
            } else if (Objects.equals(type, Wire.STRING_TYPE)) {
                cell.addOccupant(new Wire(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, SwitchDoor.STRING_TYPE)) {
                cell.addOccupant(new SwitchDoor(dungeon, cell.getPosition(), entity.getInt("id"), getLogicString(entity)));
            }
            else {
                throw new Error("unhandled entity type: " + type);
            }
        }

        if (player == null) {
            throw new Error("the player's position wasn't specified");
        }
        map.setEntry(player.getPosition());
        dungeon.setPlayer(player);

        dungeon.hadEnemiesAtStartOfDungeon = map.allEntities().stream()
            .filter(e -> e instanceof MovingEntity && !(e instanceof Player)).count() > 0;

        return dungeon;
    }

    public static String getLogicString(JSONObject entity) {
        try {
            String logic = entity.getString("logic");
            return logic;
        } catch (JSONException e) {
            return null;
        }
    }
    
    public static Dungeon generateDungeon(Random r, Pos2d start, Pos2d end, GameMode mode) {

        Pos2d dims = new Pos2d(50, 50);
        List<List<BCell>> maze = GenerateMaze.make(r, dims, start, end);

        DungeonMap map = new DungeonMap(dims.getX(), dims.getY());
        Dungeon dungeon = new Dungeon(r, "generated", mode, map, new ExitGoal());

        Player player = new Player(dungeon, start);
        map.getCell(start).addOccupant(player);
        map.getCell(end).addOccupant(new Exit(dungeon, end));
        dungeon.setPlayer(player);

        for (int y = 0; y < dims.getY(); y++) {
            for (int x = 0; x < dims.getX(); x++) {
                if (maze.get(y).get(x) == BCell.WALL) {
                    map.getCell(x, y).addOccupant(new Wall(dungeon, new Pos2d(x, y)));
                }
            }
        }

        return dungeon;
    }

    /**
     * Places a Bomb that is Currently in Player Inventory onto Map &
     * Ensures that Player is Unable to Pick it Up Again
     */
    private void placeBomb(String itemUsed, CollectableEntity currCollectable) {
        //Get Player Positions & Collectables
        Cell playerCell = dungeonMap.getPlayerCell();
        Pos2d playerPosition = playerCell.getPosition();
        int playerXCoord = playerPosition.getX();
        int playerYCoord = playerPosition.getY();
        
        //Check the Collectable Passed to this Function is a Bomb and that the ID matches
        if ((currCollectable.getTypeAsString().equals(Bomb.STRING_TYPE)) && (itemUsed.equals(currCollectable.getId()))) {
            //Retreive Bomb Removed from Collectables that is To Be Placed
            CollectableEntity collectableRemoved = currCollectable;
            Bomb removedBomb = (Bomb) collectableRemoved;

            //Update Position and Set the Bombs is_placed status to be True so Bomb cannot be re-picked up
            removedBomb.setIsPlaced();
            removedBomb.setPosition(playerXCoord, playerYCoord);

            //Ensure that the Previously Triggered Flag on the Bomb is Set to False so Bomb does not Explode
            //if Placed Next to an Already Active Floor Switch
            removedBomb.resetAdjacentSwitchRecords();

            //Placing this Bomb on the Player's Cell
            playerCell.addOccupant(removedBomb);

            //Run the Check if ALready Triggered Check
            removedBomb.checkIfAlreadyTriggered();
        }
        
    }
    
    /**
     * Removes the item from cell if player picked it up. 
     */
    private void pickupCollectablesRemoveFromCell(Cell playerCell) {
        //Getting Occupants of Player's Cell
        List<Entity> playerCellOccupants = playerCell.getOccupants();
      

        //If No Items In Player's Cell, There are No Items to Pickup
        if (playerCellOccupants.size() == 0) {
            return;
        }

        //Removing Any Collectable Occupants from Current Cell as they Are Picked Up
        Entity removedOccupant = playerCellOccupants.get(0);
        boolean ifOccupantRemoved = false;
        for (Entity occupant : playerCellOccupants) {
            if (occupant instanceof CollectableEntity) {
                //Assign the Current Collectable Occupant in Cell to be Removed
                CollectableEntity collectableOccupant = (CollectableEntity)occupant;
                if (this.inventory.add(collectableOccupant)) {
                    ifOccupantRemoved = true;
                    removedOccupant = collectableOccupant;
                }
                
            }
        }
        if (ifOccupantRemoved == true) {
            //Remove the Assigned Collectable in Cell
            playerCell.removeOccupant(removedOccupant);
        }
    }

    /**
     * Picks Up the Collectable Entities that Are in the Player's Square
     * Runs Every Tick, After the Player Has Moved
     */ 
    private void pickupCollectableEntities(String itemUsed) {
        //Retreiving Player's Cell
        Cell playerCell = dungeonMap.getPlayerCell();
        if (playerCell == null) {
            return;
        }

        //Remove Collectable From Cell
        pickupCollectablesRemoveFromCell(playerCell);

    }
    
    public Pos2d getPlayerPosition() {
        return this.player.getPosition();
    }

    public Integer getTickCount() {
        return this.tickCount;
    }

    public void tick(String itemUsed, Direction movementDirection)
            throws IllegalArgumentException, InvalidActionException {

        assert this.battleStrategies.size() > 0;
        this.tickCount++;

        // PROBLEM: if we call tick as we iterate through the cells' entities
        // certain entities could get updated twice if they move down or left
        // SOLUTION: make a list of all the entities on the dungeonMap
        //           and *only* then call tick on them all
        this.player.handleMoveOrder(movementDirection);

        dungeonMap.flood();

        CollectableEntity item = null;
        if (itemUsed != null) item = inventory.useItem(itemUsed);
        if (item instanceof Potion) activePotions.add((Potion)item);
        if (item instanceof Bomb) placeBomb(itemUsed, item);
        
        // make sure all potion effects are applied and remove inactive potions.
        List<Potion> activePotionCpy = new ArrayList<>(activePotions);
        activePotionCpy.stream().forEach(pot -> {
            pot.tick();
            if (!pot.isActive()) activePotions.remove(pot);
        });
        dungeonMap.allEntities().stream()
            .filter(e -> !(e instanceof Potion))
            .forEach(entity -> entity.tick());
        

        //Dealing With Picking Up or Placing Collectable Entities
        pickupCollectableEntities(itemUsed);

        this.spawnSpiders();
        this.spawnMercenaries();
        this.spawnHydras();

        // perform battles
        this.battleStrategies.peek().findAndPerformBattles(this);
    }

    public void build(String buildable) throws InvalidActionException {
        // this could be done better, but with just two items it's fine.
        if (Objects.equals(buildable, Shield.STRING_TYPE)) {
            if (!Shield.craft(this.inventory)) {
                throw new InvalidActionException("not enough resources to build " + buildable);
            }
        } else if (Objects.equals(buildable, Bow.STRING_TYPE)) {
            if (!Bow.craft(this.inventory)) {
                throw new InvalidActionException("not enough resources to build " + buildable);
            }
        } else {
            throw new IllegalArgumentException("unknown buildable: " + buildable);
        }
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Check if the dungeon has been cleared
     */
    public boolean isCleared() {
        Goal goal = getGoal();
        return goal.isCompleted(this);
    }

    public Goal getGoal() {
        return this.goal;
    }

    public String getGoalAsString() {
        // Return a success message (empty goal string) if dungeon cleared
        if (isCleared()) {
            return "";
        }
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
    public void setPlayer(Player player) {
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

    public Inventory getInventory() {
        return this.inventory;
    }

    /**
     * Returns the Inventory in the form of a list of
     * ItemResponse instances. 
     */
    public List<ItemResponse> getInventoryAsItemResponse() {
        return this.inventory.asItemResponses();
    }

    
    /**
     * Attempts to bribe the map's mercenary raises an InvalidActionException
     * when:
     * - The player is not in range to bribe the mercenary
     * - The player does not have any gold.
     * 
     * removes a coin from the inventory on success. Also removes the OneRing if 
     * bribing an assassin.
     */
    public void bribeMercenary(Mercenary merc) throws InvalidActionException {
            
        if (merc.getCell().getPlayerDistance() > 2) throw new InvalidActionException("Too far, the mercenary can't hear you");
        if (!inventory.pay(merc.getPrice())) throw new InvalidActionException("The player can't pay the price");
            
        merc.bribe();
    }

    public void destroyZombieToastSpawner(ZombieToastSpawner zts) {
        // check if we are close enough to the spawner

        if (this.player.getPosition().squareDistance(zts.getPosition()) > 1) {
            throw new InvalidActionException("player is too far away from the spawner");
        }
        

        // check if we have a weapon to destroy the spawner with
        BattleItem weapon = this.inventory.getOneWeapon();
        if (weapon == null) {
            throw new InvalidActionException("no weapons to destroy the spawner");
        }

        // don't do it in one line, because asserts are disabled
        // by default (and thus the entity wouldn't be removed)
        boolean removed = this.dungeonMap.removeEntity(zts);
        assert removed;

        this.inventory.usedItemForBattle(weapon, BattleDirection.ATTACK);
    }

    /**
     * When you add a strategy, it doesn't mean it's the one that is going to be
     * used. The strategy with the highest precedence will be used.
     * 
     * @see BattleStrategy
     * @param bs
     */
    public void addBattleStrategy(BattleStrategy bs) {
        this.battleStrategies.add(bs);
    }

    /**
     * @param bs the battle strategy to remove
     * @return true if the battle strategy was present.
     */
    public boolean removeBattleStrategy(BattleStrategy bs) {
        return this.battleStrategies.remove(bs);
    }

    /**
     * These helper functions should only be called by Dungeon::tick
     */
    
    /**
     * helper function that is called once per tick
     */
    private void spawnMercenaries() {
        if (!this.hadEnemiesAtStartOfDungeon)
            return;

        if (this.tickCount % Mercenary.SPAWN_EVERY_N_TICKS != 0)
            return;

        // spawn an assassin 25% of the time.
        Mercenary m;
        if (r.nextInt(100) < Assassin.SPAWN_PERCENTAGE) m = new Assassin(this, this.dungeonMap.getEntry());
        else m = new Mercenary(this, this.dungeonMap.getEntry());

        this.dungeonMap.getCell(this.dungeonMap.getEntry()).addOccupant(m);
    }

    /**
     * helper function that is called once per tick
     */
    private void spawnSpiders() {
        long spiderPopulation = this.dungeonMap.allEntities().stream()
            .filter(e -> e instanceof Spider).count();

        if (spiderPopulation < Spider.MAX_SPIDERS && (this.tickCount % Spider.SPAWN_EVERY_N_TICKS == 0)) {
            Cell c = Spider.getRandomPosition(this);
            if (c != null) {
                c.addOccupant(new Spider(this, c.getPosition()));
            }
        }
    }

    /**
     * helper function that is called once per tick
     */
    private void spawnHydras() {
        // Spawn Hydra every 50 ticks and HARD MODE enabled
        if (getGameMode().equals(GameMode.HARD) && (this.tickCount % Hydra.SPAWN_EVERY_N_TICKS == 0)) {
            Hydra.spawnHydra(this);
        }
    }

    
    public BattleStrategy getBattleStrategy() {
        return this.battleStrategies.peek();
    }
}
