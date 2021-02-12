import java.awt.Color;
import java.awt.Graphics;

public class BasicPlatform extends Object {

	public BasicPlatform(int x, int y, int width, int height, ObjectID id, Handler handler) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.id = id;
		Vx = 0;
		Vy = 0;
	}

	public void tick() {
		
	}

	public void render(Graphics g) {
		g.setColor(Color.MAGENTA);
		g.fillRect(x, y, width, height);
	}

}
