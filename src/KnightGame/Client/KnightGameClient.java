package KnightGame.Client;//new_ing

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import KnightGame.DemandChart;
import KnightGame.MemberDAO;
import KnightGame.MemberDTO;
import KnightGame.Client.KnightGameClient.ClientReceiver;
import KnightGame.Client.KnightGameClient.ClientSender;
import KnightGame.GUI.AdjustGUI;
import KnightGame.GUI.LobbyGUI;
import KnightGame.GUI.LoginGUI;
import KnightGame.GUI.MyPageGUI;
import KnightGame.GUI.PlayerInfo;
import KnightGame.GUI.RoomGUI;
import KnightGame.Game.*;
import KnightGame.Server.Result;

public class KnightGameClient implements DemandChart, KnightGameInfo
{

	public ClientSender sender;
	public ClientReceiver receiver;

	public ObjectOutputStream oos;
	public ObjectInputStream ois;
	public MemberDTO user;

	MemberDAO dao;// new

	LoginGUI login;
	LobbyGUI lobby;
	RoomGUI room;
	KnightGameMain main;

	public JTextArea chatArea;
	public JTextField chat;
	public JButton logout;

	public Vector<MemberDTO> userChart; // new

	public boolean start = false;

	public KnightGameClient()// ����
	{
		dao = new MemberDAO();
		Socket socket;
		try
		{
			socket = new Socket("127.0.0.1", 12345);
			System.out.println("�������� ����");
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());

			sender = new ClientSender(oos);
			receiver = new ClientReceiver(ois);
			receiver.start();
			newLogin("ù�α���");

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	class ClientSender
	{
		public ObjectOutputStream oos;

		public ClientSender(ObjectOutputStream oos)
		{
			try
			{
				// oos = new ObjectOutputStream(socket.getOutputStream());
				this.oos = oos;
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class ClientReceiver extends Thread
	{

		public ObjectInputStream ois;

		public ClientReceiver(ObjectInputStream ois)
		{
			try
			{
				// ois = new ObjectInputStream(socket.getInputStream());
				this.ois = ois;
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void run()
		{
			while (ois != null)
			{
				try
				{
					Result result = (Result) ois.readObject();// ��� ����
					System.out.println("������� : " + result);
					process(result);
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		public void process(Result result) throws IOException
		{
			Demand demand;
			switch (result.demand.demandKind)
			{
			case LOGOUT:
				try
				{
					demand = new Demand(user, UPDATE_USERCHART);
					oos.writeObject(demand);
					newLogin("ù�α���");
				} catch (Exception e)
				{
					// TODO: handle exception
					e.printStackTrace();
				}
				break;
			case LOGIN:
				if (result.demand.demander != null)
				{
					System.out.println("�α��� ���� : " + result.demand.demander);
					user = result.demand.demander;
					demand = new Demand(user, CHANGE_USER_LOCATION);
					demand.demandLocation = 0;
					oos.writeObject(demand);
					login.dispose();// ?
				}else{
					login.alertLabel.setText(result.resultMsg);					
				}
				System.out.println(result.resultMsg);
				break;
			case CHANGE_USER_LOCATION:
				switch (result.resultMsg)
				{
				case "lobby":
					if (room != null)
					{
						room.dispose();
					}

					lobby = new LobbyGUI(user);

					chat = lobby.chat;
					chatArea = lobby.chatArea;
					
					lobby.quickStartBtn.addActionListener(new ActionListener()
					{

						@Override
						public void actionPerformed(ActionEvent e)
						{
							// TODO Auto-generated method stub
							Demand demand = new Demand(user, CHANGE_USER_LOCATION);
							demand.demandLocation = 1;
							try
							{
								oos.writeObject(demand);
							} catch (IOException e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					});
					lobby.chat.addActionListener(new ActionListener()
					{

						@Override
						public void actionPerformed(ActionEvent e)
						{
							// TODO Auto-generated method stub
							Demand demand = new Demand(user, CHATTING);
							demand.demandMsg = lobby.chat.getText();
							lobby.chat.setText("");
							try
							{
								oos.writeObject(demand);
							} catch (IOException e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					});
					logout = lobby.logoutBtn;// new
					logout.addActionListener(new ActionListener()// new
					{
						@Override
						public void actionPerformed(ActionEvent e)
						{
							// TODO Auto-generated method stub
							
							int a = JOptionPane.showConfirmDialog(null, "�α׾ƿ� �Ͻðڽ��ϱ�?", "Ȯ��",
									JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
							if (a == 0)
							{
								try
								{
									oos.writeObject(new Demand(user, LOGOUT));
									lobby.dispose();
								} catch (IOException e1)
								{
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								
							}
							
						}
					});
					lobby.exitBtn.addActionListener(new ActionListener()// new
					{
						@Override
						public void actionPerformed(ActionEvent e)
						{
							int a = JOptionPane.showConfirmDialog(null, "������ �����Ͻðڽ��ϱ�?", "Ȯ��",
									JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
							if (a == 0)
							{
								System.exit(0);
							}	
						}
					});

					
					//�κ�->����������ȭ��->����ȭ��->�α׾ƿ�
					lobby.myPageBtn.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
								MyPageGUI mp = new MyPageGUI(user);
								
								mp.adjustBtn.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										// TODO Auto-generated method stub
										mp.adjust = new AdjustGUI(user);
										mp.adjust.submitBtn.addActionListener(new ActionListener() {
											@Override
											public void actionPerformed(ActionEvent e) {
												// TODO Auto-generated method stub
												if(mp.adjust.pwField.getText().equals("")){
													
												}else if (dao.updateAccount(user.getId(), mp.adjust.pwField.getPassword(), mp.adjust.emailField.getText())){
													mp.adjust.dispose();
													mp.dispose();
													lobby.dispose();
													Demand demand = new Demand(user, LOGOUT);
													try {
														oos.writeObject(demand);
													} catch (IOException e1) {
														// TODO Auto-generated catch block
														e1.printStackTrace();
													}
													System.out.println("���漺��");
												}
												else
													System.out.println("�������");

												// Jdialog

												//lobbyFrame.dispose();
												System.out.println("JDialog MSG :���� ����! ������ �ϼ���.");
											}
										});
									}
								});
						}
					});
					
					demand = new Demand(user, UPDATE_USERCHART);
					oos.writeObject(demand);
					
					break;
				case "room":
					if (lobby != null) {
						lobby.dispose();
					}
					room = new RoomGUI(user);
					chat = room.chat;
					chatArea = room.chatArea;
					room.chat.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e)
						{
							// TODO Auto-generated method stub
							Demand demand = new Demand(user, CHATTING);
							demand.demandMsg = room.chat.getText();
							room.chat.setText("");
							try
							{
								oos.writeObject(demand);
							} catch (IOException e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					});
					room.exit.addActionListener(new ActionListener()
					{

						@Override
						public void actionPerformed(ActionEvent e)
						{
							// TODO Auto-generated method stub
							Demand demand = new Demand(user, CHANGE_USER_LOCATION);
							demand.demandLocation = 0;
							try
							{
								oos.writeObject(demand);
							} catch (IOException e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					});
					room.ready.addActionListener(new ActionListener()
					{

						@Override
						public void actionPerformed(ActionEvent e)
						{
							Demand demand = new Demand(user, READY);
							room.ready.setEnabled(false);
							room.ready.setText("�غ� �Ϸ�");
							try
							{
								oos.writeObject(demand);
							} catch (IOException e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					});
					
					break;
				}

				user.setLocation(result.demand.demander.getLocation());

				demand = new Demand(user, UPDATE_USERCHART);
				oos.writeObject(demand);
				System.out.println(result.resultMsg);
				break;
			case CHATTING:
				System.out.println(result.resultMsg);
				chatArea.append(result.demand.demander.getId() + " : " + result.demand.demandMsg + "\n");
				chatArea.setCaretPosition(chatArea.getDocument().getLength());//���佺ũ��
				break;
			case UPDATE_USERCHART:
				System.out.println(result.resultMsg);
				System.out.println(result.userChart);

				userChart = result.userChart;
				
				try
				{
					Thread.sleep(200);
				} catch (InterruptedException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				///////////////////////////////////////////////////////////////////////////////////////////////
				//�����ڸ��
				if (userChart.get(0).getLocation() == 0) {//�κ��
					lobby.setUserChart(userChart);
					lobby.imgPanel.add(lobby.ttjs);
					lobby.ttjs.setBounds(600, 338, 180, 270);
				}else if(!result.start){//���ӹ��̸�
					if(main != null && main.gaming && userChart.size() == 2){//�������� ���ӹ�
						main.gameGUI.remove(main.gameGUI.playerInfo);
						main.gameGUI.playerInfo = new PlayerInfo(main.gameGUI.playerInfo, userChart.get(0));
						main.gameGUI.add(main.gameGUI.playerInfo);
						
						main.gameGUI.remove(main.gameGUI.comInfo);
						main.gameGUI.comInfo = new PlayerInfo(main.gameGUI.comInfo, userChart.get(1));
						main.gameGUI.add(main.gameGUI.comInfo);

						main.gameGUI.validate();
						main.gameGUI.repaint();
					}else if (userChart.size() == 2) {//Ǯ���� ��
						room.ready.setEnabled(true);
						room.remove(room.p1);
						room.p1 = new PlayerInfo(room.p1, userChart.get(0));
						room.add(room.p1);
						
						room.remove(room.p2);
						room.p2 = new PlayerInfo(room.p2, userChart.get(1));
						room.add(room.p2);

						room.validate();
						room.repaint();
					} else {//ȥ���� ��
						room.ready.setEnabled(false);
						room.remove(room.p1);
						room.p1 = new PlayerInfo(room.p1, user);
						room.add(room.p1);

						room.p2.setVisible(false);
						room.remove(room.p2);

						room.validate();
						room.repaint();
					}
				}
				////////////////////////////////////////////////////////////////////////////////////////////////
				break;
			case READY:
				if (result.start)
				{
					System.out.println("��������!!");
					room.dispose();
					main = new KnightGameMain(user.getId(), result.comId, user, oos);
					// �� id, ��� id, ���������, �׸��� ���� output��Ʈ���� �˷��ش�
					chat = main.gameGUI.chat;
					chatArea = main.gameGUI.chatArea;
					main.gameGUI.chat.addActionListener(new ActionListener()
					{
						@Override // ����ȭ�� ä���̺�Ʈ ���
						public void actionPerformed(ActionEvent e)
						{
							Demand demand = new Demand(user, CHATTING);
							demand.demandMsg = main.gameGUI.chat.getText();
							main.gameGUI.chat.setText("");
							try
							{
								oos.writeObject(demand);
							} catch (IOException e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					});

					main.start();
					if (result.turn)
					{// ������ �����̶�� �˷�������
						main.turnChange = true;// ���� ����!!
					}
				} // ���ӽ��� �Ϸ�
				System.out.println(result.resultMsg);
				break;
			case GAME_START:
				break;
			case SENDING_BOARD:
				System.out.println(result.resultMsg);
				if (result.demand.isObstacle) {
					main.setObstacle(ver - 1 - result.demand.afterX, hor - 1 - result.demand.afterY);
				} else {
					main.enemyMove(ver - 1 - result.demand.beforeX, hor - 1 - result.demand.beforeY,
							ver - 1 - result.demand.afterX, hor - 1 - result.demand.afterY);
				}
				main.setGUI(main.player.board);

				break;
			case TURN_CHANGE:
				main.turnChange = true;// ���� �ٲ���ٴ� ����� �޾Ҵ�.
				System.out.println(result.resultMsg);
				break;
			case END_GAME:
				JOptionPane.showMessageDialog(null, "���ӿ��� �¸��ϼ̽��ϴ�. �κ�� �̵��մϴ�.");
				main.gaming = false;
				System.out.println(result.resultMsg);
				break;
			default:
				System.out.println("����");
				break;
			}
		}
	}

	LoginGUI newLogin(String str) throws IOException
	{

		switch (str)
		{
		case "ù�α���":
			login = new LoginGUI();
			break;

		default:
			break;
		}

		login.loginBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{// �α��� ������ �׼� �����ʾ�
				// TODO Auto-generated method stub
				String regex = "^[a-zA-Z0-9]*${3,11}$"; // id ��ȿ���˻�
				String regexPw = "^[a-zA-Z0-9]*$"; // pw ��ȿ���˻�(����, ���ڸ� �Է��Ҽ��ְ�)
													// �̰ɷ��ϸ�
				String idField = login.inputId.getText();
				String pwField = login.inputPw.getText();

				if (idField.isEmpty())
				{ // ���̵� �����϶�
					login.alertLabel.setText("ID�� �Է��ϼž� �մϴ�.");
				} else if (pwField.isEmpty())
				{ // �н����� �����϶�
					login.alertLabel.setText("PW�� �Է��ϼž� �մϴ�.");
				} else
				{ // �Ѵ� ����ƴҽ�
					boolean check = Pattern.matches(regex, idField); // Id ��ȿ���˻�
					boolean checkPw = Pattern.matches(regexPw, pwField); // Pw

					if (check == false)
					{ // id��ȿ���˻�
						login.alertLabel.setText("ID�� ù���ڴ� ���� �ҹ��ڷ�, 4~12�ڷ� �Է��ؾ��մϴ�!");
					} else if (checkPw == false)
					{ // ��� ��ȿ���˻� ����ϰ��� dao�� db�� �����ϴ� �κ�
						login.alertLabel.setText("PW�� ����, ���ڸ� �Է°����մϴ�.");
					} else
					{ // ��� ��ȿ���˻� ����ҽ�
						System.out.println("����ο�?");
						// ���̵� �ؽ�Ʈ�ʵ�, �н����� �޾Ƽ� dao.loginChk()<<���ΰ��� db���� ����
						user = new MemberDTO();
						user.setId(idField);
						user.setPw(pwField);
						Demand demand = new Demand(user, LOGIN); // �α��μ���
						try {
							oos.writeObject(demand);// ������ ��û
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}
				}
			}
		});
		return login;
	}

	public static void main(String[] args)
	{
		new KnightGameClient();
	}
}
