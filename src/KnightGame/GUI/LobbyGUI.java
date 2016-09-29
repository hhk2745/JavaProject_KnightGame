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
	// Button : ��������, ����������, ���۹�ư, �����Խ���, ��������
	// TextArea : ä��
	// Field : ä�� �Է¶�
	// Label : ��������Ʈ
	//
	MemberDTO dto;

	// Button
	public JButton quickStartBtn;// ��������
	public JButton myPageBtn;// ����������
	public JButton sendBtn;// ä������
	//public JButton notifyBtn;// �����Խ���
	public JButton logoutBtn;// new
	public JButton exitBtn;// ��������

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
		// ImgIcon ����
		icon = new ImageIcon("panel_images/main.PNG");

		double winRate = Double// new
				.parseDouble(String.format("%.2f", (100 * dto.getWin()) / (double) (dto.getWin() + dto.getLose())));

		// ImgPanel ����
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

		// TextArea ����
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		  JScrollPane scrollPane = new JScrollPane(chatArea);
		  chatArea.setLineWrap(true);
		// Field ����
		chat = new JTextField();

		// ��ư ����
		quickStartBtn = new JButton("������");
		myPageBtn = new JButton("����������");
		sendBtn = new JButton("Enter");
		sendBtn.setEnabled(false);
		//notifyBtn = new JButton("�����Խ���");
		logoutBtn = new JButton("�α׾ƿ�");// new
		exitBtn = new JButton("��������");

		// UserInfo Panel ����
		//userInfoPanel = new JPanel();

		id = new JLabel("���̵� : " + dto.getId());
		win = new JLabel("�� : " + dto.getWin());
		lose = new JLabel("�� : " + dto.getLose());
		per = new JLabel("�·� : " + winRate + "%");

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
		// Label ����
		userListLabel = new JLabel();

		// Panel ����
		//userInfoPanel = new JPanel();

		// ��ġ
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
		index.add("������ ��Ȳ");
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
