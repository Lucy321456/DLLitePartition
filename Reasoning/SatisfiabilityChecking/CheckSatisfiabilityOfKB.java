package SatisfiabilityChecking;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Set;
import java.util.List;

import KBStructure.KB;

/*******************************************
 * check satisfiability of one KB
 * @author Lucy
 *
 */

public class CheckSatisfiabilityOfKB{
	
	public static boolean checkSatisfiability(KB kb){ 
		long sTime=System.currentTimeMillis();
		boolean sat=true;
		System.out.println("checking satisfiability of "+kb.id);
		List<Set<String>> nic=ComputeNIColusure.computeNIColusure(kb.tbox);
		long time=System.currentTimeMillis();
		System.out.println(kb.id+" number of closure_ClaDisj: "+nic.get(0).size()+" closure_ProDisj: "+nic.get(1).size()+" closure_FunPro: "+nic.get(2).size()+", time used: "+(time-sTime));
		System.out.println(kb.id+" number of queries needed to be checked: "+(nic.get(0).size()+nic.get(1).size()+nic.get(2).size()));
		
		Set<String> queries=ObtainCheckedQuery.obtainCheckedQuery(nic, kb.ClaMap, kb.ProMap);
		System.out.println(kb.id+" number of checked SQL query size: "+queries.size());
		int ct=0;
		for(String sql: queries){
			ct=ct+1;
			if(ct%100==0){
				System.out.println("SQL count: "+ct);
			}
			if(SQLQueryChecking.answerSQL(kb.st, sql)){ // SQL query has answers
				System.out.println(kb.id+" corresponding SQL:"+sql);
				System.out.println(kb.id+" number of SQLs checked:"+ct); 
				sat=false;
				//break;
			}
		}
		System.out.println("satisfiability: "+sat);
		long eTime=System.currentTimeMillis();
		System.out.println("time used: "+(eTime-sTime)); 
		return sat; 
	}
	
	public static void main(String[] args){      

		try{  		
			KB obda=new KB("dbpedia", "dbpedia", "E:/Experiment/DLLitePartition/Data/dbpedia/KBPartition/SubTBox2.txt", 
					"E:/Experiment/DLLitePartition/Data/dbpedia/KBPartition/CPMap2.txt");             
			System.out.println("finish materialize KB!!!");   
			long start=System.currentTimeMillis(); 
			boolean sat=CheckSatisfiabilityOfKB.checkSatisfiability(obda);  
			System.out.println("the satisfiability of the KB: "+sat); 
			long end=System.currentTimeMillis();
			System.out.println("total time used: "+(end-start));                 
			
		}catch(Exception e){    
			e.printStackTrace(); 
		}
		
		
	}
}