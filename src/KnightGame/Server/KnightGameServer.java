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

	HashMap<MemberDTO, ObjectOutputStream> clients;// 모든 접속자\
	HashMap<Integer, HashMap<MemberDTO, Boolean>> roomInfo;

	ObjectOutputStream oos;// 출구
	ObjectInputStream ois;// 입구

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
				demand = (Demand) ois.readObject();// 서버로부터 받은 요청을 읽어드린다
				System.out.println("사이클───────────────────────");
				System.out.println("요구자 : " + demand.demander);
				System.out.println("요구    : " + demand.demandMsg);
				process(demand);// 처리해라
				System.out.println("───────────────────────────");

			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} finally
		{
			if(roomInfo.get(demand.demander.getLocation()).get(demand.demander) != null &&
			   roomInfo.get(demand.demander.getLocation()).get(demand.demander)){//게임중이던놈이 종료했다면
				System.out.println(demand.demander.getId() + "로그아웃");
				removeUserAtLocation(demand.demander);// 접속자 위치에서 제거
				clients.remove(demand.demander);// 접속자 목록에서 제거
				Iterator<MemberDTO> member = roomInfo.get(demand.demander.getLocation()).keySet().iterator();
				MemberDTO player = member.next();
				Result result = new Result(new Demand(player, END_GAME));
				result.resultMsg = "상대방의 빡종";
				try {
					clients.get(player).writeObject(result);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				System.out.println(demand.demander.getId() + "로그아웃");
				removeUserAtLocation(demand.demander);// 접속자 위치에서 제거
				clients.remove(demand.demander);// 접속자 목록에서 제거
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
			result.resultMsg = demand.demander.getId() + "로그아웃";
			removeUserAtLocation(demand.demander);// 접속자 위치에서 제거
			clients.remove(demand.demander);// 접속자 목록에서 제거
			oos.writeObject(result);
			break;
		case LOGIN:
			result = new Result(demand);
			MemberDTO chk = new MemberDAO().loginChk(demand.demander.getId(), demand.demander.getPw());
			System.out.println(clients.get(chk));
			if (chk == null)
			{
				result.resultMsg = "아이디 또는 비밀번호가 틀렸습니다.";
				demand.demander = null;
			} else if (findUser(chk.getId()))
			{
				result.resultMsg = "현재 접속중입니다.";
				demand.demander = null;
			} else
			{
				result.resultMsg = chk.getId() + "로그인 성공";
				demand.demander = chk;// 회원정보 결과에 추가

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
			{// 방 인원 2명일때
				if (startChk(demand.demander.getLocation()))
				{// 둘다 레디상태라면
					Iterator<MemberDTO> member = roomInfo.get(demand.demander.getLocation()).keySet().iterator();
					MemberDTO player1 = member.next();
					MemberDTO player2 = member.next();
					Result readyResult = new Result(new Demand(player1, READY));// 1p가
																				// 선턴이라고
																				// 알려줌
					readyResult.start = true;// 게임시작했다고 알려줌
					readyResult.turn = true;// 첫번째놈부터 턴 시작 알려줌
					readyResult.comId = player2.getId();// 상대방 아이디 알려줌
					readyResult.resultMsg = player2.getId() + "님과 게임을 시작합니다.";
					clients.get(player1).writeObject(readyResult);

					readyResult = new Result(new Demand(player1, READY));
					readyResult.start = true;// 게임시작했다고 알려줌
					readyResult.turn = false;// 둘째놈턴 아님
					readyResult.comId = player1.getId();// 상대방 아이디 알려줌
					readyResult.resultMsg = player1.getId() + "님과 게임을 시작합니다.";
					clients.get(player2).writeObject(readyResult);
					
					updateUserChart();//게임 내 유저상태 갱신을 위해
				}
			}
			break;
		case GAME_START:
			result = new Result(demand);
			result.resultMsg = "게임시작";
			oos.writeObject(result);

			break;
		case SENDING_BOARD:// 요구자의 대결상대에게 요구자의 보드 변경사항 전송
			Iterator<MemberDTO> member = roomInfo.get(demand.demander.getLocation()).keySet().iterator();
			MemberDTO player1 = member.next();
			MemberDTO player2 = member.next();
			result = new Result(demand);
			result.resultMsg = "보드를 받아라";
			if (!demand.demander.getId().equals(player1.getId()))
			{// 같은방의 다른놈에게만 알려주면 된다.
				clients.get(player1).writeObject(result);
			} else
			{
				clients.get(player2).writeObject(result);
			}
			break;
		case TURN_CHANGE:// 요구자의 턴 종료, 상대방 턴
			Iterator<MemberDTO> member_2 = roomInfo.get(demand.demander.getLocation()).keySet().iterator();
			MemberDTO player1_2 = member_2.next();
			MemberDTO player2_2 = member_2.next();
			result = new Result(demand);
			result.resultMsg = "턴바꼈어요 ^^";
			if (!demand.demander.getId().equals(player1_2.getId()))
			{// 같은방의 다른놈에게만 알려주면 된다.
				clients.get(player1_2).writeObject(result);
			} else
			{
				clients.get(player2_2).writeObject(result);
			}

			break;
		case END_GAME:// 요구자(패배자)가 턴종료 요청 시 상대방에게 승리알림
			Iterator<MemberDTO> member_3 = roomInfo.get(demand.demander.getLocation()).keySet().iterator();
			MemberDTO player1_3 = member_3.next();
			MemberDTO player2_3 = member_3.next();
			result = new Result(demand);
			result.resultMsg = "승리하셨어요 ^^";
			if (!demand.demander.getId().equals(player1_3.getId()))
			{// 같은방의 다른놈에게만 알려주면 된다.
				clients.get(player1_3).writeObject(result);
			} else
			{
				clients.get(player2_3).writeObject(result);
			}
			break;
		default:
			result = new Result(demand);
			result.resultMsg = "잘못된 요구입니다.";
			oos.writeObject(result);
			break;
		}

		System.out.println("현재 접속자 목록 : ");
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
	{// 현재 접속중 판별
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
			result.resultMsg = "이미 " + demand.demandLocation + "번방에 계십니다.";
			return;
		}
		
		if (clients.get(demand.demander) == null)
		{// 첫접속이면 클라이언트 목록 추가
			clients.put(demand.demander, oos);
			System.out.println(demand.demander.getId() + "접속");
		} // 첫접속 처리
		
		if (roomInfo.get(demand.demandLocation) == null)
		{
			roomInfo.put(demand.demandLocation, new HashMap<>());
			System.out.println(demand.demandLocation + "번방 생성");
		}
		
		if (demand.demandLocation == 0)
		{
			removeUserAtLocation(demand.demander);
			roomInfo.get(0).put(demand.demander, false);// 로비접속
			demand.demander.setLocation(0);
			System.out.println(demand.demander.getId() + "로비 접속");
			result.resultMsg = demand.demander.getId() + "로비 접속";
			result.resultMsg = "lobby";
			clients.get(demand.demander).writeObject(result);

		} else if (roomInfo.get(demand.demandLocation).size() < 2)
		{
			removeUserAtLocation(demand.demander);
			roomInfo.get(demand.demandLocation).put(demand.demander, false);
			demand.demander.setLocation(demand.demandLocation);
			System.out.println(demand.demander.getId() + demand.demandLocation + "번방 접속");
			result.resultMsg = demand.demander.getId() + demand.demandLocation + "번방 접속";
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
			System.out.println(member.getLocation() + "에서 " + member.getId() + "를 제거합니다.");
			if (roomInfo.get(member.getLocation()).size() == 0)
			{
				roomInfo.remove(member.getLocation());
				System.out.println(member.getLocation() + "번방에 아무도 없습니다. 제거");
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
			result.resultMsg = "유저목록 자동갱신";
			clients.get(receivingMember).writeObject(result);
			clients.get(receivingMember).flush();// Vector(컬렉션) flush 해줘야함
			clients.get(receivingMember).reset();// reset도
		}
	}
}

public class KnightGameServer
{

	HashMap<MemberDTO, ObjectOutputStream> clients = new HashMap<>();// 모든 접속자\
	HashMap<Integer, HashMap<MemberDTO, Boolean>> roomInfo = new HashMap<>();

	public KnightGameServer()
	{
		try
		{
			ServerSocket serverSocket = new ServerSocket(12345);

			System.out.println(">> 클라이언트의 접속을 기다립니다.");
			while (true)
			{
				Socket socket = serverSocket.accept();
				System.out.println(">> 클라이언트 접속: " + socket);
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
