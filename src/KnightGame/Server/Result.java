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
	public boolean start;// ���ӽ��� ���ɻ��¶�� ture
	public boolean turn;// ���� ���۽� �������̶�� true
	public String comId;// ���� ���̵�
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
