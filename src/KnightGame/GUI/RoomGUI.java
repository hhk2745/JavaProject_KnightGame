package KnightGame.GUI;//new_End

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import KnightGame.MemberDTO;

public class RoomGUI extends GUIForm
{

	public JScrollPane js;
	public JTextArea chatArea = new JTextArea();
	public JTextField chat = new JTextField();
	public JButton send; // 전송버튼.
	public String msg;
	public JButton ready; // 레디 버튼.
	public JButton exit; // 나가기 버튼
	public JPanel p1;
	public JPanel p2;
	public MemberDTO dto;
	
	public JLabel bg;
	public ImageIcon bgIcon = new ImageIcon("panel_images/room.png");
	

	public RoomGUI(MemberDTO dto)
	{
		// TODO Auto-generated constructor stub
		this.dto = dto;
		super.setLayout(null);
		setBounds(20, 20, 850, 669);
		
		bg = new JLabel(){
			protected void paintComponent(Graphics g) {
			// TODO Auto-generated method stub
			g.drawImage(bgIcon.getImage(), 0, 0, null);
			setOpaque(false);
			super.paintComponent(g);
		}};
		bg.setBounds(0,0,850,669);
		add(bg);
		
		
		
		chatArea.setEditable(false);
		chatArea.setLineWrap(true);
		js = new JScrollPane(chatArea);
		js.setBounds(42, 338, 530, 212);
		add(js);
		chat.setBounds(41, 573, 432, 34);
		add(chat);
		
		

		

		p1 = new JPanel();
		p2 = new JPanel();

		p1.setLayout(new BorderLayout());
		p2.setLayout(new BorderLayout());
		p1.setBounds(50, 50, 250, 230);
		p1.setBackground(Color.black);
		p2.setBounds(550, 50, 250, 230);
		p2.setBackground(Color.black);

		add(p2);
		add(p1);
		send = new JButton("Enter");
		send.setEnabled(false);
		send.setBounds(477, 573, 97, 34);
		add(send);
		ready = new JButton("준비하기");
		ready.setEnabled(false);
		ready.setBounds(600, 340, 200, 120);
		add(ready);
		exit = new JButton("나가기");
		exit.setBounds(600, 480, 200, 120);
		add(exit);

		setVisible(true);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}
	
	public static void main(String[] args){
		new RoomGUI(new MemberDTO());
	}

}
