package KnightGame.Client;//new_ing

import java.io.Serializable;
import java.util.ArrayList;

import KnightGame.DemandChart;
import KnightGame.MemberDTO;
import KnightGame.Game.OriginalForm;

public class Demand implements Serializable, DemandChart
{
	public MemberDTO demander;
	public int demandKind;
	public String demandMsg;
	public OriginalForm[][] buffer;
	public int beforeX;
	public int beforeY;
	public int afterX;
	public int afterY;
	public boolean isObstacle;

	public int demandLocation;

	public Demand(MemberDTO demander, int demandKind)
	{
		this.demander = demander;
		this.demandKind = demandKind;
		process();
	}

	public void process()
	{
		switch (demandKind)
		{
		case LOGOUT:
			demandMsg = demander.getId() + " : 로그아웃 시켜주세요.";
			break;
		case LOGIN:
			demandMsg = demander.getId() + " : 로그인 시켜주세요";
			break;
		case CHANGE_USER_LOCATION:
			demandMsg = demander.getId() + " : 위치정보 변경해주세요";
			break;
		case CHATTING:
			demandMsg = demander.getId() + " : 채팅메세지 보내주세요";
			break;
		case UPDATE_USERCHART:
			demandMsg = demander.getId() + " : 유저정보 갱신해주세요";
			break;
		case READY:
			demandMsg = demander.getId() + " : 레디시켜주세요";
			break;
		// case GAME_START:
		// demandMsg = demander.getId() + " : 게임시켜주세요";
		// break;
		case SENDING_BOARD:
			demandMsg = demander.getId() + " : 보드 전달해주세요";
			break;
		case TURN_CHANGE:
			demandMsg = demander.getId() + " : 턴바꿔주세요";
			break;
		case END_GAME:
			demandMsg = demander.getId() + " : 제가 졌어요.. 게임 끝내주세요";
			break;
		}
	}

	@Override
	public String toString()
	{
		return demandMsg;
	}

}
