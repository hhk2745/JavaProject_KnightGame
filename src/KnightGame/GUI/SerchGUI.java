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

	// �˸� Label
	JLabel AlertLabel;

	// Button
	JButton okBtn;
	JButton cancelBtn;

	// JDialog - �̸��� �˻� ���
	JDialog jdialog;

	public SerchGUI()
	{
		setTitle("���̵�/ �н����� ã��");

		// DAO ����
		dao = new MemberDAO();

		// ImgIcon ����
		icon = new ImageIcon("panel_images/serchImg.PNG");

		// ImgPanel ����
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
		inputEmail = new JTextField();

		// ��ư ����
		okBtn = new JButton("Ȯ��");
		cancelBtn = new JButton("���");

		// Field, ��ư ��ġ
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

		// �׼� ������
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
			// Button ����
			JButton ok = new JButton("Ȯ��");

			// Frame ��ġ,ũ��
			jf.setBounds(1000, 30, 400, 200);
			// ���̾ƿ�
			jf.setLayout(new GridLayout(2, 1));
			// dao ����� ����
			String res = dao.serch(inputEmail.getText());
			// �׼� ������
			ok.addActionListener(new OkAction(jf));

			jdialog = new JDialog(jf, "��û ó�� ���.", true);
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
