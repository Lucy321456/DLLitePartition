package CQAnswering;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import KBStructure.KB;
import OneQueryAnswering.AnswerAQueryOverKB;
import Query.Query;
import QueryPartition.PartitionQuery;
import QueryPartitionAnswering.AnswerQueryPartitionWithValueTransfer;

/****************************************************
 * query answering with query partition and subquery value transfer
 * over the original KB and the SCSQA-local partition of the original KB
 * @author Lucy
 *
 */

public class QueryAnswerWithValueTransfer{
	public static Set<Map<String, String>> queryAnswer(Query query, List<KB> obdas) throws InterruptedException, ExecutionException{
		Set<Map<String, String>> answers=new HashSet<Map<String, String>>();
		Set<Set<Query>> partitions=PartitionQuery.partitionQuery(query);
		System.out.println("number of partitions: "+partitions.size());
		for(Set<Query> part: partitions){
			answers.addAll(AnswerQueryPartitionWithValueTransfer.answerOnePartition(part, obdas));
		}
		return answers;
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException{     
		Query query=new Query("q(?x,?y,?z,?e,?x1)<-12:Person(?x), 34:prizes(?x, ?x1), http://www.w3.org/2000/01/rdf-schema#comment(?x,?y), 175:doctoralAdvisor(?x,?z), http://www.w3.org/2000/01/rdf-schema#label(?z,?e)");
		
		List<KB> kbs=new ArrayList<KB>();          
		
		KB kb=new KB("BTC","BTC", "E:/Experiment/DLLitePartition/Data/BTC/KB/TBox.txt",  
				"E:/Experiment/DLLitePartition/Data/BTC/KB/CPMap.txt");    
		kbs.add(kb);             
		
//		for(int i=0; i<30; i++){                 
//		KB kb=new KB("BTC_"+i,"BTC_"+i, "E:/Experiment/DLLitePartition/Data/BTC/KBPartition/SubTBox"+i+".txt", 
//				"E:/Experiment/DLLitePartition/Data/BTC/KBPartition/CPMap"+i+".txt");
//		kbs.add(kb);  
//	    }      

		long start=System.currentTimeMillis();       
		QueryAnswerWithValueTransfer qa=new QueryAnswerWithValueTransfer();         
		Set<Map<String, String>> ans=qa.queryAnswer(query, kbs);
		System.out.println("total number of answers of this query: "+ans.size());    
		long end=System.currentTimeMillis(); 
		System.out.println("total time used: "+(end-start));                   
		
	}
}