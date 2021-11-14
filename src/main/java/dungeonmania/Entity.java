package dungeonmania;

import java.util.UUID;
import java.util.stream.Stream;

import dungeonmania.DungeonManiaController.LayerLevel;
import dungeonmania.entities.Fighter;
import dungeonmania.util.Direction;

/**
 * A superclass that represents all entities inside dungeon.
 * Each entity has a unique entity id.
 */
public abstract class Entity {
    private String id;

    protected Dungeon dungeon;
    protected Pos2d position;

    /**
     * Constructor for Entities
     * @param Dungeon dungeon
     * @param Pos2d position where the Entity is located
     */
    public Entity(Dungeon dungeon, Pos2d position) {
        this.id = "Entity-" + UUID.randomUUID().toString();
        this.dungeon = dungeon;
        this.position = position;
    }

    /**
     * Returning the ID of the Entity
     * @return String ID of entity
     */
    public String getId() {
        return this.id;
    }

    /**
     * @return the cell this entity is on
     */
    public Cell getCell() {
        return dungeon.getMap().getCell(position);
    }

    /**
     * Returning the Position of the Entity
     * @return Pos2d position of the entity
     */
    public Pos2d getPosition() {
        return position;
    }

    /**
     * Sets the Position of the Entity
     * @param int XCoord 
     * @param int YCoord
     */
    public void setPosition(int XCoord, int YCoord) {
        this.position.setX(XCoord);
        this.position.setY(YCoord);
    }

    /**
     * Find the Cell in the Direction from the Current Cell
     * @param Direction 
     * @return Cell 
     */
    public Cell inspectCell(Direction d) {
        return dungeon.getMap().getCellAround(dungeon.getMap().getCell(position), d);
    }

    /**
     * Note that it doesn't always return 4 cells. If you are on a top-most
     * cell, it will only return (left, bottom, right)
     * @return cells around the current cell
     */
    public Stream<Cell> getCellsAround() {
        // this will be easier once we get DungeonMap
        return Stream.of(
            this.dungeon.getMap().getCellAround(this.getCell(), Direction.UP),
            this.dungeon.getMap().getCellAround(this.getCell(), Direction.DOWN),
            this.dungeon.getMap().getCellAround(this.getCell(), Direction.LEFT),
            this.dungeon.getMap().getCellAround(this.getCell(), Direction.RIGHT)
        ).filter(cell -> cell != null);
    }

    @Override
    public String toString()
    {
        if (this instanceof Fighter) {
            Fighter f = (Fighter) this;
            return String.format("%s[pos=%s health=%f]", getTypeAsString(), getPosition(), f.getHealth());
        }
        return String.format("%s[pos=%s]", getTypeAsString(), getPosition());
    }

    /**
     * Find if there is a relationship between entities
     * @return FighterRelation relationship between fighters
     */
    public abstract boolean isInteractable();

    public abstract LayerLevel getLayerLevel();

    /**
     * Return the Type of the Entity as a String
     * @return String type of entity
     */
    public abstract String getTypeAsString();

    /**
     * Triggers the passive effects/behaviours of the entity.
     */
    public abstract void tick();
}