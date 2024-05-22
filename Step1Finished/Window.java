package ui;

import javafx.application.*;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.*;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Window extends Application {
    private ImagePlus photoDepart = new ImagePlus();
    private ImagePlus photoArrive = new ImagePlus();
    Image imageBruteDepart = new Image("file:images/tortue.jpg");
    FileChooser fileChooser = new FileChooser();
    private Stage primaryStage;
    private boolean isCycling = false;
    private int screenX;
    private int screenY;
    private int compteurPoints = 0;
    private int gifSpeed = 60;
    private int xMouse = 0;
    private int yMouse = 0;
    private Pane overlayPaneGauche;
    private Pane overlayPaneDroite;
    private ArrayList<PointView> pointsDepart = new ArrayList<>();
    private ArrayList<PointView> pointsArrive = new ArrayList<>();
    private ArrayList<LineView> lineDepart = new ArrayList<>();
    private ArrayList<LineView> lineArrive = new ArrayList();

    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        screenX = (int) screenBounds.getWidth();
        screenY = (int) screenBounds.getHeight();
        primaryStage.setX(screenBounds.getMinX());
        primaryStage.setY(screenBounds.getMinY());
        primaryStage.setWidth(screenX);
        primaryStage.setHeight(screenY);
        primaryStage.setResizable(false);
        showHomeScreen();
        primaryStage.show();
    }

    /* Méthode Frontend */
    public void showHomeScreen() {
        Button m1 = new Button("Morphing de formes unies simples");
        Button m2 = new Button("Morphing de formes unies arrondies");
        Button m3 = new Button("Morphing d'images");
        HBox morphMode = new HBox();
        m1.setPrefWidth(0.25 * screenX);
        m1.setPrefHeight(0.25 * screenY);
        m2.setPrefWidth(0.25 * screenX);
        m2.setPrefHeight(0.25 * screenY);
        m3.setPrefWidth(0.25 * screenX);
        m3.setPrefHeight(0.25 * screenY);
        morphMode.getChildren().addAll(m1, m2, m3);
        m1.setOnAction(e -> showMorphing1());
        m2.setOnAction(e -> showMorphing2());
        m3.setOnAction(e -> showMorphing3());
        morphMode.setAlignment(Pos.CENTER);
        morphMode.setSpacing(0.05 * screenX);
        Scene scene = new Scene(morphMode);
        morphMode.setStyle("-fx-padding: 5;" +
                "-fx-border-color: black;" +
                "-fx-padding-color: black");
        primaryStage.setScene(scene);
    }

    public void showMorphing1() {
        VBox conteneurPrincipal = new VBox();
        //ACCUEIL////////////////////////////////////////////////////////
        Button Accueil = new Button("Accueil");
        Accueil.setOnAction(e -> showHomeScreen());
        //CONTENEUR IMAGE////////////////////////////////////////////////
        HBox conteneurImage = new HBox();

        photoDepart.getImageView().setFitHeight(0.7 * screenY);
        photoDepart.getImageView().setFitWidth(0.7 * screenY);
        photoDepart.getImageView().setPreserveRatio(true);

        overlayPaneGauche = new Pane();
        overlayPaneGauche.prefWidthProperty().bind(photoDepart.getImageView().fitWidthProperty());
        overlayPaneGauche.prefHeightProperty().bind(photoDepart.getImageView().fitHeightProperty());

        ScrollPane scrollPaneGauche = new ScrollPane();
        scrollPaneGauche.setContent(photoDepart);
        scrollPaneGauche.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneGauche.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        photoArrive.getImageView().setFitWidth(0.7 * screenY);
        photoArrive.getImageView().setFitHeight(0.7 * screenY);
        photoArrive.getImageView().setPreserveRatio(true);

        overlayPaneDroite = new Pane();
        overlayPaneDroite.prefWidthProperty().bind(photoArrive.getImageView().fitWidthProperty());
        overlayPaneDroite.prefHeightProperty().bind(photoArrive.getImageView().fitHeightProperty());

        ScrollPane scrollPaneDroite = new ScrollPane();
        scrollPaneDroite.setContent(photoArrive);
        scrollPaneDroite.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneDroite.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox conteneurImageGauche = new VBox();
        conteneurImageGauche.getChildren().addAll(scrollPaneGauche, photoDepart);
        conteneurImageGauche.setAlignment(Pos.CENTER);
        VBox conteneurImageDroite = new VBox();
        conteneurImageDroite.getChildren().addAll(scrollPaneDroite, photoArrive);
        conteneurImageDroite.setAlignment(Pos.CENTER);

        scrollPaneGauche.prefWidthProperty().bind(photoDepart.getImageView().fitWidthProperty());
        scrollPaneGauche.prefHeightProperty().bind(photoDepart.getImageView().fitHeightProperty());
        conteneurImageGauche.setAlignment(Pos.CENTER);
        scrollPaneDroite.prefWidthProperty().bind(photoArrive.getImageView().fitWidthProperty());
        scrollPaneDroite.prefHeightProperty().bind(photoArrive.getImageView().fitHeightProperty());
        
        scrollPaneGauche.addEventFilter(ScrollEvent.SCROLL, Event::consume);
        scrollPaneDroite.addEventFilter(ScrollEvent.SCROLL, Event::consume);
        conteneurImage.getChildren().addAll(
                conteneurImageGauche,
                conteneurImageDroite
        );
        conteneurImage.setSpacing(0.05 * screenX);
        conteneurImage.setPrefHeight(0.7 * screenY);
        conteneurImage.setAlignment(Pos.CENTER);
        scrollPaneGauche.setContent(new StackPane(photoDepart, overlayPaneGauche));
        scrollPaneDroite.setContent(new StackPane(photoArrive, overlayPaneDroite));
        //BOUTON CONTROLE ///////////////////////////////////////////////
        HBox boutonControle = new HBox();
        Button FileReset = new Button("Réinitialiser Image");
        Button PointReset = new Button("Réinitialiser Points");
        Button boutonValidation = new Button("Générer");
        FileReset.setPrefWidth(0.15 * screenX);
        FileReset.setPrefHeight(0.05 * screenY);
        PointReset.setPrefWidth(0.15 * screenX);
        PointReset.setPrefHeight(0.05 * screenY);

        RadioButton radio1 = new RadioButton("Rapide");
        RadioButton radio2 = new RadioButton("Normal");
        RadioButton radio3 = new RadioButton("Lent");
        radio1.setOnAction(e -> gifSpeed = 40);
        radio2.setOnAction(e -> gifSpeed = 80);
        radio3.setOnAction(e -> gifSpeed = 140);
        ToggleGroup group = new ToggleGroup();
        radio1.setToggleGroup(group);
        radio2.setToggleGroup(group);
        radio3.setToggleGroup(group);
        radio2.setSelected(true);
        VBox boutonsRadios = new VBox();
        boutonsRadios.getChildren().addAll(radio1, radio2, radio3);

        boutonValidation.setPrefWidth(0.15 * screenX);
        boutonValidation.setPrefHeight(0.05 * screenY);
        boutonControle.setAlignment(Pos.CENTER);
        boutonControle.setSpacing(0.05 * screenX);
        boutonControle.getChildren().addAll(FileReset, PointReset, boutonsRadios, boutonValidation);
        //AJOUT DES ELEMENTS AU CONTENEUR PRINCIPAL//////////////////////
        conteneurPrincipal.getChildren().addAll(Accueil,conteneurImage,boutonControle);
        conteneurPrincipal.setSpacing(0.03 * screenY);
        primaryStage.getScene().setRoot(conteneurPrincipal);

        /* OBSERVEURS */
        overlayPaneGauche.setOnMousePressed(event -> {
            if (isCycling) return;
            if (event.getButton() == MouseButton.PRIMARY) {
                if (photoDepart.getImage() != null && photoArrive.getImage() != null){
                    if (!(event.getTarget() instanceof Circle)) {
                        xMouse = (int) event.getX();
                        yMouse = (int) event.getY();
                        int setx = (int) (xMouse / photoDepart.getImageView().getFitWidth() * imageBruteDepart.getWidth());
                        int sety = (int) (yMouse / photoDepart.getImageView().getFitHeight() * imageBruteDepart.getHeight());
                        PointView pv = new PointView(xMouse, yMouse, "P" + compteurPoints);
                        pv.getPoint().setX(setx);
                        pv.getPoint().setY(sety);
                        pv.getCircle().setOnMouseEntered(e -> pv.getCircle().setRadius(10d));
                        pv.getCircle().setOnMouseExited(e -> pv.getCircle().setRadius(5d));
                        Label nameLabel = new Label(pv.getName());
                        nameLabel.setTextFill(Color.BLACK);
                        pv.setLabel(nameLabel);
                        pointsDepart.add(pv);
                        overlayPaneGauche.getChildren().add(pv.getCircle());
                        nameLabel.setLayoutX(pv.getCircle().getCenterX() + pv.getCircle().getTranslateX() + pv.getCircle().getRadius() + 5);
                        nameLabel.setLayoutY(pv.getCircle().getCenterY() + pv.getCircle().getTranslateY() - 10);
                        overlayPaneGauche.getChildren().add(nameLabel);
                        if (pointsDepart.size() > 1) {
                            addLine(pointsDepart.get(pointsDepart.size() - 2), pv, overlayPaneGauche);
                        }
                        PointView pv2 = new PointView(xMouse, yMouse, "d" + compteurPoints);
                        pv2.getPoint().setX(setx);
                        pv2.getPoint().setY(sety);
                        pv2.getCircle().setOnMouseEntered(e -> pv2.getCircle().setRadius(10d));
                        pv2.getCircle().setOnMouseExited(e -> pv2.getCircle().setRadius(5d));
                        Label nameLabel2 = new Label(pv2.getName());
                        nameLabel2.setTextFill(Color.BLACK);
                        pv2.setLabel(nameLabel2);
                        pointsArrive.add(pv2);
                        overlayPaneDroite.getChildren().add(pv2.getCircle());
                        nameLabel2.setLayoutX(pv2.getCircle().getCenterX() + pv2.getCircle().getTranslateX() + pv2.getCircle().getRadius() + 5);
                        nameLabel2.setLayoutY(pv2.getCircle().getCenterY() + pv2.getCircle().getTranslateY() - 10);
                        overlayPaneDroite.getChildren().add(nameLabel2);
                        if (pointsArrive.size() > 1) {
                            addLine(pointsArrive.get(pointsArrive.size() - 2), pv2, overlayPaneDroite);
                        }
                        compteurPoints++;
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
                } else {
                    if (photoDepart.getImage() == null) {
                        fileSelect(photoDepart, photoArrive);
                    }
                }
            } else if (event.getButton() == MouseButton.SECONDARY) {
                if (event.getTarget() instanceof Circle) {
                    Circle circleClicked = (Circle) event.getTarget();
                    PointView pointViewClicked = pointsDepart.stream()
                            .filter(pv -> pv.getCircle().equals(circleClicked))
                            .findFirst().orElse(null);
                    if (pointViewClicked != null) {
                        overlayPaneGauche.getChildren().remove(circleClicked);
                        overlayPaneGauche.getChildren().remove(pointViewClicked.getLabel());
                        int removedPointNumber = Integer.parseInt(pointViewClicked.getName().substring(1));
                        removePointAndUpdate(pointViewClicked, pointsDepart, overlayPaneGauche, pointsArrive, overlayPaneDroite);
                        rollbackPoints(removedPointNumber);
                        compteurPoints--;
                    }
                }
            }
        });

        overlayPaneGauche.setOnMouseDragged(event -> {
            if (isCycling) return;
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
                    if (newX >= 5 && newX <= photoDepart.getImageView().getFitWidth() - 5 && newY >= 5 && newY <= photoDepart.getImageView().getFitHeight() - 5) {
                        circlePressed.setTranslateX(newTranslateX);
                        circlePressed.setTranslateY(newTranslateY);
                        label.setTranslateX(newTranslateX);
                        label.setTranslateY(newTranslateY);
                        pointViewPressed.getPoint().setX((int) (((pointViewPressed.getCircle().getCenterX() + pointViewPressed.getCircle().getTranslateX()) / photoDepart.getImageView().getFitWidth()) * imageBruteDepart.getWidth()));
                        pointViewPressed.getPoint().setY((int) ((pointViewPressed.getCircle().getCenterY() + pointViewPressed.getCircle().getTranslateY()) / photoDepart.getImageView().getFitHeight() * imageBruteDepart.getHeight()));
                        updateLines(pointsDepart, lineDepart, overlayPaneGauche);
                    }
                }
            }
        });

        overlayPaneDroite.setOnMousePressed(event -> {
            if (isCycling) return;
            if (event.getButton() == MouseButton.PRIMARY) {
                if (photoDepart.getImage() != null && photoArrive.getImage() != null){
                    if (!(event.getTarget() instanceof Circle)) {
                        xMouse = (int) event.getX();
                        yMouse = (int) event.getY();
                        int setx = (int) (xMouse / photoDepart.getImageView().getFitWidth() * imageBruteDepart.getWidth());
                        int sety = (int) (yMouse / photoDepart.getImageView().getFitHeight() * imageBruteDepart.getHeight());
                        PointView pv = new PointView(xMouse, yMouse, "d" + compteurPoints);
                        pv.getPoint().setX(setx);
                        pv.getPoint().setY(sety);
                        pv.getCircle().setOnMouseEntered(e -> pv.getCircle().setRadius(10d));
                        pv.getCircle().setOnMouseExited(e -> pv.getCircle().setRadius(5d));
                        Label nameLabel = new Label(pv.getName());
                        nameLabel.setTextFill(Color.BLACK);
                        pv.setLabel(nameLabel);
                        pointsArrive.add(pv);
                        overlayPaneDroite.getChildren().add(pv.getCircle());
                        nameLabel.setLayoutX(pv.getCircle().getCenterX() + pv.getCircle().getTranslateX() + pv.getCircle().getRadius() + 5);
                        nameLabel.setLayoutY(pv.getCircle().getCenterY() + pv.getCircle().getTranslateY() - 10);
                        overlayPaneDroite.getChildren().add(nameLabel);
                        if (pointsArrive.size() > 1) {
                            addLine(pointsArrive.get(pointsArrive.size() - 2), pv, overlayPaneDroite);
                        }
                        PointView pv2 = new PointView(xMouse, yMouse, "P" + compteurPoints);
                        pv2.getPoint().setX(setx);
                        pv2.getPoint().setY(sety);
                        pv2.getCircle().setOnMouseEntered(e -> pv2.getCircle().setRadius(10d));
                        pv2.getCircle().setOnMouseExited(e -> pv2.getCircle().setRadius(5d));
                        Label nameLabel2 = new Label(pv2.getName());
                        nameLabel2.setTextFill(Color.BLACK);
                        pv2.setLabel(nameLabel2);
                        pointsDepart.add(pv2);
                        overlayPaneGauche.getChildren().add(pv2.getCircle());
                        nameLabel2.setLayoutX(pv2.getCircle().getCenterX() + pv2.getCircle().getTranslateX() + pv2.getCircle().getRadius() + 5);
                        nameLabel2.setLayoutY(pv2.getCircle().getCenterY() + pv2.getCircle().getTranslateY() - 10);
                        overlayPaneGauche.getChildren().add(nameLabel2);
                        if (pointsDepart.size() > 1) {
                            addLine(pointsDepart.get(pointsDepart.size() - 2), pv2, overlayPaneGauche);
                        }
                        compteurPoints++;
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
                } else {
                    if (photoArrive.getImage() == null) {
                        fileSelect(photoArrive, photoDepart);
                    }
                }
            } else if (event.getButton() == MouseButton.SECONDARY) {
                if (event.getTarget() instanceof Circle) {
                    Circle circleClicked = (Circle) event.getTarget();
                    PointView pointViewClicked = pointsArrive.stream()
                        .filter(pv -> pv.getCircle().equals(circleClicked))
                        .findFirst().orElse(null);
                    if (pointViewClicked != null) {
                        int removedPointNumber = Integer.parseInt(pointViewClicked.getName().substring(1));
                        removePointAndUpdate(pointViewClicked, pointsArrive, overlayPaneDroite, pointsDepart, overlayPaneGauche);
                        rollbackPoints(removedPointNumber);
                        compteurPoints--;
                    }
                }
            }
        });

        overlayPaneDroite.setOnMouseDragged(event -> {
            if (isCycling) return;
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
                    if (newX >= 5 && newX <= photoArrive.getImageView().getFitWidth() - 5 && newY >= 5 && newY <= photoArrive.getImageView().getFitHeight() - 5) {
                        circlePressed.setTranslateX(newTranslateX);
                        circlePressed.setTranslateY(newTranslateY);
                        label.setTranslateX(newTranslateX);
                        label.setTranslateY(newTranslateY);
                        pointViewPressed.getPoint().setX((int) (((pointViewPressed.getCircle().getCenterX() + pointViewPressed.getCircle().getTranslateX()) / photoArrive.getImageView().getFitWidth()) * imageBruteDepart.getWidth()));
                        pointViewPressed.getPoint().setY((int) ((pointViewPressed.getCircle().getCenterY() + pointViewPressed.getCircle().getTranslateY()) / photoArrive.getImageView().getFitHeight() * imageBruteDepart.getHeight()));
                        updateLines(pointsArrive, lineArrive, overlayPaneDroite);
                    }
                }
            }
        });

        boutonValidation.setOnAction(event -> {
            if (photoDepart.getImage() != null && photoArrive.getImage() != null) {
                ArrayList<Point> pointsDebut = new ArrayList<>();
                ArrayList<Point> pointsFin = new ArrayList<>();
                for (PointView pointView : pointsDepart) {
                    pointsDebut.add(new Point((int) pointView.getPoint().getX(), (int) pointView.getPoint().getY()));
                }
                for (PointView pointView : pointsArrive) {
                    pointsFin.add(new Point((int) pointView.getPoint().getX(), (int) pointView.getPoint().getY()));
                }
                if (pointsDebut.size() == pointsFin.size() && pointsDebut.size() > 2){
                    if (isCycling) {
                        String imagePath = null;
                        try {
                            URI uri = new URI(imageBruteDepart.getUrl());
                            imagePath = Paths.get(uri).toString();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        SimpleImageMorph morph = new SimpleImageMorph(pointsDebut, pointsFin, gifSpeed, imagePath);
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
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez fermer le cycle en cliquant sur la touche 'C' pour pouvoir générer l'animation.");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez choisir au moins trois points sur chaque image.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez choisir une image de départ et une image d'arrivée.");
                alert.showAndWait();
            }
        });

        primaryStage.getScene().setOnKeyPressed((EventHandler<? super KeyEvent>) new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.C) {
                    finalizeCycle(pointsDepart, lineDepart, overlayPaneGauche);
                    finalizeCycle(pointsArrive, lineArrive, overlayPaneDroite);
                }
            }
        });

        FileReset.setOnAction(event -> {
            photoDepart.setImage(null);
            photoArrive.setImage(null);
            resetPoints();
            photoDepart.getImageView().setFitWidth(0.7 * screenY);
            photoDepart.getImageView().setFitHeight(0.7 * screenY);
            photoArrive.getImageView().setFitWidth(0.7 * screenY);
            photoArrive.getImageView().setFitHeight(0.7 * screenY);
        });

        PointReset.setOnAction(event -> {
            resetPoints();
        });
    }

    public void showMorphing2() {
        VBox conteneurPrincipal = new VBox();
        Button Accueil = new Button("Accueil");
        Accueil.setOnAction(e -> showHomeScreen());
        conteneurPrincipal.getChildren().addAll(Accueil);
        primaryStage.getScene().setRoot(conteneurPrincipal);
        //TODO: Morphing de formes unies arrondies
    }

    public void showMorphing3() {
        VBox conteneurPrincipal = new VBox();
        Button Accueil = new Button("Accueil");
        Accueil.setOnAction(e -> showHomeScreen());
        conteneurPrincipal.getChildren().addAll(Accueil);
        primaryStage.getScene().setRoot(conteneurPrincipal);
        //TODO: Morphing d'images
    }

    /* Méthodes Backend */
    private void fileSelect(ImagePlus photo1, ImagePlus photo2) {
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            try {
                Image newImage = new Image(file.toURI().toURL().toExternalForm());
                if (photo2.getImage() != null && (newImage.getWidth() != photo2.getImage().getWidth() || newImage.getHeight() != photo2.getImage().getHeight())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Les images doivent être de la même taille.");
                    alert.showAndWait();
                } else {
                    photo1.setImage(newImage);
                    if (photo2.getImage() != null) {
                        photo1.getImageView().setFitWidth(photo2.getImageView().getFitWidth());
                        photo1.getImageView().setFitHeight(photo2.getImageView().getFitHeight());
                    } else {
                        if (photo1.getImageView().getImage().getWidth() > photo1.getImageView().getImage().getHeight()) {
                            double ratio =  newImage.getHeight() / newImage.getWidth();
                            double fitWidth = 0.7 * screenY;
                            double fitHeight = fitWidth * ratio;
                            photo1.getImageView().setFitHeight(fitHeight);
                            photo1.getImageView().setFitWidth(fitWidth);
                            photo2.getImageView().setFitHeight(fitHeight);
                            photo2.getImageView().setFitWidth(fitWidth);
                        } else if (photo1.getImageView().getImage().getWidth() < photo1.getImageView().getImage().getHeight()){
                            double ratio = newImage.getWidth() / newImage.getHeight();
                            double fitHeight = 0.7 * screenY;
                            double fitWidth = fitHeight * ratio;
                            photo1.getImageView().setFitHeight(fitHeight);
                            photo1.getImageView().setFitWidth(fitWidth);
                            photo2.getImageView().setFitHeight(fitHeight);
                            photo2.getImageView().setFitWidth(fitWidth);
                        } else {
                            photo1.getImageView().setFitWidth(0.7 * screenY);
                            photo1.getImageView().setFitHeight(0.7 * screenY);
                            photo2.getImageView().setFitWidth(0.7 * screenY);
                            photo2.getImageView().setFitHeight(0.7 * screenY);
                        }
                    }
                    if (photo1 == photoDepart) {
                        imageBruteDepart = newImage;
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void rollbackPoints(int removedPointNumber) {
        pointsDepart.stream()
            .filter(pv -> Integer.parseInt(pv.getName().substring(1)) > removedPointNumber)
            .forEach(pv -> {
                int newNumber = Integer.parseInt(pv.getName().substring(1)) - 1;
                pv.setName("P" + newNumber);
            });
        pointsArrive.stream()
            .filter(pv -> Integer.parseInt(pv.getName().substring(1)) > removedPointNumber)
            .forEach(pv -> {
                int newNumber = Integer.parseInt(pv.getName().substring(1)) - 1;
                pv.setName("d" + newNumber);
            });
        updateLabels();
        updateLines(pointsDepart, lineDepart, overlayPaneGauche);
        updateLines(pointsArrive, lineArrive, overlayPaneDroite);
    }

    private void removePointAndUpdate(PointView pointView, ArrayList<PointView> points, Pane overlayPane, ArrayList<PointView> otherPoints, Pane otherPane) {
        overlayPane.getChildren().remove(pointView.getCircle());
        overlayPane.getChildren().remove(pointView.getLabel());
        int index = points.indexOf(pointView);
        points.remove(pointView);
        PointView correspondingPoint = otherPoints.get(index);
        otherPane.getChildren().remove(correspondingPoint.getCircle());
        otherPane.getChildren().remove(correspondingPoint.getLabel());
        otherPoints.remove(correspondingPoint);
        updateLines(pointsDepart, lineDepart, overlayPaneGauche);
        updateLines(pointsArrive, lineArrive, overlayPaneDroite);
    }

    private void resetPoints() {
        for (PointView pointView : pointsDepart) {
            overlayPaneGauche.getChildren().remove(pointView.getCircle());
            overlayPaneGauche.getChildren().remove(pointView.getLabel());
        }
        pointsDepart.clear();
        for (PointView pointView : pointsArrive) {
            overlayPaneDroite.getChildren().remove(pointView.getCircle());
            overlayPaneDroite.getChildren().remove(pointView.getLabel());
        }
        pointsArrive.clear();
        compteurPoints = 0;
        resetCycle();
    }

    private void addLine(PointView startPoint, PointView endPoint, Pane overlayPane) {
        LineView lineView = new LineView(startPoint, endPoint);
        lineDepart.add(lineView);
        overlayPane.getChildren().add(lineView.getLine());
    }

    private void updateLines(ArrayList<PointView> points, ArrayList<LineView> lines, Pane overlayPane) {
        overlayPane.getChildren().removeIf(node -> node instanceof Line);
        lines.clear();
        System.out.println("All lines removed");
        for (int i = 0; i < points.size() - 1; i++) {
            LineView lineView = new LineView(points.get(i), points.get(i + 1));
            lines.add(lineView);
            overlayPane.getChildren().add(lineView.getLine());
            System.out.println("Line added: " + lineView);
        }
        if (isCycling) {
            updateAllLineColorsToBlue();
        }
    }

    private void updateLabels() {
        for (PointView pointView : pointsDepart) {
            Label label = pointView.getLabel();
            if (label != null) {
                label.setText(pointView.getName());
            }
        }
        for (PointView pointView : pointsArrive) {
            Label label = pointView.getLabel();
            if (label != null) {
                label.setText(pointView.getName());
            }
        }
    }

    private void finalizeCycle(ArrayList<PointView> points, ArrayList<LineView> lines, Pane overlayPane) {
        if (points.size() > 1) {
            PointView lastPoint = points.get(points.size() - 1);
            PointView firstPoint = points.get(0);
            LineView cycleLine = new LineView(lastPoint, firstPoint);
            lines.add(cycleLine);
            overlayPane.getChildren().add(cycleLine.getLine());
            System.out.println("Cycle line added: " + cycleLine);
        }
        for (PointView point : points) {
            point.getCircle().setFill(Color.BLUE);
        }
        updateAllLineColorsToBlue();
        isCycling = true;
        printLinesState(lines);
    }

    private void updateAllLineColorsToBlue() {
        for (LineView line : lineDepart) {
            line.getLine().setStroke(Color.BLUE);
            System.out.println("Line depart updated to blue: " + line);
        }
        for (LineView line : lineArrive) {
            line.getLine().setStroke(Color.BLUE);
            System.out.println("Line arrive updated to blue: " + line);
        }
    }

    private void printLinesState(ArrayList<LineView> lines) {
        for (LineView line : lines) {
            System.out.println("Line: " + line + " Color: " + line.getLine().getStroke() + " Start: " + line.getStart().getName() + " End: " + line.getEnd().getName());
        }
    }

    private void resetCycle() {
        isCycling = false;
        for (PointView point : pointsDepart) {
            point.getCircle().setFill(Color.RED);
        }
        for (PointView point : pointsArrive) {
            point.getCircle().setFill(Color.RED);
        }
        lineDepart.clear();
        lineArrive.clear();
        overlayPaneGauche.getChildren().removeIf(node -> node instanceof javafx.scene.shape.Line);
        overlayPaneDroite.getChildren().removeIf(node -> node instanceof javafx.scene.shape.Line);
    }

    /* Main */
    public static void main(String[] args) {
        launch(args);
    }
}
