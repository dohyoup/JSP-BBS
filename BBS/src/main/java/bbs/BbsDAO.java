package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsDAO {
	
	private Connection conn; // 데이타베이스에 접근하게 해주는 객체
	private ResultSet rs; //정보를 담을 수 있는 하나의 객체
	
	public BbsDAO() {
		try {
			String dbURL = "jdbc:mysql://localhost:3306/BBS";
			String dbID ="root";
			String dbPassword ="root";
			Class.forName("com.mysql.cj.jdbc.Driver"); //데이타베이스 접근가능하기 해주는 매개역할 라이브러리
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getDate() {
		String SQL = "SELECT NOW()";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // 데이터베이스 연결객체 conn을 애용해서 SQL문장을 실행준비 단계로 만듬 
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ""; // 데이터베이스 오류
	}
	
	public int getNext() {
		String SQL = "SELECT bbsID FROM BBS ORDER BY bbsID DESC";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // 데이터베이스 연결객체 conn을 애용해서 SQL문장을 실행준비 단계로 만듬 
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1; // 첫 번째 게시물인 경우
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
	
	public int writer(String bbsTitle, String userID, String bbsContent) {
		String SQL = "INSERT INTO BBS VALUES(?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // 데이터베이스 연결객체 conn을 애용해서 SQL문장을 실행준비 단계로 만듬 
			pstmt.setInt(1, getNext());
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
	
	public ArrayList<Bbs> getList(int pageNumber){
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10";
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // 데이터베이스 연결객체 conn을 애용해서 SQL문장을 실행준비 단계로 만듬 
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				list.add(bbs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list; 
	}
	
	public boolean nextPage(int pageNumber) {
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // 데이터베이스 연결객체 conn을 애용해서 SQL문장을 실행준비 단계로 만듬 
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false; 
	}
	
	public Bbs getBbs(int bbsID) {
		String SQL = "SELECT * FROM BBS WHERE bbsID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); 
			rs = pstmt.executeQuery();
			if(rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				return bbs;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int searchCount(String searchText) {
		String SQL ="SELECT * FROM BBS WHERE bbsTitle LIKE '%"+searchText.trim()+"%' AND bbsAvailable = 1";
		int count = 0;
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(count);
		return count;
	}
	
	public ArrayList<Bbs> search(String searchText, int pageNumber) {
		String SQL = "SELECT * FROM BBS WHERE bbsTitle LIKE '%"+searchText.trim()+"%'AND bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10";
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // 데이터베이스 연결객체 conn을 애용해서 SQL문장을 실행준비 단계로 만듬 
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				list.add(bbs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("list : " + list);
		return list; 
	}
	
	public int update(int bbsID, String bbsTitle, String bbsContent) {
		String SQL = "UPDATE BBS SET bbsTitle = ?, bbsContent = ? WHERE bbsID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // 데이터베이스 연결객체 conn을 애용해서 SQL문장을 실행준비 단계로 만듬 
			pstmt.setString(1, bbsTitle);
			pstmt.setString(2, bbsContent);
			pstmt.setInt(3, bbsID);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
	
	public int delete(int bbsID) {
		String SQL = "UPDATE BBS SET bbsAvailable = 0 WHERE bbsID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // 데이터베이스 연결객체 conn을 애용해서 SQL문장을 실행준비 단계로 만듬 
			pstmt.setInt(1, bbsID);
			return pstmt.executeUpdate();//성공적으로 동작했으면 0 이상의 값을 리턴해준다.
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
	
}
