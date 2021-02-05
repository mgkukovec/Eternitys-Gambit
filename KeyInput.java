import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

	private static boolean[] pressed;

	public KeyInput() {
		pressed = new boolean[256];
	}
	
	public static boolean isPressed(int keyCode) {
		return pressed[keyCode];
	}

	public void keyPressed(KeyEvent e) {
		pressed[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
		pressed[e.getKeyCode()] = false;
	}
}
