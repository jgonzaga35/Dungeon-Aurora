package dungeonmania.entities.statics;

import java.util.stream.Stream;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.StaticEntity;

public class Wire extends StaticEntity {
    public static String STRING_TYPE = "wire";

    public Wire(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    public Integer countAdjacentSwitches() {
        Cell base = this.dungeon.getMap().getCell(this.position);
        Stream<Cell> adjacentCells = this.dungeon.getMap().getCellsAround(base);
        int count = (int) adjacentCells.filter(e -> e.hasTriggeredFloorSwitch()).count();
        return count;
    }

    public boolean isActivated() {
        if (countAdjacentSwitches() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isInteractable() {
        return false;
    }

    @Override
    public String getTypeAsString() {
        return Wire.STRING_TYPE;
    }

    @Override
    public void tick() {
        // TODO Auto-generated method stub
        
    }
    
}
