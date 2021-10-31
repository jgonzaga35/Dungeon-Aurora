// java doesn't support static folders, since it's a folder, so we use the plural
package dungeonmania.entities.collectables;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.DungeonMap;
import dungeonmania.Pos2d;
import dungeonmania.entities.CollectableEntity;

import dungeonmania.Cell;
import dungeonmania.Entity;

public class Bomb extends CollectableEntity {

    public static String STRING_TYPE = "bomb";

    public static int BLAST_RADIUS = 1;

    public boolean isPlaced = false;

    public Bomb(Dungeon dungeon, Pos2d position, boolean isPlaced) {
        super(dungeon, position);
        this.isPlaced = isPlaced;
    }

    /**
     * 
     * @return If Item is Placed (boolean)
     */
    public boolean getIsPlaced() {
        return isPlaced;
    }

    /**
     * Destroys all Entities (excl Player) in blast radius
     * @return void
     */
    public void explode() {
        //Get Current Coords of Bomb
        int bombXCoord = this.position.getX();
        int bombYCoord = this.position.getY();
        
       

        //Corners of Blast Radius Search Square
        int leftBlastXCoord = bombXCoord - BLAST_RADIUS;
        if (leftBlastXCoord < 0) {
            leftBlastXCoord = 0;
        }
        int rightBlastXCoord = bombXCoord + BLAST_RADIUS;
        //if (rightBlastXCoord > )
        int topBlastYCoord = bombYCoord - BLAST_RADIUS;
        if (topBlastYCoord < 0) {
            topBlastYCoord = 0;
        }
        int bottomBlastYCoord = bombYCoord + BLAST_RADIUS;

        //Traversing through Potential Blast Radius
        //for (int i=topLeftBlastXCoord; i <= () ) 


        return;
    }
    
    private boolean bombCheckCardinalAdjacency() {
        int bombXCoord = this.position.getX();
        int bombYCoord = this.position.getY();
        
        DungeonMap map = this.dungeon.getMap();
        int width = map.getWidth();
        int height = map.getHeight();

        List<Cell> adjacentCells = new ArrayList<Cell>();
        Cell currCell;

        //Get Cell Above
        if (bombYCoord > 0) {
            currCell = map.getCell(bombXCoord, bombYCoord - 1);
            adjacentCells.add(currCell);
        } 

        //Get Cell Below
        if (bombYCoord == (height - 1)) {
            currCell = map.getCell(bombXCoord, bombYCoord + 1);
            adjacentCells.add(currCell);
        } 

        //Check if Floor Switch to Left
        if (bombXCoord > 0) {
            currCell = map.getCell(bombXCoord - 1, bombYCoord);
            adjacentCells.add(currCell);
        } 

        //Check if Floor Switch to Right
        if (bombYCoord == (width - 1)) {
            currCell = map.getCell(bombXCoord + 1, bombYCoord);
            adjacentCells.add(currCell);
        } 

        //Iterate Through These Cardinally Adjacent Cells and Check if Any
        //Contain a Floor Switch
        for (Cell currentCell : adjacentCells) {
            List<Entity> occupants = currentCell.getOccupants();
            for (Entity currOccupant: occupants) {
                if (currOccupant.getTypeAsString() == "floor switch") {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getTypeAsString() {
        return Armour.STRING_TYPE;
    }

    @Override
    public boolean isInteractable() {
        return false; // i don't think so at least
    }

    //If Cardinally Adjacent to Floor Switch Then Explode
    @Override
    public void tick() {
        if (bombCheckCardinalAdjacency()) {
            explode();
        }
    }
}
