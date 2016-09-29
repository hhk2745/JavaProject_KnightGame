package practice;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import KnightGame.Game.KnightGameInfo;

public class GameGUI extends JFrame implements KnightGameInfo {

	public JPanel gameBoard;//∞‘¿”∆«
	public GameGUI(){
		setLayout(null);
		setBounds(10, 10, 1400, 700);
		gameBoard = new BoardPanel();
		
		
		
		for(int i=0; i<hor; i++){
			for (int j = 0; j < ver; j++) {
				ImageIcon icon = new ImageIcon("game_images/Obstacle.png");
				JLabel testLb = new JLabel(){
					protected void paintComponent(Graphics g) {
					// TODO Auto-generated method stub
					g.drawImage(icon.getImage(), 0, 0, null);
					setOpaque(false);
					super.paintComponent(g);
				}};
				testLb.setBounds(j*80 + 10, 10 + i*80, 80, 80);
				
				JButton testBtn = new JButton();
				testBtn.setBounds(hor*distance + j*80, 10 + i*80, 80, 80);
				testBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						testLb.setVisible(!testLb.isVisible());
						repaint();
						validate();
					}
				});
				

				add(testLb);
				add(testBtn);
			}
		}
		
		

		add(gameBoard);		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void main(String[] args){
		new GameGUI();
	}
}
