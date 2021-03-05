import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Coin extends Sprite {

	private int value;
	private int idleCycleOffset;
	
	public Coin(int x, int y, SpriteID id, Handler handler, BufferedImage ss, int value) {
		super(x, y, 45, 60, id, ss, handler);
		
		spriteSheet = new SpriteSheet(ss);
		spriteModel = spriteSheet.grabImage(1, 1, 45, 60, 45, 60);
		
		idleCycles = 30;
		
		this.value = value;
		idleCycleOffset = (int) ((Math.random() * (idleCycles - 0)) + 0);
		System.out.println(idleCycleOffset);
		
		yVelMax = 23;
		hitbox = new Rectangle(x, y, 45, 45);
	}

	public void tick() {
		this.setxPrev(x);
		this.setyPrev(y);
		
		yVel = Game.clamp(yVel, -yVelMax, yVelMax);

		// Can only move if jumping
		if (standingOn == null) {
			x += xVel;
			y += yVel;
		}

		if (x != xPrev || y != yPrev) {
			CollisionDetection.collisionWithObjects(this, handler);
		}

		if (standingOn == null) {
			yVel += Game.gravity;
		} else {
			yVel = 0;
		}
		
		spriteModel = spriteSheet.grabImage(1, 1, 45, 60, 46, 60);
		hitbox.x = x;
		hitbox.y = y - Game.floatingYPosition(idleCycles, idleCycleOffset, 15);
	}

	public void render(Graphics g) {
		
		g.drawImage(spriteModel, hitbox.x, hitbox.y, width, height, null);
		
		if (PlayerHUD.debugMode) {
			g.setColor(Color.white);
			g.drawRect(x, y, width, height);
			g.setColor(Color.red);
			g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
		}
	}
}
