package KnightGame.Game;

public interface KnightGameInfo {
	public static final int distance = 80;
	public static final int ver = 7;//7��
	public static final int hor = 8;//8ĭ
	public static final int X = 0;
	public static final int Y = 1;
	

	//public static final int [][] locationOfKnight = {{6,1},{6,3},{6,5}};
	//public static final int [][] locationOfPawn = {{5,0},{5,2},{5,4},{5,6}};
	//�迭������ x,y��ǥ �ٲ�־ GUI������ ����ε� x,y��ǥ��
	public static final int [][] locationOfKnight = {{1,6},{3,6},{5,6}};
	public static final int [][] locationOfPawn = {{0,5},{2,5},{4,5},{6,5}};
}
