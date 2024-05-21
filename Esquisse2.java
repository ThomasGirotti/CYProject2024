import java.util.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Esquisse2 {
    private String name;
    private HashMap<Circle, PointView> mapCP = new HashMap<>();
    private List<PointView> pointViewList = new ArrayList<>();
    private List<Line> lineList = new ArrayList<>();

    private boolean isCycle = false;

    private PointView firsPointView;
    public Esquisse2(String name){
        this.name = name;
    }

    public List<Line> getLineList(){
        return lineList;
    }

    public List<PointView> getPointViewList() {
        return pointViewList;
    }

    public PointView getFirsPointView() {
        return firsPointView;
    }

    public void addPoint(double x, double y){
        PointView pvCree = new PointView(x,y,"p"+pointViewList.size());
        mapCP.put(pvCree.getCircle(),pvCree);
        pointViewList.add(pvCree);
        
        if(pointViewList.size() > 1){
            lineList.add(new Line());
        }

        this.updateLines();



    }
    // à finir
    public void cycled(){
        pointViewList.add(pointViewList.get(0));
        lineList.add(new Line());
        for(PointView pv : pointViewList){
            pv.getCircle().setFill(Color.BLUE);
        }
        this.updateLines();
        isCycle = true;

    }

    public boolean isCycle(){
        return isCycle;
    }

    public PointView lastAddPoint(){
        return pointViewList.getLast();
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

    public void updatePoint(){
        int i = 0;
        List<PointView> pvtemp = new ArrayList<>();
        for( PointView pv : this.pointViewList){
            pvtemp.add(pv);
            pv.getLabel().setText("p"+i);
            i++;
        }
        this.pointViewList = pvtemp;

        firsPointView = pointViewList.getFirst();

    }

    public  PointView removePoint(Circle circleR){
        PointView pvR = this.mapCP.get(circleR);
        pointViewList.remove(pvR);
        updatePoint();
        if(!lineList.isEmpty()){
            lineList.removeLast();
            updateLines();
        }
        
        mapCP.remove(circleR);

        return pvR;
        
        
    }

    public void pointPressed(Circle circle, double orgSceneX, double orgSceneY, double xTranslate, double yTranslate){
        circle.setRadius(10d);
        PointView pointView = mapCP.get(circle);
        pointView.setOrgSceneX(orgSceneX);
        pointView.setOrgSceneY(orgSceneY);
        pointView.setOrgTranslateX(xTranslate);
        pointView.setOrgTranslateY(yTranslate);
    }

    public void movePoint(Circle circle, double x, double y){
        PointView pv = mapCP.get(circle);
        double offsetX = x - pv.getOrgSceneX();
        double offsetY = y - pv.getOrgSceneY();

        double newTranslateX = pv.getOrgTranslateX() + offsetX;
        double newTranslateY = pv.getOrgTranslateY() + offsetY;

        //Pour le Circle
        circle.setTranslateX(newTranslateX);
        circle.setTranslateY(newTranslateY);
        
        //Pour le Label
        Label label = pv.getLabel();


        label.setTranslateX( newTranslateX);
        label.setTranslateY( newTranslateY);
        
        updateLines();
    }

    public void clear(){
        mapCP.clear();
        pointViewList.clear();
        lineList.clear();
        isCycle = false;
    }
}
