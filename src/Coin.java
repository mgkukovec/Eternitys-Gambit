import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Coin extends Item {

	private int value;
	private int idleCycleOffset;
	private int bounceCount = 1;
	
	public Coin(int x, int y, SpriteID id, Handler handler, BufferedImage ss, int value) {
		super(x, y, 45, 60, id, ss, handler);
		hitbox = new Rectangle(x, y, 45, 45);
		spriteModel = spriteSheet.grabImage(1, 1, hitbox.width, hitbox.height, 45, 45);
		
		// Shoot out in a random direction
		xVel = (int) (Math.random() * (9 + 9)) - 9;
		yVel = (int) (Math.random() * (20 - 20)) + 20;
		
		idleCycles = 30;
		
		this.value = value;
		idleCycleOffset = (int) ((Math.random() * (idleCycles - 0)) + 0);
		System.out.println(idleCycleOffset);
		
		yVelMax = 35;
	}

	public void tick() {
		super.tick();
		
		// Slow down horizontal speed
		if (Game.currentTick % 6 == 0) {
			xVel -= Math.signum(xVel);
		}
		x += xVel;
		
		// Fall or bounce
		if (standingOn == null) {
			y += yVel;
		} else {
			yVel = -(yVelMax / (bounceCount * 2));
			standingOn = null;
			bounceCount++;
		}

		doCollision();
		
		hitbox.x = x;
		hitbox.y = y - Game.floatingYPosition(idleCycles, idleCycleOffset, 15);
	}
}
