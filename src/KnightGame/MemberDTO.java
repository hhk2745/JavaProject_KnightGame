package KnightGame;//new_end

import java.io.Serializable;
import java.util.Date;

public class MemberDTO implements Serializable
{
	private String id;
	private String pw;
	private String email;
	private Date regdate;
	private Integer win;
	private Integer lose;
	private Integer location = -1;

	public int getLocation()
	{
		return location;
	}

	public void setLocation(int location)
	{
		this.location = location;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getPw()
	{
		return pw;
	}

	public void setPw(String pw)
	{
		this.pw = pw;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public Date getRegdate()
	{
		return regdate;
	}

	public void setRegdate(Date regdate)
	{
		this.regdate = regdate;
	}

	public int getWin()
	{
		return win;
	}

	public void setWin(int win)
	{
		this.win = win;
	}

	public int getLose()
	{
		return lose;
	}

	public void setLose(int lose)
	{
		this.lose = lose;
	}

	@Override
	public String toString()
	{
		String info = "";
		info += "\n┌──────────────\n";
		info += "│아이디   : " + id + "\n";
		info += "│승/패 : " + win + "/" + lose + "\n";
		info += "│위치 : " + location + "\n";
		info += "└───────────────\n";
		return info;
	}

}
