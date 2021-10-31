// java doesn't support static folders, since it's a folder, so we use the plural
package dungeonmania.entities.collectables;


import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

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
        System.out.println("Is Placed in Bomb Constructor");
        System.out.println(isPlaced);
        this.isPlaced = isPlaced;
    }

    /**
     * 
     * @return If Item is Placed (boolean)
     */
    public boolean getIsPlaced() {
        System.out.println("Is Placed Value");
        System.out.println(isPlaced);
        return isPlaced;
    }

    /**
     * Destroys all Entities (excl Player) in blast radius
     * @return void
     */

    
    public void explode() {
        System.out.println("Now Exploding");
        //Get Current Coords of Bomb
        System.out.println("Bomb Coords");
        int bombXCoord = this.position.getX();
        System.out.println(bombXCoord);
        int bombYCoord = this.position.getY();
        System.out.println(bombYCoord);
        
        //Get Width and Height
        DungeonMap map = this.dungeon.getMap();
        System.out.println("Map Dimensions");
        int width = map.getWidth();
        System.out.println(width);
        int height = map.getHeight();
        System.out.println(height);

        //Corners of Blast Radius Search Square
        int leftBlastXCoord = bombXCoord - BLAST_RADIUS;
        if (leftBlastXCoord < 0) {
            leftBlastXCoord = 0;
        }
        System.out.println("LeftX");
        System.out.println(leftBlastXCoord);
        int rightBlastXCoord = bombXCoord + BLAST_RADIUS;
        if (rightBlastXCoord > (width - 1)) {
            rightBlastXCoord = width - 1;
        }
        System.out.println("RightX");
        System.out.println(rightBlastXCoord);
        int topBlastYCoord = bombYCoord - BLAST_RADIUS;
        if (topBlastYCoord < 0) {
            topBlastYCoord = 0;
        }
        System.out.println("TopY");
        System.out.println(topBlastYCoord);
        int bottomBlastYCoord = bombYCoord + BLAST_RADIUS;
        if (bottomBlastYCoord > (height - 1)) {
            bottomBlastYCoord = height - 1;
        }
        System.out.println("BottomY");
        System.out.println(bottomBlastYCoord);

        //Traversing through Blast Square
        for (int row=topBlastYCoord; row <= bottomBlastYCoord; row++) {
            System.out.println("Iterated through Row");
            for (int col=leftBlastXCoord; col <= rightBlastXCoord; col++) {
                System.out.println("Iterated Through Col");
                //Check if Within Radius
                int currDistanceFromBombX = Math.abs(bombXCoord - col);
                int currDistanceFromBombY = Math.abs(bombYCoord - row);
                double radialDistance = Math.sqrt(Math.pow(currDistanceFromBombX, 2) + Math.pow(currDistanceFromBombY, 2));
                System.out.println(radialDistance);
                if (Math.floor(radialDistance) <= BLAST_RADIUS) {
                    System.out.println("Within Blast Radius");
                    //Destroy All Elements Other Than Player in Cell
                    Cell currCell = map.getCell(col, row);
                    List<Entity> currCellOccupants = currCell.getOccupants();
                    Boolean isOccupantRemoved = false;
                    Entity occupantRemoved = null;
                    for (Entity occupant : currCellOccupants) {
                        System.out.println(occupant.toString());
                        if (occupant.getTypeAsString() != "player") {
                            System.out.println("Removed Occupants");
                            isOccupantRemoved = true;
                            occupantRemoved = occupant;
                        }
                    }
                    if (isOccupantRemoved == true) {
                        currCell.removeOccupant(occupantRemoved);
                    }
                }
            }
        } 
        return;
    }
    
    private boolean bombCheckCardinalAdjacency() {
        System.out.println("Checking for Adjacency");
        System.out.println("Bomb Coords");
        int bombXCoord = this.position.getX();
        System.out.println(bombXCoord);
        int bombYCoord = this.position.getY();
        System.out.println(bombYCoord);
        
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
        if (bombYCoord <= (height - 1)) {
            currCell = map.getCell(bombXCoord, bombYCoord + 1);
            adjacentCells.add(currCell);
        } 

        //Check if Floor Switch to Left
        if (bombXCoord > 0) {
            currCell = map.getCell(bombXCoord - 1, bombYCoord);
            adjacentCells.add(currCell);
        } 

        //Check if Floor Switch to Right
        if (bombYCoord <= (width - 1)) {
            currCell = map.getCell(bombXCoord + 1, bombYCoord);
            adjacentCells.add(currCell);
        } 

        System.out.println(adjacentCells.toString());
        //Iterate Through These Cardinally Adjacent Cells and Check if Any
        //Contain a Floor Switch
        for (Cell currentCell : adjacentCells) {
            List<Entity> occupants = currentCell.getOccupants();
            System.out.println(occupants.toString());
            for (Entity currOccupant: occupants) {
                if (currOccupant.getTypeAsString() == "floor switch") {
                    System.out.println("Floor switch detected");
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getTypeAsString() {
        return Bomb.STRING_TYPE;
    }

    @Override
    public boolean isInteractable() {
        return false; // i don't think so at least
    }
    
    public void setIsPlaced() {
        this.isPlaced = true;
    }

    /**
     * Called when Bomb is Placed
     */
    @Override
    public void tick() {
        //If Bomb is Cardinally Adjacent to Floor Switch then Explode
        //setIsPlaced();
        if (bombCheckCardinalAdjacency()) {
            System.out.println("Is Adjacent to Switch");
            explode();
        }
    }
}
