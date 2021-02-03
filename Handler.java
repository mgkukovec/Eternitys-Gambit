import java.awt.Graphics;
import java.util.LinkedList;

public class Handler {
	
	LinkedList<Entity> entity = new LinkedList<Entity>();
	
	// Update every Entity
	public void tick() {
		// Potential for error while removing an Entity during loop
		for(Entity tempEntity : entity) {
			tempEntity.tick();
		}
	}
	
	// Render every Entity to the screen
	public void render(Graphics g) {
		for(Entity tempEntity : entity) {
			tempEntity.render(g);
		}
	}
	
	public void addEntity(Entity entity) {
		this.entity.add(entity);
	}
	
	public void removeEntity(Entity entity) {
		this.entity.remove(entity);
	}
	
}
