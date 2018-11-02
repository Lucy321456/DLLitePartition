package QueryPartitionAnswering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import KBStructure.KB;
import OneQueryAnswering.AnswerAQueryOverKBS;
import OneQueryAnswering.AnswerAQueryOverKBSWithTransferedValues;
import OneQueryAnswering.AnswerAQueryOverKB;
import OneQueryAnswering.AnswerAQueryOverKBWithTransferedValue;
import Query.Atom;
import Query.Query;
import QueryRewrite.RewriteCQS;

/*****************************************
 * answer a partition of a conjunctive query with the strategy of subquery value transfer
 * @author Lucy
 *
 */

public class AnswerQueryPartitionWithValueTransfer{
	public static Set<Map<String, String>> answerOnePartition(Set<Query> partition, List<KB> kbs) throws InterruptedException, ExecutionException{
		Set<Map<String, String>> answers=new HashSet<Map<String, String>>();
		List<Query> partition_new=OrderPartition.orderPartition(partition);
		System.out.println("answer query partition: ");
		for(Query q: partition_new){
			q.print();
		}
		System.out.println("answering subQuery: "+partition_new.get(0).toString());
		answers.addAll(AnswerAQueryOverKBS.queryAnswerWithMultithread(partition_new.get(0),kbs));		
		if(answers.size()==0){
			System.out.println("subQuery0 has empty answer set, return!!");
			return answers;
		}
		System.out.println("total number of answers of subQuery_0: "+answers.size());
		for(int i=1; i<partition_new.size(); i++){
			System.out.println("answering subQuery_"+i+": ");
			Set<Map<String, String>> subAns=new HashSet<Map<String, String>>(); 
			Set<Map<String, String>> varBinds=ComputeVarBinds.computeVarBinds(answers, partition_new.get(i).heads); 
			
			System.out.println("subQuery "+i+" obtains binds size: "+varBinds.size());
			subAns.addAll(AnswerAQueryOverKBSWithTransferedValues.queryAnswerWithMultithread(partition_new.get(i), kbs, varBinds));			
			System.out.println("total number of answers of subQuery"+i+": "+subAns.size());
			if(subAns.size()==0){
				answers.clear();
				System.out.println("subQuery"+i+" has empty answer set, return!!");
				return answers;
			}
			else{
				answers=MergeSubAnswer.mergeSubAnswer(answers, subAns);
			}
		}
		return answers;
	}
		
	public static void main(String[] args) throws InterruptedException, ExecutionException{
		AnswerQueryPartitionWithValueTransfer qa=new AnswerQueryPartitionWithValueTransfer();
		List<KB> kbs=new ArrayList<KB>();      
		Set<Query> partition=new HashSet<Query>();
		//q(?x)<-12:Person(?x), http://www.w3.org/2000/01/rdf-schema#label(?x,Tim Berners-Lee)
		//q(?x,?y,?z,?d)<-175:Film(?x), 175:writer(?x,15:Robert_Thoeren),  175:director(?x,?y), http://www.w3.org/2000/01/rdf-schema#label(?x,?z), 12:primaryTopic(?d,?x)
		
//		KB kb=new KB("dbpedia","dbpedia", "E:/Experiment/DLLitePartition/Data/dbpedia/KB/TBox.txt",         
//				"E:/Experiment/DLLitePartition/Data/dbpedia/KB/CPMap.txt");
//		kbs.add(kb);      
		
		for(int i=0; i<30; i++){         
			KB kb=new KB("BTC_"+i,"BTC_"+i, "E:/Experiment/DLLitePartition/Data/BTC/KBPartition/SubTBox"+i+".txt",         
					"E:/Experiment/DLLitePartition/Data/BTC/KBPartition/CPMap"+i+".txt"); 
			kbs.add(kb); 
	    } 
		
		Query q1=new Query("q(?x)<-http://www.w3.org/2000/01/rdf-schema#label(?x,Tim Berners-Lee)"); 
		Query q2=new Query("q(?x)<-12:Person(?x)");   
		partition.add(q1);
		partition.add(q2);       
		
		long start=System.currentTimeMillis(); 
		Set<Map<String, String>> ans=qa.answerOnePartition(partition, kbs);    
		long end=System.currentTimeMillis(); 
		System.out.println("total number of answers of this query partition: "+ans.size());
		System.out.println("total time used:==="+(end-start));  
	}
}