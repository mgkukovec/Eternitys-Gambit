import java.awt.Graphics;
import java.util.LinkedList;

public class Handler {

	// Player is always first
	LinkedList<Sprite> loadedSprites = new LinkedList<>();
	LinkedList<Object> loadedObjects = new LinkedList<>();

	// Update every Sprite and Object
	public void tick() {
		for (Object tempObject : loadedObjects) {
			tempObject.tick();
		}
		// Potential for error while removing an Entity during loop
		for (Sprite tempSprite : loadedSprites) {
			tempSprite.tick();
		}
	}

	// Render every Entity and Object to the screen
	public void render(Graphics g) {
		for (Object tempObject : loadedObjects) {
			tempObject.render(g);
		}
		for (Sprite tempSprite : loadedSprites) {
			tempSprite.render(g);
		}
	}

	public void addSprite(Sprite sprite) {
		if (sprite instanceof Player) {
			this.loadedSprites.addFirst(sprite);
		} else {
			this.loadedSprites.add(sprite);
		}
	}

	public void removeSprite(Sprite sprite) {
		this.loadedSprites.remove(sprite);
	}
	
	public void addObject(Object object) {
		this.loadedObjects.add(object);
	}

	public void removeObject(Object object) {
		this.loadedObjects.remove(object);
	}

}