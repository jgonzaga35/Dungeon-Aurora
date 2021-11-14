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
- New mercenaries spawn every 50 ticks on maps with at least one enemy present
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
- Anduril causes Hydra to lose health when it is attacked and
removes the possibility of regaining health

## One Ring
- the player spawns back on the entry of the map
- if there is a boulder there, then the player doesn't spawn back (dies even he
  has the ring). This creates a nice balance: put a boulder on the entry so that
  mercenaries don't spawn, but be careful because it cancels the one ring

## Midnight Armour
- Midnight Armour will be displayed as buildable on the frontend once them player has enough resources to build it - even if there are zombies still remaining on the map. When the player tries to build the item while there are zombies the game will display an error.

## Bombs
- Blast radius is set to 2
- A bomb will only explode if it is placed next to a deactivated switch and then that switch is activated
  (will not explode if placed next to an already activated switch)

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
