package morphisme;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class LineView {
    private PointView start;
    private PointView end;
    private Line line;
    private double distance;

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
        this.distance = Math.sqrt(Math.pow(end.getCircle().getCenterX() - start.getCircle().getCenterX(), 2) + Math.pow(end.getCircle().getCenterY() - start.getCircle().getCenterY(), 2));
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public boolean intersects(LineView other) {
        double x1 = this.getStart().getCircle().getCenterX() + this.getStart().getCircle().getTranslateX();
        double y1 = this.getStart().getCircle().getCenterY() + this.getStart().getCircle().getTranslateY();
        double x2 = this.getEnd().getCircle().getCenterX() + this.getEnd().getCircle().getTranslateX();
        double y2 = this.getEnd().getCircle().getCenterY() + this.getEnd().getCircle().getTranslateY();
        double x3 = other.getStart().getCircle().getCenterX() + other.getStart().getCircle().getTranslateX();
        double y3 = other.getStart().getCircle().getCenterY() + other.getStart().getCircle().getTranslateY();
        double x4 = other.getEnd().getCircle().getCenterX() + other.getEnd().getCircle().getTranslateX();
        double y4 = other.getEnd().getCircle().getCenterY() + other.getEnd().getCircle().getTranslateY();
        if ((x1 == x3 && y1 == y3) || (x1 == x4 && y1 == y4) || (x2 == x3 && y2 == y3) || (x2 == x4 && y2 == y4)) {
            return false;
        } else {
            double denominator = ((x1 - x2) * (y3 - y4)) - ((y1 - y2) * (x3 - x4));
            if (denominator == 0) {
                return false;
            }
            double ua = (((x4 - x3) * (y1 - y3)) - ((y4 - y3) * (x1 - x3))) / denominator;
            double ub = (((x2 - x1) * (y1 - y3)) - ((y2 - y1) * (x1 - x3))) / denominator;
            if (ua >= 0 && ua <= 1 && ub >= 0 && ub <= 1) {
                double intersectionX = x1 + ua * (x2 - x1);
                double intersectionY = y1 + ua * (y2 - y1);
                if ((intersectionX == x1 && intersectionY == y1) ||
                    (intersectionX == x2 && intersectionY == y2) ||
                    (intersectionX == x3 && intersectionY == y3) ||
                    (intersectionX == x4 && intersectionY == y4)) {
                    return false;
                }
                return true;
            }
            return false;
        }
    }
}