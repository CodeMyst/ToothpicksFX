package rs.myst;

import javafx.scene.paint.Color;
import mars.drawingx.drawing.View;
import mars.geometry.Vector;

import java.util.List;

public class Toothpick {
    private final int ax;
    private final int ay;
    private final int bx;
    private final int by;
    private final int dir;
    private boolean isNew = true;

    public Toothpick(int x, int y, int dir) {
        this.dir = dir;
        if (dir == 1) {
            ax = x - Main.LEN / 2;
            bx = x + Main.LEN / 2;
            ay = y;
            by = y;
        } else {
            ax = x;
            bx = x;
            ay = y - Main.LEN / 2;
            by = y + Main.LEN / 2;
        }
    }

    public boolean getNew() {
        return isNew;
    }

    public void setNew(boolean v) {
        isNew = v;
    }

    public int getX() {
        return ax;
    }

    public void draw(View view, float factor) {
        view.setStroke(Color.WHITE);
        if (isNew) view.setStroke(Color.BLUE);
        view.setLineWidth(2 / factor);
        view.strokeLine(new Vector(ax, ay), new Vector(bx, by));
    }

    public boolean intersects(int x, int y) {
        if (ax == x && ay == y) return true;
        return bx == x && by == y;
    }

    public Toothpick create(List<Toothpick> others, boolean top) {
        boolean available = true;

        int x = top ? ax : bx;
        int y = top ? ay : by;

        // check every other toothpick, if it intersects then it's not available
        for (Toothpick other : others) {
            if (other != this && other.intersects(x, y)) available = false;
        }

        // if available create a new toothpick and change its direciton
        if (available) {
            return new Toothpick(x, y, dir * -1);
        }

        return null;
    }
}
