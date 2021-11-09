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

## Assassins
- Assassins just deal more damage by simply having more health than assassins (3x more).
- There is a 25% chance for a assassin to spawn in the place of a mercenary.

## Battles
- when a player and an enemy swap cell in one tick, there is no battle.
   https://edstem.org/au/courses/7065/discussion/660682

## Potions
- Allies can see the player when invisible.
- Potions take effect immediately upon usage, affecting enemies in the same tick that it has been used.

## Hydras
- Hydras can't spawn on blocking cells or on the player.

## Bombs
- Blast radius is set to 2
- A bomb will only explode if it is placed next to a deactivated switch and then that switch is activated
  (will not explode if placed next to an already activated switch)
