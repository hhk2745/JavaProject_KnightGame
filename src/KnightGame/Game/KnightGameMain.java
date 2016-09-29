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

	public Player player;// �÷��̾�
	public Player com;// ���
	public GameGUI gameGUI;// GUI
	public OriginalForm selected;// ������ ��
	public int selectedX, selectedY;// �������� ��ǥ.(moved ����)
	public boolean gaming;// ���� ���� = true ���� �������� = false (Thread �������)
	public boolean myTurn;// ��
	public boolean turnChange;
	public boolean lose;

	public MemberDTO user;
	public ObjectOutputStream oos;

	public KnightGameMain(String playerId, String comId, MemberDTO user, ObjectOutputStream oos) {
		this.user = user;
		this.oos = oos;
		player = new Player(playerId);// �÷��̾��
		gameGUI = new GameGUI();// GUI ����
		gaming = true;// ������ �����ߴٰ� �˸�
		
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
					gameGUI.allVisibleFalse();//��ε� ����
					selected.repaint();
				} // ���� ������ �� �̹��� ���󺹱�
				
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
				
				int a = JOptionPane.showConfirmDialog(null, "�׺��Ͻðڽ��ϱ�?", "Ȯ��",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (a == 0)
				{
					lose = true;
				}
				
			}
		});
		// -----------------------------------------------------------------
		com = new Player(comId);
		for (int i = 0; i < ver; i++) {// ���۽ÿ� ��븻 ����Ʈ�� ����
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

		setGUI(player.board);// �г��� �����Ѵ�

		gameGUI.setObstacleBtn.setEnabled(false);
		gameGUI.lose.setEnabled(false);
		gameGUI.endTurn.setEnabled(false);
		myTurn = false;

	}
	
	public void dispRoute(OriginalForm piece){//�̵����� ���ǥ��
		if (piece != null) {
			if (piece instanceof Pawn) {
				Pawn pawnBuffer = (Pawn)piece;
				for(int i=0; i<pawnBuffer.moveAbleDst.length; i++){
					int chkX = pawnBuffer.locationX + pawnBuffer.moveAbleDst[i][X];
					int chkY = pawnBuffer.locationY + pawnBuffer.moveAbleDst[i][Y];
					if (chkX >= 0 && chkX < ver && chkY >= 0 && chkY < hor) {
						if (pawnBuffer.destinationChk(chkX, chkY)) {
							gameGUI.checker[chkY][chkX].setVisible(true);// xy����
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
							gameGUI.checker[chkY][chkX].setVisible(true);// xy����
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
			System.out.println("��ֹ� ��ġ �Ұ�");
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
		System.out.println("��ֹ� ��ġ �Ұ�");
		return false;
	}

	public void setGUI(OriginalForm[][] board) {
		JPanel buffer = new BoardPanel();// ü���� ����
		for (int i = 0; i < ver; i++) {
			for (int j = 0; j < hor; j++) {
				if (board[i][j] != null) {
					OriginalForm piece = board[i][j];
					board[i][j].setBounds(i * distance, j * distance, distance, distance);
					if (piece instanceof Obstacle) {

					} 
					else if (piece.player.name.equals(com.name)) {// ��븻�̶��
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
								gameGUI.allVisibleFalse();//������ǥ �ʱ�ȭ
								//////////////////////////////////////////////////////////////
								if (piece.player.name.equals(player.name)) {// ������ �̺�Ʈ ���
									if (selected != null && !selected.isMoved) {
										if (selected instanceof Knight) {
											selected.icon = new ImageIcon("game_images/knightImg.png");
										}
										if (selected instanceof Pawn) {
											selected.icon = new ImageIcon("game_images/PawnImg.png");
										}
										selected.repaint();
									} // ���� ������ �� �̹��� ���󺹱�

									if (myTurn) {
										selected = piece;
										if (selected.isMoved) {// �̹� �����θ� ���� �Ұ�
											selected = null;
										} else {
											System.out.println(piece.name + "����");

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
					gameGUI.allVisibleFalse();//������ǥ �ʱ�ȭ
					/////////////////////////////////////////////////////
					Demand demand = new Demand(user, SENDING_BOARD);
					
					if(selected instanceof Obstacle){
						if(setObstacle(selectedX, selectedY)){
						//��ֹ� ��ġ���������� Ŭ���ߴٸ� ��ֹ��� ��ġ�ϰ� true�� �����Ͽ� ���� ������ ����
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
							if (selected.isMoved) {// �����ӿ� ������ ����
								oos.writeObject(demand);// ����
								selected = null;// ���ñ⹰ �ʱ�ȭ
								gameGUI.setObstacleBtn.setEnabled(false);
								//�� �Ѹ����� �����ϰ�� ��ֹ� ��ġ �Ұ�
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
		gameGUI.repaint();// �г� �ٲ�ġ��

		System.out.println(chkAlive() + "���� �������");
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
		System.out.println("������Ȳ");
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
	public void run() {// �������� ������
		while (gaming) {
			////////////////////////////////////////////////////////////////////////////////
			if (lose) {
				try {
					Demand demand = new Demand(user, CHATTING);
					demand.demandMsg = "�й�" + "\n" + com.name + " : �¸�\n�κ�� �̵��մϴ�.";
					oos.writeObject(demand);
					lose = true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Demand demand = new Demand(user, END_GAME);// ������ ���濡�� �������� �˷���
				try {
					oos.writeObject(demand);
					JOptionPane.showMessageDialog(null, "���ӿ��� �й��ϼ̽��ϴ�. �κ�� �̵��մϴ�.");
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
				if (myTurn) {// ������ ������ �Ѿ�� �����̶��
					System.out.println("�� ����");
					gameGUI.allVisibleFalse();
					gameGUI.setObstacleBtn.setEnabled(false);
					gameGUI.lose.setEnabled(false);
					gameGUI.endTurn.setEnabled(false);
					myTurn = false;
					//////////////////////////////////////////////////////////////////
					try {
						Demand demand = new Demand(user, TURN_CHANGE);
						oos.writeObject(demand);// ���� �ٲ���ٴ� ��Ǹ� �˷��ָ� �ǹǷ�
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// ----------------------------------------------------------------
					for (int i = 0; i < 4; i++) {
						if (!player.pawn[i].isDeath) {
							player.pawn[i].isMoved = false;// �̵�ǥ�� �ʱ�ȭ
							player.pawn[i].icon = new ImageIcon("game_images/pawnImg.png");
						}
					}
					for (int i = 0; i < 3; i++) {

						if (!player.knight[i].isDeath) {
							player.knight[i].isMoved = false;// �̵�ǥ�� �ʱ�ȭ
							player.knight[i].icon = new ImageIcon("game_images/knightImg.png");
						}
					}
					// -----------------------------------------------------------------

					setGUI(player.board);

				} else {// ������� ������ ������ �Դٸ�
					System.out.println("�� ����");
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
		// new KnightGameMain("���¿�","����").start();
	}

}
