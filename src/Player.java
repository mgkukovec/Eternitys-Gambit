import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Player extends Sprite {

	final int tickCoyoteTime = 5;
	final int tickBufferTime = 4;
	int tickCoyoteStart = 0;
	int tickBufferStart = 0;

	public Player(int x, int y, SpriteID id, Handler handler, BufferedImage ss) {
		super(x, y, 60, 90, id, ss, handler);

		spriteSheet = new SpriteSheet(ss);
		spriteModel = spriteSheet.grabImage(1, 1, 60, 90, 60, 90);
		health = 100;
		speed = 14;
		bodyDamage = 0;
		yVelMax = 43;
		hitbox = new Rectangle(x, y, width, height);
	}

	public void tick() {

		this.setxPrev(x);
		this.setyPrev(y);
		xVel = 0;

		KeyInput.updatePlayer(this);

		if (attemptingJump() && ableToJump()) {
			jump();
		}

		yVel = Game.clamp(yVel, -yVelMax, yVelMax);

		x += xVel;
		y += yVel;
		
		if (x != xPrev || y != yPrev) {
			CollisionDetection.collisionWithObjects(this, handler);
		}
		
		hitbox.x = x;
		hitbox.y = y;
		
		CollisionDetection.collisionWithSprites(this, handler);

		if (standingOn == null) {
			yVel += Game.gravity;
		} else {
			yVel = 0;
			tickCoyoteStart = Game.currentTick;
		}

	}

	public void render(Graphics g) {
		if (facingRight)
			g.drawImage(spriteModel, x, y, width, height, null);
		else
			g.drawImage(spriteModel, x + width, y, -width, height, null);

		if (PlayerHUD.debugMode) {
			g.setColor(Color.white);
			g.drawRect(x, y, width, height);
			g.setColor(Color.red);
			g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
		}
	}

	public boolean attemptingJump() {
		return Game.currentTick < tickBufferStart + tickBufferTime;
	}

	public boolean ableToJump() {
		return Game.currentTick < tickCoyoteStart + tickCoyoteTime && standingOn != null;
	}

	public void jump() {
		yVel = -yVelMax;
		standingOn = null;
	}

}
