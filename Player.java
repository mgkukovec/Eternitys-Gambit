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
	private void collision() {
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
			if (getBoundingBox().intersects(tempObject.getBoundingBox())) {

				int playerLeftOverlap = tempObject.x + tempObject.width - x;
				int playerRightOverlap = x + width - tempObject.x;
				int playerBottomOverlap = y + height - tempObject.y;
				int playerTopOverlap = tempObject.y + tempObject.height - y;
				
				boolean overlappingLeft = false;
				boolean overlappingRight = false;
				boolean overlappingBottom = false;
				boolean overlappingTop = false;
				
				System.out.println("LEFT: " + playerLeftOverlap);
				System.out.println("RIGHT: " + playerRightOverlap);
				System.out.println("TOP: " + playerTopOverlap);
				System.out.println("BOTTOM: " + playerBottomOverlap);
				
				//Note: these collisions avoid checking if overlap > 0 because the intersects method guarantees they are all > 0
				
				// Bottom of Player collision
				if (Math.abs(playerBottomOverlap) < Math.abs(playerTopOverlap) && falling == true) {
					System.out.println("BOTTOM collision");
					overlappingBottom = true;
					falling = false;
				}
				// Top of Player Collision
				if (Math.abs(playerTopOverlap) < Math.abs(playerBottomOverlap)) {
					System.out.println("TOP collision");
					overlappingTop = true;
				}
				// Right of Player Collision
				if (Math.abs(playerRightOverlap) < Math.abs(playerLeftOverlap)) {
					System.out.println("RIGHT collision");
					overlappingRight = true;
				}
				// Left of Player Collision
				if (Math.abs(playerLeftOverlap) < Math.abs(playerRightOverlap)) {
					System.out.println("LEFT collision");
					overlappingLeft = true;
				}
				
				if(overlappingTop && overlappingLeft) {
					if(playerLeftOverlap < playerTopOverlap) {
						System.out.println("Adjusting RIGHT");
						x = tempObject.x + tempObject.width;
					} else {
						System.out.println("Adjusting DOWN");
						y = tempObject.y + tempObject.height;
					}
				} else if (overlappingTop && overlappingRight) {
					if(playerRightOverlap < playerTopOverlap) {
						x = tempObject.x - width;
						System.out.println("Adjusting LEFT");
					} else {
						System.out.println("Adjusting BOTTOM");
						y = tempObject.y + tempObject.height;
					}
				} else if (overlappingBottom && overlappingLeft) {
					if(playerLeftOverlap < playerBottomOverlap) {
						System.out.println("Adjusting RIGHT");
						x = tempObject.x + tempObject.width;
					} else {
						System.out.println("Adjusting UP");
						y = tempObject.y - height;
					}
				} else {
					if(playerRightOverlap < playerBottomOverlap) {
						System.out.println("Adjusting LEFT");
						x = tempObject.x - width;
					} else {
						System.out.println("Adjusting UP");
						y = tempObject.y - height;
					}
				}
				System.out.println("");
				
			}
		}
	}

	public void tick() {

		xVelocity = 0;

		if (KeyInput.isPressed(KeyEvent.VK_A))
			xVelocity += -4;
		if (KeyInput.isPressed(KeyEvent.VK_D))
			xVelocity += 4;
		if (KeyInput.isPressed(KeyEvent.VK_SPACE) && falling == false) {
			yVelocity = -30;
			falling = true;
		}
		
		if(falling) {
			yVelocity += gravity;
		}

		x += xVelocity; // Replace using acceleration for running
		y += yVelocity; // Replace with gravity equation for jumping
		
		yVelocity = Game.clamp(yVelocity, -20, 20);
		
		System.out.println("FALLING " + falling);
		collision();
		System.out.println("FALLING " + falling + "\n\n");
	}

	public void render(Graphics g) {
		g.setColor(Color.blue);
		g.fillRect(x, y, width, height);
	}

	public Rectangle getBoundingBox() {
		return new Rectangle(x, y, width, height);
	}

}
