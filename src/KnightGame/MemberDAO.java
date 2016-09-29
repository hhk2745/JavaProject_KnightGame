package KnightGame;//new_End

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MemberDAO
{
	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;

	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String USER = "Websol";
	private static final String PASS = "oracle";

	public Connection getConn() // connect J
	{
		try
		{
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASS);
		} catch (Exception e)
		{
			e.printStackTrace();
		} // try - catch
		return conn;
	} // getConn : ���� �޼ҵ� �ۼ�

	public void signUp(String id, char[] pw, String email) // ȸ������
	{
		String pwStr = "";
		int res = 0;
		try
		{
			conn = getConn();
			String sql = "INSERT INTO member (ID, PW, EMAIL, REGDATE) VALUES (?, ?, ?,sysdate)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			for (int i = 0; i < pw.length; i++)// �н����� �ʵ�� char[]�� ��ȯ
				pwStr += pw[i];
			System.out.println(pwStr);
			ps.setString(2, pwStr);
			ps.setString(3, email);
			res = ps.executeUpdate();
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			close();
		}
		if (res != 0) // ������� ����� ����ƴ� ���
			System.out.println("ȸ������ ����");
		else // ������ ���(������ ������)
			System.out.println("ȸ������ ����");
	} // insertMember : ȸ�� ������ �����ϴ� �޼ҵ�

	public boolean updateAccount(String id, char[] pw, String email)
	{
		int res = 0;
		String pwStr = "";
		conn = getConn();
		String sql = "update member set pw=?, email=? where id=?";
		try
		{
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < pw.length; i++)
				pwStr += pw[i];

			ps.setString(1, pwStr);
			ps.setString(2, email);
			ps.setString(3, id);
			res = ps.executeUpdate();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			close();
		}
		if (res != 0)
			return true;
		else
			return false;
	}

	public boolean signUp_IdChk(String id) // ȸ������ ID�ߺ��˻�
	{
		boolean res = true;
		try
		{
			conn = getConn();
			String sql = "select id from member";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery(); // R�о�����۾� ==> ResultSet (���ǥ)
			while (rs.next())
			{// �Է��� ���̵� DB�� �ִٸ� �����Ҽ� ����. ==> res=false;
				if (id.equals(rs.getString("id")))
				{
					res = false;
					break;
				}
			}
		} catch (Exception e)
		{
			System.out.println("E : " + e);
		} finally
		{
			close();
		}
		return res;
	}

	// �α��� ����, ���� üũ
	public MemberDTO loginChk(String id, String pw)
	{
		MemberDTO dto = null;
		try
		{
			Connection conn = getConn();
			String sql = "select * from member where id=? and pw=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, pw);// String ���·� ��ȯ�Ͽ� DTO�� ����
			ResultSet rs = ps.executeQuery();

			if (rs.next()) // �� ������ ���°Ÿ� �α��� �����Ѱ�.
			{
				dto = new MemberDTO();
				dto.setId(id);
				dto.setPw(pw);
				dto.setEmail(rs.getString("email"));
				dto.setRegdate(rs.getDate("regdate"));
				dto.setWin(rs.getInt("win"));
				dto.setLose(rs.getInt("lose"));
			}

		} catch (Exception e)
		{
			System.out.println("E : " + e);
		} finally
		{
			close();
		}
		return dto;
	}

	// �α׾ƿ� ó��
	public MemberDTO logoutChk(MemberDTO user)
	{
		user = null;
		return user;
	}

	public String serch(String email) // ���̵�, �н����� ã�� �޼ҵ�
	{
		String res = "";
		int cnt = 0;// ��ȸ �����ߴ��� üũ
		try
		{
			conn = getConn();
			String sql = "select id, pw from member where email=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();

			if (rs.next())
			{// ���ڷ� ���� �̸��Ϸ� �˻��� �ȰŸ� if()�� ����
				res += "ID : " + rs.getString("id");
				res += ", PW : " + rs.getString("pw");
			} else
			{
				res += "�������� �ʴ� �����Դϴ�.";
			}

		} catch (Exception e)
		{
			System.out.println("E : " + e);
		} finally
		{
			close();
		}
		return res;
	}

	public void close()
	{
		if (rs != null)
			try
			{
				rs.close();
			} catch (SQLException e)
			{
			}
		if (ps != null)
			try
			{
				ps.close();
			} catch (SQLException e)
			{
			}
		if (conn != null)
			try
			{
				conn.close();
			} catch (SQLException e)
			{
			}
	}
}
