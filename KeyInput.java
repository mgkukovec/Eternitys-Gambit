import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

	private Handler handler;

	public KeyInput(Handler handler) {
		this.handler = handler;
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		System.out.println(key);
		
		// Key events for player's character (if loaded)
		// Checks if player's character is loaded (always first in loadedEntities)
		if (handler.loadedSprites.get(0).getId() == SpriteID.Player) {
			Sprite tempPlayer = handler.loadedSprites.get(0);
			if (key == KeyEvent.VK_W)
				tempPlayer.setyVelocity(-4);
			if (key == KeyEvent.VK_S)
				tempPlayer.setyVelocity(4);
			if (key == KeyEvent.VK_A)
				tempPlayer.setxVelocity(-4);
			if (key == KeyEvent.VK_D)
				tempPlayer.setxVelocity(4);
		}

	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		System.out.println(key);
		
		// Key events for player's character (if loaded)
		// Checks if player's character is loaded (always first in loadedEntities)
		if (handler.loadedSprites.get(0).getId() == SpriteID.Player) {
			Sprite tempPlayer = handler.loadedSprites.get(0);
			if (key == KeyEvent.VK_W)
				tempPlayer.setyVelocity(0);
			if (key == KeyEvent.VK_S)
				tempPlayer.setyVelocity(0);
			if (key == KeyEvent.VK_A)
				tempPlayer.setxVelocity(0);
			if (key == KeyEvent.VK_D)
				tempPlayer.setxVelocity(0);
		}
	}
}
