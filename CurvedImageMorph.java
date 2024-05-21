import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;

public class CurvedImageMorph extends ImageMorph{

    private int nbrPnt;

    public CurvedImageMorph(ArrayList<Point> pointsDebut, ArrayList<Point> pointsFin, int nbrEtapes, String nomImg) throws IOException{
        super(pointsDebut, pointsFin, nbrEtapes, nomImg);
        nbrPnt = nbrPoints/4;
    }
    

    public BufferedImage[] morphing() throws IOException{
        
        BufferedImage[] res = new BufferedImage[nbrEtapes+1];

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();


        // Obtenir la couleur de la forme
        Color couleurForme = getCouleurForme();

        // Obtenir la couleur du fond
        Color couleurFond = getCouleurFond(couleurForme);

        Color colorTmp = trouveCouleur(couleurForme,couleurFond);
        
        // création et enregistrement des images 
        for (int i=0; i<=nbrEtapes; i++){

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
        g2d.setColor(color2);
        for(int i=0;i<img.getWidth();i++){
            for(int j=0;j<img.getHeight();j++){
                img.setRGB(i, j, color1.getRGB());
            }
        }

        for (int i=0;i<nbrPnt;i++){
            CourbeBezier courbe = new CourbeBezier(points.get(i*4), points.get(i*4+1), points.get(i*4+2), points.get(i*4+3));
            Point[] tab =courbe.getTab();
            ArrayList<Point> pntCourbe = courbe.courbe((int) tab[0].distance(tab[3]));
            for(int j=0; j<pntCourbe.size(); j++){
                img.setRGB(pntCourbe.get(j).getX(), pntCourbe.get(j).getY(), color2.getRGB());
                if(j<pntCourbe.size()-1){
                    g2d.drawLine(pntCourbe.get(j).getX(), pntCourbe.get(j).getY(),pntCourbe.get(j+1).getX(), pntCourbe.get(j+1).getY());
                }
            }
        }

    }

}
