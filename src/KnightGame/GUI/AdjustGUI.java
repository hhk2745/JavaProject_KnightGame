package KnightGame.GUI;//new_End

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import KnightGame.MemberDAO;
import KnightGame.MemberDTO;
import KnightGame.GUI.AdjustGUI.Exit;
//import KnightGame.GUI.AdjustGUI.Submit;

public class AdjustGUI extends JFrame
{
	JFrame lobbyFrame;
	// DTO, DAO
	MemberDTO dto;
	MemberDAO dao;

	// View Field
	public JTextField idField;
	public JPasswordField pwField;
	public JTextField emailField;

	// Action Button
	public JButton submitBtn;
	JButton exitBtn;

	// Img
	ImageIcon icon;
	JPanel panel;

	public AdjustGUI(MemberDTO dto/*, JFrame lobbyFrame*/)
	{
		//this.lobbyFrame = lobbyFrame;
		this.dto = dto;
		dao = new MemberDAO();
		setTitle("�������� ����");

		// img
		icon = new ImageIcon("panel_images/adjust.PNG");
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
		panel.setLayout(null);

		// ����
		idField = new JTextField(dto.getId());
		pwField = new JPasswordField();
		emailField = new JTextField(dto.getEmail());
		submitBtn = new JButton("�����ϱ�");
		exitBtn = new JButton("������");

		// ��ġ, ũ��
		idField.setBounds(183, 105, 267, 50);
		pwField.setBounds(183, 213, 267, 50);
		emailField.setBounds(183, 321, 267, 50);
		submitBtn.setBounds(36, 420, 270, 58);
		exitBtn.setBounds(342, 420, 270, 58);

		// ID field Setting
		idField.setEditable(false);
		idField.setText(dto.getId());
		emailField.setText(dto.getEmail());

		// add
		panel.add(idField);
		panel.add(pwField);
		panel.add(emailField);
		panel.add(submitBtn);
		panel.add(exitBtn);

		add(panel);

		// ������
		//submitBtn.addActionListener(new Submit());
		exitBtn.addActionListener(new Exit());

		// Main Frame Setting
		// 634 x 504
		setBounds(900, 0, 640, 540);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setResizable(false);
	}

	/*
	class Submit implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
			// ��ȿ�� üũ�Ұ�.. regex pattern

			if (dao.updateAccount(dto.getId(), pwField.getPassword(), emailField.getText()))
				System.out.println("���漺��");
			else
				System.out.println("�������");

			// Jdialog

			//lobbyFrame.dispose();
			System.out.println("JDialog MSG :���� ����! ������ �ϼ���.");

			dispose();
		}
	}
*/
	class Exit implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
			dispose();
		}

	}

}