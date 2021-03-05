import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class Object {
	
	protected int x, y;
	protected int width, height;
	protected ObjectID id;
	protected BufferedImage spriteModel;
	
	public Object(int x, int y, int width, int height, ObjectID id) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.id = id;
	}
	
	public abstract void tick();
	
	/**
	 * Must call renderDebug(g).
	 */
	public abstract void render(Graphics g);

	public Rectangle getBoundingBox() {
		return new Rectangle(x, y, width, height);
	}
	
	public void renderDebug(Graphics g) {
		if(PlayerHUD.debugMode) {
			g.setColor(Color.white);
			g.drawRect(x, y, width, height);
		}
	}
	
}
