import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Player extends Sprite {

	Handler handler;

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
		
		for (Object tempObject : handler.loadedObjects) {
				Rectangle position = CollisionDetection.resolveSpriteObjectCollision(this, tempObject);
				if(this.x != position.x || this.y != position.y) {
					collision = true;
					// Potentially deal damage to player or affect in another way (spikes)
				}
				if(this.y > position.y) {
					// Collision with ground
					falling = false;
				} else if(this.y < position.y) {
					// Collision with ceiling
					yVelocity = 0;
				}
				this.x = position.x;
				this.y = position.y;
		}
		if (collision == false) {
			falling = true;
		}
		return collision;
	}

	public void tick() {
		
		this.setPrevX(x);
		this.setPrevY(y);
		
		xVelocity = 0;

		// Movement
		if (KeyInput.isPressed(KeyEvent.VK_A)) {
			xVelocity += -9;
			facingRight = false;
		}
		if (KeyInput.isPressed(KeyEvent.VK_D)) {
			xVelocity += 9;
			facingRight = true;
		}
		if (KeyInput.isPressed(KeyEvent.VK_W))
			yVelocity += -9;
		if (KeyInput.isPressed(KeyEvent.VK_S))
			yVelocity += 9;
		if (KeyInput.isPressed(KeyEvent.VK_SPACE) && falling == false) {
			yVelocity = -30;
			falling = true;
		}
		
		// Debug
		if (KeyInput.isPressed(KeyEvent.VK_F3) && PlayerHUD.pressingDebugKeyOnLastCheck == false) {
			PlayerHUD.debugMode = !PlayerHUD.debugMode;
		}
		PlayerHUD.pressingDebugKeyOnLastCheck = KeyInput.isPressed(KeyEvent.VK_F3);
		
		if(falling) {
			yVelocity += gravity;
		} else {
			yVelocity = 0;
		}
		
		yVelocity = Game.clamp(yVelocity, -30, 30);
		
		x += xVelocity; // Replace using acceleration for running
		y += yVelocity; // Replace with gravity equation for jumping
		
		//System.out.println("\nPREVIOUS (" + prevX + ", " + prevY + ")");
		//System.out.println("NEW (" + x + ", " + y + ")");
		
		//int preCollisionX = x;
		//int preCollisionY = y;
		
		collisionWithObjects();
			//falling = true;
		
		//x = Game.clamp(x, Math.min(prevX, preCollisionX), Math.max(prevX, preCollisionX));
		//y = Game.clamp(y, Math.min(prevY, preCollisionY), Math.max(prevY, preCollisionY));
	}

	public void render(Graphics g) {
		g.setColor(Color.blue);
		g.fillRect(x, y, width, height);
		
		if(facingRight)
			g.drawImage(spriteModel, x, y, width, height, null);
		else
			g.drawImage(spriteModel, x + width, y, -width, height, null);
	}
}
