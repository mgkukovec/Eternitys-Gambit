import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Slime extends Sprite {
	
	static int colliderWidth = 122;
	static int colliderHeight = 76;
	
	private int aggroRange = 700;
	private int tickOfLastJump = 0;
	private int jumpDelayTicks = 60;
	private boolean aggro = false;
	private int coinValue;

	public Slime(int x, int y, SpriteID id, Handler handler, BufferedImage ss) {
		super(x, y, 122, 76, id, ss, handler);
		hitbox = new Rectangle(x + 10, y - 10, width - (10 * 2), height - 10);
		
		spriteSheet = new SpriteSheet(ss);
		spriteModel = spriteSheet.grabImage(1, 1, 122, 76, 125, 76);
		this.handler = handler;
		health = 20;
		speed = 8;
		bodyDamage = 10;
		idleCycles = 10;
		yVelMax = 43;
		coinValue = (int) ((Math.random() * (5 - 2)) + 2);
	}

	public void tick() {
		this.setxPrev(x);
		this.setyPrev(y);
		
		aggro = awareOfPlayer();

		attemptJump();
		
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
		hitbox.x = x + 10;
		hitbox.y = y + 10;
		
	}

	public void render(Graphics g) {
		spriteModel = spriteSheet.grabImage((int) (Game.currentTick / 1.5) % idleCycles + 1, 1, 122, 76, 125, 76);
		
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
	
	public boolean attemptJump() {
		int aggroAdjustedJumpDelay = (aggro) ? jumpDelayTicks : jumpDelayTicks * 2;
		if (standingOn != null && Game.currentTick > tickOfLastJump + aggroAdjustedJumpDelay) {
			
			// Chooses direction
			if ((aggro) ? playerToRight() : Math.signum(Math.random() - 0.5) > 0) {
				xVel = -speed;
				facingRight = true;
			} else {
				xVel = speed;
				facingRight = false;
			}
			
			yVel = (aggro) ? -25 : -15;
			standingOn = null;
			tickOfLastJump = Game.currentTick;
			return true;
		}
		return false;
	}
	
	public boolean playerToRight() {
		if(handler.playerLoaded()) {
			return ((x + width / 2) - (handler.getPlayer().x + handler.getPlayer().width / 2) > 0);
		}
		return false;
	}
	
	public int distanceToPlayer() {
		return (Math.abs(x - handler.getPlayer().x));
	}
	
	public boolean awareOfPlayer() {
		return distanceToPlayer() < aggroRange;
	}

}
