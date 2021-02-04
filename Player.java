import java.awt.Color;
import java.awt.Graphics;

public class Player extends Entity{

	public Player(int x, int y, EntityID id) {
		super(x, y, id);
	}

	public void tick() {
		x += xVelocity; // Replace using acceleration for running
		y += yVelocity; // Replace with gravity equation for jumping
	}

	public void render(Graphics g) {
		g.setColor(Color.blue);
		g.fillRect(x, y, 32, 32);
	}
	
	
}
