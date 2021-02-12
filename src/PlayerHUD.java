import java.awt.Color;
import java.awt.Graphics;

public class PlayerHUD {

	public static int playerHealth = 100;
	
	public void tick() {
		// Update everything on the HUD?
		// damage might do this already, may not even need a tick method
	}
	
	public void render(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(16, 16, 120, 12);
	}
	
}
