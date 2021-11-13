package dungeonmania.entities.statics;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.DungeonMap;
import dungeonmania.Pos2d;
import dungeonmania.entities.StaticEntity;

public class LightBulb extends StaticEntity {
    public static String STRING_TYPE = "light_bulb";
    public static String ON = "_on";
    public static String OFF = "_off";

    public boolean switchedOn = false;
    public String logic;

    public LightBulb(Dungeon dungeon, Pos2d position, String logic) {
        super(dungeon, position);
        this.logic = logic;
    }

    
    public void switchOn() {
        this.switchedOn = true;
    }

    public Integer countAdjacentActivations(DungeonMap map) {
        Cell base = map.getCell(this.position);
        Stream<Cell> adjacentCells = map.getCellsAround(base);
        int count = (int) adjacentCells.filter(e -> e.hasActivatedEntity()).count();
        return count;
    }
    

    @Override
    public boolean isInteractable() {
        return true;
    }

    /**
     * Returns the type of the entity as a string.
     */
    @Override
    public String getTypeAsString() {
        if (this.switchedOn) {
            return LightBulb.STRING_TYPE + LightBulb.ON;
        }
        return LightBulb.STRING_TYPE + LightBulb.OFF;
    }

    @Override
    public void tick() {

    }
    
}
