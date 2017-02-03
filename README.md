### Java 변형체스게임

### 목적
* java언어로 네트워크 기반 체스게임을 개발
* DB와 연동하여 데이터를 관리
* Swing을 사용하여 GUI환경에서 동작
* 사용자들이 2명 이상 모이는 곳은 채팅이 되어야함
* 위 항목들을 개발 해봄으로써 java를 학습함.

### 개발 환경
* [Java SE 1.8](https://www.oracle.com/)
* [Oracle 11g 64bit](https://www.oracle.com/)

### 기능 목록
* 체스게임
* 채팅
* 회원가입, 로그인, ID/Pw찾기, 패스워드 변경


### 회원관련 화면
프로그램 실행시 나타나는 로그인, 회원가입, Id/pw 찾기 화면

![signin](https://dl.dropbox.com/s/hdlpm33e6ftuzo0/signin.png)
<p style="text-align:center">그림1. 로그인 화면</p>

![signup](https://dl.dropbox.com/s/14ev6rn2m8jf140/signup.png)
<p style="text-align:center">그림2. 회원가입 화면</p>

![find_id](https://dl.dropbox.com/s/8typa6zo1qtgsi9/find_id.png)
<p style="text-align:center">그림3. Id/Pw찾기 화면</p>

### 게임 화면
로그인 시 나타나는 게임로비, 개인정보변경, 게임 방, 게임 화면

![lobby](https://dl.dropbox.com/s/gpihlpls2j7xrqg/lobby.png)
<p style="text-align:center">그림4. 게임로비 화면</p>

![modify_info](https://dl.dropbox.com/s/x3gtw165gdlpw9q/modify_info.png)
<p style="text-align:center">그림5. 개인정보변경 화면</p>

![room](https://dl.dropbox.com/s/yb8pgj07ult1tmh/room.png)
<p style="text-align:center">그림5. 게임 방 화면</p>

![game](https://dl.dropbox.com/s/834prpungy784z8/game.png)
<p style="text-align:center">그림5. 게임 화면</p>

### DB
* 기본키는 시퀀스.nextval로 회원의 고유번호를 부여합니다.
```sql
CREATE TABLE "USER"."MEMBER"
(
   "NUM" NUMBER,
   "ID" VARCHAR2(25 BYTE) NOT NULL,
   "PW" VARCHAR2(25 BYTE) NOT NULL,
   "email" VARCHAR2(50 BYTE) NOT NULL,
   "regdate" VARCHAR2(8 BYTE) NOT NULL
   CONSTRAINT PK_USERS PRIMARY KEY(NUM)
);
```

### 주요 클래스 설명
변형체스게임을 구성하는 클래스에 대한 설명입니다.
MVC에 대한 개념이 없을 때 개발되어 java코드내에 SQL문이 직접 하드코딩 되어 있습니다.
데이터베이스 접속은 ojdbc6을 라이브러리에 추가하여 연동합니다.
READEME.md가 너무 길어지는 것 같아 주요 소스(Server, Client)만 설명드린 점 양해 부탁드립니다.
전체 소스를 보고 싶으신분은 [github](https://github.com/hhk2745/JavaProject_Movie-KnightGame) 저장소에서 확인 하실 수 있습니다. 로컬에서 동작하는 프로그램이므로 Oracle 설치, 테이블 생성 후 실행 시켜야 합니다.

* 사용자의 상태를 나타내는 DemandChart 인터페이스 입니다.
```java
public interface DemandChart
{
	public static final int LOGOUT = -2;
	public static final int LOGIN = 0;
	public static final int CHANGE_USER_LOCATION = 1;
	public static final int CHATTING = 2;
	public static final int UPDATE_USERCHART = 3;
	public static final int READY = 4;
	public static final int GAME_START = 5;
	public static final int SENDING_BOARD = 6;
	public static final int TURN_CHANGE = 7;
	public static final int END_GAME = 8;
}
```

* Server

```java
class ServerReceiver extends Thread implements DemandChart
{
	HashMap<MemberDTO, ObjectOutputStream> clients;
	HashMap<Integer, HashMap<MemberDTO, Boolean>> roomInfo;
	ObjectOutputStream oos;
	ObjectInputStream ois;
  public ServerReceiver(Socket socket, HashMap<MemberDTO, ObjectOutputStream> clients,
			HashMap<Integer, HashMap<MemberDTO, Boolean>> roomInfo){}
  @Override
	public void run(){}
  synchronized public void process(Demand demand) throws IOException{}

  public class KnightGameServer
  {
  	HashMap<MemberDTO, ObjectOutputStream> clients = new HashMap<>();
  	HashMap<Integer, HashMap<MemberDTO, Boolean>> roomInfo = new HashMap<>();

  	public KnightGameServer()
  	{
  		try
  		{
  			ServerSocket serverSocket = new ServerSocket(12345);
  			while (true)
  			{
  				Socket socket = serverSocket.accept();
  				System.out.println(">> Accept LOG :::: " + socket);
  				new ServerReceiver(socket, clients, roomInfo).start();
  			}
  		} catch (Exception e){e.printStackTrace();}
  	}
  	public static void main(String[] args)
  	{
  		new KnightGameServer();
  	}
  }
}
```

* Client

```Java
public class KnightGameClient implements DemandChart, KnightGameInfo
{
	public ClientSender sender;
	public ClientReceiver receiver;
	public ObjectOutputStream oos;
	public ObjectInputStream ois;
	public MemberDTO user;
	public MemberDAO dao;
	public LoginGUI login;
	public LobbyGUI lobby;
	public RoomGUI room;
	public KnightGameMain main;
	public JTextArea chatArea;
	public JTextField chat;
	public JButton logout;
	public Vector<MemberDTO> userChart;
	public boolean start = false;

	public KnightGameClient(){}
	class ClientSender{}
	class ClientReceiver extends Thread
  {
		public ObjectInputStream ois;
		public ClientReceiver(ObjectInputStream ois){}
		@Override
		public void run(){}
		public void process(Result result) throws IOException{}
	}

	LoginGUI newLogin(String str) throws IOException
  {
		login.loginBtn.addActionListener(new ActionListener() {});
		return login;
	}

	public static void main(String[] args)
  {
		new KnightGameClient();
	}
}
```

### 후기

위와 같이 java로 소켓 프로그래밍을 개발해 보았습니다.
객체직렬화를 하기 위해 Serializable 인터페이스를 구현 하였고,
ObjectInputStream,ObjectOutputStream클래스는 readObject(), writeObject()를 통해 객체를 입출력 하기 위해 사용되었습니다.
게임 방에 입장하는 부분에서 동기화가 필요했는데 ArrayList에 DTO를 담아서 전송하니 리스트 내의 모든 DTO가 index 0번의 정보로 복사되어 애를 먹었습니다... 레퍼런스를 참조하여 동기화를 보장하지 않는 ArrayList 대신에 Vector클래스를 권장 하고 있어 해결할 수 있었습니다.
디자인패턴에서 Command 패턴을 흉내내서 모든 명령(Demand)를 캡슐화 하여 설계해 보았습니다.
