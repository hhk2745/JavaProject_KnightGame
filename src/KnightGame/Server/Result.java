package KnightGame.Server;//new

import java.io.Serializable;
import java.util.Vector;

import KnightGame.DemandChart;
import KnightGame.MemberDTO;
import KnightGame.Client.Demand;

public class Result implements Serializable, DemandChart
{
	public Demand demand;
	public String resultMsg;
	public Vector<MemberDTO> userChart = new Vector<>();
	///////////////////////////////////////////
	public boolean start;// 게임시작 가능상태라면 ture
	public boolean turn;// 게임 시작시 선제턴이라면 true
	public String comId;// 상대방 아이디
	///////////////////////////////////////////

	public Result(Demand demand)
	{
		this.demand = demand;
	}

	@Override
	public String toString()
	{
		return resultMsg;
	}

}
