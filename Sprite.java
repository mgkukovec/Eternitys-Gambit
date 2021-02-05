import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Sprite {

	// Coordinates, origin at top left	
	protected int x, y;
	protected int xVelocity, yVelocity;
	protected int width, height;
	protected SpriteID id;
	
	public Sprite(int x, int y, int width, int height, SpriteID id) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.id = id;
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);
	public abstract Rectangle getBoundingBox();

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

	public int getxVelocity() {
		return xVelocity;
	}

	public void setxVelocity(int xVelocity) {
		this.xVelocity = xVelocity;
	}

	public int getyVelocity() {
		return yVelocity;
	}

	public void setyVelocity(int yVelocity) {
		this.yVelocity = yVelocity;
	}

	public SpriteID getId() {
		return id;
	}

	public void setId(SpriteID id) {
		this.id = id;
	}
}
