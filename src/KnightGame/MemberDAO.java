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
	} // getConn : 연결 메소드 작성

	public void signUp(String id, char[] pw, String email) // 회원가입
	{
		String pwStr = "";
		int res = 0;
		try
		{
			conn = getConn();
			String sql = "INSERT INTO member (ID, PW, EMAIL, REGDATE) VALUES (?, ?, ?,sysdate)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			for (int i = 0; i < pw.length; i++)// 패스워드 필드는 char[]형 반환
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
		if (res != 0) // 집어넣을 결과가 공백아닌 경우
			System.out.println("회원가입 성공");
		else // 공백일 경우(정보가 없으면)
			System.out.println("회원가입 실패");
	} // insertMember : 회원 정보를 저장하는 메소드

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

	public boolean signUp_IdChk(String id) // 회원가입 ID중복검사
	{
		boolean res = true;
		try
		{
			conn = getConn();
			String sql = "select id from member";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery(); // R읽어오는작업 ==> ResultSet (결과표)
			while (rs.next())
			{// 입력한 아이디가 DB에 있다면 가입할수 없다. ==> res=false;
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

	// 로그인 성공, 실패 체크
	public MemberDTO loginChk(String id, String pw)
	{
		MemberDTO dto = null;
		try
		{
			Connection conn = getConn();
			String sql = "select * from member where id=? and pw=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, pw);// String 형태로 변환하여 DTO에 저장
			ResultSet rs = ps.executeQuery();

			if (rs.next()) // 이 안으로 들어온거면 로그인 성공한것.
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

	// 로그아웃 처리
	public MemberDTO logoutChk(MemberDTO user)
	{
		user = null;
		return user;
	}

	public String serch(String email) // 아이디, 패스워드 찾는 메소드
	{
		String res = "";
		int cnt = 0;// 몇회 실패했는지 체크
		try
		{
			conn = getConn();
			String sql = "select id, pw from member where email=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();

			if (rs.next())
			{// 인자로 받은 이메일로 검색이 된거면 if()문 실행
				res += "ID : " + rs.getString("id");
				res += ", PW : " + rs.getString("pw");
			} else
			{
				res += "존재하지 않는 메일입니다.";
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
