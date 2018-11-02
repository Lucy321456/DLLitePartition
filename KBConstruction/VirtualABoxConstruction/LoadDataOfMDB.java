package VirtualABoxConstruction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


class LDTask implements Runnable{
	private String path;
	private Statement st;
	
	public LDTask(String path, Statement st){
		this.path=path;
		this.st=st;		
	}
	
	public void run(){
		LoadDataOfOneDB.loadData(path, st);
		Thread.yield();
	}
}
public class LoadDataOfMDB{
	public void loadData(List<String> paths, List<Statement> sts){
		for(int i=0; i<sts.size(); i++){
			new Thread(new LDTask(paths.get(i), sts.get(i)));
		}
	}
	
	public static void main(String[] args){
		try{
			long start=System.currentTimeMillis();
			LoadDataOfMDB LD=new LoadDataOfMDB();
			List<String> paths=new ArrayList<String>();
			List<Statement> sts=new ArrayList<Statement>();
			for(int i=0; i<30; i++){
				paths.add("E:/Experiment/DLLitePartition/Data/BTC/KBPartition/VABox"+i+"/");
				
				String driver="com.mysql.jdbc.Driver";
				String url="jdbc:mysql://127.0.0.1:3306/BTC_"+i;
				String user="root";
				String password="ycul321";
				Class.forName(driver); 
				Connection conn = DriverManager.getConnection(url, user, password);
				Statement st=conn.createStatement();
				sts.add(st);
			}
			
			for(int i=0; i<sts.size(); i++){
				sts.get(i).close();
			}
			long end=System.currentTimeMillis();
			System.out.println("used time (seconds): "+(end-start)/1000);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}