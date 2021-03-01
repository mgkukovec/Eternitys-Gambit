import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class PlayerHUD {

	public static boolean debugMode = false;
	public static boolean debugKeyHeld = false;

	public void tick() {
		debugKeyHeld = KeyInput.isPressed(KeyEvent.VK_F3);
	}

	public void render(Graphics g, Sprite player) {
		if (debugMode) {
			g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			g.setColor(Color.white);
			g.drawLine(Game.WIDTH / 2, 0, Game.WIDTH / 2, Game.HEIGHT);
			g.drawLine(0, Game.HEIGHT / 2, Game.WIDTH, Game.HEIGHT / 2);
			g.drawString("(" + player.x + ", " + player.y + ")", 7, 20);
			g.drawString("Health: " + player.health, 7, 40);
			g.drawString("Falling: " + player.falling, 7, 60);
			g.drawString("Velocity: (" + player.xVel + ", " + player.yVel + ")", 7, 80);
			g.drawString("FPS: " + Game.FPS + "/" + Game.FPSmax, 7, 100);
			g.drawString("TPS: " + Game.TPS + "/" + Game.TPSmax, 7, 120);
			g.drawString("Standing on: " + player.standingOn, 7, 140);
			g.drawString("Side collision: " + player.sideCollision, 7, 160);
		}
	}

	/**
	 * Debug key must be pressed and released to toggle.
	 */
	public static void toggleDebug() {
		if (!debugKeyHeld) {
			debugMode = !debugMode;
		}
	}

}
