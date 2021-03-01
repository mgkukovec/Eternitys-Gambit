import java.awt.Color;
import java.awt.Graphics;

public class TestPlatform extends Object {

	public TestPlatform(int x, int y, int width, int height, ObjectID id) {
		super(x, y, width, height, id);
	}

	public void tick() {
		
	}

	public void render(Graphics g) {
		g.setColor(Color.MAGENTA);
		g.fillRect(x, y, width, height);
		renderDebug(g);
	}

}
