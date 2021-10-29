1. player cannot move outside the map
2. when zombies move in a random direction, that direction cannot be
   Direction.NONE unless there all the cells around it are blocking.
3. when a player and an enemy swap cell in one tick, there is no battle.
   https://edstem.org/au/courses/7065/discussion/660682
4. when the map is too small for a spider to make a full circle, no spider spawn