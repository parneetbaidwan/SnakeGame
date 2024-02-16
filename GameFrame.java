import javax.swing.JFrame;

public class GameFrame extends JFrame{
	
	GameFrame(){
		
		//GamePanel panel  = new GamePanel();
		//shortcut
		this.add(new GamePanel());
		this.setTitle("Snake");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setAutoRequestFocus(false);
		this.pack(); // takes JFrame and fits it around all components
		this.setVisible(true);
		this.setLocationRelativeTo(null); // center in middle of window
	}
}
