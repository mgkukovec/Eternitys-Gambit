import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class Sprite {

	protected int gravity = 3;
	protected int speed = 12;
	protected boolean jumpAvailable;
	protected boolean inCollision = false;
	protected ObjectID standingOn;
	protected boolean falling;
	protected boolean facingRight;
	// Coordinates, origin at top left	
	protected int x, y;
	protected int prevX, prevY;
	protected int xVelocity, yVelocity;
	protected int xAccel, yAccel;
	protected int width, height;
	protected int health;
	protected SpriteID id;
	protected BufferedImage spriteModel;
	
	public Sprite(int x, int y, int width, int height, SpriteID id) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.id = id;
		falling = true;
		facingRight = true;
		standingOn = null;
		jumpAvailable = true;
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);

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
	
	public int getPrevX() {
		return prevX;
	}

	public void setPrevX(int prevX) {
		this.prevX = prevX;
	}

	public int getPrevY() {
		return prevY;
	}

	public void setPrevY(int prevY) {
		this.prevY = prevY;
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
	
	public int getxAccel() {
		return xAccel;
	}
	
	public void setxAccel(int xAccel) {
		this.xAccel = xAccel;
	}
	
	public int getyAccel() {
		return yAccel;
	}
	
	public void setyAccel(int yAccel) {
		this.yAccel = yAccel;
	}

	public SpriteID getId() {
		return id;
	}

	public void setId(SpriteID id) {
		this.id = id;
	}
	
	public Rectangle getBoundingBox() {
		return new Rectangle(x, y, width, height);
	}
}
