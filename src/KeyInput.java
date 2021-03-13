import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

	private static boolean[] pressed;
	private static boolean[] pressedLast;

	public KeyInput() {
		pressed = new boolean[256];
		pressedLast = new boolean[256];
		
		for(int i = 0; i < pressed.length; i++) {
			pressed[i] = false;
			pressedLast[i] = false;
		}
	}

	public static void updatePlayer(Player p) {
		// Movement
		if (KeyInput.isPressed(KeyEvent.VK_LEFT)) {
			p.xVel += -p.speed;
			p.facingRight = false;
		}
		if (KeyInput.isPressed(KeyEvent.VK_RIGHT)) {
			p.xVel += p.speed;
			p.facingRight = true;
		}
		// Queues a jump if jump key is pressed, does not count holding jump key
		if (KeyInput.isPressed(KeyEvent.VK_SPACE) && !wasPressedLast(KeyEvent.VK_SPACE)) {
			p.tickBufferStart = Game.currentTick;
		}
		
		// Abilities
		if (KeyInput.isPressed(KeyEvent.VK_D) && !wasPressedLast(KeyEvent.VK_D)) {
			if (p.ableToAttack()) {
				p.tickOfLastAttack = Game.currentTick;
			}
		}
		
		// Debug
		if (KeyInput.isPressed(KeyEvent.VK_F3)) {
			PlayerHUD.toggleDebug();
		}
		
		for(int i = 0; i < pressed.length; i++) {
			pressedLast[i] = pressed[i];
		}
	}

	public static boolean isPressed(int keyCode) {
		return (keyCode < 256) ? pressed[keyCode] : false;
	}
	
	public static boolean wasPressedLast(int keyCode) {
		return (keyCode < 256) ? pressedLast[keyCode] : false;
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() < 256)
			pressed[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() < 256)
			pressed[e.getKeyCode()] = false;
	}
}
