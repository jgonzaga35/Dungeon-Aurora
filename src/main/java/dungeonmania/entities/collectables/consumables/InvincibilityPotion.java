package dungeonmania.entities.collectables.consumables;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;

public class InvincibilityPotion extends Potion {

    public static String STRING_TYPE = "invincibility_potion";

    public InvincibilityPotion(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        duration = 5;
    }

    // @Override
    // public void use() {
    //     dungeon.getOccupants().stream()
    //     .filter(e -> e instanceof MovingEntity)
    //     .filter(e -> !(e instanceof Player))
    //     .forEach(e -> {
    //         MovingEntity enemy = (MovingEntity) e;
    //         enemy.addMovementBehaviour(
    //             new FleeMovementBehaviour(2, dungeon.getMap(), enemy.getCell())
    //         );
    //     });
    // }

    @Override
    public String getTypeAsString() {
        return STRING_TYPE;
    }

    @Override
    public void drink() {
        // TODO Auto-generated method stub
        
    }
}
