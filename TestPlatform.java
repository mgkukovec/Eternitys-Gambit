import java.awt.Color;
import java.awt.Graphics;

public class TestPlatform extends Object {

	public TestPlatform(int x, int y, int width, int height, ObjectID id, Handler handler) {
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
		
		if(PlayerHUD.debugMode) {
			g.setColor(Color.white);
			g.drawRect(x, y, width, height);
		}
	}

}
