package KnightGame.Game;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BoardPanel extends JPanel implements KnightGameInfo{
	ImageIcon icon = new ImageIcon("game_images/boardImg2.jpg");
	public BoardPanel() {
		setLayout(null);
		setBounds(10, 10, ver * distance, hor * distance);
		setVisible(true);
	}
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub

		g.drawImage(icon.getImage(), 0, 0, null);
		setOpaque(false);
		super.paintComponent(g);
	}
}
