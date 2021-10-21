package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class DungeonMap {

    private Integer explorationLevel;
    private List<List<Cell>> dungeonMap = new ArrayList<>();
    private int width;
    private int height;
    
    DungeonMap(JSONObject json) {
        this.width = json.getInt("width");
        this.height = json.getInt("height");
        
        // a grid of empty cells
        for (int y = 0; y < height; y++) {
            ArrayList<Cell> row = new ArrayList<>();
            for (int x = 0; x < width; x++) {
                row.add(new Cell(new Pos2d(x, y)));
            }
            dungeonMap.add(row);
        }
    }
    
    public Cell getCell(Pos2d pos) {
        return dungeonMap.get(pos.getY()).get(pos.getX());
    }

    public Cell getCell(int x, int y) {
        return dungeonMap.get(y).get(x);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
    
}
