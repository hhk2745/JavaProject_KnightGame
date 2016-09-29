package KnightGame.GUI;//new_End

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import KnightGame.MemberDAO;
import KnightGame.MemberDTO;

public class MyPageGUI extends JFrame
{
	JFrame lobbyFrame;

	// DAO
	MemberDAO dao;
	MemberDTO dto;

	// Img
	ImageIcon icon;
	JPanel panel;

	// Button
	public JButton adjustBtn, cancelBtn;

	// Panel
	JPanel infoPanel;

	// Label
	JLabel id;
	JLabel win;
	JLabel lose;
	JLabel per;
	public AdjustGUI adjust;

	boolean idChk = false, pwChk = false, emailChk = false;

	public MyPageGUI(MemberDTO dto/*, JFrame lobbyFrame*/)
	{
		//this.lobbyFrame = lobbyFrame;
		setTitle("����������");

		double winRate = Double
				.parseDouble(String.format("%.2f", (100 * dto.getWin()) / (double) (dto.getWin() + dto.getLose())));

		// dao ����
		this.dto = dto;
		dao = new MemberDAO();

		// ��ư ����
		adjustBtn = new JButton("������������");
		cancelBtn = new JButton("������");

		// JPanel ����
		infoPanel = new JPanel();

		// �� ����
		id = new JLabel("���̵� : " + dto.getId());
		win = new JLabel("�� : " + dto.getWin());
		lose = new JLabel("�� : " + dto.getLose());
		per = new JLabel("�·� : " + winRate + "%");

		id.setForeground(Color.BLACK);
		win.setForeground(Color.BLACK);
		lose.setForeground(Color.BLACK);
		per.setForeground(Color.BLACK);

		// ��ư ��ġ
		adjustBtn.setBounds(180, 335, 269, 37);
		cancelBtn.setBounds(339, 417, 269, 56);

		infoPanel.setBounds(180, 104, 269, 213);
		infoPanel.setLayout(new GridLayout(4, 1));
		// img
		icon = new ImageIcon("panel_images/mypage.PNG");
		panel = new JPanel()
		{
			@Override
			protected void paintComponent(Graphics g)
			{
				// TODO Auto-generated method stub
				g.drawImage(icon.getImage(), 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};

		panel.add(adjustBtn);
		panel.add(cancelBtn);
		panel.add(infoPanel);
		panel.setLayout(null);

		infoPanel.add(id);
		infoPanel.add(win);
		infoPanel.add(lose);
		infoPanel.add(per);

		add(panel);

		//adjustBtn.addActionListener(new Adjust(lobbyFrame));
		cancelBtn.addActionListener(new DenyChk());

		// Frame Setting
		// 631 x 501

		setBounds(300, 100, 631, 525);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setVisible(true);

	}

	class DenyChk implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
			dispose();
		}
	}
/*
	class Adjust implements ActionListener
	{
		JFrame lobbyFrame;

		public Adjust(JFrame lobbyFrame)
		{
			this.lobbyFrame = lobbyFrame;
			// TODO Auto-generated constructor stub
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
			new AdjustGUI(dto, lobbyFrame);

			dispose();
		}
	}*/

}