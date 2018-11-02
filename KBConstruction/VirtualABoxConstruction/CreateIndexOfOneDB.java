package VirtualABoxConstruction;

import java.util.Collections;
import java.util.List;
import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

import BufferReader.FileReader;



public class CreateIndexOfOneDB{
	
	public static void createIndex(String mapFile, Statement st){
		try{
					
			List<String> tables=new ArrayList<String>();
			
			BufferedReader br=FileReader.readFile(mapFile);
			String line="";
			while((line=br.readLine())!=null){
				String[] nt=line.split("<>");
				tables.add(nt[1]);				
			}
			Collections.sort(tables);

			String sql="";
			String t="";
			for(int i=tables.size()-1; i>=0; i--){ 
				t=tables.get(i);
				if(t.startsWith("CT")){
					sql="create index id_"+t+" on "+t+" ( P )";					
				}
				else{
					sql="create index id_"+t+" on "+t+" ( P1, P2 )";
				}	
				System.out.println(sql); 
				st.execute(sql);				
			}
			
		}catch(Exception e){ 
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){  
		CreateIndexOfOneDB CI=new CreateIndexOfOneDB();
		
		try{ 
			String driver="com.mysql.jdbc.Driver";
			String url="jdbc:mysql://127.0.0.1:3306/btc";
			String user="root";
			String password="ycul321";
			Class.forName(driver); 
			Connection conn = DriverManager.getConnection(url, user, password);
			Statement st=conn.createStatement();
			CI.createIndex("E:/Experiment/DLLitePartition/Data/BTC/KBPartition/CPMap6.txt", st);  
			
			conn.close();
			st.close();
		}catch(Exception e){  
			e.printStackTrace();
		}
	}
}