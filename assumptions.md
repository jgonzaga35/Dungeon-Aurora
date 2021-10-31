# Assumptions

## Player
- player cannot move outside the map

## ZombieToast
- when zombies move in a random direction, that direction cannot be
  Direction.NONE unless there all the cells around it are blocking.

## Spiders
- Spiders bounce off of map borders like boulders.
- when the map is too small for a spider to make a full circle, no spider spawns
  (that is, when the map is smaller than 3x3)
- Spiders cannot spawn onto a cell where there is a boulder
- The maximum number of spiders at any given time is 5

## Mercenaries
- players can't bribe mercenaries through walls.
- There can only be one mercenary on a map at a time.
- New mercenaries spawn every 10 ticks on maps with at least one enemy present at that point.
- IllegalArgumentException when the player bribes a friendly mercenary.

## Battles
- when a player and an enemy swap cell in one tick, there is no battle.
   https://edstem.org/au/courses/7065/discussion/660682

## Potions
- Allies can see the player when invisible.