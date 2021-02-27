import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Game extends Canvas implements Runnable {
	public static int WIDTH = 1280;
	public static int HEIGHT = WIDTH * 9 / 16;
	public static int TPSmax = 30;
	public static int FPSmax = 60;
	
	public static int FPS = 0;
	public static int TPS = 0;
	public static int currentTick = 0;
	
	private static final long serialVersionUID = -1442798787354930462L;
	private Thread thread;
	private boolean running = false;
	private Handler handler;
	private PlayerHUD hud;
	public BufferedImage playerSS, platformSS;
	private Camera camera;
	private Window window;

	public Game() {
		handler = new Handler();
		hud = new PlayerHUD();
		camera = new Camera(0, 0);
		
		this.addKeyListener(new KeyInput());
		window = new Window(WIDTH, HEIGHT, "Miss Adventure", this);
		
		BufferedImageLoader imageLoader = new BufferedImageLoader();
		playerSS = imageLoader.loadImage("/spriteSheet.png");
		platformSS = imageLoader.loadImage("/Rocks.png");
		
		// This might belong in it's own class
		// Does each room need it's own class?
		handler.addSprite(new Player(150, 100, 60, 90, SpriteID.Player, handler, playerSS));
		handler.addObject(new BasicPlatform(50, 500, 480, 270, ObjectID.BasicPlatform, handler, platformSS));
		handler.addObject(new BasicPlatform(50 + 480, 500, 480, 270, ObjectID.BasicPlatform, handler, platformSS));
		handler.addObject(new BasicPlatform(50 + (480 * 2), 500, 480, 270, ObjectID.BasicPlatform, handler, platformSS));
		handler.addObject(new TestPlatform(50 + 480, 230, 480, 30, ObjectID.BasicPlatform, handler));
		handler.addObject(new TestPlatform(1300, 0, 20, 500, ObjectID.BasicPlatform, handler));
		
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
		this.requestFocus();
		
		FPS = 0;
		long nsCycleStart;
		long nsPrevCycleStart = System.nanoTime();
		double nsPerTick = 1000000000.0 / TPSmax;
		double delta = 0; // units "tick"
		long timer = System.currentTimeMillis();
		int runningFPS = 0;

		while (running) {
			nsCycleStart = System.nanoTime();
			delta += (nsCycleStart - nsPrevCycleStart) / nsPerTick; // One tick has passed
			nsPrevCycleStart = nsCycleStart;
			while (delta >= 1) {
				tick();
				currentTick++;
				delta--;
			}
			if (running) {
				render();
			}
			runningFPS++;

			// Updates FPS and TPS once per second
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				FPS = runningFPS;
				TPS = currentTick;
				runningFPS = 0;
				currentTick = 0;
			}
			
			WIDTH = window.getWindowWidth();
			HEIGHT = window.getWindowHeight();
			
			// FPS cap, for 60 FPS sleeps (1/60 of a second - time it took to tick and render)
			try {
				Thread.sleep((1000 / FPSmax) - ((System.nanoTime() - nsCycleStart) / 1000000));
			} catch (InterruptedException e) {
				System.out.println("Thread was interrupted.");
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
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		// Anything between these two statements will be affected by the camera
		g2d.translate(camera.getX(), camera.getY());

		// Render all Sprites and Objects
		handler.render(g);
		
		g2d.translate(-camera.getX(), -camera.getY());
		
		if(handler.loadedSprites.isEmpty() == false) {
			hud.render(g, handler.loadedSprites.get(0));
		}
		
		
		g.dispose();
		bs.show();
	}
	
	public static int clamp(int value, int min, int max) {
		return Math.max(min, (Math.min(max, value)));
	}
	
	public static void main(String[] args) {
		new Game();
	}
}
