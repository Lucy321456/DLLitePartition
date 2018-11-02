package KBStructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import BufferReader.FileReader;



public class KB{
	public TBox tbox;
	public Map<String, String> ClaMap;
	public Map<String, String> ProMap;
	public Statement st;
	public String id;
	
	public KB(String id, String dbName, String tboxPath, String CPMap){
		this.id=id;
		tbox=new TBox(tboxPath);
		ClaMap=new HashMap<String, String>();
		ProMap=new HashMap<String, String>();
		st=null;
		
		try{
			BufferedReader br=FileReader.readFile(CPMap);
			String line="";
			while((line=br.readLine())!=null){
				String[] nt=line.split("<>");
				if(nt[1].startsWith("CT")){
					ClaMap.put(nt[0], nt[1]);
				}
				else{
					ProMap.put(nt[0], nt[1]);
				}
				
			}
			
			String driver="com.mysql.jdbc.Driver";
			String url="jdbc:mysql://127.0.0.1:3306/"+dbName;
			String user="root";
			String password="ycul321";
			Class.forName(driver); 
			Connection conn = DriverManager.getConnection(url, user, password);
			st=conn.createStatement();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}