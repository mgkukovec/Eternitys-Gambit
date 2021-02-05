import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Player extends Sprite{
	
	public Player(int x, int y, SpriteID id) {
		super(x, y, id);
	}

	public void tick() {
		
		xVelocity = 0;
		yVelocity = 0;
		
		if(KeyInput.isPressed(KeyEvent.VK_W))
			yVelocity += -4;
		if(KeyInput.isPressed(KeyEvent.VK_S))
			yVelocity += 4;
		if(KeyInput.isPressed(KeyEvent.VK_A))
			xVelocity += -4;
		if(KeyInput.isPressed(KeyEvent.VK_D))
			xVelocity += 4;
		
		
			
		
		x += xVelocity; // Replace using acceleration for running
		y += yVelocity; // Replace with gravity equation for jumping
	}

	public void render(Graphics g) {
		g.setColor(Color.blue);
		g.fillRect(x, y, 32, 32);
	}
	
	
}
