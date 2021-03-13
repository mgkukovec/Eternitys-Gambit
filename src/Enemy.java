import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class Enemy extends Sprite {

	protected boolean aggro;
	protected int aggroRange;
	protected int killValue;
	
	public Enemy(int x, int y, int width, int height, SpriteID id, BufferedImage ss, Handler handler) {
		super(x, y, width, height, id, ss, handler);
		aggro = false;
	}

	public void tick() {
		setxPrev(x);
		setyPrev(y);
		aggro = awareOfPlayer();
	}

	public void render(Graphics g) {
		if (facingRight)
			g.drawImage(spriteModel, x, y, width, height, null);
		else
			g.drawImage(spriteModel, x + width, y, -width, height, null);

		if (PlayerHUD.debugMode) {
			g.setColor(Color.white);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
			g.drawString(String.valueOf(health), x, y - 2);
			g.drawRect(x, y, width, height);
			g.setColor(Color.red);
			g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
		}
	}
	
	public boolean playerToRight() {
		return (x + width / 2) - (handler.player.x + handler.player.width / 2) > 0;
	}
	
	public boolean playerAbove() {
		return (y + height / 2) - (handler.player.y + handler.player.height / 2) > 0;
	}
	public boolean awareOfPlayer() {
		return (handler.player.x == Game.clamp(handler.player.x, x - aggroRange, x + width + aggroRange)
			 && handler.player.y == Game.clamp(handler.player.y, y - aggroRange, y + height + aggroRange));
	}
	

}
