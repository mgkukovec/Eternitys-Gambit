import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class Object {
	
	protected int x, y;
	protected int width, height;
	protected int Vx, Vy;
	protected ObjectID id;
	protected BufferedImage spriteModel;
	
	public abstract void tick();
	public abstract void render(Graphics g);

	public Rectangle getBoundingBox() {
		return new Rectangle(x, y, width, height);
	}
	
}
