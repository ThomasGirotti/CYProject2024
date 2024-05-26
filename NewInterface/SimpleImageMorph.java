package morphisme;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;


public class SimpleImageMorph extends ImageMorph{

    public SimpleImageMorph(ArrayList<Point> pointsDebut, ArrayList<Point> pointsFin, int nbrEtapes, String nomImg) throws IOException{
        super(pointsDebut, pointsFin, nbrEtapes, nomImg);
    }

    public BufferedImage[] morphing() throws IOException{
        BufferedImage[] res = new BufferedImage[nbrEtapes+1];


        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        // Obtenir la couleur de la forme
        Color couleurForme = getCouleurForme();

        // Obtenir la couleur du fond
        Color couleurFond = getCouleurFond();
        Color colorTmp = trouveCouleur(couleurForme,couleurFond);
        // création et enregistrement des images 
        for (int i=0; i <= nbrEtapes; i++){

            double alpha = i / (double) nbrEtapes;

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            ArrayList<Point> pointsInter = this.interpollation(alpha);
            dessinContour(image,pointsInter, colorTmp, couleurForme);
            rempliForme(image, colorTmp, couleurFond, couleurForme);
            res[i]=image;
        }
        return res;
    }

    
    public void dessinContour(BufferedImage img, ArrayList<Point> points, Color color1, Color color2){

        Graphics2D g2d = img.createGraphics();

        for(int i=0;i<img.getWidth();i++){
            for(int j=0;j<img.getHeight();j++){
                img.setRGB(i, j, color1.getRGB());
            }
        }

        for(int i=0;i<nbrPoints-1;i++){
            g2d.setColor(color2);
            g2d.drawLine((int) points.get(i).getX(), (int) points.get(i).getY(), (int) points.get(i+1).getX(), (int) points.get(i+1).getY());
        }
        g2d.drawLine((int) points.get(nbrPoints-1).getX(), (int) points.get(nbrPoints-1).getY(), (int) points.get(0).getX(), (int) points.get(0).getY());
    }
}