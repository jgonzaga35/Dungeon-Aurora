package dungeonmania.entities.collectables.consumables;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.movings.Player;

public class HealthPotion extends Potion {

    public static String STRING_TYPE = "health_potion";

    public HealthPotion(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        maxDuration = -1;
    }

    @Override
    public void applyEffectsEveryTick() {
        // No lasting effects
        
    }
    
    @Override
    public void expire() {
        // No lasting effects
        
    }

    @Override
    public String getTypeAsString() {
        return HealthPotion.STRING_TYPE;
    }

    @Override
    public void onDrink() {
        Player player = (Player) dungeon.getMap()
            .allEntities().stream()
            .filter(e -> e instanceof Player).findFirst().get();
        
        player.setHealth(10);
    }
    
}
