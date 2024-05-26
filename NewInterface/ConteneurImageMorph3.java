package morphisme;

import java.util.ArrayList;
import java.util.Comparator;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class ConteneurImageMorph3 extends ConteneurImage{
    //Constructeur faisant appel au constructeur de la classe mère
    public ConteneurImageMorph3(Stage primaryStage, int screenX, int screenY, ImageImportListener listener) {
        super(primaryStage, screenX, screenY, listener);
    }

    protected void rollbackPoints(int removedPointNumber){
        super.rollbackPoints(removedPointNumber);
        drawDroite();
    }

    protected void handleMousePressed(MouseEvent event) {
        super.handleMousePressed(event);
        drawDroite();
    }

    protected void handleMouseDragged(MouseEvent event) {
        super.handleMouseDragged(event);
        voidDroite();
    }

    protected void handleMouseReleased(MouseEvent event) {
        drawDroite();
    }

    protected void fileSelect() {
        super.fileSelect(getPhoto());
        if (getPhoto().getImage() != null) {
            initialisationPointsVisage();
        }
    }

    //Méthode permettant de réinitialiser les points
    protected void resetPoints() {
        super.resetPoints();
        initialisationPointsVisage();
    }

    //Méthode permettant d'initialiser les points du visage dans les coins
    private void initialisationPointsVisage(){
        PointView pv1 = new PointView(0, 0, "P" + getCompteurPoints());
        pv1.setName("P0");
        getPoints().add(pv1);
        getOverlayPane().getChildren().add(pv1.getCircle());
        PointView pv2 = new PointView(0, (int) getPhoto().getImageView().getFitHeight(), "P" + getCompteurPoints());
        pv2.setName("P1");
        getPoints().add(pv2);
        getOverlayPane().getChildren().add(pv2.getCircle());
        PointView pv3 = new PointView((int) getPhoto().getImageView().getFitWidth(), (int) getPhoto().getImageView().getFitHeight(), "P" + getCompteurPoints());
        pv3.setName("P2");
        getPoints().add(pv3);
        getOverlayPane().getChildren().add(pv3.getCircle());
        PointView pv4 = new PointView((int) getPhoto().getImageView().getFitWidth(), 0, "P" + getCompteurPoints());
        pv4.setName("P3");
        getPoints().add(pv4);
        getOverlayPane().getChildren().add(pv4.getCircle());
        for (PointView pointView : getPoints()) {
            pointView.getCircle().setMouseTransparent(true);
        }
        setCompteurPoints(4);
    }

    private void voidDroite() {
        getOverlayPane().getChildren().removeIf(node -> node instanceof Line);
    }

    protected void drawDroite() {
        getOverlayPane().getChildren().removeIf(node -> node instanceof Line);
        getLines().clear();
        ArrayList<LineView> lineMarquage = new ArrayList<>();
        for (PointView point : getPoints()) {
            for (PointView point2 : getPoints()) {
                if (!point.equals(point2)) {
                    LineView lineView = new LineView(point, point2);
                    lineView.setDistance(Math.sqrt(Math.pow(lineView.getEnd().getPoint().getX() - lineView.getStart().getPoint().getX(), 2) + Math.pow(lineView.getEnd().getPoint().getY() - lineView.getStart().getPoint().getY(), 2)));
                    getLines().add(lineView);
                }
            }
        }
        getLines().sort(Comparator.comparingDouble(LineView::getDistance));
        for (LineView line : getLines()) {
            boolean coupe = false;
            for (LineView lineMarquageParcours : lineMarquage) {
                if (line.intersects(lineMarquageParcours)) {
                    coupe = true;
                    break;
                }
            }
            if (!coupe && !lineMarquage.contains(new LineView(line.getEnd(), line.getStart()))) {
                lineMarquage.add(line);
                line.getLine().setMouseTransparent(true);
                getOverlayPane().getChildren().add(line.getLine());
                setLines(lineMarquage);
            }
        }
    }
}