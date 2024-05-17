import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class SimpleImageMorph extends ImageMorph{

    public SimpleImageMorph(ArrayList<Point> pointsDebut, ArrayList<Point> pointsFin, int nbrEtapes, String nomImg){
        super(pointsDebut, pointsFin, nbrEtapes, nomImg);
    }

    public ArrayList<BufferedImage> morphing() throws IOException{
        ArrayList<BufferedImage> res = new ArrayList<>();

        BufferedImage originalImage = ImageIO.read(inputFile);

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();


        // Chemin du dossier pour sauvegarder les images
        String directoryPath = "morph_images";
        File directory = new File(directoryPath);

        // Créer le dossier s'il n'existe pas
        if (!directory.exists()) {
            directory.mkdir(); 
        }

        
        // Calculer le centroïde du polygone carré
        Point centroid = this.calculateCentroid(pointsDebut);

        // Obtenir la couleur de la forme
        Color shapeColor = new Color(originalImage.getRGB(centroid.getX(), centroid.getY()), true);

        // Obtenir la couleur du fond
        Color backgroundColor = getBackgroundColor(originalImage, pointsDebut);

        
        // création et enregistrement des images 
        for (int i=0; i<=nbrEtapes; i++){

            double alpha = i / (double) nbrEtapes;

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();

            g2d.setColor(backgroundColor);
            g2d.fillRect(0, 0, width, height);

            ArrayList<Point> pointsInter = this.interpollation(alpha);
            g2d.setColor(shapeColor);
            g2d.fillPolygon(tabX(pointsInter), tabY(pointsInter), nbrPoints);
            g2d.dispose();

            File imageFile = new File(directory, "morph" + i + ".png");
            ImageIO.write(image, "PNG", imageFile);
            res.add(image);
        }
        return res;
    }

    public static int[] tabX (ArrayList<Point> points){
        int taille = points.size();
        int[] x = new int [taille];
        for(int i=0;i<taille;i++){
            x[i]=points.get(i).getX();
        }
        return x;
    }

    public static int[] tabY (ArrayList<Point> points){
        int taille = points.size();
        int[] y = new int [taille];
        for(int i=0;i<taille;i++){
            y[i]=points.get(i).getY();
        }
        return y;
    }

    public Point calculateCentroid(ArrayList<Point> points){
        int sumX = 0, sumY = 0, numPoints = nbrPoints;
        for (int i = 0; i < numPoints; i++) {
            sumX += points.get(i).getX();
            sumY += points.get(i).getY();
        }
        return new Point(sumX / numPoints, sumY / numPoints);
    }

    public static Color getBackgroundColor(BufferedImage img, ArrayList<Point> points){
        Polygon polygon = new Polygon();
        for( Point p : points){
            polygon.addPoint(p.getX(),p.getY());
        }

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                if (!polygon.contains(x, y)) {
                    return new Color(img.getRGB(x, y), true); 
                }
            }
        }

        return Color.WHITE;
    }
}
