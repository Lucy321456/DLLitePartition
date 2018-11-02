package SatisfiabilityChecking;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import KBStructure.KB;

class Task implements Callable<String>{
	private int id;
	public Task(int id){
		this.id=id;
	}
	public String call() throws Exception {
		for(int i=0; i<10; i++){
			System.out.println("#"+id+"a"+i);
		}
		return "a"+id;
	}
}

public class ThreadTest{
	public static List<Future<String>> checkSatisfiability(List<Integer> ints){
		boolean ck=true;
		ExecutorService exec=Executors.newCachedThreadPool();
		List<Future<String>> results=new ArrayList<Future<String>>();
		for(int i=0; i<ints.size(); i++){
			results.add(exec.submit(new Task(ints.get(i))));
		}
		exec.shutdown();
       return results;
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException{   
		ThreadTest CK=new ThreadTest();
		List<Integer> ints=new ArrayList<Integer>();
		for(int i=0; i<14; i++){
			ints.add(i);
		}
		List<Future<String>> list=CK.checkSatisfiability(ints);
//		for(int i=0; i<list.size(); i++){
//			System.out.println(list.get(i).get()); 
//		}
	}
}