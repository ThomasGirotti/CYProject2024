import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.input.MouseEvent;
import javafx.event.*;

public class PointView {
	double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;
    private String name;
    private Circle circle;
    private Point point;
    public PointView(double x, double y, String name) {
        this.name = name;
    	this.circle = new Circle(x, y, 5d); // Crée un cercle avec le rayon de 10 pixels
        this.circle.setFill(Color.RED); // Définit la couleur de remplissage à rouge
       
    }
    
    public Circle getCircle() {
    	return this.circle;
    }
    
    public String getName() {
    	return this.name;
    }
    
    public void setX(double x) {
        circle.setCenterX(x);
    }

    public void setY(double y) {
        circle.setCenterY(y);
    }

    public void setOrgSceneX(double orgSceneX) {
        this.orgSceneX = orgSceneX;
    }

    public void setOrgSceneY(double orgSceneY) {
        this.orgSceneY = orgSceneY;
    }

    public void setOrgTranslateX(double orgTranslateX) {
        this.orgTranslateX = orgTranslateX;
    }

    public void setOrgTranslateY(double orgTranslateY) {
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
    
}
