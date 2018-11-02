package VirtualABoxConstruction;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



public class LoadDataOfOneDB{
	
	public static void loadData(String path, Statement st){ 
		try{
			
			List<String> tableFiles=new ArrayList<String>();      
			File file = new File(path);
			File[] Files = file.listFiles();
			for (int i = 0; i < Files.length; i++) {
			  if(!Files[i].isDirectory()){
			     tableFiles.add(Files[i].toString());
			  }
			}
		   System.out.println("number of all the files: "+tableFiles.size());
		   
		   for(int i=0; i<tableFiles.size(); i++){
			   System.out.println(tableFiles.get(i));
			   String subFile=tableFiles.get(i).replace("\\", "/");	
			   String tname=subFile.substring(subFile.lastIndexOf("/")+1, subFile.indexOf("."));
			   String sql="";
			   String load="";
			   if(tname.startsWith("CT")){ 
				   sql="create table "+tname+" (P varchar(1820))";
				   load="load data infile '"+subFile+"' into table "+tname+" lines terminated by '\r\n' ";
			   } 
			   else if(tname.startsWith("PT")){ 
				   sql="create table "+tname+" (P1 varchar(1820), P2 varchar(1820))";
				   load="load data infile '"+subFile+"' into table "+tname+" fields terminated by ',' enclosed by '\"' lines terminated by '\r\n' ";				  
			   }
			   st.execute(sql);
			   st.execute(load);
		   }
			
		}catch(Exception e){
			e.printStackTrace(); 
		}
	}
	
	public static void main(String[] args){ 
		LoadDataOfOneDB ld=new LoadDataOfOneDB();
		long start=System.currentTimeMillis();
		try{        
			String driver="com.mysql.jdbc.Driver";
			String url="jdbc:mysql://127.0.0.1:3306/BTC";
			String user="root";
			String password="ycul321";
			Class.forName(driver); 
			Connection conn = DriverManager.getConnection(url, user, password);
			Statement st=conn.createStatement();
			
			ld.loadData("E:/Experiment/DLLitePartition/Data/BTC/KBPartition/VABox/", st);   
			
			conn.close();
			st.close();
			
			long end=System.currentTimeMillis();
			System.out.println("used time (seconds): "+(end-start)/1000);
			
		}catch(Exception e){ 
			e.printStackTrace();
		}
	}
}