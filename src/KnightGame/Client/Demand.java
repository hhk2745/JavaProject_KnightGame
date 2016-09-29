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
			demandMsg = demander.getId() + " : �α׾ƿ� �����ּ���.";
			break;
		case LOGIN:
			demandMsg = demander.getId() + " : �α��� �����ּ���";
			break;
		case CHANGE_USER_LOCATION:
			demandMsg = demander.getId() + " : ��ġ���� �������ּ���";
			break;
		case CHATTING:
			demandMsg = demander.getId() + " : ä�ø޼��� �����ּ���";
			break;
		case UPDATE_USERCHART:
			demandMsg = demander.getId() + " : �������� �������ּ���";
			break;
		case READY:
			demandMsg = demander.getId() + " : ��������ּ���";
			break;
		// case GAME_START:
		// demandMsg = demander.getId() + " : ���ӽ����ּ���";
		// break;
		case SENDING_BOARD:
			demandMsg = demander.getId() + " : ���� �������ּ���";
			break;
		case TURN_CHANGE:
			demandMsg = demander.getId() + " : �Ϲٲ��ּ���";
			break;
		case END_GAME:
			demandMsg = demander.getId() + " : ���� �����.. ���� �����ּ���";
			break;
		}
	}

	@Override
	public String toString()
	{
		return demandMsg;
	}

}
