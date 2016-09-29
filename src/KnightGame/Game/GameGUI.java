package KnightGame.Game;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GameGUI extends JFrame implements KnightGameInfo {

	public JPanel gameBoard;//게임판
	public JButton setObstacleBtn;
	public JButton lose;//항복 버튼
	public JButton endTurn;//턴종료 버튼
	public JPanel playerInfo;//내정보
	public JPanel comInfo;//상대정보
	public JTextArea chatArea;
	public JTextField chat;
	public RouteChecker[][] checker = new RouteChecker[hor][ver];
	
	public JLabel bg;
	public ImageIcon bgIcon = new ImageIcon("panel_images/game.png");
	
	public GameGUI(){
		setLayout(null);
		setBounds(10, 10, 1000, 700);
		
		
		bg = new JLabel(){
			protected void paintComponent(Graphics g) {
			// TODO Auto-generated method stub
			g.drawImage(bgIcon.getImage(), 0, 0, null);
			setOpaque(false);
			super.paintComponent(g);
		}};
		bg.setBounds(0,0,1000,700);
		add(bg);
		
		
		
		/////////////
		for(int i=0; i<hor; i++){
			for(int j=0; j<ver; j++){
				checker[i][j] = new RouteChecker(i,j);
				checker[i][j].setBounds(10 + j*distance, 10 + i*distance, distance, distance);
				
				add(checker[i][j]);
			}
		}
		allVisibleFalse();
		////////
		add(gameBoard = new BoardPanel());
		
		setObstacleBtn = new JButton("장애물 설치");
		setObstacleBtn.setBounds(600, 220, 110, 180);
		add(setObstacleBtn);
		
		endTurn = new JButton("턴종료");
		endTurn.setBounds(730, 220, 110, 180);
		add(endTurn);
		
		lose = new JButton("항복");
		lose.setBounds(860, 220, 110, 180);
		add(lose);
		
		
		playerInfo = new JPanel();
		playerInfo.setBounds(600,10,170,170);
		playerInfo.setBackground(Color.black);
		add(playerInfo);
		
		comInfo = new JPanel();
		comInfo.setBounds(800,10,170,170);
		comInfo.setBackground(Color.black);
		add(comInfo);
		
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		  JScrollPane scrollPane = new JScrollPane(chatArea);
		  chatArea.setLineWrap(true);
		  scrollPane.setBounds(600,430,370,170);
		add(scrollPane);
		
		chat = new JTextField();
		chat.setBounds(600, 610, 370, 30);
		add(chat);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void allVisibleFalse(){
		for(int i=0; i<checker.length; i++){
			for(int j=0; j<checker[i].length; j++){
				checker[i][j].setVisible(false);
			}
		}
	}
	
	
	public static void main(String[] args){
		new GameGUI();
	}
}
