package KnightGame.GUI;//new_End

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import KnightGame.MemberDAO;
import KnightGame.MemberDTO;
import KnightGame.GUI.LoginGUI.ActionSignUp;
import KnightGame.GUI.LoginGUI.Login;
import KnightGame.GUI.LoginGUI.Serch;
import KnightGame.GUI.LoginGUI.Login.Cancel;
import KnightGame.GUI.LoginGUI.Login.Move;

public class LoginGUI extends GUIForm
{
	// DAO
	MemberDAO dao;
	MemberDTO dto;// �α��� ������ �����ų dto

	// Img
	ImageIcon icon = new ImageIcon("panel_images/loginImg.PNG");
	JPanel imgPanel;

	// Field
	public JTextField inputId;
	public JPasswordField inputPw;

	// �˸� ��
	public JLabel alertLabel;

	// Button
	public JButton loginBtn;
	JButton signupBtn;
	JButton serchBtn;

	public LoginGUI()
	{
		setTitle("�α��� â");

		// DAO ����
		dao = new MemberDAO();

		// imgPanel����
		imgPanel = new JPanel()
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
		imgPanel.setLayout(null);

		// Field ����
		inputId = new JTextField();
		inputPw = new JPasswordField();

		// ��ư ����
		loginBtn = new JButton("�α���");
		signupBtn = new JButton("ȸ������");
		serchBtn = new JButton("���̵�/��й�ȣ ã��");

		// �˸� �� ����
		alertLabel = new JLabel("TEMP");
		alertLabel.setFont(new Font("�޸�����ü", Font.TYPE1_FONT, 20));
		alertLabel.setForeground(Color.WHITE);

		// Field ��ġ
		inputId.setBounds(230, 180, 200, 25);
		inputPw.setBounds(230, 225, 200, 25);

		// ��ư ��ġ
		loginBtn.setBounds(470, 172, 100, 85);
		signupBtn.setBounds(190, 294, 160, 40);
		serchBtn.setBounds(376, 294, 160, 40);

		// �� ��ġ,ũ��
		alertLabel.setBounds(100, 130, 500, 50);

		// add
		imgPanel.add(serchBtn);
		imgPanel.add(signupBtn);
		imgPanel.add(inputId);
		imgPanel.add(inputPw);
		imgPanel.add(loginBtn);
		imgPanel.add(alertLabel);

		// add(panel)
		add(imgPanel);

		// Frame Setting
		setResizable(false);
		setBounds(300, 200, 704, 439);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// ��ư �׼�
		signupBtn.addActionListener(new ActionSignUp());
		serchBtn.addActionListener(new Serch());
	}

	class Serch implements ActionListener // ID,PWã�� Ŭ����
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
			new SerchGUI();
		}
	}

	// ���� �α��� �����ʾ���
	public MemberDTO getDTO()
	{
		return this.dto;
	}

	public void loginError()
	{
		new Login();
	}

	class Login
	{
		static final int LOGIN_ERROR = 3;

		public Login()
		{
			AlertMsg();
		}

		void AlertMsg()
		{
			// Frame, Panel ����
			JFrame jf = new JFrame();
			JPanel jp = new JPanel();

			// Dialog, Button ����
			JDialog jdialog = new JDialog(jf, "���", false);
			JButton move = new JButton("�̵�");
			JButton cancel = new JButton("���");

			// add
			jf.setLayout(new GridLayout(3, 1));
			jf.add(new JLabel("�ش���̵� �������� �ʽ��ϴ�."));
			jf.add(new JLabel("ȸ���������� �̵��Ͻðڽ��ϱ�?"));

			// add
			jp.setLayout(new GridLayout(1, 2));
			jp.add(move);
			jp.add(cancel);

			// add(Panel)
			jf.add(jp);

			// Frame Setting
			jf.setBounds(1000, 30, 300, 300);
			jf.setVisible(true);
			jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			move.addActionListener(new Move(jf));
			cancel.addActionListener(new Cancel(jf));
		}

		class Move implements ActionListener
		{
			JFrame jf;

			public Move(JFrame jf)
			{
				// TODO Auto-generated constructor stub
				this.jf = jf;
			}

			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub
				jf.dispose();
				new SignUpGUI();
			}
		}

		class Cancel implements ActionListener
		{
			JFrame jf;

			public Cancel(JFrame jf)
			{
				this.jf = jf;
			}

			@Override
			public void actionPerformed(ActionEvent e)
			{
				jf.dispose();
			}
		}

	}

	class ActionSignUp implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{ // ȸ�������̸� ���⼭ SignUpŬ���� ȣ���ؼ�.
			new SignUpGUI();
		}
	}

}
