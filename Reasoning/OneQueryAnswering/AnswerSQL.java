package OneQueryAnswering;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*************************************************
 * answer translated SQL queries
 * @author Lucy
 *
 */

public class AnswerSQL{
	public static Set<Map<String, String>> answerSQL(String sql, Statement st, List<String> headVar){
		Set<Map<String, String>> answers=new HashSet<Map<String, String>>();
		try{			
			ResultSet rs=st.executeQuery(sql);
			int n=rs.getMetaData().getColumnCount();
			if(headVar.size()>0){
				while(rs.next()){
					Map<String, String> ans=new HashMap<String, String>();
					for(int i=1; i<n+1; i++){
						ans.put(headVar.get(i-1), rs.getString(i));
					}
					answers.add(ans);	
				}
			}
			else{
				while(rs.next()){
					Map<String, String> ans=new HashMap<String, String>();
					answers.add(ans);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return answers;
	}
}