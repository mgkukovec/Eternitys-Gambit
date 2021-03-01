import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BasicPlatform extends Object {
	
	SpriteSheet ss;

	public BasicPlatform(int x, int y, int width, int height, ObjectID id, BufferedImage ss) {
		super(x, y, width, height, id);
		this.ss = new SpriteSheet(ss);
		spriteModel = this.ss.grabImage(1, 1, 1920, 1080, 1920, 1080);
	}

	public void tick() {
		
	}

	public void render(Graphics g) {
		g.setColor(Color.MAGENTA);
		g.fillRect(x, y, width, height);
		g.drawImage(spriteModel, x, y, width, height, null);
		renderDebug(g);
	}

}
