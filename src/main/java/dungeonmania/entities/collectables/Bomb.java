// java doesn't support static folders, since it's a folder, so we use the plural
package dungeonmania.entities.collectables;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Hashtable;
import java.lang.Math;

import dungeonmania.Dungeon;
import dungeonmania.DungeonMap;
import dungeonmania.Pos2d;
import dungeonmania.DungeonManiaController.LayerLevel;
import dungeonmania.entities.LogicalEntity;
import dungeonmania.entities.statics.FloorSwitch;
import dungeonmania.entities.movings.Player;
import dungeonmania.util.Direction;

import dungeonmania.Cell;
import dungeonmania.Entity;


public class Bomb extends LogicalEntity {

    public static String STRING_TYPE = "bomb";

    public static int BLAST_RADIUS = 2;

    public boolean isPlaced = false;

    public Logic logic;

    private Hashtable<String, Boolean> adjacentSwitchStatus = new Hashtable<String, Boolean>();

    public Bomb(Dungeon dungeon, Pos2d position, boolean isPlaced, String logic) {
        super(dungeon, position, logic);
        this.isPlaced = isPlaced;
        this.logic = parseLogic(logic);
    }

    /**
     * 
     * @return If Item is Placed (boolean)
     */
    public boolean getIsPlaced() {
        return isPlaced;
    }

    /**
     * This check is always run after the object is created to ensure that if the bomb is placed next
     * to an already activated floor switch it does not explode.
     */
    public void checkIfAlreadyTriggered() {
        bombCheckCardinalAdjacency();
    }

    /**
     * Resets the previously triggered flag to false if the item is picked up and then placed again.
     */
    public void resetAdjacentSwitchRecords() {
        adjacentSwitchStatus = new Hashtable<String, Boolean>();
        return; 
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

        //Remove the Occupant if it is Not the Player
        Entity occupant = null;
        for (int i=0; i < currCellOccupants.size(); i++) {
            occupant = currCellOccupants.get(i);
            if (occupant.getTypeAsString().equals(Player.STRING_TYPE) == false) {
                currCell.removeOccupant(occupant);
                i--;
            }
        }
        
        return;
    }

    /**
     * Destroys all Entities (excl Player) in blast radius
     * @return void
     */
    @Override
    public void activate() {
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
     * Checks if the inputted switch has been activated after bomb was placed.
     * @return Boolean  
     */
    private boolean checkSwitchNewlyActivated(Entity currOccupant) {
        //Confirmed Occupant is a Floor Switch
        FloorSwitch currentSwitch = (FloorSwitch) currOccupant;

        //Adding Switch to the Dictionary
        String currId = currentSwitch.getId();
        Boolean currSwitchActivated = currentSwitch.isActivated();

        if (adjacentSwitchStatus.containsKey(currId) == false) {
            //If this switch has not yet been checked
            adjacentSwitchStatus.put(currId, currSwitchActivated); 
        } else {
            //If switch has already been checked
            if (adjacentSwitchStatus.get(currId) == false) {
                //If the current switch was previously not activated
                adjacentSwitchStatus.put(currId, currSwitchActivated);
                if (currSwitchActivated == true) {
                    return true;
                }
            }  
        }

        return false;
    }

    /**
     * Checks whether the bomb is Cardinally Adjacent to any Floor Switch
     */
    private boolean bombCheckCardinalAdjacency() {
        int bombXCoord = this.position.getX();
        int bombYCoord = this.position.getY();

        DungeonMap map = this.dungeon.getMap();
        Cell bombCell = map.getCell(bombXCoord, bombYCoord); 

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

        for (Cell currentCell : adjacentCells) {
            if (currentCell.getOccupants() != null) {
                List<Entity> occupants = currentCell.getOccupants();
                for (Entity currOccupant: occupants) {
                    if (currOccupant != null) {
                        if (currOccupant.getTypeAsString().equals(FloorSwitch.STRING_TYPE)) {
                            if (checkSwitchNewlyActivated(currOccupant) == true) {
                                return true;
                            }
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
    public boolean canConnect() {
        return true;
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
        this.getConnectedEntities()
        .stream()
        .filter(e -> e instanceof FloorSwitch )
        .forEach(f -> f.tick());

        if (Objects.isNull(logic)) {
            if (bombCheckCardinalAdjacency()) {
                activate();
            }
        } else if (Objects.equals(logic, Logic.AND)) {
            andActivation();
        } else if (Objects.equals(logic, Logic.OR)) {
            orActivation();
        } else if (Objects.equals(logic, Logic.XOR)) {
            xorActivation();
        } else if (Objects.equals(logic, Logic.CO_AND)) {
            co_andActivation();
        }
    }


    @Override
    public LayerLevel getLayerLevel() {
        return LayerLevel.COLLECTABLE;
    }

    @Override
    public void deactivate() {

        
    }


}
