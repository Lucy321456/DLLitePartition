package QueryMaterialization;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import AnalysisURI.URIComputation;
import BufferReader.FileReader;
import Query.Atom;
import Query.Query;

/********************************************
 * replace names in a query with their delegates
 * @author Lucy
 *
 */

public class MaterializeQuery{
	
	public static void materializeQuery(String query, String query_new, String prefix, String sameAs){
		try{
			Map<String, String> repElement=new HashMap<String, String>();
			BufferedReader br_sameAs=FileReader.readFile(sameAs);
			String line_sameAs="";
			while((line_sameAs=br_sameAs.readLine())!=null){
				String[] nt=line_sameAs.split("<>");
				repElement.put(nt[0], nt[1]);
			}
			
			Map<String, String> prefixMap=new HashMap<String, String>();
			BufferedReader br_prefix=FileReader.readFile(prefix);
			String line_prefix="";
			while((line_prefix=br_prefix.readLine())!=null){
				String[] nt=line_prefix.split("<>");
				prefixMap.put(nt[0], nt[1]);
			}
			
			FileWriter fw=new FileWriter(query_new);
			BufferedReader br_query=FileReader.readFile(query);
			String line_query="";
			while((line_query=br_query.readLine())!=null){
				fw.write(computeNewQuery(line_query, prefixMap, repElement)+"\r\n");
			}
			fw.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static String computeNewQuery(String query, Map<String, String> prefix, Map<String, String> repElement){
		Query query_new=new Query(query);
		for(Atom at: query_new.body){
			at.con=computeNewElement(at.con, prefix, repElement);
			if(!at.var1.startsWith("?")){
				at.var1=computeNewElement(at.var1, prefix, repElement);
			}
			if(at.var2.length()>0 & !at.var2.startsWith("?")){
				at.var2=computeNewElement(at.var2, prefix, repElement);
			}
		}
		
		
		return query_new.toString();
	}
	
	public static String computeNewElement(String ele, Map<String, String> prefixMap, Map<String, String> repElement){
		if(ele.startsWith("?")||ele.startsWith("%%")){
			return ele;
		}
		else{
			String ele_new=ele;
			String[] nt=URIComputation.computeURI(ele);
			if(prefixMap.containsKey(nt[0])){
				ele_new=prefixMap.get(nt[0])+":"+nt[1];
			}
			if(repElement.containsKey(ele_new)){
				ele_new=repElement.get(ele_new);
			}			
			return ele_new;
		}		
	}
	
	public static void main(String[] args){ 
		MaterializeQuery MQ=new MaterializeQuery();
		MQ.materializeQuery("CQ_BTC.txt", "CQ_BTC_new.txt", "E:/Experiment/DLLitePartition/Data/BTC/KB/prefix.txt", "E:/Experiment/DLLitePartition/Data/BTC/KB/repElement.txt");
		System.out.println("finish"); 
	}
	
}