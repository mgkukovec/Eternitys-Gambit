
public class Camera {

	private int x, y;
	private final int facingBias = 100;
	
	public Camera(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void tick(Sprite player) {
		
		int endLocationX = -player.getxPrev() + Game.WIDTH / 2 - (player.width / 2) + ((player.facingRight) ? -facingBias : facingBias);
		int endLocationY = -player.getyPrev() + Game.HEIGHT / 2;
		
		if (x == endLocationX && y == endLocationY) {
			return;
		}
		
		// High constant = slow camera adjustment
		x += Math.ceil(Math.signum(endLocationX - x) * Math.abs(endLocationX - x) / (8)); // Left and Right
		y += Math.ceil(Math.signum(endLocationY - y) * Math.abs(endLocationY - y) / ((endLocationY > y) ? 12 : 2)); // Jumping, Falling
		
		// Lowest object at y=500
		y = Game.clamp(y, Game.HEIGHT - (500 + 200), 100000); // 500 is the floor
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
