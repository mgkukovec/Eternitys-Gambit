import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class PlayerHUD {

	public static boolean debugMode = false;
	public static boolean debugToggle = false;
	
	public void tick() {
		// Update everything on the HUD?
		// damage might do this already, may not even need a tick method
	}
	
	public void render(Graphics g, Sprite player) {
		//g.setColor(Color.gray);
		//g.fillRect(16, 16, 120, 12);
		
		if (debugMode == true) {
			g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			g.setColor(Color.white);
			g.drawString("(" + player.x + ", " + player.y + ")", 7, 20);
			g.drawString("Health:   " + player.health, 7, 40);
			g.drawString("Falling:  " + player.falling, 7, 60);
			g.drawString("Velocity: (" + player.xVelocity + ", " + player.yVelocity + ")", 7, 80);
			
		}
	}
	
}
