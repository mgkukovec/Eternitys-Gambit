import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class BasicPlatform extends Object {

	public BasicPlatform(int x, int y, int width, int height, ObjectID id, Handler handler) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.id = id;
	}

	public void tick() {
		
	}

	public void render(Graphics g) {
		g.setColor(Color.MAGENTA);
		g.fillRect(x, y, width, height);
	}

	public Rectangle getBoundingBox() {
		return new Rectangle(x, y, width, height);
	}

}
