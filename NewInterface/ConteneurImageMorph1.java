package morphisme;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ConteneurImageMorph1 extends ConteneurImage{
    //Constructeur faisant appel au constructeur de la classe mère
    public ConteneurImageMorph1(Stage primaryStage, int screenX, int screenY, ImageImportListener listener) {
        super(primaryStage, screenX, screenY, listener);
    }

    //Méthode permettant de rollback les points
    @Override
    protected void rollbackPoints(int removedPointNumber){
        super.rollbackPoints(removedPointNumber);
        updateLines();
    }

    //Méthode permettant de mettre à jour les lignes
    protected void updateLines() {
        getOverlayPane().getChildren().removeIf(node -> node instanceof Line);
        getLines().clear();
        for (int i = 0; i < getPoints().size() - 1; i++) {
            LineView lineView = new LineView(getPoints().get(i), getPoints().get(i + 1));
            getLines().add(lineView);
            lineView.getLine().setMouseTransparent(true);
            getOverlayPane().getChildren().add(lineView.getLine());
        }
        if (getPoints().size() > 2) {
            PointView lastPoint = getPoints().get(getPoints().size() - 1);
            PointView firstPoint = getPoints().get(0);
            LineView cycleLine = new LineView(lastPoint, firstPoint);
            getLines().add(cycleLine);
            cycleLine.getLine().setMouseTransparent(true);
            getOverlayPane().getChildren().add(cycleLine.getLine());
        }
    }

    @Override
    protected void handleMousePressed(MouseEvent event) {
        if (getPhoto().getImage() != null) {
            if (!(event.getTarget() instanceof Circle)) {
                int xMouse = (int) event.getX();
                int yMouse = (int) event.getY();
                int setx = (int) (xMouse / getPhoto().getImageView().getFitWidth() * getImageBrute().getWidth());
                int sety = (int) (yMouse / getPhoto().getImageView().getFitHeight() * getImageBrute().getHeight());
                PointView pv = new PointView(xMouse, yMouse, "P" + getCompteurPoints());
                pv.getPoint().setX(setx);
                pv.getPoint().setY(sety);
                pv.getCircle().setOnMouseEntered(e -> pv.getCircle().setRadius(10d));
                pv.getCircle().setOnMouseExited(e -> pv.getCircle().setRadius(5d));
                Label nameLabel = new Label(pv.getName());
                nameLabel.setTextFill(Color.BLACK);
                nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                pv.setLabel(nameLabel);
                getPoints().add(pv);
                getOverlayPane().getChildren().add(pv.getCircle());
                nameLabel.setLayoutX(pv.getCircle().getCenterX() + pv.getCircle().getTranslateX() + pv.getCircle().getRadius() + 5);
                nameLabel.setLayoutY(pv.getCircle().getCenterY() + pv.getCircle().getTranslateY() - 10);
                getOverlayPane().getChildren().add(nameLabel);
                if (getPoints().size() > 1) {
                    addLine(getPoints().get(getPoints().size() - 2), pv);
                }
                setCompteurPoints(getCompteurPoints() + 1);
            } else {
                Circle circlePressed = (Circle) event.getTarget();
                PointView pointViewPressed = getPoints().stream()
                    .filter(pv -> pv.getCircle().equals(circlePressed))
                    .findFirst().orElse(null);
                if (pointViewPressed != null) {
                    circlePressed.setRadius(10d);
                    pointViewPressed.setOrgSceneX((int) event.getSceneX());
                    pointViewPressed.setOrgSceneY((int) event.getSceneY());
                    pointViewPressed.setOrgTranslateX((int) circlePressed.getTranslateX());
                    pointViewPressed.setOrgTranslateY((int) circlePressed.getTranslateY());
                }
            }
        } else {
            fileSelect(getPhoto());
        }
    }

    @Override
    protected void handleMouseDragged(MouseEvent event) {
        super.handleMouseDragged(event);
        updateLines();
    }

    private void addLine(PointView startPoint, PointView endPoint) {
        LineView lineView = new LineView(startPoint, endPoint);
        getLines().add(lineView);
        updateLines();
    }
}