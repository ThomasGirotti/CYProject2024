package morphisme;

public class Point {
	private int x;
	private int y;
	private String nom;
	public Point(int x, int y) {
		this.x = x;
        this.y = y;
	}

    public Point(int x, int y, String nom) {
		this(x, y);
		this.nom = nom;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
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
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		return x == other.x && y == other.y;
	}

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}

	public float distance(Point p){
		return(int)(Math.sqrt(Math.pow(x-p.x, 2)+Math.pow(y-p.y, 2)));
	}
}