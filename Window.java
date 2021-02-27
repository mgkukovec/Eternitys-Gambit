import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class Window extends Canvas {

	private static final long serialVersionUID = -8255319694373975038L;
	private JFrame frame;
	
	public Window(int width, int height, String title, Game game) {
		frame = new JFrame(title);
		
		frame.setPreferredSize(new Dimension(width, height));
		frame.setMaximumSize(new Dimension(width, height));
		frame.setMinimumSize(new Dimension(width, height));
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.add(game);
		frame.setVisible(true);
		game.start();
	}
	
	public int getWindowWidth() {
		return frame.getSize().width;
	}
	
	public int getWindowHeight() {
		return frame.getSize().height;
	}
	
	/* FOR FULLSCREEN
	public int getScreenWidth() {
		return (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth());
	}
	
	public int getScreenHeight() {
		return (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight());
	}
	
	public JFrame getFrame() {
		return frame;
	}
	*/

}
