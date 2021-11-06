package dungeonmania.entities.movings;

import java.util.Random;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.DungeonMap;
import dungeonmania.Entity;
import dungeonmania.Pos2d;
import dungeonmania.battlestrategies.BattleStrategy.BattleDirection;
import dungeonmania.entities.Fighter;
import dungeonmania.entities.MovingEntity;
import dungeonmania.movement.CircleMovementBehaviour;

public class Spider extends MovingEntity implements Fighter {

    public static final String STRING_TYPE = "spider";
    public static final int MAX_SPIDERS = 5;
    public static final int SPAWN_EVERY_N_TICKS = 50;

    private float health = 1;
    

    /**
     * Generates a random location for the Spider to spawn.
     * @param dungeon
     * @return the cell on which the spawn the spider. {@code null} if there are
     * no cell on which a spider can spawn
     */
    public static Cell getRandomPosition(Dungeon dungeon) {
        DungeonMap dungeonMap = dungeon.getMap();
        int width = dungeonMap.getWidth();
        int height = dungeonMap.getHeight();

        if (width <= 2 || height <= 2) {
            // don't spawn anything if the spiders don't have the space to move around
            return null;
        }

        for (int i = 0; i < width * height; i++) {
            // For a map with i cells, loop i times
            Random random = new Random();
            int x = random.nextInt(width - 2) + 1;
            int y = random.nextInt(height - 2) + 1;
            Pos2d spawn = new Pos2d(x, y);

            if (!dungeonMap.getCell(spawn).hasBoulder()) {
                return dungeonMap.getCell(spawn);
            }
        }

        // Return null if no suitable location is found.
        return null;
    }

    public Spider(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        addMovementBehaviour(
            new CircleMovementBehaviour(
                4,
                dungeon.getMap(),
                dungeon.getMap().getCell(position)
            )
        );
    }

    @Override
    public void tick() {
        this.move();
    }

    @Override
    public String getTypeAsString() {
        return STRING_TYPE;
    }

    @Override
    public float getHealth() {
        return this.health;
    }

    @Override
    public void setHealth(float h) {
        this.health = h;
    }

    @Override
    public float getAttackDamage(Fighter target) {
        return 5;
    }

    @Override
    public float getDefenceCoef() {
        return 1;
    }

    @Override
    public void usedItemFor(BattleDirection d) {
        // does nothing since spiders cannot have/use items
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
