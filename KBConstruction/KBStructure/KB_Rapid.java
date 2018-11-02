package KBStructure;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import edu.ntua.isci.common.dl.LoadedOntology;
import edu.ntua.isci.common.dl.LoadedOntologyAccess;
import edu.ntua.isci.qa.algorithm.Engine;
import edu.ntua.isci.qa.algorithm.rapid.dllite.DLRapid;
import edu.ntua.isci.qa.owl.OWL2LogicTheory;
import BufferReader.FileReader;


public class KB_Rapid{
	public Engine engine;
	public Map<String, String> ClaMap;
	public Map<String, String> ProMap;
	public Statement st;
	
	public KB_Rapid(String dbName, String tboxPath, String CPMap){
		
		ClaMap=new HashMap<String, String>();
		ProMap=new HashMap<String, String>();
		st=null;
		
		try{
			Map<String, Object> props = new HashMap<>();		
			props.put(LoadedOntologyAccess.PRESENTATION_TYPE, LoadedOntologyAccess.SIMPLE_SHORT_FORM);
			
			LoadedOntology ontRef = LoadedOntology.createFromPath("file:/"+tboxPath);

			LoadedOntologyAccess loa = new LoadedOntologyAccess(ontRef, props);			
			engine = DLRapid.createFastUnfoldRapid();	
			engine.importOntology(ontRef, loa, OWL2LogicTheory.DL_LITE);
			
			
			
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