package hr.fer.seminar.window;

/**
 * Represents point in 2D coordinate system.
 * 
 * @author Filip Husnjak
 */
public class Point {
	
	/**
	 * X coordinate
	 */
	private int x;
	
	/**
	 * Y coordinate
	 */
	private int y;
	
	/**
	 * Constructs new {@link Point} with specified parameters
	 * 
	 * @param x
	 *        x coordinate
	 * @param y
	 *        y coordinate
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns x coordinate of this point
	 * 
	 * @return x coordinate of this point
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns y coordinate of this point
	 * 
	 * @return y coordinate of this point
	 */
	public int getY() {
		return y;
	}
	
}
