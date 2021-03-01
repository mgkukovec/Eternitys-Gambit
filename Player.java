import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Player extends Sprite {

	long msTimeJumpQueued = 0;
	long msTimeLastOnPlatform = 0;
	boolean previouslyPressingJump = false;
	boolean jumpedSinceOnPlatform = false;
	int msBufferTime = 150; // Able to "queue" a jump N milliseconds before touching ground
	int msCoyoteTime = 150; // Able to jump N milliseconds after leaving the ground

	public Player(int x, int y, int width, int height, SpriteID id, Handler handler, BufferedImage ss) {
		super(x, y, width, height, id);
		this.handler = handler;

		SpriteSheet spriteSheet = new SpriteSheet(ss);
		spriteModel = spriteSheet.grabImage(1, 1, 60, 90, 60, 90);
		health = 100;
		speed = 14;
		bodyDamage = 0;
	}

	// Not every Sprite will have the same collision
	// Enemies do not deal damage to each other
	// Some enemies can ignore platforms
	private boolean collisionWithSprites() {
		for (Sprite tempSprite : handler.loadedSprites) {
			if (getBoundingBox().intersects(tempSprite.getBoundingBox())) {
				// Collision with enemy, replace 5 with enemyCollisionDamage
				// Also need a timer so you aren't constantly taking collision damage
				health -= 5;
				return true;
			}
		}
		return false;
	}

	public void tick() {

		this.setxPrev(x);
		this.setyPrev(y);

		xVel = 0;

		// Movement
		if (KeyInput.isPressed(KeyEvent.VK_A)) {
			xVel += -speed;
			facingRight = false;
		}
		if (KeyInput.isPressed(KeyEvent.VK_D)) {
			xVel += speed;
			facingRight = true;
		}
		
		// Queues a jump if jump key is pressed, does not count holding jump key
		if (KeyInput.isPressed(KeyEvent.VK_SPACE) && !previouslyPressingJump) {
			msTimeJumpQueued = System.currentTimeMillis();
		}

		// Determine if able to jump
		boolean inCoyoteTime = System.currentTimeMillis() < msTimeLastOnPlatform + msCoyoteTime;
		boolean inBufferTime = System.currentTimeMillis() < msTimeJumpQueued + msBufferTime;
		if(standingOn != null) {
			jumpedSinceOnPlatform = false;
		}
		
		// Jump
		if (!jumpedSinceOnPlatform && inBufferTime && inCoyoteTime) {
			yVel = -45;
			falling = true;
			jumpedSinceOnPlatform = true;
		}

		// Debug
		if (KeyInput.isPressed(KeyEvent.VK_F3)) {
			PlayerHUD.toggleDebug();
		}

		yVel = Game.clamp(yVel, -yVelMax, yVelMax);

		x += xVel;
		y += yVel;

		if (x != xPrev || y != yPrev)
			CollisionDetection.collisionWithObjects(this, handler);

		if (falling) {
			yVel += Game.gravity;
		} else {
			yVel = 0;
			msTimeLastOnPlatform = System.currentTimeMillis();
		}
		
		previouslyPressingJump = KeyInput.isPressed(KeyEvent.VK_SPACE);
	}

	public void render(Graphics g) {
		if (facingRight)
			g.drawImage(spriteModel, x, y, width, height, null);
		else
			g.drawImage(spriteModel, x + width, y, -width, height, null);

		if (PlayerHUD.debugMode) {
			g.setColor(Color.white);
			g.drawRect(x, y, width, height);
		}
	}
}
