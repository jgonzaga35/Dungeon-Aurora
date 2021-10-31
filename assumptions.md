- player cannot move outside the map

- when zombies move in a random direction, that direction cannot be
  Direction.NONE unless there all the cells around it are blocking.

- Spiders can't spawn in cells on the border of the map that cause them to move off the map when moving in circles.
- when the map is too small for a spider to make a full circle, no spider spawns
  (that is, when the map is smaller than 3x3)
- Spiders cannot spawn onto a cell where there is a boulder
- The maximum number of spiders at any given time is 5

- when a player and an enemy swap cell in one tick, there is no battle.
   https://edstem.org/au/courses/7065/discussion/660682
- players can't bribe mercenaries through walls.
- There can only be one mercenary on a map at a time.
- New mercenaries spawn every 10 ticks on maps with at least one enemy present at that point.
- Allies can see the player when invisible.
- IllegalArgumentException when the player bribes a friendly mercenary.

- Potions take effect immediately upon usage, affecting enemies in the same tick that it has been used.