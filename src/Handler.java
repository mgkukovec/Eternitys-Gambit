import java.awt.Graphics;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class Handler {

	Player player;
	CopyOnWriteArrayList<Sprite> loadedSprites = new CopyOnWriteArrayList<>();
	CopyOnWriteArrayList<Object> loadedObjects = new CopyOnWriteArrayList<>();

	// Update every Sprite and Object
	// checkForComodification potential error, list can't be modified during this loop
	public void tick() {
		if (player != null) {
			player.tick();
		}
		Iterator<Object> objectIterator = loadedObjects.iterator();
		while (objectIterator.hasNext()) {
			objectIterator.next().tick();
		}
		
		Iterator<Sprite> spriteIterator = loadedSprites.iterator();
		while (spriteIterator.hasNext()) {
			spriteIterator.next().tick();
		}
	}

	// Render every Entity and Object to the screen
	// Player is the first Sprite rendered
	// TODO: Concurrent Modification
	public void render(Graphics g) {
		Iterator<Object> objectIterator = loadedObjects.iterator();
		while (objectIterator.hasNext()) {
			objectIterator.next().render(g);
		}
		
		Iterator<Sprite> spriteIterator = loadedSprites.iterator();
		while (spriteIterator.hasNext()) {
			spriteIterator.next().render(g);
		}
		if (player != null) {
			player.render(g);
		}
	}
	
	public void addPlayer(Player p) {
		player = p;
	}

	public void addSprite(Sprite sprite) {
		this.loadedSprites.add(sprite);
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
