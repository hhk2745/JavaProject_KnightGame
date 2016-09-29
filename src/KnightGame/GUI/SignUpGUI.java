package KnightGame.GUI;//new_End

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import KnightGame.MemberDAO;

public class SignUpGUI extends JFrame
{
	// DAO
	MemberDAO dao;

	// Img
	ImageIcon icon;
	JPanel panel;

	// Button
	JButton idChkBtn;
	JButton pwChkBtn;
	JButton mailChkBtn;
	JButton agreeBtn;
	JButton denyBtn;

	// Field
	JTextField inputIdField;
	JPasswordField inputPw1Field;
	JPasswordField inputPw2Field;
	JTextField inputEmailField;

	// 알림 Label
	JLabel AlertLabel;

	// 가입 승인여부 체크
	boolean idChk = false, pwChk = false, emailChk = false, vis = false;

	// Width, Height (field, button)
	int fieldWidth = 200, fieldHeight = 30;
	int buttonWidth = 110, buttonHeight = 35;

	public SignUpGUI()
	{
		// dao 생성
		dao = new MemberDAO();

		// Frame Setting
		setTitle("회원가입");
		setBounds(1000, 0, 618, 710);

		// img
		icon = new ImageIcon("panel_images/signinImg.PNG");
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
		/****************************** NEW ********************************/
		// 입력창생성
		inputIdField = new JTextField(); // 아이디 입력. - 중복체크 버튼 옆에 넣고.
		inputPw1Field = new JPasswordField(); // 패스워드 입력.
		inputPw2Field = new JPasswordField(); // 패스워드 재확인.
		inputEmailField = new JTextField(); // 이메일 입력.

		// 중복체크 및 가입,거절 버튼 생성.
		idChkBtn = new JButton("중복체크");
		pwChkBtn = new JButton("PW 확인");
		mailChkBtn = new JButton("확인");
		agreeBtn = new JButton("가입");
		denyBtn = new JButton("취소");

		// 알림라벨 생성
		AlertLabel = new JLabel("TEMP");

		/****************************** BOUNDS ********************************/
		// Field_setBounds
		inputIdField.setBounds(230, 205, fieldWidth, fieldHeight);
		inputPw1Field.setBounds(230, 270, fieldWidth, fieldHeight);
		inputPw2Field.setBounds(230, 330, fieldWidth, fieldHeight);
		inputEmailField.setBounds(230, 405, fieldWidth, fieldHeight);

		// Button_setBounds
		idChkBtn.setBounds(457, 207, buttonWidth, buttonHeight);
		pwChkBtn.setBounds(457, 295, buttonWidth, buttonHeight);
		mailChkBtn.setBounds(457, 395, buttonWidth, buttonHeight);
		agreeBtn.setBounds(120, 558, (int) (buttonWidth * 1.7), buttonHeight + 11);
		denyBtn.setBounds(337, 558, (int) (buttonWidth * 1.7), buttonHeight + 11);

		// Label_setting
		AlertLabel.setBounds(100, 150, fieldWidth * 3, fieldHeight);
		AlertLabel.setFont(new Font("휴먼편지체", Font.BOLD, 20));
		AlertLabel.setForeground(Color.WHITE);// new

		/****************************** ADD ********************************/
		// Field_add
		panel.add(inputIdField);
		panel.add(inputPw1Field);
		panel.add(inputPw2Field);
		panel.add(inputEmailField);

		// Button_add
		panel.add(idChkBtn);
		panel.add(pwChkBtn);
		panel.add(mailChkBtn);
		panel.add(agreeBtn);
		panel.add(denyBtn);

		// Label_add
		panel.add(AlertLabel);

		// panel_setting
		panel.setLayout(null);

		// panel_add
		add(panel);

		// Frame Setting
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		agreeBtn.setEnabled(vis);
		// addAction
		idChkBtn.addActionListener(new IdChk());
		pwChkBtn.addActionListener(new PwChk());
		mailChkBtn.addActionListener(new MailChk());
		agreeBtn.addActionListener(new AgreeChk());
		denyBtn.addActionListener(new DenyChk());
	}

	// ID 중복체크
	class IdChk implements ActionListener// 수정
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			String regexID = "^[a-z][a-z\\d]{3,11}$"; // id 유효성검사(첫글자가 영문 소문자로,
														// 4~12글자수만)
			String idField = inputIdField.getText(); // 아이디 텍스트필드
			boolean checkID = Pattern.matches(regexID, idField); // Id 유효성검사
																	// 참,거짓체크

