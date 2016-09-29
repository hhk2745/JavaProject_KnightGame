package KnightGame.GUI; // new_End

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import KnightGame.MemberDTO;

public class LobbyGUI extends GUIForm
{
	// Button : 빠른입장, 마이페이지, 전송버튼, 공지게시판, 게임종료
	// TextArea : 채팅
	// Field : 채팅 입력란
	// Label : 유저리스트
	//
	MemberDTO dto;

	// Button
	public JButton quickStartBtn;// 빠른입장
	public JButton myPageBtn;// 마이페이지
	public JButton sendBtn;// 채팅전송
	//public JButton notifyBtn;// 공지게시판
	public JButton logoutBtn;// new
	public JButton exitBtn;// 게임종료

	// UserChart
	Vector<MemberDTO> userChart = new Vector<>();

	// Label
	public JLabel userListLabel;

	// InfoLabel
	JLabel id;
	JLabel win;
	JLabel lose;
	JLabel per;

	// Panel
	//public JPanel userInfoPanel;
	JPanel mainPanel;

	// Img
	ImageIcon icon;
	public JPanel imgPanel;

	// JTable
	JTable tt;
	public JScrollPane ttjs;

	// public
	public boolean isRoom = false;

	public LobbyGUI(MemberDTO dto)
	{
		this.dto = dto;
		setTitle("Knight Game");

		
		// super.setLayout(null);
		// ImgIcon 생성
		icon = new ImageIcon("panel_images/main.PNG");

		double winRate = Double// new
				.parseDouble(String.format("%.2f", (100 * dto.getWin()) / (double) (dto.getWin() + dto.getLose())));

		// ImgPanel 생성
		imgPanel = new JPanel()
		{
			@Override
			protected void paintComponent(Graphics g)
			{
				g.drawImage(icon.getImage(), 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		imgPanel.setLayout(null);

		// TextArea 생성
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		  JScrollPane scrollPane = new JScrollPane(chatArea);
		  chatArea.setLineWrap(true);
		// Field 생성
		chat = new JTextField();

		// 버튼 생성
		quickStartBtn = new JButton("방입장");
		myPageBtn = new JButton("마이페이지");
		sendBtn = new JButton("Enter");
		sendBtn.setEnabled(false);
		//notifyBtn = new JButton("공지게시판");
		logoutBtn = new JButton("로그아웃");// new
		exitBtn = new JButton("게임종료");

		// UserInfo Panel 생성
		//userInfoPanel = new JPanel();

		id = new JLabel("아이디 : " + dto.getId());
		win = new JLabel("승 : " + dto.getWin());
		lose = new JLabel("패 : " + dto.getLose());
		per = new JLabel("승률 : " + winRate + "%");

		//id.setBounds(20, 20, 100, 30);//
		//win.setBounds(20, 50, 100, 30);
		//lose.setBounds(20, 80, 100, 30);
		//per.setBounds(20, 110, 100, 30);

		id.setForeground(Color.BLACK);
		win.setForeground(Color.BLACK);
		lose.setForeground(Color.BLACK);
		per.setForeground(Color.BLACK);

		/*
		userInfoPanel.add(id);
		userInfoPanel.add(win);
		userInfoPanel.add(lose);
		userInfoPanel.add(per);
*/
		// Label 생성
		userListLabel = new JLabel();

		// Panel 생성
		//userInfoPanel = new JPanel();

		// 위치
		quickStartBtn.setBounds(70, 122, 330, 85);
		myPageBtn.setBounds(450, 122, 330, 85);
		logoutBtn.setBounds(70, 230, 330, 85);
		exitBtn.setBounds(450, 230, 330, 85);
		//notifyBtn.setBounds(329, 230, 215, 85);

		scrollPane.setBounds(42, 338, 530, 212);
		chat.setBounds(41, 573, 432, 34);
		sendBtn.setBounds(477, 573, 97, 34);

		//userInfoPanel.setBounds(737, 105, 268, 213);
		//userInfoPanel.setLayout(new GridLayout(4, 1));

		setUserChart(userChart);

		imgPanel.add(ttjs);
		imgPanel.add(quickStartBtn);
		imgPanel.add(myPageBtn);
		imgPanel.add(sendBtn);
		//imgPanel.add(notifyBtn);
		imgPanel.add(logoutBtn);
		imgPanel.add(exitBtn);

		imgPanel.add(scrollPane);
		imgPanel.add(chat);
		//imgPanel.add(userInfoPanel);

		// add(Panel)
		super.add(imgPanel);

		// actionListener
		//notifyBtn.addActionListener(new Notify());
		//myPageBtn.addActionListener(new MyPage(this));

		setBounds(20, 20, 850, 669);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setResizable(false);
	}

	/*
	class MyPage implements ActionListener
	{
		JFrame lobbyFrame;

		public MyPage(JFrame lobbyFrame)
		{
			this.lobbyFrame = lobbyFrame;
			// TODO Auto-generated constructor stub
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub

			//new MyPageGUI(dto, lobbyFrame);
			///
		}
	}*/
/*
	class Notify implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
			new NotifyGUI();

		}
	}*/

	public void setUserChart(Vector<MemberDTO> userChart)
	{
		Vector index = new Vector<>();
		index.add("접속자 현황");
		Vector<Vector> data = new Vector<>();
		for (int i = 0; i < userChart.size(); i++)
		{
			data.add(new Vector());
			data.get(i).add(userChart.get(i).getId());
		}
		tt = new JTable(data, index);
		tt.setEnabled(false);
		ttjs = new JScrollPane(tt);

	}
	public static void main(String[] args){
		MemberDTO test = new MemberDTO();
		test.setId("aa");
		test.setWin(72);
		test.setLose(34);
		new LobbyGUI(test);
	}
}
