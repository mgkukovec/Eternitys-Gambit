import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Player extends Sprite {

	Handler handler;
	long msTimeOfLastSpacePress = 0;
	long msTimeLastOnPlatform = 0;
	int msJumpBuffer = 50;	// Able to "queue" a jump before touching the ground within N milliseconds
	int msCoyoteTime = 150;	// Able to jump while falling if touching platform at most N milliseconds prior

	public Player(int x, int y, int width, int height, SpriteID id, Handler handler, BufferedImage ss) {
		super(x, y, width, height, id);
		this.handler = handler;
		
		SpriteSheet spriteSheet = new SpriteSheet(ss);
		spriteModel = spriteSheet.grabImage(1, 1, 60, 90, 60, 90);
		health = 100;
	}

	// Not every Sprite will have the same collision
	// Enemies do not deal damage to each other
	// Some enemies can ignore platforms
	private boolean collisionWithSprites() {
		for (Sprite tempSprite : handler.loadedSprites) {
			if (tempSprite.getId() == SpriteID.Enemy) {
				if (getBoundingBox().intersects(tempSprite.getBoundingBox())) {
					// Collision with enemy, replace 5 with enemyCollisionDamage
					// Also need a timer so you aren't constantly taking collision damage
					health -= 5;
					return true;
				}
			}
		}
		return false;
	}
	
	
	private boolean collisionWithObjects() {
		// create a list of some kind to hold all the objects that MIGHT cause collision
		boolean collision = false;
		falling = true;
		this.standingOn = null;
		
		for (Object o : handler.loadedObjects) {
				Rectangle resolvedPos = CollisionDetection.resolveAABBCollision(this, o);
				if (this.x != resolvedPos.x || this.y != resolvedPos.y) {
					collision = true;
					
					if (this.y < resolvedPos.y) {
						// Collision with ceiling
						yVelocity = 0;
					}
				}
				
				this.x = resolvedPos.x;
				this.y = resolvedPos.y;
				
				// On top of tempObject
				if(this.y + this.height == o.y && !((this.x < o.x && this.x + this.width < o.x) || (this.x > o.x + o.width && this.x + this.width > o.x + o.width))) {
					collision = true;
					falling = false;
					this.jumpAvailable = true;
					this.standingOn = o.id;
				}
		}
		return collision;
	}

	public void tick() {
		
		this.setPrevX(x);
		this.setPrevY(y);
		
		xVelocity = 0;

		// Movement
		if (KeyInput.isPressed(KeyEvent.VK_A)) {
			xVelocity += -speed;
			facingRight = false;
		}
		if (KeyInput.isPressed(KeyEvent.VK_D)) {
			xVelocity += speed;
			facingRight = true;
		}
		if (KeyInput.isPressed(KeyEvent.VK_SPACE)) {
			//yVelocity = -30;
			msTimeOfLastSpacePress = System.currentTimeMillis();
		}
		
		if ((msCoyoteTime <= 0 && falling == false) || (jumpAvailable && msTimeOfLastSpacePress >= System.currentTimeMillis() - msJumpBuffer && System.currentTimeMillis() < msTimeLastOnPlatform + msCoyoteTime)) {
			yVelocity = -45;
			falling = true;
			jumpAvailable = false;
		}
		
		// Debug
		if (KeyInput.isPressed(KeyEvent.VK_F3) && PlayerHUD.pressingDebugKeyOnLastCheck == false) {
			PlayerHUD.debugMode = !PlayerHUD.debugMode;
		}
		PlayerHUD.pressingDebugKeyOnLastCheck = KeyInput.isPressed(KeyEvent.VK_F3);
		
		yVelocity = Game.clamp(yVelocity, -45, 40);
		
		x += xVelocity; // Replace using acceleration for running
		y += yVelocity; // Replace with gravity equation for jumping
		
		inCollision = collisionWithObjects();
		
		if(falling) {
			yVelocity += gravity;
		} else {
			yVelocity = 0;
			msTimeLastOnPlatform = System.currentTimeMillis();
		}
	}

	public void render(Graphics g) {
		if(facingRight)
			g.drawImage(spriteModel, x, y, width, height, null);
		else
			g.drawImage(spriteModel, x + width, y, -width, height, null);
		
		if(PlayerHUD.debugMode) {
			g.setColor(Color.white);
			g.drawRect(x, y, width, height);
		}
	}
}
