import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Game extends Canvas implements Runnable {
	public static final int WIDTH = 640, HEIGHT = WIDTH / 16 * 9;
	public static int FPS;
	public static int TPS = 30;
	public static int FPSmax = 60;
	
	private static final long serialVersionUID = -1442798787354930462L;
	private Thread thread;
	private boolean running = false;
	private Handler handler;
	private PlayerHUD hud;
	public BufferedImage spriteSheet;
	private Camera camera;

	public Game() {
		handler = new Handler();
		hud = new PlayerHUD();
		camera = new Camera(0, 0);
		
		this.addKeyListener(new KeyInput());
		new Window(WIDTH, HEIGHT, "Miss Adventure", this);
		
		BufferedImageLoader imageLoader = new BufferedImageLoader();
		spriteSheet = imageLoader.loadImage("/spriteSheet.png");
		
		// This might belong in it's own class
		// Does each room need it's own class?
		handler.addSprite(new Player(150, 100, 60, 90, SpriteID.Player, handler, spriteSheet));
		handler.addObject(new BasicPlatform(150, 300, 300, 1, ObjectID.BasicPlatform, handler));
		handler.addObject(new BasicPlatform(350, 250, 50, 10, ObjectID.BasicPlatform, handler));
		handler.addObject(new BasicPlatform(150, 220, 50, 10, ObjectID.BasicPlatform, handler));
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Game loop
	public void run() {
		FPS = 0;
		
		long nsCycleStart;
		long nsPrevCycleStart = System.nanoTime();
		double nsPerTick = 1000000000.0 / TPS;
		double delta = 0; // units "tick"
		long timer = System.currentTimeMillis();
		int runningFPS = 0;
		
		this.requestFocus();

		while (running) {
			nsCycleStart = System.nanoTime();
			delta += (nsCycleStart - nsPrevCycleStart) / nsPerTick; // One tick has passed
			nsPrevCycleStart = nsCycleStart;
			while (delta >= 1) {
				tick();
				delta--;
			}
			if (running) {
				render();
			}
			runningFPS++;

			// Updates FPS once per second
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				FPS = runningFPS;
				runningFPS = 0;
			}
			
			// FPS cap
			// Waits (usually) 1/60 of a second - time it took to tick and render
			try {
				Thread.sleep((1000 / FPSmax) - ((System.nanoTime() - nsCycleStart) / 1000000));
			} catch (InterruptedException e) {
				System.out.println("Thread was interrupted during sleep.");
			} catch (IllegalArgumentException e) {
				System.out.println("FPS dropped below max.");
			}
			
		}
		stop();
	}

	private void tick() {
		handler.tick();
		hud.tick();
		
		// Player should be the first object in this list
		for(int i = 0; i < handler.loadedSprites.size(); i++) {
			if(handler.loadedSprites.get(i).getId() == SpriteID.Player) {
				camera.tick(handler.loadedSprites.get(i));
				break;
			}
		}
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2d = (Graphics2D) g;

		// Background
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		// Anything between these two statements will be affected by the camera
		g2d.translate(camera.getX(), camera.getY());

		handler.render(g);
		
		g2d.translate(-camera.getX(), -camera.getY());
		
		if(handler.loadedSprites.isEmpty() == false) {
			hud.render(g, handler.loadedSprites.get(0));
		}
		
		g.dispose();
		bs.show();
	}
	
	public static int clamp(int value, int min, int max) {
		//return Math.max(min, (Math.min(max, value)));
		if(value < min) {
			return min;
		}
		if (value > max) {
			return max;
		}
		return value;
	}
	
	

	public static void main(String[] args) {
		new Game();
	}
}
