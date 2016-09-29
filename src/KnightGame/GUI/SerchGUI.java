package KnightGame.GUI;//new_End

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
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import KnightGame.MemberDAO;

class SerchGUI extends JFrame
{
	// DAO
	MemberDAO dao;

	// Img
	ImageIcon icon;
	JPanel imgPanel;

	// Field
	JTextField inputEmail;

	// 알림 Label
	JLabel AlertLabel;

	// Button
	JButton okBtn;
	JButton cancelBtn;

	// JDialog - 이메일 검색 결과
	JDialog jdialog;

	public SerchGUI()
	{
		setTitle("아이디/ 패스워드 찾기");

		// DAO 생성
		dao = new MemberDAO();

		// ImgIcon 생성
		icon = new ImageIcon("panel_images/serchImg.PNG");

		// ImgPanel 생성
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
		inputEmail = new JTextField();

		// 버튼 생성
		okBtn = new JButton("확인");
		cancelBtn = new JButton("취소");

		// Field, 버튼 위치
		inputEmail.setBounds(300, 250, 270, 50);
		okBtn.setBounds(236, 343, 187, 48);
		cancelBtn.setBounds(452, 343, 187, 48);

		// add
		imgPanel.add(inputEmail);
		imgPanel.add(okBtn);
		imgPanel.add(cancelBtn);

		// add(Panel)
		add(imgPanel);

		// Frame Setting
		setBounds(700, 0, 854, 516);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		// 액션 리스너
		okBtn.addActionListener(new Ok());
		cancelBtn.addActionListener(new Cancel());

	}

	class Ok implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// Frame
			JFrame jf = new JFrame();
			// Panel
			JPanel jp = new JPanel();
			// Button 생성
			JButton ok = new JButton("확인");

			// Frame 위치,크기
			jf.setBounds(1000, 30, 400, 200);
			// 레이아웃
			jf.setLayout(new GridLayout(2, 1));
			// dao 결과값 저장
			String res = dao.serch(inputEmail.getText());
			// 액션 리스너
			ok.addActionListener(new OkAction(jf));

			jdialog = new JDialog(jf, "요청 처리 결과.", true);
			jf.add(new JLabel(res));
			jf.add(ok);
			jf.setVisible(true);
			jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}

		class OkAction implements ActionListener
		{
			JFrame jf;

			public OkAction(JFrame jf)
			{
				// TODO Auto-generated constructor stub
				this.jf = jf;
			}

			@Override
			public void actionPerformed(ActionEvent e)
			{
				jf.dispose();
			}
		}
	}

	class Cancel implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
			dispose();
		}
	}

	public static void main(String[] args)
	{
		new SerchGUI();
	}

}
