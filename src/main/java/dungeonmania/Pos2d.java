package dungeonmania;

import java.util.Objects;

import dungeonmania.util.Position;

public class Pos2d {
    private int x;
    private int y;

    public Pos2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public int squareDistance(Pos2d o) {
        return (o.getX() - this.getX()) * (o.getX() - this.getX()) + (o.getY() - this.getY()) * (o.getY() - this.getY());
    }

    public Pos2d plus(Pos2d o) {
        return new Pos2d(o.getX() + this.x, o.getY() + this.y);
    }

    public static Pos2d from(Position pos) {
        return new Pos2d(pos.getX(), pos.getY());
    }

    @Override
    public String toString() {
        return "(" + getX() + ", " + getY() + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;

        Pos2d checkPos = (Pos2d) obj;

        return x == checkPos.getX() && y == checkPos.getY();
    }

}