package KnightGame.GUI;//new_End

import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import KnightGame.MemberDTO;

public class GUIForm extends JFrame
{
	GUIForm seenGUI;
	public JTextArea chatArea;
	public JTextField chat;
	public LoginGUI login;
	public LobbyGUI lobby;
	public RoomGUI room;

	public GUIForm()
	{
		setTitle("GUI �⺻ Ʋ");
		// setBounds(10,10,1000,600);
	}

	public void setUserChart(Vector<MemberDTO> userChart)
	{
	}

}
