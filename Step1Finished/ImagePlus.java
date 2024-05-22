package ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class ImagePlus extends StackPane {
    private ImageView imageView;
    public ImagePlus() {
        this.imageView = new ImageView();
        getChildren().add(imageView);
        drawPlusSign();
    }
    private void drawPlusSign() {
        Line verticalLine = new Line();
        verticalLine.setStartX(0);
        verticalLine.setStartY(-20);
        verticalLine.setEndX(0);
        verticalLine.setEndY(20);
        verticalLine.setStrokeWidth(5);
        verticalLine.setStroke(Color.GRAY);
        Line horizontalLine = new Line();
        horizontalLine.setStartX(-20);
        horizontalLine.setStartY(0);
        horizontalLine.setEndX(20);
        horizontalLine.setEndY(0);
        horizontalLine.setStrokeWidth(5);
        horizontalLine.setStroke(Color.GRAY);
        getChildren().addAll(verticalLine, horizontalLine);
    }
    public void setImage(Image image) {
        if (image != null) {
            imageView.setImage(image);
            getChildren().removeIf(node -> node instanceof Line);
        } else {
            imageView.setImage(null);
            drawPlusSign();
        }
    }
    public Image getImage() {
        return imageView.getImage();
    }
    public ImageView getImageView() {
        return imageView;
    }
}