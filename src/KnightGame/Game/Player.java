package KnightGame.Game;

import java.io.Serializable;
import java.util.ArrayList;


public class Player implements KnightGameInfo, Serializable{

	public String name;
	public OriginalForm [][] board;
	//public ArrayList<ArrayList<OriginalForm>> board;
	public Knight[] knight;
	public Pawn[] pawn;
	public Player(String name){
		this.name = name;
		
		board = new OriginalForm[ver][hor];
		for(int i=0; i<ver; i++){
			for(int j=0; j<hor; j++){
				board[i][j] = null;
			}
		}
		
		pawn = new Pawn[4];
		for(int i=0; i<4; i++){
			pawn[i] = new Pawn(board, this);
			pawn[i].name = "폰"+i;
			pawn[i].locationX = locationOfPawn[i][X];
			pawn[i].locationY = locationOfPawn[i][Y];
			board[locationOfPawn[i][X]][locationOfPawn[i][Y]] = pawn[i];
		}
		
		knight = new Knight[3];
		for(int i=0; i<3; i++){
			knight[i] = new Knight(board, this);
			knight[i].name = "나이트"+i;
			knight[i].locationX = locationOfKnight[i][X];
			knight[i].locationY = locationOfKnight[i][Y];
			board[locationOfKnight[i][X]][locationOfKnight[i][Y]] = knight[i];
		}
		
	}
	
	public boolean isAllMovedChk(){//죽은말은 카운트하지 말자
		boolean res = true;
		for(int i=0; i<4; i++){
			if(!pawn[i].isDeath)
				res &= pawn[i].isMoved;
		}
		for(int i=0; i<3; i++){
			if(!knight[i].isDeath)
				res &= knight[i].isMoved;
		}
		return res;
	}
	
	public static void main(String[] args){
		//테스트 메인
		new Player("유저");
	}
}
