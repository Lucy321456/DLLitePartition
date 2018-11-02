package SatisfiabilityChecking;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/************************************************************
 * check obtained SQL queries
 * @author Lucy
 *
 */

public class SQLQueryChecking{
	
	public static boolean answerSQL(Statement st, String sql){
		boolean lab=false;
		try{
			ResultSet rs=st.executeQuery(sql);
			int n=rs.getMetaData().getColumnCount();
			while(rs.next()){
				lab=true;
				break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}		
		
		return lab;
	}
}