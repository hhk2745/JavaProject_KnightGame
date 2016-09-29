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
	MemberDTO dto;// 로그인 성공시 저장시킬 dto

	// Img
	ImageIcon icon = new ImageIcon("panel_images/loginImg.PNG");
	JPanel imgPanel;

	// Field
	public JTextField inputId;
	public JPasswordField inputPw;

	// 알림 라벨
	public JLabel alertLabel;

	// Button
	public JButton loginBtn;
	JButton signupBtn;
	JButton serchBtn;

	public LoginGUI()
	{
		setTitle("로그인 창");

		// DAO 생성
		dao = new MemberDAO();

		// imgPanel생성
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

		// Field 생성
		inputId = new JTextField();
		inputPw = new JPasswordField();

		// 버튼 생성
		loginBtn = new JButton("로그인");
		signupBtn = new JButton("회원가입");
		serchBtn = new JButton("아이디/비밀번호 찾기");

		// 알림 라벨 생성
		alertLabel = new JLabel("TEMP");
		alertLabel.setFont(new Font("휴먼편지체", Font.TYPE1_FONT, 20));
		alertLabel.setForeground(Color.WHITE);

		// Field 위치
		inputId.setBounds(230, 180, 200, 25);
		inputPw.setBounds(230, 225, 200, 25);

		// 버튼 위치
		loginBtn.setBounds(470, 172, 100, 85);
		signupBtn.setBounds(190, 294, 160, 40);
		serchBtn.setBounds(376, 294, 160, 40);

		// 라벨 위치,크기
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

		// 버튼 액션
		signupBtn.addActionListener(new ActionSignUp());
		serchBtn.addActionListener(new Serch());
	}

	class Serch implements ActionListener // ID,PW찾기 클릭시
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
			new SerchGUI();
		}
	}

	// 여긴 로그인 리스너없고
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
			// Frame, Panel 생성
			JFrame jf = new JFrame();
			JPanel jp = new JPanel();

			// Dialog, Button 생성
			JDialog jdialog = new JDialog(jf, "경고", false);
			JButton move = new JButton("이동");
			JButton cancel = new JButton("취소");

			// add
			jf.setLayout(new GridLayout(3, 1));
			jf.add(new JLabel("해당아이디가 존재하지 않습니다."));
			jf.add(new JLabel("회원가입으로 이동하시겠습니까?"));

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
		{ // 회원가입이면 여기서 SignUp클래스 호출해서.
			new SignUpGUI();
		}
	}

}
