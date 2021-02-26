
public class Camera {

	private int x, y;
	
	public Camera(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void tick(Sprite player) {
		//x = -player.getX() + Game.WIDTH / 2 - (player.width / 2);
		//y = -player.getY() + Game.HEIGHT / 2 - (player.width / 2);
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
