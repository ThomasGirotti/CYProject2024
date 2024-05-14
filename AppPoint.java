/* Application avec points déplaçable dans un Node Pane et nommé. Les points peuvent
 * être redéplacé.
 */


import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.control.Label;
import java.util.*;
import javafx.scene.shape.Line;
public class AppPoint extends Application {
	private int i = 0;
	private double xMouse = 0d;
	private double yMouse = 0d;
	private HashMap<Circle, PointView> mapCP = new HashMap<>();
	private HashMap<Circle, Label> mapCL = new HashMap<>();
	
	//pour implémenter les lignes.
	private HashMap<Circle, List<Line>> mapCListL = new HashMap<>();
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Point");
		primaryStage.setHeight(500);
		primaryStage.setWidth(500);
		
		Pane p = new Pane();
		
		Scene root = new Scene(p, 500, 500);
		
		primaryStage.setScene(root);
		primaryStage.show();
		
		p.setOnMouseClicked(event ->{
			if(event.getTarget() instanceof Circle && event.isAltDown()) {
				Circle circleClicked = (Circle) event.getTarget();
				PointView pointViewClicked = (PointView) mapCP.get(circleClicked);
				Label labelClicked = (Label) mapCL.get(circleClicked);
				p.getChildren().remove(circleClicked);
				p.getChildren().remove(labelClicked);
				mapCP.remove(circleClicked);
				mapCL.remove(circleClicked);
			}
		    if(!(event.getTarget() instanceof Circle)) {
				xMouse = event.getX();
				yMouse = event.getY();
				i++;
				PointView pv = new PointView(xMouse, yMouse, "p"+i);
				 // Créez un Label pour le nom du PointView
		        Label nameLabel = new Label(pv.getName());
				mapCP.put( pv.getCircle(), pv);
				mapCL.put(pv.getCircle(), nameLabel);
				p.getChildren().add(pv.getCircle());

		        // Positionnez le Label à côté du cercle
		        nameLabel.setLayoutX(pv.getCircle().getCenterX() + pv.getCircle().getTranslateX() + pv.getCircle().getRadius() + 5);
		        nameLabel.setLayoutY(pv.getCircle().getCenterY() + pv.getCircle().getTranslateY() - 10);
		        p.getChildren().add(nameLabel);
			}
		});
		
		p.setOnMousePressed(event ->{
			if(event.getTarget() instanceof Circle) {
				Circle circlePressed = (Circle) event.getTarget();
				PointView pointViewPressed = mapCP.get(circlePressed);
				circlePressed.setRadius(2d);
				
				//on récupère la position initiale du cercle lorsqu'il est cliqué
			    pointViewPressed.setOrgSceneX(event.getSceneX());
		        pointViewPressed.setOrgSceneY(event.getSceneY());
		        
		        // on récupère la positio
		        pointViewPressed.setOrgTranslateX(circlePressed.getTranslateX());
		        pointViewPressed.setOrgTranslateY(circlePressed.getTranslateY());
			}
		});
		
		p.setOnMouseDragged(event -> {
		    if(event.getTarget() instanceof Circle) {
		        Circle circlePressed = (Circle) event.getTarget();
		        PointView pointViewPressed = mapCP.get(circlePressed);
		        Label label = mapCL.get(circlePressed);
		        // Calcule le déplacement par rapport aux coordonnées initiales de la souris
		        double offsetX = event.getSceneX() - pointViewPressed.getOrgSceneX();
		        double offsetY = event.getSceneY() - pointViewPressed.getOrgSceneY();

		        // Calcule les nouvelles coordonnées de translation en ajoutant le déplacement
		        // par rapport aux coordonnées initiales de translation du cercle
		        double newTranslateX = pointViewPressed.getOrgTranslateX() + offsetX;
		        double newTranslateY = pointViewPressed.getOrgTranslateY() + offsetY;
		        
		        // Met à jour les coordonnées de translation du cercle
		        circlePressed.setTranslateX(newTranslateX);
		        circlePressed.setTranslateY(newTranslateY);
		        
		        //Met à jour les coordonnées de translation du label.
		        label.setTranslateX(newTranslateX);
		        label.setTranslateY(newTranslateY);

		        // Met à jour les coordonnées du PointView
		        pointViewPressed.setX(circlePressed.getCenterX());
		        pointViewPressed.setY(circlePressed.getCenterY());
		    }
		});
		
		p.setOnMouseReleased(event -> {
			if(event.getTarget() instanceof Circle) {
				Circle circlePressed = (Circle) event.getTarget();
				PointView pointViewPressed = mapCP.get(circlePressed);
				circlePressed.setRadius(5d);
				
			}
		});
		
	
	}
    public static void main (String[] args) {
        launch(args);
    }
}
