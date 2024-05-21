import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class TestCurvedImage {
     public static void main(String[] args) throws IOException {
        Point a = new Point(250,100);
        Point b = new Point(250,100);
        Point c = new Point(100,200);
        Point d = new Point(300,200);

        Point e = new Point(300,300);
        Point f = new Point(300,300);
        Point g = new Point(300,200);
        Point h = new Point(200,300);

        Point i = new Point(150,200);
        Point j = new Point(150,250);
        Point k = new Point(200,300);
        Point l = new Point(100,200);


        Point a2 = new Point(100,150);
        Point b2 = new Point(100,150);
        Point c2 = new Point(100,300);
        Point d2 = new Point(200,100);

        Point e2 = new Point(225,200);
        Point f2 = new Point(325,250);
        Point g2 = new Point(200,100);
        Point h2 = new Point(400,400);

        Point i2 = new Point(300,450);
        Point j2 = new Point(150,400);
        Point k2 = new Point(400,400);
        Point l2 = new Point(100,300);

        ArrayList<Point> coord = new ArrayList<>();

        // Points de la forme du cœur
        coord.add(new Point(250, 250)); // A
        coord.add(new Point(400, 100)); // B
        coord.add(new Point(250, 400)); // C
        coord.add(new Point(100, 100)); // D

        // Points de contrôle pour les courbes de Bézier
        coord.add(new Point(325, 150)); // E
        coord.add(new Point(325, 350)); // F
        coord.add(new Point(175, 150)); // G
        coord.add(new Point(175, 350)); // H


        ArrayList<Point> debut = new ArrayList<>();
        ArrayList<Point> fin = new ArrayList<>();
        debut.add(c);
        debut.add(a);
        debut.add(b);
        debut.add(d);

        debut.add(g);
        debut.add(e);
        debut.add(f);
        debut.add(h);

        debut.add(k);
        debut.add(i);
        debut.add(j);
        debut.add(l);
        
        fin.add(c2);
        fin.add(a2);
        fin.add(b2);
        fin.add(d2);

        fin.add(g2);
        fin.add(e2);
        fin.add(f2);
        fin.add(h2);

        fin.add(k2);
        fin.add(i2);
        fin.add(j2);
        fin.add(l2);

        CurvedImageMorph test = new CurvedImageMorph(debut, fin, 20, "img/morph0.png");

        BufferedImage[] tab = test.morphing();
        
        // création du GIF
        test.creerGif(tab);
        
    }
}
