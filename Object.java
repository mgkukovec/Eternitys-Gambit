import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Object {
	
	protected int x, y;
	protected int width, height;
	protected ObjectID id;
	
	public abstract void tick();
	public abstract void render(Graphics g);
	public abstract Rectangle getBoundingBox();
	
}