			// TODO Auto-generated method stub
			if (inputIdField.getText().isEmpty())
			{
				AlertLabel.setText("아이디는 공백이 될수 없습니다.");
				idChk = false;
			} else if (checkID == false)
			{
				AlertLabel.setText("ID는 첫글자는 영문 소문자로, 4~12자로 입력해야합니다!");
			} else
			{
				if (dao.signUp_IdChk(inputIdField.getText()))
				{ // ID부분만 완성됏어요 이렇게 pw랑 이메일도요?

					AlertLabel.setText("가입 가능한 아이디 입니다.");
					int a = JOptionPane.showConfirmDialog(null, "입력한 ID을 사용하겠습니까? 확인누르면 다시 입력하지 못합니다.", "확인",
							JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (a == 0)
					{
						JOptionPane.showMessageDialog(null, "성공적으로 체크되었습니다!");
						AlertLabel.setText("모든 입력란에 채워주시고 확인받으세요!!");
						idChk = true;
						inputIdField.setEnabled(false);
						idChkBtn.setEnabled(false);
						if (idChk && pwChk && emailChk)
						{
							AlertLabel.setText("모두 확인받고 입력하셨습니다! 가입승인 눌러주세요");
							vis = true;
							agreeBtn.setEnabled(vis);
						}
					} else
					{
						JOptionPane.showMessageDialog(null, "취소됐습니다.");
						AlertLabel.setText("모든 입력란에 채워주시고 확인받으세요!!");
						idChk = false;
					}
				} else
				{
					AlertLabel.setText("중복된 아이디 입니다.");
					idChk = false;
				}
			}
		}
	}

	// PW 체크
	class PwChk implements ActionListener// 수정
	{
		String regexPw = "^[a-zA-Z0-9]*$"; // pw 유효성검사(영어, 숫자만 입력할수있게)
		String PwField = inputPw1Field.getText(); // 아이디 텍스트필드
		boolean checkPw = Pattern.matches(regexPw, PwField); // Pw 유효성검사 참,거짓체크

		@Override // 그 회원가입 라벨 있잖아요? 처음에 출력할때 TRAM 가 아닌 모두 채워주세요 라고 출력하면 어떨까요
		// 그래야 중복체크시 취소되면 다시 모두 채워주세요 라고 출력해지게요
		public void actionPerformed(ActionEvent e)
		{
			if (inputPw1Field.getPassword().length == 0) // 비번 공백일때
			{
				AlertLabel.setText("비밀번호를 입력하셔야합니다.");
				pwChk = false;
			} else if (checkPw == false)
			{ // 유효성검사(영어,숫자만 입력가능하도록)
				AlertLabel.setText("PW에 영어, 숫자만 입력가능합니다.");
				pwChk = false;
			} else if (inputPw2Field.getPassword().length == 0)
			{ // 비번재확인 공백일때
				AlertLabel.setText("비밀번호 재확인을 입력하셔야합니다.");
				pwChk = false;
			} else if (inputPw1Field.getPassword().length == inputPw2Field.getPassword().length)
				for (int i = 0; i < inputPw1Field.getPassword().length; i++)
				{
					if (inputPw1Field.getPassword()[i] != inputPw2Field.getPassword()[i])
					{
						AlertLabel.setText("패스워드가 불일치합니다.");
						pwChk = false;
						break;
					} else
					{
						AlertLabel.setText("패스워드가 일치합니다.");
						int a = JOptionPane.showConfirmDialog(null, "입력한 PW을 사용하겠습니까? 확인누르면 다시 입력하지 못합니다.", "확인",
								JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
						if (a == 0)
						{
							JOptionPane.showMessageDialog(null, "성공적으로 체크되었습니다!");
							AlertLabel.setText("모든 입력란에 채워주시고 확인받으세요!!");
							pwChk = true;
							inputPw1Field.setEnabled(false);
							inputPw2Field.setEnabled(false);
							pwChkBtn.setEnabled(false);
							if (idChk && pwChk && emailChk)
							{
								AlertLabel.setText("모두 확인받고 입력하셨습니다! 가입승인 눌러주세요");
								vis = true;
								agreeBtn.setEnabled(vis);
							}
							break;
						} else
						{
							JOptionPane.showMessageDialog(null, "취소됐습니다.");
							AlertLabel.setText("모든 입력란에 채워주시고 확인받으세요!!");
							pwChk = false;
							break;
						}
					}
				}
			else
			{
				AlertLabel.setText("패스워드가 불일치합니다.");
				pwChk = false;
			}
		}
	}

	// 메일 유효성 검사
	class MailChk implements ActionListener// 수정
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
			String email = inputEmailField.getText();

			if (inputEmailField.getText().isEmpty())
			{
				AlertLabel.setText("Email을 입력하셔야합니다.");
				emailChk = false;
			}

			if (Pattern.matches(regex, email))
			{
				AlertLabel.setText("이메일 형식 일치");
				int a = JOptionPane.showConfirmDialog(null, "입력한 이메일을 사용하겠습니까? 확인누르면 다시 입력하지 못합니다.", "확인",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (a == 0)
				{
					JOptionPane.showMessageDialog(null, "성공적으로 체크되었습니다!");
					inputEmailField.setEnabled(false);
					mailChkBtn.setEnabled(false);
					emailChk = true;
					if (idChk && pwChk && emailChk)
					{
						AlertLabel.setText("모두 확인받고 입력하셨습니다! 가입승인 눌러주세요");
						vis = true;
						agreeBtn.setEnabled(vis);
					}
				} else
				{
					JOptionPane.showMessageDialog(null, "취소됐습니다.");
					AlertLabel.setText("모든 입력란에 채워주시고 확인받으세요!!");
					emailChk = false;
				}
			} else
			{
				AlertLabel.setText("이메일 형식 불일치");
				emailChk = false;
			}
		}
	}

	// 회원가입 처리
	class AgreeChk implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (!idChk)
				AlertLabel.setText("아이디 에러");
			else if (!pwChk)
				AlertLabel.setText("패스워드 에러");
			else if (!emailChk)
				AlertLabel.setText("이메일 에러");
			else
			{
				dao.signUp(inputIdField.getText(), inputPw1Field.getPassword(), inputEmailField.getText());
				dispose();
			}
		}
	}

	// 회원가입 취소
	class DenyChk implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
			dispose();
		}
	}

}
