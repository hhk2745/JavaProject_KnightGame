package KnightGame.Game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import KnightGame.DemandChart;
import KnightGame.MemberDTO;
import KnightGame.Client.Demand;

public class KnightGameMain extends Thread implements KnightGameInfo, DemandChart {

	public Player player;// 플레이어
	public Player com;// 상대
	public GameGUI gameGUI;// GUI
	public OriginalForm selected;// 선택한 말
	public int selectedX, selectedY;// 보드위의 좌표.(moved 갱신)
	public boolean gaming;// 게임 시작 = true 부터 게임종료 = false (Thread 종료시점)
	public boolean myTurn;// 턴
	public boolean turnChange;
	public boolean lose;

	public MemberDTO user;
	public ObjectOutputStream oos;

	public KnightGameMain(String playerId, String comId, MemberDTO user, ObjectOutputStream oos) {
		this.user = user;
		this.oos = oos;
		player = new Player(playerId);// 플레이어와
		gameGUI = new GameGUI();// GUI 생성
		gaming = true;// 게임을 시작했다고 알림
		
		gameGUI.setObstacleBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (selected != null && !selected.isMoved) {
					if (selected instanceof Knight) {
						selected.icon = new ImageIcon("game_images/knightImg.png");
					}
					if (selected instanceof Pawn) {
						selected.icon = new ImageIcon("game_images/PawnImg.png");
					}
					gameGUI.allVisibleFalse();//경로도 없앰
					selected.repaint();
				} // 전에 선택한 말 이미지 원상복구
				
				selected = new Obstacle();
			}
		});
		gameGUI.endTurn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				turnChange = true;
				gameGUI.endTurn.setEnabled(false);
			}
		});
		gameGUI.lose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				int a = JOptionPane.showConfirmDialog(null, "항복하시겠습니까?", "확인",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (a == 0)
				{
					lose = true;
				}
				
			}
		});
		// -----------------------------------------------------------------
		com = new Player(comId);
		for (int i = 0; i < ver; i++) {// 시작시에 상대말 디폴트값 지정
			for (int j = 0; j < hor; j++) {
				if (com.board[i][j] != null) {
					com.board[i][j].locationX = ver - 1 - i;
					com.board[i][j].locationY = hor - 1 - j;
					player.board[ver - 1 - i][hor - 1 - j] = com.board[i][j];
				}
			}
		}
		// -----------------------------------------------------------------

		dispBoard(player.board);
		dispBoard(com.board);

		setGUI(player.board);// 패널을 갱신한다

		gameGUI.setObstacleBtn.setEnabled(false);
		gameGUI.lose.setEnabled(false);
		gameGUI.endTurn.setEnabled(false);
		myTurn = false;

	}
	
	public void dispRoute(OriginalForm piece){//이동가능 경로표시
		if (piece != null) {
			if (piece instanceof Pawn) {
				Pawn pawnBuffer = (Pawn)piece;
				for(int i=0; i<pawnBuffer.moveAbleDst.length; i++){
					int chkX = pawnBuffer.locationX + pawnBuffer.moveAbleDst[i][X];
					int chkY = pawnBuffer.locationY + pawnBuffer.moveAbleDst[i][Y];
					if (chkX >= 0 && chkX < ver && chkY >= 0 && chkY < hor) {
						if (pawnBuffer.destinationChk(chkX, chkY)) {
							gameGUI.checker[chkY][chkX].setVisible(true);// xy반전
						}
					}
				}
			} else if (piece instanceof Knight) {
				Knight KnightBuffer = (Knight)piece;
				for(int i=0; i<KnightBuffer.moveAbleDst.length; i++){
					int chkX = KnightBuffer.locationX + KnightBuffer.moveAbleDst[i][X];
					int chkY = KnightBuffer.locationY + KnightBuffer.moveAbleDst[i][Y];
					if (chkX >= 0 && chkX < ver && chkY >= 0 && chkY < hor) {
						if (KnightBuffer.routeChk(chkX, chkY) && KnightBuffer.destinationChk(chkX, chkY)){
							gameGUI.checker[chkY][chkX].setVisible(true);// xy반전
						}
					}
				}
			} else if (piece instanceof Obstacle) {

			}
		}
	}

	public void enemyMove(int beforeX, int beforeY, int afterX, int afterY) {
		player.board[beforeX][beforeY].moving(afterX, afterY);
		player.board[afterX][afterY] = player.board[beforeX][beforeY];
		player.board[beforeX][beforeY] = null;
	}
	
	public boolean setObstacle(int x, int y){
		if(player.board[x][y]!=null){
			System.out.println("장애물 설치 불가");
			return false;
		}
		Obstacle obstacle = new Obstacle();
		boolean res = true;
		for(int i=0; i<obstacle.setAbleDst.length; i++){
			int chkX = x+obstacle.setAbleDst[i][X];//0~ ver-1
			int chkY = y+obstacle.setAbleDst[i][Y];//0~ hor-1
			if((chkX>=0 && chkX<ver) && (chkY>=0 && chkY<hor)){
				if(player.board[chkX][chkY] instanceof Obstacle){
					res = false;
				}
			}
		}
		if(res){
			player.board[x][y] = obstacle;
			selected = null;
			setGUI(player.board);
			return true;
		}
		System.out.println("장애물 설치 불가");
		return false;
	}

	public void setGUI(OriginalForm[][] board) {
		JPanel buffer = new BoardPanel();// 체스판 생성
		for (int i = 0; i < ver; i++) {
			for (int j = 0; j < hor; j++) {
				if (board[i][j] != null) {
					OriginalForm piece = board[i][j];
					board[i][j].setBounds(i * distance, j * distance, distance, distance);
					if (piece instanceof Obstacle) {

					} 
					else if (piece.player.name.equals(com.name)) {// 상대말이라면
						if (piece instanceof Knight) {
							piece.icon = new ImageIcon("game_images/knightImg2.png");
						}
						if (piece instanceof Pawn) {
							piece.icon = new ImageIcon("game_images/PawnImg2.png");
						}
						piece.repaint();
					}

					buffer.add(board[i][j]);

					if (piece instanceof Obstacle) {

					} 
					else if (piece.player.name.equals(player.name))
						piece.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent e) {
								//////////////////////////////////////////////////////////////
								gameGUI.allVisibleFalse();//선택좌표 초기화
								//////////////////////////////////////////////////////////////
								if (piece.player.name.equals(player.name)) {// 내말만 이벤트 등록
									if (selected != null && !selected.isMoved) {
										if (selected instanceof Knight) {
											selected.icon = new ImageIcon("game_images/knightImg.png");
										}
										if (selected instanceof Pawn) {
											selected.icon = new ImageIcon("game_images/PawnImg.png");
										}
										selected.repaint();
									} // 전에 선택한 말 이미지 원상복구

									if (myTurn) {
										selected = piece;
										if (selected.isMoved) {// 이미 움직인말 선택 불가
											selected = null;
										} else {
											System.out.println(piece.name + "선택");

											if (selected instanceof Knight) {
												selected.icon = new ImageIcon("game_images/knightSelectedImg.png");
											}
											if (selected instanceof Pawn) {
												selected.icon = new ImageIcon("game_images/PawnSelectedImg.png");
											}
											selected.repaint();
											//////////////////////////////////////////////////////////////////////////////
											dispRoute(selected);
										}
									}

								}
							}
						});
				}
			}
		}

		buffer.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				selectedX = e.getX() / distance;
				selectedY = e.getY() / distance;
			}
		});
		buffer.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (myTurn && selected != null) {
					/////////////////////////////////////////////////////
					gameGUI.allVisibleFalse();//선택좌표 초기화
					/////////////////////////////////////////////////////
					Demand demand = new Demand(user, SENDING_BOARD);
					
					if(selected instanceof Obstacle){
						if(setObstacle(selectedX, selectedY)){
						//장애물 설치가능지역을 클릭했다면 장애물을 설치하고 true를 리턴하여 다음 사항을 실행
							try {
								demand.isObstacle = true;
								demand.afterX = selectedX;
								demand.afterY = selectedY;
								oos.writeObject(demand);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							turnChange = true;
						}
					} else {

						demand.beforeX = selected.locationX;
						demand.beforeY = selected.locationY;

						selected.moving(selectedX, selectedY);

						demand.afterX = selectedX;
						demand.afterY = selectedY;

						///////////////////////////////////////////////////////////
						try {
							Thread.sleep(50);
							if (selected.isMoved) {// 움직임에 성공한 말만
								oos.writeObject(demand);// 전송
								selected = null;// 선택기물 초기화
								gameGUI.setObstacleBtn.setEnabled(false);
								//말 한마리라도 움직일경우 장애물 설치 불가
								setGUI(player.board);
							}
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					///////////////////////////////////////////////////////////
				}
			}
		});

		gameGUI.gameBoard.setVisible(false);
		gameGUI.gameBoard = buffer;
		gameGUI.add(gameGUI.gameBoard);
		gameGUI.repaint();// 패널 바꿔치기

		System.out.println(chkAlive() + "마리 살아있음");
		if (chkAlive() == 0) {
			lose = true;
		}

	}

	public int chkAlive() {
		int liveCnt = 0;
		for (int i = 0; i < ver; i++) {
			for (int j = 0; j < hor; j++) {
				if (player.board[i][j] != null && !(player.board[i][j] instanceof Obstacle)) {
					if (player.board[i][j].player.name.equals(player.name)) {
						liveCnt++;
					}
				}

			}
		}
		return liveCnt;
	}

	public void dispBoard(OriginalForm[][] board) {
		System.out.println("보드현황");
		for (int i = 0; i < ver; i++) {
			for (int j = 0; j < hor; j++) {
				if (board[i][j] == null || (board[i][j] instanceof Obstacle))
					System.out.print(board[i][j] + "\t\t");
				else
					System.out.print(board[i][j] + "\t");

			}
			System.out.println();
		}
	}

	@Override
	public void run() {// 게임진행 쓰레드
		while (gaming) {
			////////////////////////////////////////////////////////////////////////////////
			if (lose) {
				try {
					Demand demand = new Demand(user, CHATTING);
					demand.demandMsg = "패배" + "\n" + com.name + " : 승리\n로비로 이동합니다.";
					oos.writeObject(demand);
					lose = true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Demand demand = new Demand(user, END_GAME);// 진놈이 상대방에게 게임종료 알려줌
				try {
					oos.writeObject(demand);
					JOptionPane.showMessageDialog(null, "게임에서 패배하셨습니다. 로비로 이동합니다.");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				gaming = false;
			}
			////////////////////////////////////////////////////////////////////////////////
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (turnChange) {
				turnChange = false;
				if (myTurn) {// 내턴이 끝나고 넘어가는 순간이라면
					System.out.println("턴 종료");
					gameGUI.allVisibleFalse();
					gameGUI.setObstacleBtn.setEnabled(false);
					gameGUI.lose.setEnabled(false);
					gameGUI.endTurn.setEnabled(false);
					myTurn = false;
					//////////////////////////////////////////////////////////////////
					try {
						Demand demand = new Demand(user, TURN_CHANGE);
						oos.writeObject(demand);// 턴이 바뀌었다는 사실만 알려주면 되므로
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// ----------------------------------------------------------------
					for (int i = 0; i < 4; i++) {
						if (!player.pawn[i].isDeath) {
							player.pawn[i].isMoved = false;// 이동표시 초기화
							player.pawn[i].icon = new ImageIcon("game_images/pawnImg.png");
						}
					}
					for (int i = 0; i < 3; i++) {

						if (!player.knight[i].isDeath) {
							player.knight[i].isMoved = false;// 이동표시 초기화
							player.knight[i].icon = new ImageIcon("game_images/knightImg.png");
						}
					}
					// -----------------------------------------------------------------

					setGUI(player.board);

				} else {// 상대턴이 끝나고 내턴이 왔다면
					System.out.println("턴 시작");
					gameGUI.setObstacleBtn.setEnabled(true);
					gameGUI.lose.setEnabled(true);
					gameGUI.endTurn.setEnabled(true);
					myTurn = true;
				}
			}
		}
		try {
			gameGUI.dispose();
			Demand demand = new Demand(user, CHANGE_USER_LOCATION);
			demand.demandLocation = 0;
			oos.writeObject(demand);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// new KnightGameMain("오승원","고동민").start();
	}

}
