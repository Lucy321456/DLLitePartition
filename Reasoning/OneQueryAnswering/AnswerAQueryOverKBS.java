package OneQueryAnswering;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import KBStructure.KB;
import Query.Query;

/*************************************************
 * answer a query over multiple KBs by multithreading without transfered values
 * @author Lucy
 *
 */

class QATask implements Callable<Set<Map<String, String>>>{
	private KB kb;
	private Query query;
	
	public QATask(KB kb, Query query){
		this.kb=kb;
		this.query=query;
	}
	
	public Set<Map<String, String>> call(){
		Set<Map<String, String>> answer=new HashSet<Map<String, String>>();
		answer.addAll(AnswerAQueryOverKB.queryAnswer(query, kb));
		return answer;
	}
}
public class AnswerAQueryOverKBS{
	public static Set<Map<String, String>> queryAnswerWithMultithread(Query query, List<KB> kbs) throws InterruptedException, ExecutionException{
		long start=System.currentTimeMillis();
		Set<Map<String, String>> answers=new HashSet<Map<String, String>>();
		List<Future<Set<Map<String, String>>>> results=new ArrayList<Future<Set<Map<String, String>>>>();
		ExecutorService exec=Executors.newCachedThreadPool();
		for(int i=0; i<kbs.size(); i++){
			results.add(exec.submit(new QATask(kbs.get(i),query)));
		}
		for(int i=0; i<results.size(); i++){
			answers.addAll(results.get(i).get());
		}
		exec.shutdown();
		System.out.println("answer size "+answers.size());
		long end=System.currentTimeMillis();
		System.out.println("time used by multiple thread: "+(end-start));  
		return answers;
	}
	
	public static Set<Map<String, String>> queryAnswerWithoutMultithread(Query query, List<KB> kbs) throws InterruptedException, ExecutionException{
		long sTime=System.currentTimeMillis();
		Set<Map<String, String>> answers=new HashSet<Map<String, String>>();
		for(KB kb: kbs){
			answers.addAll(AnswerAQueryOverKB.queryAnswer(query, kb));
		}
		System.out.println("answer size "+answers.size());
		long eTime=System.currentTimeMillis();
		System.out.println("time used without multiple thread: "+(eTime-sTime));  
		return answers;
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException{   
		AnswerAQueryOverKBS QA=new AnswerAQueryOverKBS(); 
		Query query=new Query("q(?x,?y,?z)<-1:Film(?x), 1:writer(?x,365:Robert_Thoeren),1:director(?x,?y),http://www.w3.org/2000/01/rdf-schema#comment(?x,?z), 32:primaryTopic(?d,?x)"); 
		List<KB> kbs=new ArrayList<KB>();
		for(int i=0; i<14; i++){
			KB kb=new KB("dbpedia_"+i,"dbpedia_"+i, "E:/Experiment/DLLitePartition/Data/dbpedia/KBPartition/SubTBox"+i+".txt", 
					"E:/Experiment/DLLitePartition/Data/dbpedia/KBPartition/CPMap"+i+".txt");
			kbs.add(kb); 
		}
		
		long start=System.currentTimeMillis();
		Set<Map<String, String>> answers=QA.queryAnswerWithMultithread(query, kbs);   
		long end=System.currentTimeMillis();
		System.out.println("total number of answers of this query: "+answers.size());
		System.out.println("total time used: "+(end-start));
	}
}