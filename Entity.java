import java.awt.Graphics;

public abstract class Entity {

	// Coordinates, origin at bottom left	
	protected int x, y;
	protected int xVelocity, yVelocity;
	protected EntityID id;
	
	public Entity(int x, int y, EntityID id ) {
		this.x = x;
		this.y = y;
		this.id = id;
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

	public EntityID getId() {
		return id;
	}

	public void setId(EntityID id) {
		this.id = id;
	}
}
