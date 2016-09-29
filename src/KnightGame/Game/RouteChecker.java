package KnightGame.Game;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class RouteChecker extends JLabel{
	public int x;
	public int y;
	ImageIcon icon = new ImageIcon("game_images/route.png");
	public RouteChecker(int x, int y){
		this.x = x;
		this.y = y;
	}
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		g.drawImage(icon.getImage(), 0, 0, null);
		setOpaque(false);
		super.paintComponent(g);
	}
}
