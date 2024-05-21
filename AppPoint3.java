import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.*;
import javafx.scene.shape.Line;

public class AppPoint3 extends Application {
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    public void start(Stage primaryStage){
        primaryStage.setTitle("Point");
        primaryStage.setHeight(500);
        primaryStage.setWidth(500);
         
        Pane p = new Pane();
        
        Scene root = new Scene(p, 500, 500);
         
        primaryStage.setScene(root);
        primaryStage.show();

        //créé une esquisse sans point
        Esquisse2 esquisse = new Esquisse2("inshallah ça fonctionne");
        
        //ctrl + C + Entré: clear le pane et clear l'esquisse.
        root.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            pressedKeys.add(event.getCode());
            if (pressedKeys.contains(KeyCode.CONTROL) && pressedKeys.contains(KeyCode.C) && pressedKeys.contains(KeyCode.ENTER)) {
                p.getChildren().clear();
                esquisse.clear();
            }
        });

        root.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            pressedKeys.remove(event.getCode());
        });
        

        //Pour tout les click de souris
        p.setOnMouseClicked(event ->{
            
            //clique sur p0: on cycle
            if(event.getTarget() instanceof Circle){
                if(((Circle) event.getTarget()).equals(esquisse.getPointViewList().getFirst().getCircle())){
                    esquisse.cycled();
                    p.getChildren().add(esquisse.getLineList().getLast());
                }
            }

            //si clique sur point + alt  et pas de cycle: on supprime le point 
            if(event.getTarget() instanceof Circle && event.isAltDown() && !esquisse.isCycle()){
                List<Line> lineList = esquisse.getLineList();
                if(!lineList.isEmpty()){
                    p.getChildren().remove(lineList.getLast());
                }

                PointView pvR = esquisse.removePoint((Circle) event.getTarget()); 
                p.getChildren().remove(pvR.getCircle());
                p.getChildren().remove(pvR.getLabel());

            }

            // si on clique pas sur un cercle et qu'il n'y a pas de cycle: ajouter un point
            if(!(event.getTarget() instanceof Circle) && !esquisse.isCycle()){
                double xMouse = event.getX();
                double yMouse = event.getSceneY();

                esquisse.addPoint(xMouse, yMouse);
                PointView pv = esquisse.lastAddPoint();
                p.getChildren().add(pv.getCircle());
                p.getChildren().add(pv.getLabel());
                List<Line> lineList = esquisse.getLineList();
                if(!lineList.isEmpty()){
                    p.getChildren().add(lineList.getLast());
                }
            }

        });



        p.setOnMousePressed(event ->{
            //si on appuie sur un cercle il grossi et on stocke les données de déplcement si drag se suit
            if(event.getTarget() instanceof Circle){
                Circle circle = (Circle) event.getTarget();
                esquisse.pointPressed(circle, event.getSceneX(), event.getSceneY(), circle.getTranslateX(), circle.getTranslateY());  //implémenter pointPressed(Circle circle, double orgScene, )
            
            }
        });

        //déplacer les points
        p.setOnMouseDragged(event ->{
            if(event.getTarget() instanceof Circle){
                esquisse.movePoint((Circle) event.getTarget(), event.getSceneX(), event.getSceneY()); // implémenter movePoint(Circle circle, double sceneX, double sceneY)
            }
        });

        //remettre point à la taille initiale
        p.setOnMouseReleased(event ->{
            if(event.getTarget() instanceof Circle){
                Circle circle = (Circle) event.getTarget();
                circle.setRadius(5d);
            }
        });
    }
    public static void main(String[] args){
        launch(args);
    }
}