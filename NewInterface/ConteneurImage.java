package morphisme;

import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ConteneurImage {
    private ImagePlus photo = new ImagePlus();
    private Pane overlayPane;
    private ScrollPane scrollPane;
    private VBox conteneurImage;
    private ImageImportListener listener;
    private ArrayList<PointView> points = new ArrayList<>();
    private ArrayList<LineView> lines = new ArrayList<>();
    private int compteurPoints = 0;
    private Image imageBrute;
    private FileChooser fileChooser = new FileChooser();
    private Stage primaryStage;
    private int screenX;
    private int screenY;

    public ConteneurImage(Stage primaryStage, int screenX, int screenY, ImageImportListener listener) {
        this.primaryStage = primaryStage;
        this.screenX = screenX;
        this.screenY = screenY;
        this.listener = listener;
        initUI();
    }

    private void initUI() {
        photo.getImageView().setFitHeight(0.95 * screenY);
        photo.getImageView().setFitWidth(0.95 * screenY);
        photo.getImageView().setPreserveRatio(true);

        overlayPane = new Pane();
        overlayPane.prefWidthProperty().bind(photo.getImageView().fitWidthProperty());
        overlayPane.prefHeightProperty().bind(photo.getImageView().fitHeightProperty());

        scrollPane = new ScrollPane();
        scrollPane.setContent(photo);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        conteneurImage = new VBox();
        conteneurImage.getChildren().addAll(scrollPane, photo);
        conteneurImage.setAlignment(Pos.CENTER);

        scrollPane.prefWidthProperty().bind(photo.getImageView().fitWidthProperty());
        scrollPane.prefHeightProperty().bind(photo.getImageView().fitHeightProperty());
        scrollPane.addEventFilter(ScrollEvent.SCROLL, Event::consume);
        scrollPane.setContent(new StackPane(photo, overlayPane));

        overlayPane.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                handleMousePressed(event);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                handleMouseRightPressed(event);
            }
        });

        overlayPane.setOnMouseDragged(event -> {
            if (event.getTarget() instanceof Circle) {
                handleMouseDragged(event);
            }
        });

        overlayPane.setOnMouseReleased(event -> {
            if (event.getTarget() instanceof Circle) {
                handleMouseReleased(event);
            }
        });
    }

    public VBox getConteneurImage() {
        return conteneurImage;
    }

    protected void handleMouseReleased(MouseEvent event) {
        updateLabels();
    }

    protected void handleMousePressed(MouseEvent event) {
        if (photo.getImage() != null) {
            if (!(event.getTarget() instanceof Circle)) {
                int xMouse = (int) event.getX();
                int yMouse = (int) event.getY();
                int setx = (int) (xMouse / photo.getImageView().getFitWidth() * imageBrute.getWidth());
                int sety = (int) (yMouse / photo.getImageView().getFitHeight() * imageBrute.getHeight());
                PointView pv = new PointView(xMouse, yMouse, "P" + compteurPoints);
                pv.getPoint().setX(setx);
                pv.getPoint().setY(sety);
                pv.getCircle().setOnMouseEntered(e -> pv.getCircle().setRadius(10d));
                pv.getCircle().setOnMouseExited(e -> pv.getCircle().setRadius(5d));
                Label nameLabel = new Label(pv.getName());
                nameLabel.setTextFill(Color.BLACK);
                nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                pv.setLabel(nameLabel);
                points.add(pv);
                overlayPane.getChildren().add(pv.getCircle());
                nameLabel.setLayoutX(pv.getCircle().getCenterX() + pv.getCircle().getTranslateX() + pv.getCircle().getRadius() + 5);
                nameLabel.setLayoutY(pv.getCircle().getCenterY() + pv.getCircle().getTranslateY() - 10);
                overlayPane.getChildren().add(nameLabel);
                compteurPoints++;
            } else {
                Circle circlePressed = (Circle) event.getTarget();
                PointView pointViewPressed = points.stream()
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
            if (this instanceof ConteneurImageMorph3) {
                ((ConteneurImageMorph3) this).fileSelect();
            } else {
                fileSelect(photo);
            }
        }
    }

    protected void handleMouseRightPressed(MouseEvent event) {
        if (event.getTarget() instanceof Circle) {
            Circle circleClicked = (Circle) event.getTarget();
            PointView pointViewClicked = points.stream()
                .filter(pv -> pv.getCircle().equals(circleClicked))
                .findFirst().orElse(null);
            if (pointViewClicked != null) {
                overlayPane.getChildren().remove(circleClicked);
                overlayPane.getChildren().remove(pointViewClicked.getLabel());
                int removedPointNumber = Integer.parseInt(pointViewClicked.getName().substring(1));
                removePointAndUpdate(pointViewClicked, points, overlayPane);
                rollbackPoints(removedPointNumber);
                compteurPoints--;
            }
        }
    }

    protected void handleMouseDragged(MouseEvent event) {
        Circle circlePressed = (Circle) event.getTarget();
        PointView pointViewPressed = points.stream()
            .filter(pv -> pv.getCircle().equals(circlePressed))
            .findFirst().orElse(null);
        if (pointViewPressed != null) {
            Label label = pointViewPressed.getLabel();
            double offsetX = event.getSceneX() - pointViewPressed.getOrgSceneX();
            double offsetY = event.getSceneY() - pointViewPressed.getOrgSceneY();
            double newTranslateX = pointViewPressed.getOrgTranslateX() + offsetX;
            double newTranslateY = pointViewPressed.getOrgTranslateY() + offsetY;
            double newX = circlePressed.getCenterX() + newTranslateX;
            double newY = circlePressed.getCenterY() + newTranslateY;
            if (newX >= 5 && newX <= photo.getImageView().getFitWidth() - 5 && newY >= 5 && newY <= photo.getImageView().getFitHeight() - 5) {
                circlePressed.setTranslateX(newTranslateX);
                circlePressed.setTranslateY(newTranslateY);
                label.setTranslateX(newTranslateX);
                label.setTranslateY(newTranslateY);
                pointViewPressed.getPoint().setX((int) (((pointViewPressed.getCircle().getCenterX() + pointViewPressed.getCircle().getTranslateX()) / photo.getImageView().getFitWidth()) * imageBrute.getWidth()));
                pointViewPressed.getPoint().setY((int) ((pointViewPressed.getCircle().getCenterY() + pointViewPressed.getCircle().getTranslateY()) / photo.getImageView().getFitHeight() * imageBrute.getHeight()));
            }
        }
    }

    protected void fileSelect(ImagePlus photo) {
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            try {
                Image newImage = new Image(file.toURI().toURL().toExternalForm());
                if (photo.getImage() != null && (newImage.getWidth() != photo.getImage().getWidth() || newImage.getHeight() != photo.getImage().getHeight())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Les images doivent être de la même taille.");
                    alert.showAndWait();
                } else {
                    photo.setImage(newImage);
                    if (photo.getImageView().getImage().getWidth() > photo.getImageView().getImage().getHeight()) {
                        double ratio =  newImage.getHeight() / newImage.getWidth();
                        double fitWidth = 0.95 * screenY;
                        double fitHeight = fitWidth * ratio;
                        photo.getImageView().setFitHeight(fitHeight);
                        photo.getImageView().setFitWidth(fitWidth);
                    } else if (photo.getImageView().getImage().getWidth() < photo.getImageView().getImage().getHeight()) {
                        double ratio =  newImage.getWidth() / newImage.getHeight();
                        double fitHeight = 0.95 * screenY;
                        double fitWidth = fitHeight * ratio;
                        photo.getImageView().setFitHeight(fitHeight);
                        photo.getImageView().setFitWidth(fitWidth);
                    } else {
                        photo.getImageView().setFitHeight(0.95 * screenY);
                        photo.getImageView().setFitWidth(0.95 * screenY);
                    }
                    if (photo == this.photo) {
                        imageBrute = newImage;
                    }
                    if (listener != null) {
                        listener.onImageImported();
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void fileReset() {
        photo.setImage(null);
        photo.getImageView().setFitHeight(0.95 * screenY);
        photo.getImageView().setFitWidth(0.95 * screenY);
        resetPoints();
    }

    protected void resetPoints() {
        overlayPane.getChildren().removeIf(node -> node instanceof Circle);
        overlayPane.getChildren().removeIf(node -> node instanceof Label);
        overlayPane.getChildren().removeIf(node -> node instanceof Line);
        points.clear();
        lines.clear();
        compteurPoints = 0;
    }

    protected void rollbackPoints(int removedPointNumber) {
        points.stream()
            .filter(pv -> Integer.parseInt(pv.getName().substring(1)) > removedPointNumber)
            .forEach(pv -> {
                int newNumber = Integer.parseInt(pv.getName().substring(1)) - 1;
                pv.setName("P" + newNumber);
            });
        updateLabels();
    }

    private void removePointAndUpdate(PointView pointView, ArrayList<PointView> points, Pane overlayPane) {
        overlayPane.getChildren().remove(pointView.getCircle());
        overlayPane.getChildren().remove(pointView.getLabel());
        points.remove(pointView);
    }

    private void updateLabels() {
        for (PointView pointView : points) {
            Label label = pointView.getLabel();
            if (label != null) {
                label.setText(pointView.getName());
            }
        }
    }

    public ImagePlus getPhoto() {
        return photo;
    }

    public ArrayList<PointView> getPoints() {
        return points;
    }

    public ArrayList<LineView> getLines() {
        return lines;
    }

    public int getCompteurPoints() {
        return compteurPoints;
    }

    public Pane getOverlayPane() {
        return overlayPane;
    }

    public Image getImageBrute() {
        return imageBrute;
    }

    public void setCompteurPoints(int compteurPoints) {
        this.compteurPoints = compteurPoints;
    }

    public void setPoints(ArrayList<PointView> points) {
        this.points = points;
    }

    public void setLines(ArrayList<LineView> lines) {
        this.lines = lines;
    }
}