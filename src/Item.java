import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class Item extends Sprite {

	public Item(int x, int y, int width, int height, SpriteID id, BufferedImage ss, Handler handler) {
		super(x, y, width, height, id, ss, handler);
	}
	
	public void tick() {
		this.setxPrev(x);
		this.setyPrev(y);
		
		if (standingOn == null) {
			yVel += Game.gravity;
		} else {
			yVel = 0;
		}
		yVel = Game.clamp(yVel, -yVelMax, yVelMax);
	}

	public void render(Graphics g) {
		g.drawImage(spriteModel, hitbox.x, hitbox.y, hitbox.width, hitbox.height, null);
		
		if (PlayerHUD.debugMode) {
			g.setColor(Color.white);
			g.drawRect(x, y, width, height);
			g.setColor(Color.red);
			g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
		}
	}
}
