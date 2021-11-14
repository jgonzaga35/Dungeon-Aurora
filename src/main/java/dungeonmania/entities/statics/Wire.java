package dungeonmania.entities.statics;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.Pos2d;
import dungeonmania.entities.StaticEntity;

public class Wire extends StaticEntity {
    public static String STRING_TYPE = "wire";
    private List<Entity> connectedEntities = new ArrayList<Entity>();
    private List<String> connectedId = new ArrayList<String>();
    
    public Wire(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        addConnectedEntities(dungeon.getMap().getCell(position), new ArrayList<String>());
    }

    public List<Entity> getConnectedEntities() {
        return this.connectedEntities;
    }
    
    /**
     * Checks if the given entity can connect to the wire.
     * @param e
     * @return
     */
    @Override
    public boolean canConnect() {
        return true;
    }

    /**
     * Add all entities that are either switches or interact via switches
     * Every Wire should have a list of entities that are connected
     * to the circuit and not just the individual wire.
     */
    public void addConnectedEntities(Cell cell, List<String> connectedIds) {
        
        // Get cardinally adjacent cells
        Stream<Cell> adjacentCells = this.dungeon.getMap().getCellsAround(cell);
        
        // Add connected entities from adjacent cells 
        adjacentCells.forEach(c -> {
                            c.getOccupants().stream()
                            .filter(e -> e.canConnect())
                            .forEach(s -> {
                                System.out.println(s.getTypeAsString());
                                if (s instanceof Wire && !connectedIds.contains(s.getId())) {
                                    connectedId.add(s.getId());
                                    addConnectedEntities(c, connectedId);
                                } else {
                                    if (!connectedEntities.contains(s)) {
                                        connectedEntities.add(s);
                                        connectedId.add(s.getId());
                                    }
                                }
                            });
        });
    }

    public Integer countActivatedSwitches() {
        int count = (int) connectedEntities.stream()
                                           .filter(e -> e instanceof FloorSwitch && ((FloorSwitch) e).isActivated())
                                           .count();
        return count;
    }

    public boolean isActivated() {
        if (countActivatedSwitches() > 0) {
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

    }
    
}
