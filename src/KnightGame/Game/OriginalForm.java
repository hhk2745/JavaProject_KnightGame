package KnightGame.Game;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

interface Move {
	public void moving(int x, int y);
	public boolean destinationChk(int x, int y);
}
interface Route {
	public boolean routeChk(int x, int y);
}

public abstract class OriginalForm extends JLabel implements Move, KnightGameInfo{
	public Player player;
	public String name;
	public OriginalForm[][] board;
	public ImageIcon icon;
	
	public boolean isMoved;
	public boolean isDeath = false;
	
	public boolean routeAble;
	public boolean arriveAble;
	public int locationX, locationY;
	public int[][] moveAbleDst;
}

abstract class PawnForm extends OriginalForm{
	public static final int [][] moveAbleDst = new int[][]{
		{1,0},	{-1,0},	{0,1},	{0,-1} };//도착가능한 좌표 변위
}
abstract class KnightForm extends OriginalForm implements Route{
	public static final int [][] moveAbleDst = new int[][]{//도착가능한 좌표 변위
		{1,-2},{2,-1},	{2,1},{1,2},
		{-1,2},{-2,1},	{-2,-1},{-1,-2} };
	public static final  int [][] routeAbleDst = new int[][]{
		{1,0},	{-1,0},	{0,1},	{0,-1} };//지나칠수 있는 경로 좌표 변위
}
abstract class ObstacleForm extends OriginalForm{
	public static final int [][] setAbleDst = new int[][]{//도착가능한 좌표 변위
		{-1,-1},{0,-1},	{1,-1},
		{-1,0},  		{1,0},
		{-1,1},	{0,1},  {1,1} };
}

class Obstacle extends ObstacleForm{

	public Obstacle(){
		icon = new ImageIcon("game_images/Obstacle.png");
	}
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		g.drawImage(icon.getImage(), 0, 0, null);
		setOpaque(false);
		super.paintComponent(g);
	}
	
	@Override
	public void moving(int x, int y) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean destinationChk(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String toString() {
		return "장애물";
	}
}

class Pawn extends PawnForm {
	public Pawn (OriginalForm[][] board, Player player){
		this.board = board;
		this.player = player;
		icon = new ImageIcon("game_images/pawnImg.png");
			
	}
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		g.drawImage(icon.getImage(), 0, 0, null);
		setOpaque(false);
		super.paintComponent(g);
	}
	
	@Override
	public void moving(int x, int y) {
		// TODO Auto-generated method stub
		if (!isMoved) {
			
			if(board[x][y] instanceof Obstacle){
				System.out.println("장애물이 있습니다. 이동 불가.");
				return;
			}
			
			if (destinationChk(x, y)) {
				if (board[x][y] != null && board[x][y].player == this.player) {// 아군말을  만나면
					System.out.println("아군말이 가로막고 있습니다.");
					return;
				} else {
					board[locationX][locationY] = null;
					this.locationX = x;
					this.locationY = y;
					board[x][y] = this;
					System.out.println(name + "이동 성공");
					isMoved = true;
				}
			}
		}else{
			System.out.println("이미 움직인 말입니다");
		}
	}

	@Override
	public boolean destinationChk(int x, int y) {
		// TODO Auto-generated method stub
		for (int i = 0; i < moveAbleDst.length; i++) {
			if (moveAbleDst[i][0] == x - locationX && moveAbleDst[i][1] == y - locationY) {
				///////////////////////////////////////////////////////////////
				if (board[x][y] != null) {
					if (board[x][y] instanceof Obstacle) {
						System.out.println("장애물이 가로막고 있습니다. 이동 불가");
						return false;
					} else if (board[x][y].player.name.equals(player.name)) {
						System.out.println("아군말이 가로막고 있습니다. 이동 불가");
						return false;
					}
				}
				////////////////////////////////////////////////////////////////
				System.out.println(name + "이 " + x + "," + y + "로 이동을 시도합니다.");
				return true;
			}
		}
		System.out.println(name + "(" + x + "," + y +")이동 불가능한 경로입니다.");
		return false;
	}
	@Override
	public String toString() {
		return name+"( "+player.name+" )";
	}
	
}

class Knight extends KnightForm {
	public Knight (OriginalForm[][] board, Player player){
		this.board = board;
		this.player = player;
		icon = new ImageIcon("game_images/knightImg.png");
	}
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		g.drawImage(icon.getImage(), 0, 0, null);
		setOpaque(false);
		super.paintComponent(g);
	}
	@Override
	public void moving(int x, int y) {
		// TODO Auto-generated method stub
		if (!isMoved) {
			

			if(board[x][y] instanceof Obstacle){
				System.out.println("장애물이 있습니다. 이동 불가.");
				return;
			}
			
			if (destinationChk(x, y)) {
				if (routeChk(x, y)) {
					if (board[x][y] != null && board[x][y].player == this.player) {// 아군말을
																					// 만나면
						System.out.println("아군말이 도착지를 가로막고 있습니다.");
						return;
					}
					board[locationX][locationY] = null;
					this.locationX = x;
					this.locationY = y;
					board[x][y] = this;
					System.out.println(name + "이동 성공");
					isMoved = true;
				} else {
					System.out.println("점프 불가능");
				}
			}
		}else{
			System.out.println("이미 움직인 말입니다");
		}
	}
	
	@Override
	public boolean routeChk(int x, int y){
		
		int vectorX = x-locationX;
		int vectorY = y-locationY;
		int routeX = 0;
		int routeY = 0;
		if(Math.abs(vectorX) > Math.abs(vectorY)){
			routeX += vectorX/2;
		}else{
			routeY += vectorY/2;
		}
		System.out.println("중간경로");
		System.out.println(locationX+routeX);System.out.println(locationY+routeY);
		if(board[locationX+routeX][locationY+routeY] instanceof Obstacle){
			System.out.println("장애물이 있습니다. 점프 불가.");
			return false;
		}
		if(board[locationX+routeX][locationY+routeY] == null){
			System.out.println("이동경로에 아무도 없습니다. 점프 가능.");
			return true;
		}
		if(board[locationX+routeX][locationY+routeY].player.name == this.player.name){
			System.out.println("이동경로에 아군이 위치해있습니다. 점프 가능.");
			return true;
		}
		
			
		return false;
	}

	@Override
	public boolean destinationChk(int x, int y) {
		for (int i = 0; i < moveAbleDst.length; i++) {
			if (moveAbleDst[i][0] == x - locationX && moveAbleDst[i][1] == y - locationY) {
				///////////////////////////////////////////////////////////////
				if (board[x][y] != null) {
					if (board[x][y] instanceof Obstacle) {
						System.out.println("장애물이 가로막고 있습니다. 이동 불가");
						return false;
					} else if (board[x][y].player.name.equals(player.name)) {
						System.out.println("아군말이 가로막고 있습니다. 이동 불가");
						return false;
					}
				}
				////////////////////////////////////////////////////////////////
				System.out.println(name + "이 " + x + "," + y + "로 이동을 시도합니다.");
				return true;
			}
		}
		System.out.println(name + "(" + x + "," + y +")이동 불가능한 경로입니다.");
		return false;
	}
	@Override
	public String toString() {
		return name+"("+player.name+")";
	}
	
}



