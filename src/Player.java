import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Player extends Sprite {

	// Jump stuff
	final int tickCoyoteTime = 5;
	final int tickBufferTime = 4;
	int tickCoyoteStart = 0;
	int tickBufferStart = 0;
	boolean jumpedOnce = false;
	boolean jumping = false;
	int maxJumpHoldTicks = 8;
	int jumpTickStart = 0;
	
	// Attack stuff
	int tickOfLastAttack = -100;
	int attackDamage = 10;
	int attackWidth = 140;
	int attackHeight = 120;
	int tickAttackDuration = 4;
	int tickAttackDelay = 10;
	Rectangle attackHitbox;
	
	int health;

	public Player(int x, int y, SpriteID id, Handler handler, BufferedImage ss) {
		super(x, y, 60, 90, id, ss, handler);

		spriteSheet = new SpriteSheet(ss);
		spriteModel = spriteSheet.grabImage(1, 1, 60, 90, 60, 120);
		health = 100;
		speed = 16;
		bodyDamage = 0;
		yVelMax = 43;
		hitbox = new Rectangle(x, y, width, height);
		attackHitbox = new Rectangle(0, 0, 0, 0);
	}

	public void tick() {

		this.setxPrev(x);
		this.setyPrev(y);
		xVel = 0;

		KeyInput.updatePlayer(this);

		if (attemptingJump() && ableToJump()) {
			jumping = true;
			jumpedOnce = true;
			jumpTickStart = Game.currentTick;
		}
		
		// Jumping
		if (jumping && maxJumpHoldTicks + jumpTickStart > Game.currentTick && KeyInput.isPressed(KeyEvent.VK_SPACE)) {
			if (Game.currentTick - jumpTickStart == 0) {
				yVel = -35 + ((Game.currentTick - jumpTickStart) / 2);
			}
		}
		else {
			jumping = false;
			
			if (yVel < 0 && maxJumpHoldTicks + jumpTickStart > Game.currentTick) {
				yVel += Game.gravity * 3;
				System.out.println("Fast grav");
			} else {
				yVel += Game.gravity;
			}
		}

		yVel = Game.clamp(yVel, -43, 43);

		x += xVel;
		y += yVel;
		
		doCollision();
		
		hitbox.x = x;
		hitbox.y = y;
		
		//CollisionDetection.collisionWithSprites(this, handler);

		if (standingOn == null) {
			//yVel += Game.gravity;
		} else {
			yVel = 0;
			tickCoyoteStart = Game.currentTick;
			jumpedOnce = false;
		}
		
		if(attacking()) {
			attack();
		}
	}

	public void render(Graphics g) {
		if (facingRight) {
			g.drawImage(spriteModel, x, y, width, height, null);
			if (attacking()) {
				g.drawImage(spriteSheet.grabImage(attackAnimationFrame(), 3, 140, 120, 140, 120), attackHitbox.x, attackHitbox.y, attackHitbox.width, attackHitbox.height, null);
			}
		}
		else {
			g.drawImage(spriteModel, x + width, y, -width, height, null);
			if (attacking()) {
				g.drawImage(spriteSheet.grabImage(attackAnimationFrame(), 3, 140, 120, 140, 120), attackHitbox.x + attackHitbox.width, attackHitbox.y, -attackHitbox.width, attackHitbox.height, null);
			}
		}

		if (PlayerHUD.debugMode) {
			g.setColor(Color.white);
			g.drawRect(x, y, width, height);
			g.setColor(Color.red);
			g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
			
			if (attacking()) {
				g.setColor(Color.orange);
				g.drawRect(attackHitbox.x, attackHitbox.y, attackHitbox.width, attackHitbox.height);
			}
		}
	}

	public boolean attemptingJump() {
		return Game.currentTick < tickBufferStart + tickBufferTime;
	}
	public boolean ableToJump() {
			return Game.currentTick < tickCoyoteStart + tickCoyoteTime && !jumpedOnce;
	}
	
	public boolean ableToAttack() {
		return Game.currentTick > tickOfLastAttack + tickAttackDelay;
	}
	public boolean attacking() {
		return tickOfLastAttack + tickAttackDuration > Game.currentTick;
	}
	public int attackAnimationFrame() {
		return Game.currentTick - tickOfLastAttack + 1;
	}
	public void attack() {
		if (facingRight) {
			attackHitbox = new Rectangle(x + width, y + height - attackHeight, attackWidth, attackHeight);
		} else {
			attackHitbox = new Rectangle(x - attackWidth, y + height - attackHeight, attackWidth, attackHeight);
		}
		// Do damage based on hitbox
		if (attackAnimationFrame() == 3) {
			LinkedList<Sprite> spritesHit = CollisionDetection.collisionWithSprites(attackHitbox, handler);
			for (Sprite s : spritesHit) {
				s.health -= attackDamage;
			}
		}
		
		// Belongs in render
		System.out.println("Attack Frame: " + (attackAnimationFrame()));
	}

}
