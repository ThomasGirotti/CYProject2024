package ui;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PointView {
    private int orgSceneX, orgSceneY;
    private int orgTranslateX, orgTranslateY;
    private String name;
    private Circle circle;
    private Label label;
    private Point point;

    public PointView(int x, int y, String name) {
        this.name = name;
        this.point = new Point(x, y, name);
        this.circle = new Circle(x, y, 5d); // Crée un cercle avec le rayon de 5 pixels
        this.circle.setFill(Color.RED); // Définit la couleur de remplissage à rouge
    }

    public Circle getCircle() {
        return this.circle;
    }

    public String getName() {
        return this.name;
    }

    public Point getPoint() {
        return this.point;
    }

    public void setX(double x) {
        circle.setCenterX(x);
    }

    public void setY(double y) {
        circle.setCenterY(y);
    }

    public void setOrgSceneX(int orgSceneX) {
        this.orgSceneX = orgSceneX;
    }

    public void setOrgSceneY(int orgSceneY) {
        this.orgSceneY = orgSceneY;
    }

    public void setOrgTranslateX(int orgTranslateX) {
        this.orgTranslateX = orgTranslateX;
    }

    public void setOrgTranslateY(int orgTranslateY) {
        this.orgTranslateY = orgTranslateY;
    }

    public double getOrgTranslateX() {
        return orgTranslateX;
    }

    public double getOrgTranslateY() {
        return orgTranslateY;
    }

    public double getOrgSceneX() {
        return orgSceneX;
    }

    public double getOrgSceneY() {
        return orgSceneY;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public void updatePointCoordinates() {
        this.point.setX((int) (circle.getCenterX() + circle.getTranslateX()));
        this.point.setY((int) (circle.getCenterY() + circle.getTranslateY()));
    }
}
