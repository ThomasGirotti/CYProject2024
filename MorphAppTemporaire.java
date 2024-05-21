package ui;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.*;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.net.URLDecoder;

public class MorphAppTemporaire extends Application {
    private ImageView photoDepart = new ImageView("file:images/ratons.jpg");
    Image image = new Image("file:images/tortue.jpg");
    private ImageView photoArrive = new ImageView("file:images/tortue.jpg");
    FileChooser fileChooser = new FileChooser();
    private Stage primaryStage;
    private Pane overlaypane1;
    private Pane overlaypane2;

    private int i = 0;
    int xMouse = 0;
    int yMouse = 0;
    private ArrayList<PointView> pointsDepart = new ArrayList<>();
    private ArrayList<PointView> pointsArrive = new ArrayList<>();
    private PriorityQueue<Integer> availableNumbers = new PriorityQueue<>();

    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        showHomeScreen();
        primaryStage.show();
    }

    public void showHomeScreen() {
        Button m1 = new Button("Morphing de formes unies simples");
        Button m2 = new Button ("Morphing de formes unies arrondies");
        Button m3 = new Button ("Morphing d'images");
        VBox morphMode = new VBox();
        m1.setPrefWidth(300);
        m2.setPrefWidth(300);
        m3.setPrefWidth(300);
        morphMode.getChildren().add(m1);
        morphMode.getChildren().add(m2);
        morphMode.getChildren().add(m3);
        m1.setOnAction(new BHandler());
        m2.setOnAction(new BHandler());
        m3.setOnAction(new BHandler());
        Scene scene = new Scene(morphMode, 1500, 900);
        morphMode.setStyle("-fx-padding: 5;"+
                "-fx-border-color: black;" +
                "-fx-padding-color: black");
        primaryStage.setScene(scene);
    }

    public void showMorphing1(){
        VBox conteneurPrincipal = new VBox();
        HBox conteneurImage = new HBox();
        VBox conteneurImageGauche = new VBox();
        Button Accueil = new Button("Accueil");
        ScrollPane scrollPaneGauche = new ScrollPane();
        scrollPaneGauche.setContent(photoDepart);
        scrollPaneGauche.prefWidthProperty().bind(photoDepart.fitWidthProperty());
        scrollPaneGauche.prefHeightProperty().bind(photoDepart.fitHeightProperty());
        scrollPaneGauche.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneGauche.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        conteneurImageGauche.getChildren().add(scrollPaneGauche);
        photoDepart.setFitWidth(image.getWidth());
        photoDepart.setFitHeight(image.getHeight());
        photoDepart.setPreserveRatio(true);
        conteneurImageGauche.getChildren().add(photoDepart);
        Button FileSelector1 = new Button("Parcourir...");
        conteneurImageGauche.getChildren().add(FileSelector1);
        Button FileReset1 = new Button("Réinitialiser Image");
        conteneurImageGauche.getChildren().add(FileReset1);
        Button PointReset1 = new Button("Réinitialiser Points");
        conteneurImageGauche.getChildren().add(PointReset1);
        conteneurImageGauche.setAlignment(Pos.CENTER);
        VBox conteneurSlider = new VBox();
        conteneurSlider.setAlignment(Pos.CENTER);
        VBox conteneurImageDroite = new VBox();
        ScrollPane scrollPaneDroite = new ScrollPane();
        scrollPaneDroite.setContent(photoArrive);
        scrollPaneDroite.prefWidthProperty().bind(photoArrive.fitWidthProperty());
        scrollPaneDroite.prefHeightProperty().bind(photoArrive.fitHeightProperty());
        scrollPaneDroite.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneDroite.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneGauche.vvalueProperty().addListener((obs, oldVal, newVal) -> scrollPaneDroite.setVvalue(newVal.doubleValue()));
        scrollPaneGauche.hvalueProperty().addListener((obs, oldVal, newVal) -> scrollPaneDroite.setHvalue(newVal.doubleValue()));
        scrollPaneDroite.vvalueProperty().addListener((obs, oldVal, newVal) -> scrollPaneGauche.setVvalue(newVal.doubleValue()));
        scrollPaneDroite.hvalueProperty().addListener((obs, oldVal, newVal) -> scrollPaneGauche.setHvalue(newVal.doubleValue()));
        conteneurImageDroite.getChildren().add(scrollPaneDroite);
        photoArrive.setFitWidth(image.getWidth());
        photoArrive.setFitHeight(image.getHeight());
        photoArrive.setPreserveRatio(true);
        conteneurImageDroite.getChildren().add(photoArrive);
        Button FileSelector2 = new Button("Parcourir...");
        conteneurImageDroite.getChildren().add(FileSelector2);
        Button FileReset2 = new Button("Réinitialiser Image");
        conteneurImageDroite.getChildren().add(FileReset2);
        Button PointReset2 = new Button("Réinitialiser Points");
        conteneurImageDroite.getChildren().add(PointReset2);
        conteneurImageDroite.setAlignment(Pos.CENTER);
        HBox boutonControle = new HBox();
        Button FileReset = new Button("Réinitialiser Image");
        Button PointReset = new Button("Réinitialiser Points");
        Button boutonValidation = new Button("Générer");
        boutonControle.getChildren().addAll(FileReset, PointReset, boutonValidation);
        conteneurImage.getChildren().addAll(
                conteneurImageGauche,
                conteneurSlider,
                conteneurImageDroite
        );
        conteneurImage.setAlignment(Pos.CENTER);
        TextArea pointsDisplay = new TextArea();
        pointsDisplay.setEditable(false);
        overlaypane1 = new Pane();
        overlaypane1.setMouseTransparent(false);
        overlaypane1.prefWidthProperty().bind(photoDepart.fitWidthProperty());
        overlaypane1.prefHeightProperty().bind(photoDepart.fitHeightProperty());
        scrollPaneGauche.setContent(new StackPane(photoDepart, overlaypane1));
        overlaypane2 = new Pane();
        overlaypane2.setMouseTransparent(false);
        overlaypane2.prefWidthProperty().bind(photoArrive.fitWidthProperty());
        overlaypane2.prefHeightProperty().bind(photoArrive.fitHeightProperty());
        scrollPaneDroite.setContent(new StackPane(photoArrive, overlaypane2));
        overlaypane1.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) { // Clic gauche
                if (!(event.getTarget() instanceof Circle)) {
                    xMouse = (int) event.getX();
                    yMouse = (int) event.getY();
                    int pointNumber = availableNumbers.isEmpty() ? ++i : availableNumbers.poll();
                    PointView pv = new PointView(xMouse, yMouse, "p" + pointNumber);
                    pv.getCircle().setOnMouseEntered(e -> pv.getCircle().setRadius(10d));
                    pv.getCircle().setOnMouseExited(e -> pv.getCircle().setRadius(5d));
                    Label nameLabel = new Label(pv.getName());
                    nameLabel.setTextFill(Color.WHITE);
                    pv.setLabel(nameLabel);
                    pointsDepart.add(pv);
                    overlaypane1.getChildren().add(pv.getCircle());
                    nameLabel.setLayoutX(pv.getCircle().getCenterX() + pv.getCircle().getTranslateX() + pv.getCircle().getRadius() + 5);
                    nameLabel.setLayoutY(pv.getCircle().getCenterY() + pv.getCircle().getTranslateY() - 10);
                    overlaypane1.getChildren().add(nameLabel);
                    PointView pv2 = new PointView(xMouse, yMouse, "p" + pointNumber);
                    pv2.getCircle().setOnMouseEntered(e -> pv2.getCircle().setRadius(10d));
                    pv2.getCircle().setOnMouseExited(e -> pv2.getCircle().setRadius(5d));
                    Label nameLabel2 = new Label(pv2.getName());
                    nameLabel2.setTextFill(Color.WHITE);
                    pv2.setLabel(nameLabel2);
                    pointsArrive.add(pv2);
                    overlaypane2.getChildren().add(pv2.getCircle());
                    nameLabel2.setLayoutX(pv2.getCircle().getCenterX() + pv2.getCircle().getTranslateX() + pv2.getCircle().getRadius() + 5);
                    nameLabel2.setLayoutY(pv2.getCircle().getCenterY() + pv2.getCircle().getTranslateY() - 10);
                    overlaypane2.getChildren().add(nameLabel2);
                } else {
                    Circle circlePressed = (Circle) event.getTarget();
                    PointView pointViewPressed = pointsDepart.stream()
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
            } else if (event.getButton() == MouseButton.SECONDARY) { // Clic droit
                if (event.getTarget() instanceof Circle) {
                    Circle circleClicked = (Circle) event.getTarget();
                    PointView pointViewClicked = pointsDepart.stream()
                            .filter(pv -> pv.getCircle().equals(circleClicked))
                            .findFirst().orElse(null);
                    if (pointViewClicked != null) {
                        overlaypane1.getChildren().remove(circleClicked);
                        overlaypane1.getChildren().remove(pointViewClicked.getLabel());
                        pointsDepart.remove(pointViewClicked);
                        availableNumbers.add(Integer.parseInt(pointViewClicked.getName().substring(1)));
                    }
                    PointView pointViewClicked2 = pointsArrive.stream()
                            .filter(pv -> pv.getName().equals(pointViewClicked.getName()))
                            .findFirst().orElse(null);
                    if (pointViewClicked2 != null) {
                        overlaypane2.getChildren().remove(pointViewClicked2.getCircle());
                        overlaypane2.getChildren().remove(pointViewClicked2.getLabel());
                        pointsArrive.remove(pointViewClicked2);
                    }
                }
            }
        });
		overlaypane1.setOnMouseDragged(event -> {
            if (event.getTarget() instanceof Circle) {
                Circle circlePressed = (Circle) event.getTarget();
                PointView pointViewPressed = pointsDepart.stream()
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
                    if (newX >= 5 && newX <= photoDepart.getFitWidth() - 5 && newY >= 5 && newY <= photoDepart.getFitHeight() - 5) {
                        circlePressed.setTranslateX(newTranslateX);
                        circlePressed.setTranslateY(newTranslateY);
                        label.setTranslateX(newTranslateX);
                        label.setTranslateY(newTranslateY);
                        pointViewPressed.updatePointCoordinates();
                    }
                }
            }
        });
        
		overlaypane1.setOnMouseReleased(event -> {
            pointsDisplay.setText(afficherPoints(pointsDepart, pointsArrive));
        });

        overlaypane2.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) { // Clic gauche
                if (!(event.getTarget() instanceof Circle)) {
                    xMouse = (int) event.getX();
                    yMouse = (int) event.getY();
                    int pointNumber = availableNumbers.isEmpty() ? ++i : availableNumbers.poll();
                    PointView pv = new PointView(xMouse, yMouse, "p" + pointNumber);
                    pv.getCircle().setOnMouseEntered(e -> pv.getCircle().setRadius(10d));
                    pv.getCircle().setOnMouseExited(e -> pv.getCircle().setRadius(5d));
                    Label nameLabel = new Label(pv.getName());
                    nameLabel.setTextFill(Color.WHITE);
                    pv.setLabel(nameLabel);
                    pointsArrive.add(pv);
                    overlaypane2.getChildren().add(pv.getCircle());
                    nameLabel.setLayoutX(pv.getCircle().getCenterX() + pv.getCircle().getTranslateX() + pv.getCircle().getRadius() + 5);
                    nameLabel.setLayoutY(pv.getCircle().getCenterY() + pv.getCircle().getTranslateY() - 10);
                    overlaypane2.getChildren().add(nameLabel);
                    PointView pv2 = new PointView(xMouse, yMouse, "p" + pointNumber);
                    pv2.getCircle().setOnMouseEntered(e -> pv2.getCircle().setRadius(10d));
                    pv2.getCircle().setOnMouseExited(e -> pv2.getCircle().setRadius(5d));
                    Label nameLabel2 = new Label(pv2.getName());
                    nameLabel2.setTextFill(Color.WHITE);
                    pv2.setLabel(nameLabel2);
                    pointsDepart.add(pv2);
                    overlaypane1.getChildren().add(pv2.getCircle());
                    nameLabel2.setLayoutX(pv2.getCircle().getCenterX() + pv2.getCircle().getTranslateX() + pv2.getCircle().getRadius() + 5);
                    nameLabel2.setLayoutY(pv2.getCircle().getCenterY() + pv2.getCircle().getTranslateY() - 10);
                    overlaypane1.getChildren().add(nameLabel2);
                } else {
                    Circle circlePressed = (Circle) event.getTarget();
                    PointView pointViewPressed = pointsArrive.stream()
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
            } else if (event.getButton() == MouseButton.SECONDARY) { // Clic droit
                if (event.getTarget() instanceof Circle) {
                    Circle circleClicked = (Circle) event.getTarget();
                    PointView pointViewClicked = pointsArrive.stream()
                            .filter(pv -> pv.getCircle().equals(circleClicked))
                            .findFirst().orElse(null);
                    if (pointViewClicked != null) {
                        overlaypane2.getChildren().remove(circleClicked);
                        overlaypane2.getChildren().remove(pointViewClicked.getLabel());
                        pointsArrive.remove(pointViewClicked);
                        availableNumbers.add(Integer.parseInt(pointViewClicked.getName().substring(1)));
                    }
                    PointView pointViewClicked2 = pointsDepart.stream()
                            .filter(pv -> pv.getName().equals(pointViewClicked.getName()))
                            .findFirst().orElse(null);
                    if (pointViewClicked2 != null) {
                        overlaypane1.getChildren().remove(pointViewClicked2.getCircle());
                        overlaypane1.getChildren().remove(pointViewClicked2.getLabel());
                        pointsDepart.remove(pointViewClicked2);
                    }
                }
            }
        });
		overlaypane2.setOnMouseDragged(event -> {
            if (event.getTarget() instanceof Circle) {
                Circle circlePressed = (Circle) event.getTarget();
                PointView pointViewPressed = pointsArrive.stream()
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
                    if (newX >= 5 && newX <= photoArrive.getFitWidth() - 5 && newY >= 5 && newY <= photoArrive.getFitHeight() - 5) {
                        circlePressed.setTranslateX(newTranslateX);
                        circlePressed.setTranslateY(newTranslateY);
                        label.setTranslateX(newTranslateX);
                        label.setTranslateY(newTranslateY);
                        pointViewPressed.updatePointCoordinates();
                    }
                }
            }
        });
		overlaypane2.setOnMouseReleased(event -> {
            pointsDisplay.setText(afficherPoints(pointsDepart, pointsArrive));
        });
        PointReset1.setOnAction(event -> {
            resetPoints();
        });
        PointReset2.setOnAction(event -> {
            resetPoints();
        });
        conteneurPrincipal.getChildren().addAll(
                Accueil,
                conteneurImage,
                boutonControle,
                pointsDisplay
        );
        primaryStage.getScene().setRoot(conteneurPrincipal);
        FileSelector1.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    Image newImage = new Image(file.toURI().toURL().toExternalForm());
                    if (photoArrive.getImage() != null && (newImage.getWidth() != photoArrive.getImage().getWidth() || newImage.getHeight() != photoArrive.getImage().getHeight())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Les images doivent être de la même taille.");
                        alert.showAndWait();
                    } else {
                        photoDepart.setImage(newImage);
                        photoDepart.setFitWidth(newImage.getWidth());
                        photoDepart.setFitHeight(newImage.getHeight());
                        photoArrive.setFitWidth(newImage.getWidth());
                        photoArrive.setFitHeight(newImage.getHeight());
                        image = newImage;
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        FileSelector2.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    Image newImage = new Image(file.toURI().toURL().toExternalForm());
                    if (photoDepart.getImage() != null && (newImage.getWidth() != photoDepart.getImage().getWidth() || newImage.getHeight() != photoDepart.getImage().getHeight())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Les images doivent être de la même taille.");
                        alert.showAndWait();
                    } else if (photoDepart.getImage() == null) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Choisissez d'abord une image de départ");
                        alert.showAndWait();
                    } else {
                        photoArrive.setImage(newImage);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        FileReset1.setOnAction(event -> {
            photoDepart.setImage(null);
            photoArrive.setImage(null);
            resetPoints();
        });
        FileReset2.setOnAction(event -> {
            photoDepart.setImage(null);
            photoArrive.setImage(null);
            resetPoints();
        });
        Accueil.setOnAction(event -> {
            showHomeScreen();
        });
        boutonValidation.setOnAction(event -> {
            //cast des points en ArrayList<Point>
            ArrayList<Point> pointsDebut = new ArrayList<>();
            ArrayList<Point> pointsFin = new ArrayList<>();
            for (PointView pointView : pointsDepart) {
                pointsDebut.add(new Point((int)pointView.getPoint().getX(), (int)pointView.getPoint().getY()));
            }
            for (PointView pointView : pointsArrive) {
                pointsFin.add(new Point((int)pointView.getPoint().getX(), (int)pointView.getPoint().getY()));
            }
            //print image path
            String imagePath = new File(image.getUrl()).getPath();
            if (imagePath.startsWith("file:\\")) {
                imagePath = imagePath.substring(6);
            }
            if (imagePath.startsWith("file:")) {
                imagePath = imagePath.substring(5);
            }
            try {
                imagePath = URLDecoder.decode(imagePath, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println(imagePath);
            SimpleImageMorph morph = new SimpleImageMorph(pointsDebut, pointsFin, 60, imagePath);
            try {
                ArrayList<BufferedImage> images = morph.morphing();
                morph.creerGif(images);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                File file = new File("morph.gif");
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        FileReset.setOnAction(event -> {
            photoDepart.setImage(null);
            photoArrive.setImage(null);
            resetPoints();
        });
        PointReset.setOnAction(event -> {
            resetPoints();
        });
    }
    private void resetPoints() {
        for (PointView pointView : pointsDepart) {
            overlaypane1.getChildren().remove(pointView.getCircle());
            overlaypane1.getChildren().remove(pointView.getLabel());
            availableNumbers.add(Integer.parseInt(pointView.getName().substring(1)));
        }
        pointsDepart.clear();
        for (PointView pointView : pointsArrive) {
            overlaypane2.getChildren().remove(pointView.getCircle());
            overlaypane2.getChildren().remove(pointView.getLabel());
            availableNumbers.add(Integer.parseInt(pointView.getName().substring(1)));
        }
        pointsArrive.clear();
        i = 0;
        availableNumbers.clear();
    }
    private String afficherPoints(ArrayList<PointView> points1, ArrayList<PointView> points2) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < points1.size(); i++) {
            sb.append(points1.get(i).getName())
                .append(": (")
                .append(points1.get(i).getPoint().getX())
                .append(", ")
                .append(points1.get(i).getPoint().getY())
                .append(") == (")
                .append(points2.get(i).getPoint().getX())
                .append(", ")
                .append(points2.get(i).getPoint().getY())
                .append(")\n");
        }
        return sb.toString();
    }
    class BHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            if(e.getSource() instanceof Button) {
                Button clickedB = (Button) e.getSource();
                if(clickedB.getText().equals("Morphing de formes unies simples")) {
                    showMorphing1();
                } else if(clickedB.getText().equals("Morphing de formes unies arrondies")) {
                    // Code pour le morphing de formes unies arrondies
                } else if (clickedB.getText().equals("Morphing d'images")){
                    // Code pour le morphing d'images
                }
            }
        }
    }
    public static void main (String[] args) {
        launch(args);
    }
}
