import java.awt.Color;
import java.awt.Graphics;

public class PlayerHUD {

	public static int playerHealth = 100;
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(16, 16, 120, 12);
	}
	
}
