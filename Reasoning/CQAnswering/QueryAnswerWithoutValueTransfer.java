package CQAnswering;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import KBStructure.KB;
import Query.Query;
import QueryPartition.PartitionQuery;
import QueryPartitionAnswering.AnswerQueryPartitionWithoutValueTransfer;

/*************************************************
 * query answering with query partition but without subquery value transfer
 * over the original KB and the SCSQA-local partition of the original KB
 * @author Lucy
 *
 */

public class QueryAnswerWithoutValueTransfer{
	
	public static Set<Map<String, String>> queryAnswer(Query query, List<KB> obdas) throws InterruptedException, ExecutionException{
		Set<Map<String, String>> answers=new HashSet<Map<String, String>>();
		Set<Set<Query>> partitions=PartitionQuery.partitionQuery(query);
		for(Set<Query> set: partitions){
			answers.addAll(AnswerQueryPartitionWithoutValueTransfer.answerOnePartition(set, obdas));
		}
		return answers;
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException{
		QueryAnswerWithoutValueTransfer qa=new QueryAnswerWithoutValueTransfer();
        Query query=new Query("q(?x,?y,?z,?d)<-1:Work(?x), 1:writer(?x,365:Robert_Thoeren),1:director(?x,?y), 32:primaryTopic(?z,?x), 1:birthDate(?y,?d)"); 

		List<KB> kbs=new ArrayList<KB>();    
		
//		KB kb=new KB("dbpedia","dbpedia", "E:/Experiment/DLLitePartition/Data/dbpedia/KB/TBox.txt",     
//				"E:/Experiment/DLLitePartition/Data/dbpedia/KB/CPMap.txt");   
//		kbs.add(kb);        
		
		for(int i=0; i<14; i++){         
			KB kb=new KB("dbpedia_"+i,"dbpedia_"+i, "E:/Experiment/DLLitePartition/Data/dbpedia/KBPartition/SubTBox"+i+".txt",  
					"E:/Experiment/DLLitePartition/Data/dbpedia/KBPartition/CPMap"+i+".txt");
			kbs.add(kb); 
		}  

		long start=System.currentTimeMillis();    
		Set<Map<String, String>> ans=qa.queryAnswer(query, kbs);  
		System.out.println("total number of answers: "+ans.size());  
		long end=System.currentTimeMillis(); 
		System.out.println("total time used: "+(end-start));       
	}
}