import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Object {
	
	protected int x, y;
	protected int width, height;
	protected int Vx, Vy;
	protected ObjectID id;
	
	public abstract void tick();
	public abstract void render(Graphics g);

	public Rectangle getBoundingBox() {
		return new Rectangle(x, y, width, height);
	}
	
}
