package ui;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import java.io.File;
import java.io.IOException;

public class MorphApp extends Application {
    private ImageView photoDepart = new ImageView();
    private ImageView photoArrive = new ImageView();
    FileChooser fileChooser = new FileChooser();

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Morphing");
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

        Scene scene = new Scene(morphMode, 500, 500);
        morphMode.setStyle("-fx-padding: 5;"+
                "-fx-border-color: black;" +
                "-fx-padding-color: black");

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    class BHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            if(e.getSource() instanceof Button) {
                Button clickedB = (Button) e.getSource();
                if(clickedB.getText().equals("Morphing de formes unies simples")) {
                    Stage secondStage = new Stage();
                    VBox conteneurPrincipal = new VBox();
                    HBox conteneurImage = new HBox();
                    VBox conteneurImageGauche = new VBox();
                    ScrollPane scrollPaneGauche = new ScrollPane();
                    scrollPaneGauche.setContent(photoDepart);
                    scrollPaneGauche.setPrefSize(500, 500);
                    scrollPaneGauche.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                    scrollPaneGauche.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                    conteneurImageGauche.getChildren().add(scrollPaneGauche);
                    photoDepart.setFitWidth(500);
                    photoDepart.setFitHeight(500);
                    photoDepart.setPreserveRatio(true);
                    conteneurImageGauche.getChildren().add(photoDepart);
                    Button FileSelector1 = new Button("Parcourir...");
                    conteneurImageGauche.getChildren().add(FileSelector1);
                    conteneurImageGauche.setAlignment(Pos.CENTER);
                    Slider slider = new Slider(0.1, 2, 1);
                    slider.setPrefHeight(500);
                    slider.setOrientation(Orientation.VERTICAL);
                    slider.valueProperty().addListener((observable, oldValue, newValue) -> {
                        double zoom = newValue.doubleValue();
                        photoDepart.setFitWidth(500 * zoom);
                        photoDepart.setFitHeight(500 * zoom);
                        photoArrive.setFitWidth(500 * zoom);
                        photoArrive.setFitHeight(500 * zoom);
                    });
                    VBox conteneurSlider = new VBox();
                    conteneurSlider.getChildren().add(slider);
                    conteneurSlider.setAlignment(Pos.CENTER);
                    VBox conteneurImageDroite = new VBox();
                    conteneurImageDroite.setMinSize(500, 500);
                    conteneurImageDroite.setMaxSize(500, 500);
                    conteneurImageDroite.setPrefSize(500, 500);
                    photoArrive.setFitWidth(500);
                    photoArrive.setFitHeight(500);
                    photoArrive.setPreserveRatio(true);
                    conteneurImageDroite.getChildren().add(photoArrive);
                    Button FileSelector2 = new Button("Parcourir...");
                    conteneurImageDroite.getChildren().add(FileSelector2);
                    conteneurImageDroite.setAlignment(Pos.CENTER);
                    Button boutonValidation = new Button("Générer");
                    conteneurImage.getChildren().addAll(
                            conteneurImageGauche,
                            conteneurSlider,
                            conteneurImageDroite
                    );
                    conteneurPrincipal.getChildren().addAll(
                            conteneurImage,
                            boutonValidation
                    );
                    Scene scene = new Scene(conteneurPrincipal, 1200, 700);
                    secondStage.setScene(scene);
                    secondStage.show();
                    FileSelector1.setOnAction(event -> {
                        File file = fileChooser.showOpenDialog(secondStage);
                        if (file != null) {
                            try {
                                photoDepart.setImage(new javafx.scene.image.Image(file.toURI().toURL().toExternalForm()));
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    FileSelector2.setOnAction(event -> {
                        File file = fileChooser.showOpenDialog(secondStage);
                        if (file != null) {
                            try {
                                photoArrive.setImage(new javafx.scene.image.Image(file.toURI().toURL().toExternalForm()));
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                }
            }
        }
    }

    public static void main (String[] args) {
        launch(args);
    }
}
