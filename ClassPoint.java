
public class Point {
	private double x;
	private double y;
	private String nom;
	public Point(double x, double y, String nom) {
		this.x = x;
		this.y = y;
		this.nom = nom;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public String getNom() {
		return this.nom;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Point) {
			Point pObj = (Point) obj;
			if(this.getNom().equals(pObj.nom) && this.getX() == pObj.getX() && this.getY() == pObj.getY()) {
				return true;
			}
		}
		return false;
	}
	
	
}
