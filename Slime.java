import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Slime extends Sprite {
	
	protected int aggroRange = 700;
	protected int tickOfLastJump = 0;
	protected int jumpDelayTicks = 60;
	protected boolean aggro = false;

	public Slime(int x, int y, int width, int height, SpriteID id, Handler handler, BufferedImage ss) {
		super(x, y, width, height, id);
		
		SpriteSheet spriteSheet = new SpriteSheet(ss);
		spriteModel = spriteSheet.grabImage(2, 1, 60, 90, 60, 90);
		this.handler = handler;
		this.health = 20;
		this.speed = 8;
		this.bodyDamage = 10;
	}

	public void tick() {
		
		this.setxPrev(x);
		this.setyPrev(y);
		
		aggro = awareOfPlayer();

		attemptJump();
		
		yVel = Game.clamp(yVel, -yVelMax, yVelMax);

		// Can only move if jumping
		if (falling) {
			x += xVel;
			y += yVel;
		}

		if (x != xPrev || y != yPrev)
			CollisionDetection.collisionWithObjects(this, handler);

		if (falling) {
			yVel += Game.gravity;
		} else {
			yVel = 0;
			//msTimeLastOnPlatform = System.currentTimeMillis();
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
			falling = true;
			tickOfLastJump = Game.currentTick;
			return true;
		}
		return false;
	}
	
	public boolean playerToRight() {
		if(handler.playerLoaded()) {
			return (x - handler.getPlayer().x > 0);
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
