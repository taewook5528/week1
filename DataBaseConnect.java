/*
 *filename : DataBaseConnect.java
 *author : team Tic Toc
 *since : 2016.11.25
 *purpose/function : DB 작업을 수행할 때 항상 처리해야 하는 부분(드라이버 로딩, Connection 생성, 예외 처리 등)을 
 *					별도의 클래스에 정의함으로써 반복적으로 수행되어야 하는 기능들의 중복정의를 피할 수 있도록 하였다. 
 *					데이터베이스와의 연결을 위해 JDBC를 사용하였다.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseConnect {
	private static Connection con = null;
	private static Statement st = null;
	private static ResultSet rs = null;
	private static String password;

	/*DB 작업에 필요한 Connection 객체를 생성해주는 메소드*/
	public static void connect(String password) {
		DataBaseConnect.password = password;
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost", "root", password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*Connection 객체를 닫아주는 기능을 수행하는 메소드*/
	public static void close(Connection con)
	{
		try{
			con.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	/*Query문을 execute 해주는 메소드*/
	public static ResultSet execute(String query) {
		rs = null;
		try {
			st = con.createStatement();
			rs = st.executeQuery(query);

			if (st.execute(query)) {
				rs = st.getResultSet();
			}
			return rs;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public static void update(String query) {
		try {
			st.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*Statement 객체를 닫아주는 기능을 수행하는 메소드*/
	public static void close(Statement st)
	{
		try{
			st.close();
		}
		catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	/*ResultSet 객체를 닫아주는 기능을 수행하는 메소드*/
	public static void close(ResultSet rs)
	{
		try{
			rs.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	public static boolean getBoolValue(int tinyint) {
		return tinyint == 0 ? false : true;
	}
}