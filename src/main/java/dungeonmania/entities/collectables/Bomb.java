// java doesn't support static folders, since it's a folder, so we use the plural
package dungeonmania.entities.collectables;


import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;
import java.lang.Math;

import dungeonmania.Dungeon;
import dungeonmania.DungeonMap;
import dungeonmania.Pos2d;
import dungeonmania.entities.CollectableEntity;

import dungeonmania.util.Direction;

import dungeonmania.Cell;
import dungeonmania.Entity;


public class Bomb extends CollectableEntity {

    public static String STRING_TYPE = "bomb";

    public static int BLAST_RADIUS = 2;

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
     * Finds the Square Search Area within the Map
     * @return Hashtable<String, Integer> : Format 
     * [leftBlastXCoord, rightBlastXCoord, topBlastYCoord, bottomBlastYCoord]
     */
    private Hashtable<String, Integer> findBlastSearchArea() {
        Hashtable<String, Integer> outputDict = new Hashtable<String, Integer>();

        //Get Current Coords of Bomb
        int bombXCoord = this.position.getX();
        int bombYCoord = this.position.getY();
        outputDict.put("bombXCoord", bombXCoord);
        outputDict.put("bombYCoord", bombYCoord);
        
        //Get Width and Height
        DungeonMap map = this.dungeon.getMap();
        int width = map.getWidth();
        int height = map.getHeight();

        //Corners of Blast Radius Search Square
        int leftBlastXCoord = bombXCoord - BLAST_RADIUS;
        if (leftBlastXCoord < 0) {
            leftBlastXCoord = 0;
        }
        outputDict.put("leftBlastXCoord", leftBlastXCoord);
        
        int rightBlastXCoord = bombXCoord + BLAST_RADIUS;
        if (rightBlastXCoord > (width - 1)) {
            rightBlastXCoord = width - 1;
        }
        outputDict.put("rightBlastXCoord", rightBlastXCoord);

        int topBlastYCoord = bombYCoord - BLAST_RADIUS;
        if (topBlastYCoord < 0) {
            topBlastYCoord = 0;
        }
        outputDict.put("topBlastYCoord", topBlastYCoord);
        
        int bottomBlastYCoord = bombYCoord + BLAST_RADIUS;
        if (bottomBlastYCoord > (height - 1)) {
            bottomBlastYCoord = height - 1;
        }
        outputDict.put("bottomBlastYCoord", bottomBlastYCoord);

        return outputDict;
    }

    /**
     * Returns the Direct Distance Between the Inputted Column and Row Compared to the 
     * bomb coordinates.
     * @return radialDistance 
     */
    private double getRadialDistance(Hashtable<String, Integer> dimensionDetails, int col, int row) {
        //Check if Within Radius
        int currDistanceFromBombX = Math.abs(dimensionDetails.get("bombXCoord") - col);
        int currDistanceFromBombY = Math.abs(dimensionDetails.get("bombYCoord") - row);
        double radialDistance = Math.sqrt(Math.pow(currDistanceFromBombX, 2) + Math.pow(currDistanceFromBombY, 2));
        return radialDistance;
    }

    /**
     * Destroys all Occupants in Cell Given Column and Row, other than the player.
     */
    private void destroyOtherOccupantsCell(int col, int row) {
        //Retrieve Map, Cell and Current Occupants
        DungeonMap map = this.dungeon.getMap();
        Cell currCell = map.getCell(col, row);
        List<Entity> currCellOccupants = currCell.getOccupants();

        //Remove the Occupant if Not a Player
        Boolean isOccupantRemoved = false;
        Entity occupantRemoved = null;

        for (Entity occupant : currCellOccupants) {
            if (occupant.getTypeAsString() != "player") {
                isOccupantRemoved = true;
                occupantRemoved = occupant;
            }
        }
        
        if (isOccupantRemoved == true) {
            currCell.removeOccupant(occupantRemoved);
        }
        
        return;
    }

    /**
     * Destroys all Entities (excl Player) in blast radius
     * @return void
     */
    public void explode() {

        Hashtable<String, Integer> dimensionDetails = findBlastSearchArea();

        //Traversing through Blast Square
        for (int row=dimensionDetails.get("topBlastYCoord"); row <= dimensionDetails.get("bottomBlastYCoord"); row++) {
            for (int col=dimensionDetails.get("leftBlastXCoord"); col <= dimensionDetails.get("rightBlastXCoord"); col++) {
                double radialDistance = getRadialDistance(dimensionDetails, col, row);
                if (Math.floor(radialDistance) <= BLAST_RADIUS) {
                   destroyOtherOccupantsCell(col, row);
                }
            }
        } 
        return;
    }
    
    /**
     * Checks whether the bomb is Cardinally Adjacent to any Floor Switch
     */
    private boolean bombCheckCardinalAdjacency() {
        int bombXCoord = this.position.getX();
        int bombYCoord = this.position.getY();
        

        DungeonMap map = this.dungeon.getMap();
        Cell bombCell = map.getCell(bombXCoord, bombYCoord); 
        /*int width = map.getWidth();
        int height = map.getHeight();*/

        List<Cell> adjacentCells = new ArrayList<Cell>();

        //Get All Cells Cardinally Adjacent
        if (map.getCellAround(bombCell, Direction.UP) != null ) {
            adjacentCells.add(map.getCellAround(bombCell, Direction.UP));
        }
        if (map.getCellAround(bombCell, Direction.DOWN) != null) {
            adjacentCells.add(map.getCellAround(bombCell, Direction.DOWN));
        }
        if (map.getCellAround(bombCell, Direction.LEFT) != null) {
            adjacentCells.add(map.getCellAround(bombCell, Direction.LEFT));
        }
        if (map.getCellAround(bombCell, Direction.RIGHT) != null) {
            adjacentCells.add(map.getCellAround(bombCell, Direction.RIGHT));
        }

        //Iterate Through These Cardinally Adjacent Cells and Check if Any
        //Contain a Floor Switch
        if (adjacentCells == null) {
            return false;
        }

        for (Cell currentCell : adjacentCells) {
            if (currentCell.getOccupants() != null) {
                List<Entity> occupants = currentCell.getOccupants();
                for (Entity currOccupant: occupants) {
                    if (currOccupant != null) {
                        if (currOccupant.getTypeAsString() == "floor switch") {
                            return true;
                        }
                    }
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
     * Called when Bomb is Placed and after every tick.
     * Cause the Bomb to Explode if it is Cardinally Adjacent
     * to any Floor Switch.
     */
    @Override
    public void tick() {
        if (bombCheckCardinalAdjacency()) {
            explode();
        }
    }
}
