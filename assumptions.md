- player cannot move outside the map
- when zombies move in a random direction, that direction cannot be
  Direction.NONE unless there all the cells around it are blocking.
- Spiders can't spawn in cells on the border of the map that cause them to move off the map when moving in circles.
- when the map is too small for a spider to make a full circle, no spider spawns
  (that is, when the map is smaller than 3x3)
- when a player and an enemy swap cell in one tick, there is no battle.
   https://edstem.org/au/courses/7065/discussion/660682