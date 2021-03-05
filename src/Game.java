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
	public static int FPSmax = 30;
	
	public static int FPS = 0;
	public static int TPS = 0;
	public static int currentTick = 0;
	
	public static final int gravity = 3;
	
	private static final long serialVersionUID = -1442798787354930462L;
	private Thread thread;
	private boolean running = false;
	private Handler handler;
	private PlayerHUD hud;
	public BufferedImage playerSS, platformSS;
	public BufferedImage slimeSS, coinSS;
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
		slimeSS = imageLoader.loadImage("/SlimeSheet.png");
		coinSS = imageLoader.loadImage("/coins.png");
		
		handler.addSprite(new Player(200, 100, SpriteID.Player, handler, playerSS));
		handler.addSprite(new Slime(600, 100, SpriteID.Slime, handler, slimeSS));
		handler.addSprite(new Coin(100, 100, SpriteID.Coin, handler, coinSS, 50));
		handler.addSprite(new Coin(110, 100, SpriteID.Coin, handler, coinSS, 50));
		handler.addSprite(new Coin(133, 100, SpriteID.Coin, handler, coinSS, 50));
		handler.addObject(new BasicPlatform(50, 500, 480, 270, ObjectID.BasicPlatform, platformSS));
		handler.addObject(new BasicPlatform(50 + 480, 500, 480, 270, ObjectID.BasicPlatform, platformSS));
		handler.addObject(new BasicPlatform(50 + (480 * 2), 500, 480, 270, ObjectID.BasicPlatform, platformSS));
		handler.addObject(new TestPlatform(50 + 480, 145, 480, 30, ObjectID.BasicPlatform));
		handler.addObject(new TestPlatform(1100, 200, 20, 300, ObjectID.BasicPlatform));
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
		int runningTPS = 0;

		while (running) {
			nsCycleStart = System.nanoTime();
			delta += (nsCycleStart - nsPrevCycleStart) / nsPerTick; // One tick has passed
			nsPrevCycleStart = nsCycleStart;
			while (delta >= 1) {
				tick();
				currentTick++;
				runningTPS++;
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
				TPS = runningTPS;
				runningFPS = 0;
				runningTPS = 0;
			}
			
			WIDTH = window.getWindowWidth();
			HEIGHT = window.getWindowHeight();
			
			// FPS cap
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
		System.gc();
		hud.tick();
		
		// Player is last in list
		if(handler.playerLoaded())
			camera.tick(handler.getPlayer());
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
		
		// Affected by camera
		g2d.translate(camera.getX(), camera.getY());

		// Render all Sprites and Objects
		handler.render(g);
		System.gc();
		
		g2d.translate(-camera.getX(), -camera.getY());
		// End affected by camera
		
		// Player is last in list
		if(handler.playerLoaded())
			hud.render(g, handler.getPlayer());
		
		g.dispose();
		bs.show();
	}
	
	/**
	 * Bounds a value between an Integer range.
	 * 
	 * @param n		the value to check
	 * @param min	the minimum n can be
	 * @param max	the maximum n can be
	 * @return		the bounded value of n
	 */
	public static int clamp(int n, int min, int max) {
		return Math.max(min, (Math.min(max, n)));
	}
	
	public static int floatingYPosition(int idleCycles, int cycleOffset, int heightOffGround) {
		return (int) (heightOffGround * (0.5 * Math.cos(2 * ((Game.currentTick + cycleOffset % idleCycles)) * Math.PI / idleCycles) + 0.5)) - heightOffGround;
	}
	
	public static void main(String[] args) {
		new Game();
	}
}
