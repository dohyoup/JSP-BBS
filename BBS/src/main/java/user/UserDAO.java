package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
	private Connection conn; // 데이타베이스에 접근하게 해주는 객체
	private PreparedStatement pstmt; //SQL 인젝션 공격 방어를 위함
	private ResultSet rs; //정보를 담을 수 있는 하나의 객체
	
	public UserDAO() {
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
	
	public int login(String userID, String userPassword) {
		String SQL = "SELECT userPassword FROM USER WHERE userID = ?";
		try {
			pstmt = conn.prepareStatement(SQL); //SQL문 담기
			pstmt.setString(1, userID); //SQL문의 ? 에 변수 값 넣기 앞에 숫자로 몇번째 ?에 변수를 넣을 건지 지정
			rs = pstmt.executeQuery(); // 쿼리 결과값 받아오기
			if(rs.next()) {
				if(rs.getString(1).equals(userPassword)) {
					return 1; // 로그인 성공
				}
				return 0; //비밀번호 불일치
			}else
				return -1; // 아이디 없음
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -2; //데이터베이스 오류
	}
    
	public int join(User user) {
		String SQL = "INSERT INTO USER VALUES(?, ?, ?, ?, ?)";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getUserPassword());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getUserGender());
			pstmt.setString(5, user.getUserEmail());
			return pstmt.executeUpdate(); //중복 데이터를 허용하지 않고 또한 userID에 PRIMARY KEY 설정을 해두었기 때문에 존재하는 아이디일지 return -1로 간다	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
	
}
