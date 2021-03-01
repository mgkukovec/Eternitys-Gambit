import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

	private static boolean[] pressed;

	public KeyInput() {
		pressed = new boolean[256];
	}
	
	/**
	 * Checks if a key is currently pressed.
	 * 
	 * @param keyCode	the ASCII value of a key, use KeyEvent.VK_<key>
	 * @return			if the key is pressed
	 */
	public static boolean isPressed(int keyCode) {
		return (keyCode < 256) ? pressed[keyCode] : false;
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() < 256)
			pressed[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() < 256)
			pressed[e.getKeyCode()] = false;
	}
}
