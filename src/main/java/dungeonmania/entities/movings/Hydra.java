package dungeonmania.entities.movings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.Pos2d;
import dungeonmania.Utils;
import dungeonmania.entities.Fighter;
import dungeonmania.entities.MovingEntity;
import dungeonmania.movement.RandomMovementBehaviour;
import dungeonmania.battlestrategies.BattleStrategy.BattleDirection;

public class Hydra extends MovingEntity implements Fighter{
    
    public static final String STRING_TYPE = "hydra";
    public static final int SPAWN_EVERY_N_TICKS = 50;
    private float health = 4;  

    public Hydra(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);

        // Hydras are limited by the same movement constraints as Zombie Toasts
        this.addMovementBehaviour(new RandomMovementBehaviour(4, dungeon.getMap(), dungeon.getMap().getCell(position)));
    }

    /**
     * A Factory method that makes a new instance of Hydra 
     * at a random coordinate.
     * @param dungeon
     * @return
     */
    public static Hydra spawnHydra(Dungeon dungeon) {
        Cell cell = randomPosition(dungeon);
        if (cell != null) {
            Hydra hydra = new Hydra(dungeon, cell.getPosition());
            cell.addOccupant(hydra);
            return hydra;
        }
        return null;
    }

    /**
     * Generates a random location for the Hydra to spawn.
     * @param dungeon
     * @return
     */
    private static Cell randomPosition(Dungeon dungeon) {
        // check cells where can spawn a hydra
        List<Cell> availableCells = new ArrayList<Cell>();
        for (int y = 0; y < dungeon.getMap().getHeight(); y++) {
            for (int x = 0; x < dungeon.getMap().getWidth(); x++) {
                if (!dungeon.getMap().getCell(x,y).isBlocking() 
                && !dungeon.getMap().getCell(x,y).hasPlayer()) {
                    availableCells.add(dungeon.getMap().getCell(x, y));
                }
            }
        }

        if (availableCells.size() == 0) {
            return null; // don't spawn anything
        }

        // choose a random cell
        Cell cell = Utils.choose(availableCells);
        return cell;
    }

    @Override 
    public String getTypeAsString() {
        return Hydra.STRING_TYPE;
    }

    @Override 
    public void tick() {
        this.move();
    }

    @Override 
    public float getHealth() {
        return this.health;
    }

    @Override
    public void setHealth(float h) {

        float damageReceived = this.health - h; 
        Random random = this.dungeon.getRandom();
        int x = random.nextInt(2);
    
        if (x == 1) {
            this.health = h;
        } else if (x == 0) {
            this.health = this.health + damageReceived;
        }
    }

    @Override
    public float getAttackDamage() {
        return 1;
    }

    @Override
    public float getDefenceCoef() {
        return 1;
    }

    @Override
    public void usedItemFor(BattleDirection d) {
        // hydras don't use items, so that doesn't matter
    }

    @Override
    public FighterRelation getFighterRelation() {
        return FighterRelation.ENEMY;
    }

    @Override
    public Entity getEntity() {
        return this;
    }

}
