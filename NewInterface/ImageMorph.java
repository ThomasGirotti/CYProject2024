package morphisme;

import java.util.ArrayList;
import java.util.Stack;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class ImageMorph {

    protected ArrayList<Point> pointsDebut;
    protected ArrayList<Point> pointsFin;
    protected int nbrEtapes;
    protected int nbrPoints;
    protected BufferedImage originalImage;

    public ImageMorph(ArrayList<Point> pointsDebut, ArrayList<Point> pointsFin, int nbrEtapes, String nomImg) throws IOException{
        this.nbrEtapes=nbrEtapes;
        this.pointsDebut=pointsDebut;
        this.pointsFin=pointsFin;
        File inputFile = new File(nomImg);
        originalImage = ImageIO.read(inputFile);
        // récuperation du nombre de points de controles
        this.nbrPoints = pointsDebut.size();
    }

    private Color[] getTwoMainColors() {
        Color centerColor = new Color(originalImage.getRGB(originalImage.getWidth() / 2, originalImage.getHeight() / 2));
        Color cornerColor = new Color(originalImage.getRGB(0, 0));
        return new Color[] { centerColor, cornerColor };
    }

    public Color getCouleurForme() {
        Color[] mainColors = getTwoMainColors();
        return mainColors[0];
    }

    public Color getCouleurFond() {
        Color[] mainColors = getTwoMainColors();
        return mainColors[1];
    }

    public ArrayList<Point> interpollation(double alpha){
        // création du tableau des points intermédiaires
        ArrayList<Point> res = new ArrayList<>();
        // on rempli le tableau
        for(int i=0;i<nbrPoints;i++){
            int x = (int) (pointsDebut.get(i).getX() * (1 - alpha) + pointsFin.get(i).getX() * alpha);
            int y = (int) (pointsDebut.get(i).getY() * (1 - alpha) + pointsFin.get(i).getY() * alpha);
            Point a = new Point(x,y);
            res.add(a);
        }
        // retourne le tableau des points intermédiaires
        return res;
    }

    protected void rempliForme(BufferedImage img, Color fondActuel, Color fondFinal, Color interieur){
        Point start = pointExt(img, fondActuel);
        if(start.getX()>=img.getWidth() || start.getY()>=img.getHeight()){
            for(int i=0;i<img.getWidth();i++){
                for(int j=0;j<img.getHeight();j++){
                    img.setRGB(i, j, interieur.getRGB());
                }
            }
        }else{
            diffusion(img, start, fondActuel, fondFinal);
            for(int i=0;i<img.getWidth();i++){
                for(int j=0;j<img.getHeight();j++){
                    if(img.getRGB(i, j)==fondActuel.getRGB()){
                        img.setRGB(i, j, interieur.getRGB());
                    }
                }
            }
        }
        
    }

    protected Point pointExt(BufferedImage img, Color couleurFond){
        int width = img.getWidth();
        int height = img.getHeight();
        for(int i=0; i<width; i++){
            if(img.getRGB(i, 0)==couleurFond.getRGB()){
                return new Point(i, 0);
            }
            if(img.getRGB(i, height-1)==couleurFond.getRGB()){
                return new Point(i, height-1);
            }
        }
        for(int j=0; j<height; j++){
            if(img.getRGB(0, j)==couleurFond.getRGB()){
                return new Point(0, j);
            }
            if(img.getRGB(width-1, j)==couleurFond.getRGB()){
                return new Point(width-1, j);
            }
        }
        return new Point(width,height);
    }


    protected void diffusion(BufferedImage img, Point point, Color color1, Color color2){
        int width = img.getWidth();
        int height = img.getHeight();
        // Crée une pile pour stocker les pixels à explorer
        Stack<Point> stack = new Stack<>();
        stack.push(new Point((int) point.getX(), (int) point.getY()));
        // Parcours de la pile
        while (!stack.isEmpty()) {
            Point current = stack.pop();
            int x = (int) current.getX();
            int y = (int) current.getY();
            // Vérifie si le pixel actuel est de la couleur cible
            if (img.getRGB(x, y) == color1.getRGB()) {
                img.setRGB(x, y, color2.getRGB()); // Remplace la couleur
                // Ajoute les pixels voisins à la pile s'ils sont dans les limites de l'image
                if (x - 1 >= 0) stack.push(new Point(x - 1, y));
                if (x + 1 < width) stack.push(new Point(x + 1, y));
                if (y - 1 >= 0) stack.push(new Point(x, y - 1));
                if (y + 1 < height) stack.push(new Point(x, y + 1));
            }
        }
    }

    protected Color trouveCouleur(Color color1, Color color2){
        Color res = new Color(0);
        if((color1.getRGB()!=res.getRGB()) && (color2.getRGB()!=res.getRGB())){
            return res;
        }else {
            res = new Color(1);
            if((color1.getRGB()!=res.getRGB()) && (color2.getRGB()!=res.getRGB())){
                return res;
            }else {
                res = new Color(2);
            }
        }
        return res;
    }

    public void creerGif(BufferedImage[] images, int gifSpeed) throws IIOException, IOException{
        File fichierGif = new File("morph.gif");
        ImageOutputStream output = new FileImageOutputStream(fichierGif);
        GifSequenceWriter gifWriter = new GifSequenceWriter(output, BufferedImage.TYPE_INT_ARGB, 20, true);
        for (int i = 0; i < gifSpeed; i++){
            gifWriter.writeToSequence(images[i]);
        }
        gifWriter.close();
        output.close();
    }
}