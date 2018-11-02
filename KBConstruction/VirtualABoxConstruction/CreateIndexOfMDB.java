package VirtualABoxConstruction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class CITask implements Callable<Void>{
	private Statement st;
	private String mapFile;
	public CITask(Statement st, String mapFile){
		this.st=st;
		this.mapFile=mapFile;
	}
	public Void call() throws Exception{
		CreateIndexOfOneDB.createIndex(mapFile, st);
		return null;		
	}
}

public class CreateIndexOfMDB{
	public static void createIndex(List<String> mapFiles, List<Statement> sts){
		ExecutorService exec=Executors.newCachedThreadPool();
		for(int i=0; i<mapFiles.size(); i++){
			exec.submit(new CITask(sts.get(i),mapFiles.get(i)));
		}
		exec.shutdown();
	}
	
	public static void main(String[] args){
		CreateIndexOfMDB CI=new CreateIndexOfMDB();
		long start=System.currentTimeMillis();
		try{
			List<String> mapFiles=new ArrayList<String>();
			List<Statement> sts=new ArrayList<Statement>();
			for(int i=0; i<14; i++){
				mapFiles.add("E:/Experiment/DLLitePartition/Data/dbpedia/KBPartition/CPMap"+i+".txt");
				
				String driver="com.mysql.jdbc.Driver";
				String url="jdbc:mysql://127.0.0.1:3306/dbpedia_"+i;
				String user="root";
				String password="ycul321";
				Class.forName(driver); 
				Connection conn = DriverManager.getConnection(url, user, password);
				Statement st=conn.createStatement();
				sts.add(st);
				
			}
			CI.createIndex(mapFiles, sts);
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