import java.util.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;

public class EsquisseMorph2 {
    private String name;
    private HashMap<Circle, PointView> mapCP = new HashMap<>();
    private List<PointView> pointViewList = new ArrayList<>();
    private List<CubicCurve> curveList = new ArrayList<>();
    private Map<CubicCurve,Map<String,PointView>> curveControlPVMap = new HashMap<>();

    private boolean isCycle = false;

    private PointView firsPointView;
    public Esquisse21(String name){
        this.name = name;
    }

    public Map<CubicCurve, Map<String, PointView>> getCurveControlPVMap() {
        return curveControlPVMap;
    }

    public List<CubicCurve> getCurveList(){
        return curveList;
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
            CubicCurve curve = new CubicCurve();
            curveList.add(curve);
            curve.setFill(null);
            curve.setStroke(Color.BLACK);
            curve.setStrokeWidth(2);
            HashMap<String,PointView> controlMap = new HashMap<>();
            PointView c1 = new PointView(pointViewList.get(pointViewList.size() - 2).getCircle().getCenterX() + 10, pointViewList.get(pointViewList.size() - 2).getCircle().getCenterX() + 10, "c1");
            PointView c2 = new PointView(x+10, y+10, "c2");
            controlMap.put("c1", c1);
            controlMap.put("c2", c2);
            curveControlPVMap.put(curve, controlMap);
            curve.setControlX1(controlMap.get("c1").getCircle().getCenterX());
            curve.setControlX1(controlMap.get("c1").getCircle().getCenterY());
            curve.setControlX2(x+10);
            curve.setControlY2(y+10);
            mapCP.put(c1.getCircle(),c1);
            mapCP.put(c2.getCircle(), c2);
        }

        this.updateLines();



    }
    // à finir
    public void cycled(){
        pointViewList.add(pointViewList.get(0));
        CubicCurve curve = new CubicCurve();
        curveList.add(curve);
        HashMap<String,PointView> controlMap = new HashMap<>();
        PointView c1 = new PointView(pointViewList.get(pointViewList.size() - 2).getCircle().getCenterX() + 10, pointViewList.get(pointViewList.size() - 2).getCircle().getCenterX() + 10, "c1");
        PointView c2 = new PointView(pointViewList.getFirst().getCircle().getCenterX()+10, pointViewList.getFirst().getCircle().getCenterY()+10, "c2");
        controlMap.put("c1", c1);
        controlMap.put("c2", c2);
        curveControlPVMap.put(curve, controlMap);
        curve.setControlX1(controlMap.get("c1").getCircle().getCenterX());
        curve.setControlX1(controlMap.get("c1").getCircle().getCenterY());
        curve.setControlX2(pointViewList.getFirst().getCircle().getCenterX()+10);
        curve.setControlY2(pointViewList.getFirst().getCircle().getCenterY()+10);
        mapCP.put(c1.getCircle(),c1);
        mapCP.put(c2.getCircle(), c2);
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
        for (int i = 0; i < curveList.size(); i++) {
            CubicCurve line = curveList.get(i);
            PointView start = pointViewList.get(i);
            PointView end = pointViewList.get(i + 1);
            PointView c1 = curveControlPVMap.get(curveList.get(i)).get("c1");
            PointView c2 = curveControlPVMap.get(curveList.get(i)).get("c2");

            line.setStartX(start.getCircle().getCenterX() + start.getCircle().getTranslateX());
            line.setStartY(start.getCircle().getCenterY() + start.getCircle().getTranslateY());
            line.setEndX(end.getCircle().getCenterX() + end.getCircle().getTranslateX());
            line.setEndY(end.getCircle().getCenterY() + end.getCircle().getTranslateY());
            line.setControlX1(c1.getCircle().getCenterX() + c1.getCircle().getTranslateX());
            line.setControlY1(c1.getCircle().getCenterY() + c1.getCircle().getTranslateY());
            line.setControlX2(c2.getCircle().getCenterX() + c2.getCircle().getTranslateX());
            line.setControlY2(c2.getCircle().getCenterY() + c2.getCircle().getTranslateY());
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

    public  ArrayList<PointView> removePoint(Circle circleR){
        PointView pvR = this.mapCP.get(circleR);
        pointViewList.remove(pvR);
        updatePoint();
        ArrayList<PointView> pvRList = new ArrayList<>();
        pvRList.add(pvR);
        if(!curveList.isEmpty()){
            CubicCurve curve = curveList.removeLast();
            Map<String,PointView> pvRmap =curveControlPVMap.remove(curve);
            pvRList.add(pvRmap.get("c1"));
            pvRList.add(pvRmap.get("c2"));
            updateLines();
        }
        
        mapCP.remove(circleR);

        return pvRList;
        
        
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
        curveList.clear();
        isCycle = false;
    }
}
