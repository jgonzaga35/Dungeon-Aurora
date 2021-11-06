package dungeonmania.entities.movings;

import java.util.List;
import java.util.Random;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.DungeonMap;
import dungeonmania.Entity;
import dungeonmania.Pos2d;
import dungeonmania.entities.Fighter;
import dungeonmania.entities.MovingEntity;
import dungeonmania.movement.RandomMovementBehaviour;
import dungeonmania.battlestrategies.BattleStrategy.BattleDirection;

public class Hydra extends MovingEntity implements Fighter{
    
    public static final String STRING_TYPE = "hydra";
    private float health = 20; // dont know what health should be 

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
     * Generates a random location for the Spider to spawn.
     * @param dungeon
     * @return
     */
    private static Cell randomPosition(Dungeon dungeon) {
        // check cells where can spawn a zombie
        List<Cell> availableCells = this.getCellsAround()
            .filter(cell -> !cell.isBlocking())
            .collect(Collectors.toList());

        if (availableCells.size() == 0)
            return; // don't spawn anything

        // choose a random cell
        Cell cell = Utils.choose(availableCells);
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

        float damageReceived = getHealth() - h*5; 
        Random random = new Random();
        int x = random.nextInt(1);
        if (x == 0) {
            this.health = h;
        } else if (x == 1) {
            this.health += damageReceived;
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
