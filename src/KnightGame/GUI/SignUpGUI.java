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

	// �˸� Label
	JLabel AlertLabel;

	// ���� ���ο��� üũ
	boolean idChk = false, pwChk = false, emailChk = false, vis = false;

	// Width, Height (field, button)
	int fieldWidth = 200, fieldHeight = 30;
	int buttonWidth = 110, buttonHeight = 35;

	public SignUpGUI()
	{
		// dao ����
		dao = new MemberDAO();

		// Frame Setting
		setTitle("ȸ������");
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
		// �Է�â����
		inputIdField = new JTextField(); // ���̵� �Է�. - �ߺ�üũ ��ư ���� �ְ�.
		inputPw1Field = new JPasswordField(); // �н����� �Է�.
		inputPw2Field = new JPasswordField(); // �н����� ��Ȯ��.
		inputEmailField = new JTextField(); // �̸��� �Է�.

		// �ߺ�üũ �� ����,���� ��ư ����.
		idChkBtn = new JButton("�ߺ�üũ");
		pwChkBtn = new JButton("PW Ȯ��");
		mailChkBtn = new JButton("Ȯ��");
		agreeBtn = new JButton("����");
		denyBtn = new JButton("���");

		// �˸��� ����
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
		AlertLabel.setFont(new Font("�޸�����ü", Font.BOLD, 20));
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

	// ID �ߺ�üũ
	class IdChk implements ActionListener// ����
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			String regexID = "^[a-z][a-z\\d]{3,11}$"; // id ��ȿ���˻�(ù���ڰ� ���� �ҹ��ڷ�,
														// 4~12���ڼ���)
			String idField = inputIdField.getText(); // ���̵� �ؽ�Ʈ�ʵ�
			boolean checkID = Pattern.matches(regexID, idField); // Id ��ȿ���˻�
																	// ��,����üũ

			// TODO Auto-generated method stub
			if (inputIdField.getText().isEmpty())
			{
				AlertLabel.setText("���̵�� ������ �ɼ� �����ϴ�.");
				idChk = false;
			} else if (checkID == false)
			{
				AlertLabel.setText("ID�� ù���ڴ� ���� �ҹ��ڷ�, 4~12�ڷ� �Է��ؾ��մϴ�!");
			} else
			{
				if (dao.signUp_IdChk(inputIdField.getText()))
				{ // ID�κи� �ϼ��Ѿ�� �̷��� pw�� �̸��ϵ���?

					AlertLabel.setText("���� ������ ���̵� �Դϴ�.");
					int a = JOptionPane.showConfirmDialog(null, "�Է��� ID�� ����ϰڽ��ϱ�? Ȯ�δ����� �ٽ� �Է����� ���մϴ�.", "Ȯ��",
							JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (a == 0)
					{
						JOptionPane.showMessageDialog(null, "���������� üũ�Ǿ����ϴ�!");
						AlertLabel.setText("��� �Է¶��� ä���ֽð� Ȯ�ι�������!!");
						idChk = true;
						inputIdField.setEnabled(false);
						idChkBtn.setEnabled(false);
						if (idChk && pwChk && emailChk)
						{
							AlertLabel.setText("��� Ȯ�ιް� �Է��ϼ̽��ϴ�! ���Խ��� �����ּ���");
							vis = true;
							agreeBtn.setEnabled(vis);
						}
					} else
					{
						JOptionPane.showMessageDialog(null, "��ҵƽ��ϴ�.");
						AlertLabel.setText("��� �Է¶��� ä���ֽð� Ȯ�ι�������!!");
						idChk = false;
					}
				} else
				{
					AlertLabel.setText("�ߺ��� ���̵� �Դϴ�.");
					idChk = false;
				}
			}
		}
	}

	// PW üũ
	class PwChk implements ActionListener// ����
	{
		String regexPw = "^[a-zA-Z0-9]*$"; // pw ��ȿ���˻�(����, ���ڸ� �Է��Ҽ��ְ�)
		String PwField = inputPw1Field.getText(); // ���̵� �ؽ�Ʈ�ʵ�
		boolean checkPw = Pattern.matches(regexPw, PwField); // Pw ��ȿ���˻� ��,����üũ

		@Override // �� ȸ������ �� ���ݾƿ�? ó���� ����Ҷ� TRAM �� �ƴ� ��� ä���ּ��� ��� ����ϸ� ����
		// �׷��� �ߺ�üũ�� ��ҵǸ� �ٽ� ��� ä���ּ��� ��� ��������Կ�
		public void actionPerformed(ActionEvent e)
		{
			if (inputPw1Field.getPassword().length == 0) // ��� �����϶�
			{
				AlertLabel.setText("��й�ȣ�� �Է��ϼž��մϴ�.");
				pwChk = false;
			} else if (checkPw == false)
			{ // ��ȿ���˻�(����,���ڸ� �Է°����ϵ���)
				AlertLabel.setText("PW�� ����, ���ڸ� �Է°����մϴ�.");
				pwChk = false;
			} else if (inputPw2Field.getPassword().length == 0)
			{ // �����Ȯ�� �����϶�
				AlertLabel.setText("��й�ȣ ��Ȯ���� �Է��ϼž��մϴ�.");
				pwChk = false;
			} else if (inputPw1Field.getPassword().length == inputPw2Field.getPassword().length)
				for (int i = 0; i < inputPw1Field.getPassword().length; i++)
				{
					if (inputPw1Field.getPassword()[i] != inputPw2Field.getPassword()[i])
					{
						AlertLabel.setText("�н����尡 ����ġ�մϴ�.");
						pwChk = false;
						break;
					} else
					{
						AlertLabel.setText("�н����尡 ��ġ�մϴ�.");
						int a = JOptionPane.showConfirmDialog(null, "�Է��� PW�� ����ϰڽ��ϱ�? Ȯ�δ����� �ٽ� �Է����� ���մϴ�.", "Ȯ��",
								JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
						if (a == 0)
						{
							JOptionPane.showMessageDialog(null, "���������� üũ�Ǿ����ϴ�!");
							AlertLabel.setText("��� �Է¶��� ä���ֽð� Ȯ�ι�������!!");
							pwChk = true;
							inputPw1Field.setEnabled(false);
							inputPw2Field.setEnabled(false);
							pwChkBtn.setEnabled(false);
							if (idChk && pwChk && emailChk)
							{
								AlertLabel.setText("��� Ȯ�ιް� �Է��ϼ̽��ϴ�! ���Խ��� �����ּ���");
								vis = true;
								agreeBtn.setEnabled(vis);
							}
							break;
						} else
						{
							JOptionPane.showMessageDialog(null, "��ҵƽ��ϴ�.");
							AlertLabel.setText("��� �Է¶��� ä���ֽð� Ȯ�ι�������!!");
							pwChk = false;
							break;
						}
					}
				}
			else
			{
				AlertLabel.setText("�н����尡 ����ġ�մϴ�.");
				pwChk = false;
			}
		}
	}

	// ���� ��ȿ�� �˻�
	class MailChk implements ActionListener// ����
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
			String email = inputEmailField.getText();

			if (inputEmailField.getText().isEmpty())
			{
				AlertLabel.setText("Email�� �Է��ϼž��մϴ�.");
				emailChk = false;
			}

			if (Pattern.matches(regex, email))
			{
				AlertLabel.setText("�̸��� ���� ��ġ");
				int a = JOptionPane.showConfirmDialog(null, "�Է��� �̸����� ����ϰڽ��ϱ�? Ȯ�δ����� �ٽ� �Է����� ���մϴ�.", "Ȯ��",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (a == 0)
				{
					JOptionPane.showMessageDialog(null, "���������� üũ�Ǿ����ϴ�!");
					inputEmailField.setEnabled(false);
					mailChkBtn.setEnabled(false);
					emailChk = true;
					if (idChk && pwChk && emailChk)
					{
						AlertLabel.setText("��� Ȯ�ιް� �Է��ϼ̽��ϴ�! ���Խ��� �����ּ���");
						vis = true;
						agreeBtn.setEnabled(vis);
					}
				} else
				{
					JOptionPane.showMessageDialog(null, "��ҵƽ��ϴ�.");
					AlertLabel.setText("��� �Է¶��� ä���ֽð� Ȯ�ι�������!!");
					emailChk = false;
				}
			} else
			{
				AlertLabel.setText("�̸��� ���� ����ġ");
				emailChk = false;
			}
		}
	}

	// ȸ������ ó��
	class AgreeChk implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (!idChk)
				AlertLabel.setText("���̵� ����");
			else if (!pwChk)
				AlertLabel.setText("�н����� ����");
			else if (!emailChk)
				AlertLabel.setText("�̸��� ����");
			else
			{
				dao.signUp(inputIdField.getText(), inputPw1Field.getPassword(), inputEmailField.getText());
				dispose();
			}
		}
	}

	// ȸ������ ���
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
