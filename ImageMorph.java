import java.util.ArrayList;
import javax.imageio.IIOException;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageMorph {

    protected ArrayList<Point> pointsDebut;
    protected ArrayList<Point> pointsFin;
    protected int nbrEtapes;
    protected int nbrPoints;
    protected File inputFile;

    public ImageMorph(ArrayList<Point> pointsDebut, ArrayList<Point> pointsFin, int nbrEtapes, String nomImg){

        this.nbrEtapes=nbrEtapes;
        this.pointsDebut=pointsDebut;
        this.pointsFin=pointsFin;
        this.inputFile = new File(nomImg);

        // récuperation du nombre de points de controles
        this.nbrPoints = pointsDebut.size();
    }

    public ArrayList<Point> interpollation(double alpha){

        // création du tableau des points intermédiaires
        ArrayList<Point> res = new ArrayList<>();

        // on rempli le tableau
        for(int i=0;i<nbrPoints;i++){
            int x = (int) (pointsDebut.get(i).getX() * (1 - alpha) + pointsFin.get(i).getX() * alpha);
            int y = (int) (pointsDebut.get(i).getY() * (1 - alpha) + pointsFin.get(i).getY() * alpha);
            Point a = new Point(x,y);
            res.add(i,a);
        }

        // retourne le tableau des points intermédiaires
        return res;
    }

    public void creerGif(ArrayList<BufferedImage> images) throws IIOException, IOException{
        File fichierGif = new File("morph.gif");
        ImageOutputStream output = new FileImageOutputStream(fichierGif);
        GifSequenceWriter gifWriter = new GifSequenceWriter(output, BufferedImage.TYPE_INT_ARGB, 100, true);
        for (int i=0; i<images.size();i++){
            gifWriter.writeToSequence(images.get(i));
        }
        gifWriter.close();
        output.close();
    }


}