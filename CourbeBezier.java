import java.util.ArrayList;

public class CourbeBezier {

    //contient 4 points: depart, controle1, controle2, fin
    private Point[] tab;
    
    public CourbeBezier(Point depart, Point ctrl1, Point ctrl2, Point fin){
        tab = new Point[4];
        tab[0]=depart;
        tab[1]=ctrl1;
        tab[2]=ctrl2;
        tab[3]=fin;
    }
    
    public Point[] getTab(){
        return tab;
    }

    public ArrayList<Point> courbe(int nbr){
        ArrayList<Point> pointsCourbe = new ArrayList<>();
        
        for (int i = 0; i <= nbr; i++) {
            double t = (double) i / nbr;
            Point pointCourant = calculerPointCourbe(t);
            pointsCourbe.add(pointCourant);
        }
        
        return pointsCourbe;
    }

    private Point calculerPointCourbe(double t) {
        double x = 0, y = 0;
        Point[] points = this.getTab();

        // Formule de Bézier cubique
        double oneMinusT = 1 - t;

        // P(t) = (1-t)^3 * P0 + 3 * (1-t)^2 * t * P1 + 3 * (1-t) * t^2 * P2 + t^3 * P3
        x += Math.pow(oneMinusT, 3) * points[0].getX();
        y += Math.pow(oneMinusT, 3) * points[0].getY();

        x += 3 * Math.pow(oneMinusT, 2) * t * points[1].getX();
        y += 3 * Math.pow(oneMinusT, 2) * t * points[1].getY();

        x += 3 * oneMinusT * Math.pow(t, 2) * points[2].getX();
        y += 3 * oneMinusT * Math.pow(t, 2) * points[2].getY();

        x += Math.pow(t, 3) * points[3].getX();
        y += Math.pow(t, 3) * points[3].getY();

        return new Point((int) x, (int) y);
    }


}