package QueryPartitionAnswering;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import Query.Query;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import KBStructure.KB;
import OneQueryAnswering.AnswerAQueryOverKBS;
import OneQueryAnswering.AnswerAQueryOverKB;

/*********************************************************
 * answer a partition of a query without the strategy of subquery value transfer
 * @author Lucy
 *
 */
public class AnswerQueryPartitionWithoutValueTransfer{
   
	public static Set<Map<String, String>> answerOnePartition(Set<Query> partition, List<KB> kbs) throws InterruptedException, ExecutionException{
		Set<Map<String, String>> answers=new HashSet<Map<String, String>>();
		List<Set<Map<String, String>>> ans=new ArrayList<Set<Map<String, String>>>();
		System.out.println("answering query partition: ");
		int ct=0;
		for(Query query: partition){ 
			ct=ct+1;
			System.out.println("answering subQuery"+ct+": ");
			query.print();
			Set<Map<String, String>> subAns=new HashSet<Map<String, String>>();
			subAns.addAll(AnswerAQueryOverKBS.queryAnswerWithMultithread(query, kbs));
			if(subAns.size()>0){
				ans.add(subAns);
				System.out.println("number of subAnswers: "+subAns.size());
			}
			else{
				System.out.println("subQuery"+ct+" has empty answer set, return!!");
				return answers;
			}			
		}
		
		return mergeSubAnswer(ans);
		
	}
	
	public static Set<Map<String, String>> mergeSubAnswer(List<Set<Map<String, String>>> subAns){
		Set<Map<String, String>> ans_new=new HashSet<Map<String, String>>();
		ans_new.addAll(subAns.get(0));
		if(subAns.size()==1){
			return ans_new;
		}
		else{
			for(int i=1; i<subAns.size(); i++){
				Set<Map<String, String>> ans_added=new HashSet<Map<String, String>>();
				for(Map<String, String> map1: ans_new){
					lab: for(Map<String, String> map2: subAns.get(i)){
						Map<String, String> ans=new HashMap<String, String>();
						for(String s: map1.keySet()){
							if(map2.containsKey(s)){
								if(map1.get(s).equals(map2.get(s))){
									ans.put(s, map1.get(s));
								}
								else{
									continue lab;
								}
							}
						}
						ans.putAll(map2);
						ans_added.add(ans);
					}
				}
				ans_new.clear();
				ans_new.addAll(ans_added);
			}
		}
		return ans_new;
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException{ 
		AnswerQueryPartitionWithoutValueTransfer qa=new AnswerQueryPartitionWithoutValueTransfer(); 
		List<KB> kbs=new ArrayList<KB>();      
		Set<Query> partition=new HashSet<Query>();
		//q(?x)<-12:Person(?x), http://www.w3.org/2000/01/rdf-schema#label(?x,Tim Berners-Lee)
		//q(?x,?y,?z,?d)<-175:Film(?x), 175:writer(?x,15:Robert_Thoeren),  175:director(?x,?y), http://www.w3.org/2000/01/rdf-schema#label(?x,?z), 12:primaryTopic(?d,?x)
		
//		KB kb=new KB("dbpedia","dbpedia", "C:/Experiment/DLLitePartition/Data/dbpedia/KB/TBox.txt",         
//				"C:/Experiment/DLLitePartition/Data/dbpedia/KB/CPMap.txt");
//		kbs.add(kb);  
		
		for(int i=0; i<14; i++){       
			KB kb=new KB("dbpedia_"+i,"dbpedia_"+i, "C:/Experiment/DLLitePartition/Data/dbpedia/KBPartition/SubTBox"+i+".txt",         
					"C:/Experiment/DLLitePartition/Data/dbpedia/KBPartition/CPMap"+i+".txt"); 
			kbs.add(kb);
	    } 
		
		Query q1=new Query("q(?x)<-1:Person(?x)"); 
		Query q2=new Query("q(?x)<-http://www.w3.org/2000/01/rdf-schema#label(?x,Alan Turing@en)"); 
		partition.add(q1);
		partition.add(q2);      
		
		long start=System.currentTimeMillis();
		Set<Map<String, String>> ans=qa.answerOnePartition(partition, kbs);   
		long end=System.currentTimeMillis(); 
		System.out.println("total time used:==="+(end-start));   
	}

}