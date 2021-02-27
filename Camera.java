
public class Camera {

	private int x, y;
	private final int facingBias = 100;
	
	public Camera(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void tick(Sprite player) {
		
		int endLocationX = -player.getX() + Game.WIDTH / 2 - (player.width / 2) + ((player.facingRight) ? -facingBias : facingBias);
		int endLocationY = -player.getY() + Game.HEIGHT / 2;
		
		if (x == endLocationX && y == endLocationY) {
			return;
		}
		
		x += (double) Math.signum(endLocationX - x) * Math.abs(endLocationX - x) / (8); // Higher constant = slower camera adjustment
		
		if (endLocationY > y) {
			y += (double) Math.signum(endLocationY - y) * Math.abs(endLocationY - y) / (6);
		} else {
			y += (double) Math.signum(endLocationY - y) * Math.abs(endLocationY - y) / (2); // When falling, adjust camera faster
		}
		
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
