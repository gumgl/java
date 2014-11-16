package Game;

public class Position { // Vector class
	protected int x, y;
	
	public Position() {
		this.x = 0;
		this.y = 0;
	}

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Position(Position position) {
		this.x = position.x;
		this.y = position.y;
	}
	
	public void move(int dx, int dy) {
		this.x += dx;
		this.y += dy;
	}
	
	public void move(Position position) {
		this.x += position.x;
		this.y += position.y;
	}
	
	// Checks if xMin <= x <= xMax
	public boolean inBounds(int xMin, int yMin, int xMax, int yMax) {
		return (x >= xMin && x <= xMax && y >= yMin && y <= yMax);
	}
	public boolean equals(Position position) {
		// If both moves are the same, subtracting them should cancel them out
		Position difference = subtract(this, position);
		return (difference.getX() == 0 && difference.getY() == 0);
	}

	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public String toString() {
		return "("+x+","+y+")";
	}
	
	public static Position add(Position... positions) {
		Position sum = new Position();
		for (Position position : positions)
			sum.move(position);
		return sum;
	}
	public static Position subtract(Position pos1, Position pos2) {
		return add(pos1, multiply(pos2, -1));
	}
	
	public static Position multiply(Position pos, float coefficient) {
		return new Position((int)(pos.getX() * coefficient), (int)(pos.getY() * coefficient));
	}
}
