package ui;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Esquisse {
    private String name;
    private Map<Circle, PointView> mapCP = new HashMap<>();
    private List<PointView> pointViewList = new ArrayList<>();
    private List<Line> lineList = new ArrayList<>();
    private boolean isCycle = false;

    public Esquisse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Line> getLineList() {
        return lineList;
    }

    public List<PointView> getPointViewList() {
        return pointViewList;
    }

    public void addPoint(int x, int y, String pointName) {
        PointView pvCree = new PointView(x, y, pointName);
        mapCP.put(pvCree.getCircle(), pvCree);
        pointViewList.add(pvCree);
        if (pointViewList.size() > 1) {
            lineList.add(new Line());
        }
        this.updateLines();
    }

    public void cycled() {
        pointViewList.add(pointViewList.get(0));
        lineList.add(new Line());
        for (PointView pv : pointViewList) {
            pv.getCircle().setFill(Color.BLUE);
        }
        this.updateLines();
        isCycle = true;
    }

    public boolean isCycle() {
        return isCycle;
    }

    public void updateLines() {
        for (int i = 0; i < lineList.size(); i++) {
            Line line = lineList.get(i);
            PointView start = pointViewList.get(i);
            PointView end = pointViewList.get(i + 1);
            line.setStartX(start.getCircle().getCenterX() + start.getCircle().getTranslateX());
            line.setStartY(start.getCircle().getCenterY() + start.getCircle().getTranslateY());
            line.setEndX(end.getCircle().getCenterX() + end.getCircle().getTranslateX());
            line.setEndY(end.getCircle().getCenterY() + end.getCircle().getTranslateY());
        }
    }

    public void clear() {
        mapCP.clear();
        pointViewList.clear();
        lineList.clear();
        isCycle = false;
    }

    public void pointPressed(Circle circle, int orgSceneX, int orgSceneY, int xTranslate, int yTranslate) {
        circle.setRadius(10d);
        PointView pointView = mapCP.get(circle);
        pointView.setOrgSceneX(orgSceneX);
        pointView.setOrgSceneY(orgSceneY);
        pointView.setOrgTranslateX(xTranslate);
        pointView.setOrgTranslateY(yTranslate);
    }

    public void movePoint(Circle circle, double x, double y) {
        PointView pv = mapCP.get(circle);
        double offsetX = x - pv.getOrgSceneX();
        double offsetY = y - pv.getOrgSceneY();
        double newTranslateX = pv.getOrgTranslateX() + offsetX;
        double newTranslateY = pv.getOrgTranslateY() + offsetY;
        circle.setTranslateX(newTranslateX);
        circle.setTranslateY(newTranslateY);
        Label label = pv.getLabel();
        label.setTranslateX(newTranslateX);
        label.setTranslateY(newTranslateY);
        updateLines();
    }
}