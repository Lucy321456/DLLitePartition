package OneQueryAnswering;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import KBStructure.KB;
import Query.Atom;
import Query.Query;
import QueryRewrite.RewriteCQS;
import QueryRewrite.TranslateCQSToSQLS;




class QATTask implements Callable<Set<Map<String, String>>>{
	private Query query;
	private KB kb;
	private Set<Map<String, String>> binds;
	
	public QATTask(Query query, KB kb, Set<Map<String, String>> binds){
		this.query=query;
		this.kb=kb;
		this.binds=binds;
	}
	
	public Set<Map<String, String>> call(){
		return AnswerAQueryOverKBWithTransferedValue.queryAnswer(query, kb, binds);
	}
}

public class AnswerAQueryOverKBSWithTransferedValues{
	
	public static Set<Map<String, String>> queryAnswerWithMultithread(Query query, List<KB> obdas, Set<Map<String, String>> binds) throws InterruptedException, ExecutionException{
		long start=System.currentTimeMillis();
		Set<Map<String, String>> answers=new HashSet<Map<String, String>>();
		ExecutorService exec=Executors.newCachedThreadPool();
		List<Future<Set<Map<String, String>>>> results=new ArrayList<Future<Set<Map<String, String>>>>();
		for(int i=0; i<obdas.size(); i++){
			results.add(exec.submit(new QATTask(query, obdas.get(i),binds)));
		}
		for(int i=0; i<results.size(); i++){
			answers.addAll(results.get(i).get());
		}
		exec.shutdown();
		long end=System.currentTimeMillis();
		System.out.println("time used by multiple threads: "+(end-start));
		return answers;
	}
	
	public static Set<Map<String, String>> queryAnswerWithoutMultithread(Query query, List<KB> obdas, Set<Map<String, String>> binds) throws InterruptedException, ExecutionException{
		long start=System.currentTimeMillis();
		Set<Map<String, String>> answers=new HashSet<Map<String, String>>();
		for(KB kb: obdas){
			answers.addAll(AnswerAQueryOverKBWithTransferedValue.queryAnswer(query, kb, binds));
		}
		long end=System.currentTimeMillis();
		System.out.println("time used without multiple threads: "+(end-start));
		return answers;
	}
}