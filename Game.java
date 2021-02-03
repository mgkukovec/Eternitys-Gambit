import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = -1442798787354930462L;
	
	public static final int WIDTH = 640, HEIGHT = WIDTH / 16 * 9;
	private Thread thread;
	private boolean running = false;
	
	public Game() {
		new Window(WIDTH, HEIGHT, "GAME TITLE", this);
	}

	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	// Game loop
	public void run() {
		long lastTime = System.nanoTime();
		double ticksPerSecond = 60.0;
		double nsPerTick = 1000000000 / ticksPerSecond;
		double delta = 0;	// units "tick"
		long timer = System.currentTimeMillis();
		int frames = 0;
		
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick; // One tick has passed
			lastTime = now;
			while (delta >= 1) {
				tick();
				delta--;
			}
			if(running) {
				render();
			}
			frames++;
			
			// Prints FPS once per second
			// if statement checks if current time is 1 sec larger than timer
			// if yes, update timer to 1 sec later
			// frames is displayed and reset once per second, so displays FPS
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();
	}
	
	private void tick() {
		
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.black);
		g.fillRect(0,  0, WIDTH, HEIGHT);
		
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) {
		new Game();
	}
}
