import java.awt.Graphics;
import java.util.LinkedList;

public class Handler {

	// Player is always first
	LinkedList<Sprite> loadedSprites = new LinkedList<Sprite>();

	// Update every Sprite
	public void tick() {
		// Potential for error while removing an Entity during loop
		for (Sprite tempSprite : loadedSprites) {
			tempSprite.tick();
		}
	}

	// Render every Entity to the screen
	public void render(Graphics g) {
		for (Sprite tempSprite : loadedSprites) {
			tempSprite.render(g);
		}
	}

	public void addEntity(Sprite sprite) {
		if (sprite instanceof Player) {
			this.loadedSprites.addFirst(sprite);
		} else {
			this.loadedSprites.add(sprite);
		}
	}

	public void removeEntity(Sprite sprite) {
		this.loadedSprites.remove(sprite);
	}

}
