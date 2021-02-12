import java.awt.image.BufferedImage;

public class SpriteSheet {

	private BufferedImage sprite;
	
	public SpriteSheet(BufferedImage ss) {
		this.sprite = ss;
	}
	
	public BufferedImage grabImage(int col, int row, int width, int height, int colSize, int rowSize) {
		return sprite.getSubimage((row * rowSize) - rowSize, (col * colSize) - colSize, width, height);
	}
}
