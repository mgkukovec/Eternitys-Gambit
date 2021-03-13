import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Slime extends Enemy {

	static int colliderWidth = 122;
	static int colliderHeight = 76;

	private int tickOfLastJump = 0;
	private int jumpDelayTicks = 60;

	public Slime(int x, int y, SpriteID id, Handler handler, BufferedImage ss) {
		super(x, y, 122, 76, id, ss, handler);

		hitbox = new Rectangle(x + 10, y - 10, width - (10 * 2), height - 10);
		spriteModel = spriteSheet.grabImage(1, 1, 122, 76, 125, 76);
		idleCycles = 10;

		health = 30;
		speed = 8;
		bodyDamage = 10;
		yVelMax = 43;
		aggroRange = 500;
		killValue = (int) ((Math.random() * (5 - 2)) + 2);
	}

	public void tick() {
		
		// Check death conditions
		if (health <= 0) {
			handler.loadedSprites.remove(this);
			return;
		}
		
		super.tick();
		
		if (ableToJump()) {
			jump();
		}

		// Can only move horizontally if jumping
		if (standingOn == null) {
			yVel += Game.gravity;
			yVel = Game.clamp(yVel, -yVelMax, yVelMax);
			x += xVel;
			y += yVel;
		} else {
			yVel = 0;
		}

		doCollision();

		hitbox.x = x + 10;
		hitbox.y = y + 10;

	}

	public void render(Graphics g) {
		spriteModel = spriteSheet.grabImage((int) (Game.currentTick) % idleCycles + 1, 1, 122, 76, 125, 76);
		super.render(g);
	}

	public void jump() {
		// Chooses direction based on player if aggro'd, otherwise random
		if ((aggro) ? playerToRight() : Math.signum(Math.random() - 0.5) > 0) {
			xVel = -speed;
			facingRight = true;
		} else {
			xVel = speed;
			facingRight = false;
		}

		yVel = (aggro) ? -30 : -18; // Jump higher if aggro'd
		standingOn = null;
		tickOfLastJump = Game.currentTick;
	}

	public boolean ableToJump() {
		int aggroAdjustedJumpDelay = (aggro) ? jumpDelayTicks : jumpDelayTicks * 2;
		return standingOn != null && Game.currentTick > tickOfLastJump + aggroAdjustedJumpDelay;
	}

}
