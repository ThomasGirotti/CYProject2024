package ui;

public class Point {
	private int x;
	private int y;
	private String nom;
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public Point(int x, int y, String nom) {
		this.x = x;
		this.y = y;
		this.nom = nom;
	}
	public double getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public String getNom() {
		return this.nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
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
	@Override
	public String toString() {
		return (this.getNom() + "(" + this.getX() + " , " + this.getY() + ")");
	}
}