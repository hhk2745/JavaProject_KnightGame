package KnightGame.Game;

public interface KnightGameInfo {
	public static final int distance = 80;
	public static final int ver = 7;//7줄
	public static final int hor = 8;//8칸
	public static final int X = 0;
	public static final int Y = 1;
	

	//public static final int [][] locationOfKnight = {{6,1},{6,3},{6,5}};
	//public static final int [][] locationOfPawn = {{5,0},{5,2},{5,4},{5,6}};
	//배열에서는 x,y좌표 바꿔넣어서 GUI에서는 제대로된 x,y좌표로
	public static final int [][] locationOfKnight = {{1,6},{3,6},{5,6}};
	public static final int [][] locationOfPawn = {{0,5},{2,5},{4,5},{6,5}};
}
