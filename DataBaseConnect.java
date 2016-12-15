/*
 *filename : DataBaseConnect.java
 *author : team Tic Toc
 *since : 2016.11.25
 *purpose/function : DB �۾��� ������ �� �׻� ó���ؾ� �ϴ� �κ�(����̹� �ε�, Connection ����, ���� ó�� ��)�� 
 *					������ Ŭ������ ���������ν� �ݺ������� ����Ǿ�� �ϴ� ��ɵ��� �ߺ����Ǹ� ���� �� �ֵ��� �Ͽ���. 
 *					�����ͺ��̽����� ������ ���� JDBC�� ����Ͽ���.
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

	/*DB �۾��� �ʿ��� Connection ��ü�� �������ִ� �޼ҵ�*/
	public static void connect(String password) {
		DataBaseConnect.password = password;
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost", "root", password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*Connection ��ü�� �ݾ��ִ� ����� �����ϴ� �޼ҵ�*/
	public static void close(Connection con)
	{
		try{
			con.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	/*Query���� execute ���ִ� �޼ҵ�*/
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
	
	/*Statement ��ü�� �ݾ��ִ� ����� �����ϴ� �޼ҵ�*/
	public static void close(Statement st)
	{
		try{
			st.close();
		}
		catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	/*ResultSet ��ü�� �ݾ��ִ� ����� �����ϴ� �޼ҵ�*/
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