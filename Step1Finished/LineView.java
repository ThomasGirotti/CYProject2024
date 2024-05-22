package ui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class LineView {
    private PointView start;
    private PointView end;
    private Line line;

    public LineView(PointView start, PointView end) {
        this.start = start;
        this.end = end;
        this.line = new Line();
        this.line.setStroke(Color.RED);
        update();
    }

    public void update() {
        line.setStartX(start.getCircle().getCenterX() + start.getCircle().getTranslateX());
        line.setStartY(start.getCircle().getCenterY() + start.getCircle().getTranslateY());
        line.setEndX(end.getCircle().getCenterX() + end.getCircle().getTranslateX());
        line.setEndY(end.getCircle().getCenterY() + end.getCircle().getTranslateY());
    }

    public PointView getStart() {
        return start;
    }

    public PointView getEnd() {
        return end;
    }

    public Line getLine() {
        return line;
    }
}