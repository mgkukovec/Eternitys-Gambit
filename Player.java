import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class Player extends Sprite {

	Handler handler;

	public Player(int x, int y, int width, int height, SpriteID id, Handler handler) {
		super(x, y, width, height, id);
		this.handler = handler;
	}

	// Not every Sprite will have the same collision
	// Enemies do not deal damage to each other
	// Some enemies can ignore platforms
	private void collision(int prevX, int prevY) {
		// Collision with Enemies
		for (Sprite tempSprite : handler.loadedSprites) {
			if (tempSprite.getId() == SpriteID.Enemy) {
				if (getBoundingBox().intersects(tempSprite.getBoundingBox())) {
					// Collision with enemy, replace 5 with enemyCollisionDamage
					// Also need a timer so you aren't constantly taking collision damage
					PlayerHUD.playerHealth -= 5;
				}
			}
		}

		// Collision with Object
		for (Object tempObject : handler.loadedObjects) {
				Rectangle position = CollisionDetection.rayBasedCollisionAdjustment(this, prevX, prevY, tempObject.getBoundingBox());
				this.x = position.x;
				this.y = position.y;
				
		}
	}

	public void tick() {

		xVelocity = 0;
		yVelocity = 0;

		if (KeyInput.isPressed(KeyEvent.VK_A))
			xVelocity += -7;
		if (KeyInput.isPressed(KeyEvent.VK_D))
			xVelocity += 7;
		if (KeyInput.isPressed(KeyEvent.VK_W))
			yVelocity += -7;
		if (KeyInput.isPressed(KeyEvent.VK_S))
			yVelocity += 7;
		if (KeyInput.isPressed(KeyEvent.VK_SPACE) && falling == false) {
			yVelocity = -30;
			falling = true;
		}
		
		if(falling) {
			yVelocity += gravity;
		}

		this.setPrevX(x);
		this.setPrevY(y);
		
		yVelocity = Game.clamp(yVelocity, -20, 12);
		
		x += xVelocity; // Replace using acceleration for running
		y += yVelocity; // Replace with gravity equation for jumping
		
		//System.out.println("\nPREVIOUS (" + prevX + ", " + prevY + ")");
		//System.out.println("NEW (" + x + ", " + y + ")");
		
		int preCollisionX = x;
		int preCollisionY = y;
		
		collision(prevX, prevY);
			//falling = true;
		
		x = Game.clamp(x, Math.min(prevX, preCollisionX), Math.max(prevX, preCollisionX));
		y = Game.clamp(y, Math.min(prevY, preCollisionY), Math.max(prevY, preCollisionY));
	}

	public void render(Graphics g) {
		g.setColor(Color.blue);
		g.fillRect(x, y, width, height);
	}
}
