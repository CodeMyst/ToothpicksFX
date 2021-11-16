package rs.myst;

import javafx.scene.paint.Color;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import mars.geometry.Transformation;
import mars.geometry.Vector;
import mars.input.InputEvent;
import mars.input.InputState;

import java.util.ArrayList;
import java.util.List;

public class Main implements Drawing {
    // length of every toothpick
    public static final int LEN = 63;

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;

    private final List<Toothpick> toothpicks = new ArrayList<>();

    // min and max toothpic X pos, used for calculating the scale
    private int minX = -WIDTH / 2;
    private int maxX = HEIGHT / 2;

    private int updateCounter = 0;

    private boolean start = false;

    @Override
    public void init(View view) {
        toothpicks.add(new Toothpick(0, 0, 1));
    }

    @Override
    public void receiveEvent(View view, InputEvent event, InputState state, Vector pointerWorld, Vector pointerViewBase) {
        if (event.isMouseButton()) start = true;
    }

    @Override
    public void draw(View view) {
        // speed limiter
        if (!start) return;
        updateCounter++;
        if (updateCounter < 0) return;
        else updateCounter = 0;

        DrawingUtils.clear(view, Color.BLACK);

        float scale = (float) WIDTH / (maxX - minX);

        view.setTransformation(new Transformation().scale(scale));

        for (Toothpick toothpick : toothpicks) {
            toothpick.draw(view, scale);
            minX = Math.min(toothpick.getX(), minX);
            maxX = Math.max(toothpick.getX(), maxX);
        }

        List<Toothpick> next = new ArrayList<>();

        // find free ends of each toothpick
        // only do this for "new" toothpicks
        for (Toothpick toothpick : toothpicks) {
            if (!toothpick.getNew()) continue;

            Toothpick nextA = toothpick.create(toothpicks, true);
            Toothpick nextB = toothpick.create(toothpicks, false);

            if (nextA != null) next.add(nextA);
            if (nextB != null) next.add(nextB);

            toothpick.setNew(false);
        }

        toothpicks.addAll(next);
    }

    public static void main(String[] args) {
        DrawingApplication.launch(WIDTH, HEIGHT);
    }
}
