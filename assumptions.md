# Assumptions

## Player
- player cannot move outside the map

## ZombieToast
- when zombies move in a random direction, that direction cannot be
  Direction.NONE unless there all the cells around it are blocking.

## ZombieToastSpawners

- valid weapons to destroy zombie toast spawners are bows and swords.
  https://edstem.org/au/courses/7065/discussion/661191

## Spiders
- Spiders bounce off of map borders like boulders.
- when the map is too small for a spider to make a full circle, no spider spawns
  (that is, when the map is smaller than 3x3)
- Spiders cannot spawn onto a cell where there is a boulder
- The maximum number of spiders at any given time is 5

## Mercenaries
- players can't bribe mercenaries through walls.
- New mercenaries spawn every 20 ticks on maps with at least one enemy present
  at the *start of the map.*
- IllegalArgumentException when the player bribes a friendly mercenary.
- One treasure is the price to bribe a mercenary
- Mercenaries don't start their own battles (they wait for the player to
  initiate them)
- Battle radius is a circle, and the mercenary needs to be completely within
  that circle.
- Battle radius of mercenaries is 3 cells
- When there is no path to the player for the mercenary, the mercenary will more to the cell with the lowest `movement_factor`.
- When a boulder has been pushed onto the player starting position mercenaries and assassins are prevented from spawning.

## Assassins
- Assassins just deal more damage by simply having more health than assassins (3x more).
- There is a 25% chance for a assassin to spawn in the place of a mercenary.

## Battles
- when a player and an enemy swap cell in one tick, there is no battle.
   https://edstem.org/au/courses/7065/discussion/660682

## Potions
- Allies can see the player when invisible.
- Potions take effect immediately upon usage, affecting enemies in the same tick that it has been used.
- When enemies are fleeing from an invincible player, they do not plan their path but simply run in the cardinal direction that maximizes the distance between them and the player. They do not try to avoid hindrances like swamps.

## Hydras
- Hydras can't spawn on blocking cells or on the player.
- Hydras have 20 health points.
- Hydras health has a 50% chance of increasing instead of decreasing
by the attack damage amount when attacked

## Anduril
- Anduril has an initial durability of 20
- Anduril has base damage of 7
- Anduril causes Hydra to lose health when it is attacked and removes the possibility of regaining health

## One Ring
- the player spawns back on the entry of the map
- if there is a boulder there, then the player doesn't spawn back (dies even he
  has the ring). This creates a nice balance: put a boulder on the entry so that
  mercenaries don't spawn, but be careful because it cancels the one ring

## Bombs
- Blast radius is set to 2
- A bomb will only explode if it is placed next to a deactivated switch and then that switch is activated
  (will not explode if placed next to an already activated switch)

## Sun Stone
- Sun Stone automatically opens doors as with a key (does not call the tick method to use it)
- Once the Sun Stone is collected under no circumstances can it be removed including through usage
- If the Sun Stone is used to build an item it is removed
- The Sun Stone can be used in place of Treasure and if there is both Treasure and the Sun Stone in inventory
  to build a buildable entity, the Treasure will be ket.
- The only exception to the above is when crafting the Sceptre buildable entity, in which the Sun Stone cannot be used 
  interchangably with Treasure when crafting with recipes that include both the Sun Stone and Treasure.

## Sceptre
- When the player has the scepter and a coin available the scepter is used to bribe mercenaries to avoid spending coins. 
- Triggered through interact.
- Recipe precedence:

| Order of use | Recipe                                  |
| ------------ | --------------------------------------- |
| 1            | `sun_stone` + `treasure` + `2 * arrow`  |
| 2            | `sun_stone` + `treasure` + `wood`       |
| 3            | `sun_stone` + `key` + `2 * arrow`       |
| 4            | `sun_stone` + `key` + `wood`            |

## Battles

| Entity       | Health | Attack Damage | Spawns Every        |
| ------------ | ------ | ------------- | ------------------- |
| Player       | 10     | 1             | n/a                 |
| Zombie Toast | 4      | 1             | 20 or 15            |
| Mercenary    | 6      | 1             | 50                  |
| Assassin     | 18     | 1             | 25% chance every 50 |
| Hydra        | 20     | 1             | 50                  |

Mercenary battle radius: 3
Assassin battle radius: 3

| Item   | Damage Bonus | Defence Bonus | Durability |
| ------ | ------------ | ------------- | ---------- |
| Bow    | 1            | 3             | 100        |
| Shield | 0            | 3             | 20         |
| Sword  | 2            | 1             | 20         |
| Armour | 0            | 2             | 30         |

## Loot
- Entities that drop loot when they die, have that loot in their inventory the whole time. 
- These entities use any items in their inventories in battle (excluding the one ring).
- Item drop probabilities:

| *Spawn Probability Matrix* (%) | Armour         | OneRing        | Anduril        |
| ------------                   | ------         | ------         | ------         |
| Mercenary                      | 30             | 10             | 20             |
| Assassin                       | 30             | 10             | 20             |
| Hydra                          | 0              | 10             | 20             |
| ZombieToast                    | 15             | 10             | 20             |
| Spider                         | 0              | 10             | 20             |
## Logic Switches
In each tick of the dungeon, we first run the ticks for each floor switch before running
the ticks for other entities that use logic. 
Example: if there is a regular switch, an XOR switch and an XOR lightbulb connected to
the same wires/circuit, then if the regular switch is activated, the XOR switch will also activate,
and finally the XOR lightbulb will not activate since it receives 2 activation signals.
