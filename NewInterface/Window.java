package morphisme;

import javafx.application.*;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Window extends Application implements ImageImportListener {
    private ArrayList<ConteneurImage> conteneursImages = new ArrayList<>();
    private VBox miniaturesBox = new VBox();
    private ConteneurImage conteneurActif;
    private Stage primaryStage;
    private int screenX;
    private int screenY;
    private int gifSpeed = 60;
    private int indicator;

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

    public void showHomeScreen() {
        //reset all variables
        conteneursImages.clear();
        miniaturesBox.getChildren().clear();
        conteneurActif = null;
        indicator = 0;
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
        primaryStage.setScene(scene);
    }

    private void initializeMorphingUI() {
        // Bouton pour revenir à l'accueil
        Button Accueil = new Button("Accueil");
        Accueil.setOnAction(e -> showHomeScreen());

        // Initialisation des conteneurs d'images par défaut
        ConteneurImage conteneur1;
        ConteneurImage conteneur2;
        if (indicator == 1) {
            conteneur1 = new ConteneurImageMorph1(primaryStage, screenX, screenY, this);
            conteneur2 = new ConteneurImageMorph1(primaryStage, screenX, screenY, this);
        } else if (indicator == 2) {
            conteneur1 = new ConteneurImageMorph2(primaryStage, screenX, screenY, this);
            conteneur2 = new ConteneurImageMorph2(primaryStage, screenX, screenY, this);
        } else if (indicator == 3) {
            conteneur1 = new ConteneurImageMorph3(primaryStage, screenX, screenY, this);
            conteneur2 = new ConteneurImageMorph3(primaryStage, screenX, screenY, this);
        } else {
            conteneur1 = new ConteneurImage(primaryStage, screenX, screenY, this);
            conteneur2 = new ConteneurImage(primaryStage, screenX, screenY, this);
        }
        conteneursImages.add(conteneur1);
        conteneursImages.add(conteneur2);
        conteneurActif = conteneur1;

        // Liste des miniatures à gauche
        miniaturesBox.setSpacing(10);
        miniaturesBox.setPrefWidth(0.13 * screenX);
        miniaturesBox.setPrefHeight(0.95 * screenY);
        miniaturesBox.setAlignment(Pos.CENTER);

        // Ajouter les conteneurs d'images à la liste des miniatures
        for (ConteneurImage ci : conteneursImages) {
            Button miniatureButton = new Button();
            miniatureButton.setPrefSize(screenX * 0.08, screenX * 0.08);
            ImageView imageView = new ImageView(ci.getPhoto().getImageView().getImage());
            imageView.setFitWidth(screenX * 0.08);
            imageView.setFitHeight(screenX * 0.08);
            miniatureButton.setGraphic(imageView);
            miniatureButton.setOnAction(e -> setActiveConteneur(ci));
            miniaturesBox.getChildren().add(miniatureButton);
        }

        // Bouton pour ajouter un nouveau conteneur d'image
        Button addConteneurButton = new Button("+");
        addConteneurButton.setPrefSize(screenX * 0.085, screenX * 0.085);
        addConteneurButton.setOnAction(e -> addConteneurImage(miniaturesBox, addConteneurButton));
        miniaturesBox.getChildren().add(addConteneurButton);

        // ScrollPane pour la liste des miniatures
        ScrollPane scrollPane = new ScrollPane(miniaturesBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setMaxHeight(0.95 * screenY);

        // Conteneur actif au centre
        Pane conteneurActifPane = new Pane();
        conteneurActifPane.getChildren().add(conteneurActif.getConteneurImage());
        conteneurActifPane.setMaxHeight(0.95 * screenY);

        // Boutons de gestion à droite
        VBox boutonsGestion = new VBox();
        boutonsGestion.setSpacing(20);
        boutonsGestion.setAlignment(Pos.CENTER);
        boutonsGestion.setPrefWidth(0.15 * screenX);
        Button fileReset = new Button("Réinitialiser Image");
        Button pointReset = new Button("Réinitialiser Points");
        Button boutonValidation = new Button("Générer");
        fileReset.setPrefSize(150, 50);
        pointReset.setPrefSize(150, 50);
        boutonValidation.setPrefSize(150, 50);
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
        boutonsGestion.getChildren().addAll( pointReset, fileReset, boutonsRadios, boutonValidation);
        fileReset.setOnAction(e -> {
            conteneurActif.fileReset();
            onImageImported();
        });
        pointReset.setOnAction(e -> conteneurActif.resetPoints());
        boutonValidation.setOnAction(e -> generateMorphing());

        // Ajout à l'interface principale
        HBox root = new HBox();
        root.getChildren().addAll(Accueil, scrollPane, conteneurActifPane, boutonsGestion);
        root.setSpacing(0.05 * screenX);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, screenX, screenY);
        primaryStage.setScene(scene);
    }

    private void addConteneurImage(VBox miniaturesBox, Button addConteneurButton) {
        miniaturesBox.getChildren().remove(addConteneurButton);
        ConteneurImage conteneur;
        if (indicator == 1) {
            conteneur = new ConteneurImageMorph1(primaryStage, screenX, screenY, this);
        } else if (indicator == 2) {
            conteneur = new ConteneurImageMorph2(primaryStage, screenX, screenY, this);
        } else if (indicator == 3) {
            conteneur = new ConteneurImageMorph3(primaryStage, screenX, screenY, this);
        } else {
            conteneur = new ConteneurImage(primaryStage, screenX, screenY, this);
        }
        conteneursImages.add(conteneur);
        Button miniatureButton = new Button();
        miniatureButton.setPrefSize(screenX * 0.08, screenX * 0.08);
        ImageView imageView = new ImageView(conteneur.getPhoto().getImageView().getImage());
        imageView.setFitWidth(screenX * 0.08);
        imageView.setFitHeight(screenX * 0.08);
        miniatureButton.setGraphic(imageView);
        miniatureButton.setOnAction(e -> setActiveConteneur(conteneur));
        miniaturesBox.getChildren().add(miniatureButton);
        miniaturesBox.getChildren().add(addConteneurButton);
    }

    private void setActiveConteneur(ConteneurImage conteneur) {
        conteneurActif = conteneur;
        // Utiliser le bon index pour accéder à conteneurActifPane
        Pane conteneurActifPane = (Pane) ((HBox) primaryStage.getScene().getRoot()).getChildren().get(2);
        conteneurActifPane.getChildren().clear();
        conteneurActifPane.getChildren().add(conteneur.getConteneurImage());
        if (conteneur instanceof ConteneurImageMorph1) {
            ((ConteneurImageMorph1) conteneur).updateLines();
        } else if (conteneur instanceof ConteneurImageMorph3) {
            ((ConteneurImageMorph3) conteneur).drawDroite();
        }
    }

    @Override
    public void onImageImported() {
        // Mettez à jour les miniatures correctement
        for (int i = 0; i < conteneursImages.size(); i++) {
            Button button = (Button) miniaturesBox.getChildren().get(i);
            ConteneurImage conteneur = conteneursImages.get(i);
            ImageView imageView = new ImageView(conteneur.getPhoto().getImageView().getImage());
            imageView.setFitWidth(screenX * 0.08);
            imageView.setFitHeight(screenX * 0.08);
            imageView.setPreserveRatio(true);
            button.setGraphic(imageView);
        }
    }


    private boolean verifierConditions() {
        int maxPoints = 0;
        boolean sameNumberOfPoints = true;
        int firstImagePoints = conteneursImages.get(0).getPoints().size();
        for (ConteneurImage ci : conteneursImages) {
            if (ci.getPhoto().getImageView().getImage() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez importer une image pour chaque conteneur.");
                alert.showAndWait();
                return false;
            }
            int pointsCount = ci.getPoints().size();
            if (pointsCount < 3) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez choisir au moins trois points sur chaque image.");
                alert.showAndWait();
                return false;
            }
            if (pointsCount != firstImagePoints) {
                sameNumberOfPoints = false;
            }
            if (pointsCount > maxPoints) {
                maxPoints = pointsCount;
            }
        }
        if (!sameNumberOfPoints) {
            String warningText = "Toutes les images n'ont pas le même nombre de points. \nVérifiez que chaque image possèdent " + maxPoints + " points qui est le maximum actuel entre vos images.";
            Label label = new Label(warningText);
            label.setWrapText(true);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.getDialogPane().setContent(label);
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private void generateMorphing() {
        if(verifierConditions()) {
            switch (indicator) {
                case 1:
                    int gifSteps = (gifSpeed * (conteneursImages.size() - 1)) + (conteneursImages.size() - 1);
                    BufferedImage[] tab = new BufferedImage[gifSteps];
                    SimpleImageMorph morph = null;
                    int currentIndex = 0;
                    for (int i = 0; i < conteneursImages.size() - 1; i++) {
                        ConteneurImage conteneurDepart = conteneursImages.get(i);
                        ConteneurImage conteneurArrive = conteneursImages.get(i + 1);
                        ArrayList<Point> pointsDebut = new ArrayList<>();
                        ArrayList<Point> pointsFin = new ArrayList<>();
                        for (PointView pointView : conteneurDepart.getPoints()) {
                            pointsDebut.add(new Point((int) pointView.getPoint().getX(), (int) pointView.getPoint().getY()));
                        }
                        for (PointView pointView : conteneurArrive.getPoints()) {
                            pointsFin.add(new Point((int) pointView.getPoint().getX(), (int) pointView.getPoint().getY()));
                        }
                        String imagePath = null;
                        try {
                            URI uri = new URI(conteneurDepart.getPhoto().getImage().getUrl());
                            imagePath = Paths.get(uri).toString();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        try {
                            morph = new SimpleImageMorph(pointsDebut, pointsFin, gifSpeed, imagePath);
                            BufferedImage[] morphImages = morph.morphing();
                            for (BufferedImage image : morphImages) {
                                tab[currentIndex] = image;
                                currentIndex++;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        morph.creerGif(tab, gifSteps);
                        File file = new File("morph.gif");
                        Desktop.getDesktop().open(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                
                    break;
                case 2:

                    break;
                case 3:

                    break;
                default:
                    break;
            }
        }
    }

    public void showMorphing1() {
        indicator = 1;
        initializeMorphingUI();
    }

    public void showMorphing2() {
        indicator = 2;
        initializeMorphingUI();
    }

    public void showMorphing3() {
        indicator = 3;
        initializeMorphingUI();
    }

    public static void main(String[] args) {
        launch(args);
    }
}