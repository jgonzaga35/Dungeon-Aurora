- player cannot move outside the map
- when zombies move in a random direction, that direction cannot be
  Direction.NONE unless there all the cells around it are blocking.
- when the map is too small for a spider to make a full circle, no spider spawns
  (that is, when the map is smaller than 3x3)
