package SatisfiabilityChecking;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import KBStructure.KB;

/*********************************************************
 * checking satisfiability of subKBs with parallelization (multithreading)
 * @author Lucy
 *
 */

class CKTask implements Callable<Boolean>{
	private KB kb;
	public CKTask(KB kb){
		this.kb=kb;
	}
	public Boolean call() throws Exception {
		return CheckSatisfiabilityOfKB.checkSatisfiability(kb);
	}
}

public class CheckSatisfiabilityOfKBS{
	public static boolean checkSatisfiability(List<KB> kbs) throws InterruptedException, ExecutionException{
		ExecutorService exec=Executors.newCachedThreadPool();
		List<Future<Boolean>> results=new ArrayList<Future<Boolean>>();
		for(int i=0; i<kbs.size(); i++){
			results.add(exec.submit(new CKTask(kbs.get(i))));
		}
		
		boolean flag=false;
		while(true){
			int finished=1;
			for(int i=0; i<results.size();i++){
				if(results.get(i).isDone()){
					if(!results.get(i).get()){
//						for(int j=0; j<results.size(); j++){
//							results.get(j).cancel(true);	
//						}
						exec.shutdownNow();
						System.out.println(exec.isShutdown() + "~~~~~~~~~~~~~~~~~~~~" + exec.isTerminated()); 
						return false;
					}
				}
				else{
					finished=0;
				}
				
			}
			if(finished==1){
				break;
			}
		}	
		exec.shutdown(); 
		return true;

	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException{  
		CheckSatisfiabilityOfKBS CK=new CheckSatisfiabilityOfKBS();
		List<KB> kbs=new ArrayList<KB>();
		for(int i=0; i<14; i++){
			KB kb=new KB("dbpedia_"+i,"dbpedia_"+i, "E:/Experiment/DLLitePartition/Data/dbpedia/KBPartition/SubTBox"+i+".txt", 
					"E:/Experiment/DLLitePartition/Data/dbpedia/KBPartition/CPMap"+i+".txt");  
			kbs.add(kb);
		}
		long start=System.currentTimeMillis(); 
		boolean b=CK.checkSatisfiability(kbs);		
		System.out.println("Satisfiability Checking Results: "+b);  
		long end=System.currentTimeMillis();
		System.out.println("total time used: "+(end-start));     
	}
}