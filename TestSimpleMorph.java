import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class TestSimpleMorph {
    public static void main(String[] args) throws IOException {
        Point a = new Point(150,150);
        Point b = new Point(375,150);
        Point c = new Point(375,375);
        Point d = new Point(150,375);
        //Point e = new Point(250,375);

        Point a2 = new Point(250,125);
        Point b2 = new Point(250,125);
        Point c2 = new Point(400,400);
        Point d2 = new Point(125,375);
        //Point e2 = new Point(250,375);


        ArrayList<Point> debut = new ArrayList<>();
        ArrayList<Point> fin = new ArrayList<>();
        
        debut.add(a);
        debut.add(b);
        debut.add(c);
        debut.add(d);
        //debut.add(e);

        fin.add(a2);
        fin.add(b2);
        fin.add(c2);
        fin.add(d2);
        //fin.add(e2);

        SimpleImageMorph test = new SimpleImageMorph(debut, fin, 20, "img/test.png");

        BufferedImage[] tab = test.morphing();
        
        // création du GIF
        test.creerGif(tab);
        
    }
}
