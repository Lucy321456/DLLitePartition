package OneQueryAnswering;

import KBStructure.KB;
import Query.Query;
import QueryRewrite.RewriteCQS;
import QueryRewrite.TranslateCQSToSQLS;

import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/***********************************************
 * answer a query over a KB without query partition
 * @author Lucy
 *
 */
public class AnswerAQueryOverKB{ 
	
	public static Set<Map<String, String>> queryAnswer(Query query, KB kb){
		long start=System.currentTimeMillis();
		Set<Map<String, String>>  answer=new HashSet<Map<String, String>>();
		Set<Query> rewrites=RewriteCQS.rewriteCQ(kb.tbox, query); 
		System.out.println("over "+kb.id+" number of rewriting CQs: "+rewrites.size());
		Set<String> sqls=TranslateCQSToSQLS.translateCQS(rewrites, kb.ClaMap, kb.ProMap);
		System.out.println("over "+kb.id+" number of rewriting SQLs: "+sqls.size());
		for(String sql: sqls){   
			if(sql.length()>0){
				answer.addAll(AnswerSQL.answerSQL(sql,kb.st,query.heads));
			}
		}
        long end=System.currentTimeMillis();
        System.out.println("over "+kb.id+" number of answers: "+answer.size());
        System.out.println("over "+kb.id+" time used===: "+(end-start));
		return answer; 
	}

	
	public static void main(String[] args){      
		try{   
			Query q=new Query("q(?x,?y)<-41:core#subject(?x,365:Category:Computer_pioneers), 32:name(?x,?y)");  
			
			long totalTime=0;  
			KB kb=new KB("dbpedia","dbpedia", "E:/Experiment/DLLitePartition/Data/dbpedia/KB/TBox.txt",           
					"E:/Experiment/DLLitePartition/Data/dbpedia/KB/CPMap.txt");                 
			long start=System.currentTimeMillis();     
			AnswerAQueryOverKB qa=new AnswerAQueryOverKB();          
			Set<Map<String, String>> ans=qa.queryAnswer(q, kb); 
			System.out.println("number of answers: "+ans.size());         
			long end=System.currentTimeMillis(); 
			System.out.println("total time used: "+(end-start));                     
			
//            for(int i=0; i<30; i++){                                    
//    			long totalTime=0;
//    			KB kb=new KB("BTC_"+i,"BTC_"+i, "E:/Experiment/DLLitePartition/Data/BTC/KBPartition/SubTBox"+i+".txt",         
//    					"E:/Experiment/DLLitePartition/Data/BTC/KBPartition/CPMap"+i+".txt");                
//
//    			long sTime=System.currentTimeMillis();     
//    			AnswerAQueryOverKB qa=new AnswerAQueryOverKB();           
//    			Set<Map<String, String>> ans=qa.queryAnswer(q, kb); 
//    			System.out.println("number of answers: "+ans.size());   
//    			long eTime=System.currentTimeMillis(); 
//    			System.out.println("total time used: "+(eTime-sTime));   
//            } 

            
		}catch(Exception e){     
			e.printStackTrace(); 
		}
	}
}