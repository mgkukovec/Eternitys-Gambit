import java.awt.Graphics;
import java.util.LinkedList;

public class Handler {

	// Player is always first
	LinkedList<Entity> loadedEntities = new LinkedList<Entity>();

	// Update every Entity
	public void tick() {
		// Potential for error while removing an Entity during loop
		for (Entity tempEntity : loadedEntities) {
			tempEntity.tick();
		}
	}

	// Render every Entity to the screen
	public void render(Graphics g) {
		for (Entity tempEntity : loadedEntities) {
			tempEntity.render(g);
		}
	}

	public void addEntity(Entity entity) {
		if (entity instanceof Player) {
			this.loadedEntities.addFirst(entity);
		} else {
			this.loadedEntities.add(entity);
		}
	}

	public void removeEntity(Entity entity) {
		this.loadedEntities.remove(entity);
	}

}
