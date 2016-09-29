package KnightGame.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import KnightGame.MemberDTO;

public class PlayerInfo extends JPanel{
	public JLabel id;

	public JLabel profile;
	public ImageIcon profileIcon = new ImageIcon("panel_images/profile.png");
	public PlayerInfo(JPanel playerPanel, MemberDTO player){
		setBounds(playerPanel.getBounds());
		setLayout(new BorderLayout());
		setBackground(Color.white);
		id = new JLabel("¾ÆÀÌµð : " + player.getId());
		
		profile = new JLabel(){
			protected void paintComponent(Graphics g) {
			// TODO Auto-generated method stub
			g.drawImage(profileIcon.getImage(), 0, 0, null);
			//setOpaque(false);
			super.paintComponent(g);
		}};
		add(new JLabel(), BorderLayout.WEST);
		add(new JLabel(), BorderLayout.NORTH);
		add(new JLabel(), BorderLayout.EAST);
		add(profile, BorderLayout.CENTER);
		add(id, BorderLayout.SOUTH);
		
		setVisible(true);
		
	}

}
