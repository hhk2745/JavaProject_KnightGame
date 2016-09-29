package KnightGame.Server;//new

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import KnightGame.DemandChart;
import KnightGame.MemberDAO;
import KnightGame.MemberDTO;
import KnightGame.Client.Demand;

class ServerReceiver extends Thread implements DemandChart
{

	HashMap<MemberDTO, ObjectOutputStream> clients;// ��� ������\
	HashMap<Integer, HashMap<MemberDTO, Boolean>> roomInfo;

	ObjectOutputStream oos;// �ⱸ
	ObjectInputStream ois;// �Ա�

	public ServerReceiver(Socket socket, HashMap<MemberDTO, ObjectOutputStream> clients,
			HashMap<Integer, HashMap<MemberDTO, Boolean>> roomInfo)
	{
		try
		{
			this.clients = clients;
			this.roomInfo = roomInfo;

			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		Demand demand = null;
		try
		{
			while (ois != null)
			{
				demand = (Demand) ois.readObject();// �����κ��� ���� ��û�� �о�帰��
				System.out.println("����Ŭ����������������������������������������������");
				System.out.println("�䱸�� : " + demand.demander);
				System.out.println("�䱸    : " + demand.demandMsg);
				process(demand);// ó���ض�
				System.out.println("������������������������������������������������������");

			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} finally
		{
			if(roomInfo.get(demand.demander.getLocation()).get(demand.demander) != null &&
			   roomInfo.get(demand.demander.getLocation()).get(demand.demander)){//�������̴����� �����ߴٸ�
				System.out.println(demand.demander.getId() + "�α׾ƿ�");
				removeUserAtLocation(demand.demander);// ������ ��ġ���� ����
				clients.remove(demand.demander);// ������ ��Ͽ��� ����
				Iterator<MemberDTO> member = roomInfo.get(demand.demander.getLocation()).keySet().iterator();
				MemberDTO player = member.next();
				Result result = new Result(new Demand(player, END_GAME));
				result.resultMsg = "������ ����";
				try {
					clients.get(player).writeObject(result);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				System.out.println(demand.demander.getId() + "�α׾ƿ�");
				removeUserAtLocation(demand.demander);// ������ ��ġ���� ����
				clients.remove(demand.demander);// ������ ��Ͽ��� ����
			}
			try
			{
				updateUserChart();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	synchronized public void process(Demand demand) throws IOException
	{
		Result result;
		switch (demand.demandKind)
		{
		case LOGOUT:
			result = new Result(demand);
			result.resultMsg = demand.demander.getId() + "�α׾ƿ�";
			removeUserAtLocation(demand.demander);// ������ ��ġ���� ����
			clients.remove(demand.demander);// ������ ��Ͽ��� ����
			oos.writeObject(result);
			break;
		case LOGIN:
			result = new Result(demand);
			MemberDTO chk = new MemberDAO().loginChk(demand.demander.getId(), demand.demander.getPw());
			System.out.println(clients.get(chk));
			if (chk == null)
			{
				result.resultMsg = "���̵� �Ǵ� ��й�ȣ�� Ʋ�Ƚ��ϴ�.";
				demand.demander = null;
			} else if (findUser(chk.getId()))
			{
				result.resultMsg = "���� �������Դϴ�.";
				demand.demander = null;
			} else
			{
				result.resultMsg = chk.getId() + "�α��� ����";
				demand.demander = chk;// ȸ������ ����� �߰�

			}
			oos.writeObject(result);
			break;
		case CHANGE_USER_LOCATION:
			setUserLocation(demand);
			break;
		case CHATTING:
			sendMsg(demand);
			break;
		case UPDATE_USERCHART:
			updateUserChart();
			break;
		case READY:
			result = new Result(demand);

			roomInfo.get(demand.demander.getLocation()).put(demand.demander, true);
			oos.writeObject(result);
			
			if (roomInfo.get(demand.demander.getLocation()).size() == 2)
			{// �� �ο� 2���϶�
				if (startChk(demand.demander.getLocation()))
				{// �Ѵ� ������¶��
					Iterator<MemberDTO> member = roomInfo.get(demand.demander.getLocation()).keySet().iterator();
					MemberDTO player1 = member.next();
					MemberDTO player2 = member.next();
					Result readyResult = new Result(new Demand(player1, READY));// 1p��
																				// �����̶��
																				// �˷���
					readyResult.start = true;// ���ӽ����ߴٰ� �˷���
					readyResult.turn = true;// ù��°����� �� ���� �˷���
					readyResult.comId = player2.getId();// ���� ���̵� �˷���
					readyResult.resultMsg = player2.getId() + "�԰� ������ �����մϴ�.";
					clients.get(player1).writeObject(readyResult);

					readyResult = new Result(new Demand(player1, READY));
					readyResult.start = true;// ���ӽ����ߴٰ� �˷���
					readyResult.turn = false;// ��°���� �ƴ�
					readyResult.comId = player1.getId();// ���� ���̵� �˷���
					readyResult.resultMsg = player1.getId() + "�԰� ������ �����մϴ�.";
					clients.get(player2).writeObject(readyResult);
					
					updateUserChart();//���� �� �������� ������ ����
				}
			}
			break;
		case GAME_START:
			result = new Result(demand);
			result.resultMsg = "���ӽ���";
			oos.writeObject(result);

			break;
		case SENDING_BOARD:// �䱸���� ����뿡�� �䱸���� ���� ������� ����
			Iterator<MemberDTO> member = roomInfo.get(demand.demander.getLocation()).keySet().iterator();
			MemberDTO player1 = member.next();
			MemberDTO player2 = member.next();
			result = new Result(demand);
			result.resultMsg = "���带 �޾ƶ�";
			if (!demand.demander.getId().equals(player1.getId()))
			{// �������� �ٸ��𿡰Ը� �˷��ָ� �ȴ�.
				clients.get(player1).writeObject(result);
			} else
			{
				clients.get(player2).writeObject(result);
			}
			break;
		case TURN_CHANGE:// �䱸���� �� ����, ���� ��
			Iterator<MemberDTO> member_2 = roomInfo.get(demand.demander.getLocation()).keySet().iterator();
			MemberDTO player1_2 = member_2.next();
			MemberDTO player2_2 = member_2.next();
			result = new Result(demand);
			result.resultMsg = "�Ϲٲ���� ^^";
			if (!demand.demander.getId().equals(player1_2.getId()))
			{// �������� �ٸ��𿡰Ը� �˷��ָ� �ȴ�.
				clients.get(player1_2).writeObject(result);
			} else
			{
				clients.get(player2_2).writeObject(result);
			}

			break;
		case END_GAME:// �䱸��(�й���)�� ������ ��û �� ���濡�� �¸��˸�
			Iterator<MemberDTO> member_3 = roomInfo.get(demand.demander.getLocation()).keySet().iterator();
			MemberDTO player1_3 = member_3.next();
			MemberDTO player2_3 = member_3.next();
			result = new Result(demand);
			result.resultMsg = "�¸��ϼ̾�� ^^";
			if (!demand.demander.getId().equals(player1_3.getId()))
			{// �������� �ٸ��𿡰Ը� �˷��ָ� �ȴ�.
				clients.get(player1_3).writeObject(result);
			} else
			{
				clients.get(player2_3).writeObject(result);
			}
			break;
		default:
			result = new Result(demand);
			result.resultMsg = "�߸��� �䱸�Դϴ�.";
			oos.writeObject(result);
			break;
		}

		System.out.println("���� ������ ��� : ");
		System.out.println(clients);

	}

	public void sendMsg(Demand demand)
	{
		Iterator<MemberDTO> member = roomInfo.get(demand.demander.getLocation()).keySet().iterator();
		boolean res = true;
		while (member.hasNext())
		{
			MemberDTO buffer = member.next();
			try
			{
				clients.get(buffer).writeObject(new Result(demand));
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean startChk(int roomNo)
	{
		Iterator<MemberDTO> member = roomInfo.get(roomNo).keySet().iterator();
		boolean res = true;
		while (member.hasNext())
		{
			MemberDTO buffer = member.next();
			res &= roomInfo.get(roomNo).get(buffer);
		}

		return res;
	}

	public boolean findUser(String id)
	{// ���� ������ �Ǻ�
		Iterator<MemberDTO> member = clients.keySet().iterator();
		while (member.hasNext())
		{
			if (member.next().getId().equals(id))
			{
				return true;
			}
		}
		return false;
	}

	public void setUserLocation(Demand demand) throws IOException
	{
		Result result = new Result(demand);

		if (demand.demander.getLocation() == demand.demandLocation)
		{
			result.resultMsg = "�̹� " + demand.demandLocation + "���濡 ��ʴϴ�.";
			return;
		}
		
		if (clients.get(demand.demander) == null)
		{// ù�����̸� Ŭ���̾�Ʈ ��� �߰�
			clients.put(demand.demander, oos);
			System.out.println(demand.demander.getId() + "����");
		} // ù���� ó��
		
		if (roomInfo.get(demand.demandLocation) == null)
		{
			roomInfo.put(demand.demandLocation, new HashMap<>());
			System.out.println(demand.demandLocation + "���� ����");
		}
		
		if (demand.demandLocation == 0)
		{
			removeUserAtLocation(demand.demander);
			roomInfo.get(0).put(demand.demander, false);// �κ�����
			demand.demander.setLocation(0);
			System.out.println(demand.demander.getId() + "�κ� ����");
			result.resultMsg = demand.demander.getId() + "�κ� ����";
			result.resultMsg = "lobby";
			clients.get(demand.demander).writeObject(result);

		} else if (roomInfo.get(demand.demandLocation).size() < 2)
		{
			removeUserAtLocation(demand.demander);
			roomInfo.get(demand.demandLocation).put(demand.demander, false);
			demand.demander.setLocation(demand.demandLocation);
			System.out.println(demand.demander.getId() + demand.demandLocation + "���� ����");
			result.resultMsg = demand.demander.getId() + demand.demandLocation + "���� ����";
			result.resultMsg = "room";
			clients.get(demand.demander).writeObject(result);

		} else
		{
			demand.demandLocation++;
			setUserLocation(demand);
		}

	}

	public void removeUserAtLocation(MemberDTO member)
	{
		if (roomInfo.get(member.getLocation()) != null)
		{
			roomInfo.get(member.getLocation()).remove(member);
			System.out.println(member.getLocation() + "���� " + member.getId() + "�� �����մϴ�.");
			if (roomInfo.get(member.getLocation()).size() == 0)
			{
				roomInfo.remove(member.getLocation());
				System.out.println(member.getLocation() + "���濡 �ƹ��� �����ϴ�. ����");
			}
		}
	}

	public void updateUserChart() throws IOException
	{

		Iterator<MemberDTO> rMember = clients.keySet().iterator();
		while (rMember.hasNext())
		{
			Vector<MemberDTO> userChart = new Vector<>();
			MemberDTO receivingMember = rMember.next();
			Iterator<MemberDTO> sMember = clients.keySet().iterator();
			while (sMember.hasNext())
			{
				MemberDTO sendingMember = sMember.next();
				if (receivingMember.getLocation() == sendingMember.getLocation())
				{
					userChart.add(sendingMember);
				}

			}
			Result result = new Result(new Demand(receivingMember, UPDATE_USERCHART));
			result.userChart = userChart;
			result.resultMsg = "������� �ڵ�����";
			clients.get(receivingMember).writeObject(result);
			clients.get(receivingMember).flush();// Vector(�÷���) flush �������
			clients.get(receivingMember).reset();// reset��
		}
	}
}

public class KnightGameServer
{

	HashMap<MemberDTO, ObjectOutputStream> clients = new HashMap<>();// ��� ������\
	HashMap<Integer, HashMap<MemberDTO, Boolean>> roomInfo = new HashMap<>();

	public KnightGameServer()
	{
		try
		{
			ServerSocket serverSocket = new ServerSocket(12345);

			System.out.println(">> Ŭ���̾�Ʈ�� ������ ��ٸ��ϴ�.");
			while (true)
			{
				Socket socket = serverSocket.accept();
				System.out.println(">> Ŭ���̾�Ʈ ����: " + socket);
				new ServerReceiver(socket, clients, roomInfo).start();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{

		new KnightGameServer();
	}
}
