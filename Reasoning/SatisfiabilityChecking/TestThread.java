package SatisfiabilityChecking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import KBStructure.KB;

class CKTask0 implements Callable<Boolean> {
	private int sleepTime;
	private KB kb;
	
	public CKTask0(int sleepTime, KB kb) {
		this.sleepTime = sleepTime;
		this.kb=kb;
	}
	public Boolean call() throws Exception {
		Thread.sleep(sleepTime * 1000);
		return CheckSatisfiabilityOfKB.checkSatisfiability(kb);
	}
}

public class TestThread {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		int size = 10;
		long sTime=System.currentTimeMillis();
		List<KB> kbs=new ArrayList<KB>();
		for(int i=0; i<29; i++){
			KB kb=new KB("btc_"+i,"btc_"+i, "E:/Experiment/DLLitePartition/Data/BTC/KBPartition/SubTBox"+i+".txt", 
					"E:/Experiment/DLLitePartition/Data/BTC/KBPartition/CPMap"+i+".txt");
			kbs.add(kb);
		}
	
		ExecutorService exec = Executors.newCachedThreadPool();
		List<Future<Boolean>> results = new ArrayList<>();
		for(int i = 0; i < kbs.size(); i++) {
			results.add(exec.submit(new CKTask0(Math.max(i, 2), kbs.get(i))));
		}
		while(true) {
			int finished=1;
			for(Future<Boolean> future : results) {
				if(future.isDone() && !future.get()){
					for(Future<Boolean> tmp : results) {
						Thread.interrupted();
//						exec.shutdownNow();
						tmp.cancel(true);
					}
					exec.shutdown();
					break;
				}
				else{
					finished=0;
				}
			}
			if(finished==1){
				break;
			}
		}
        long eTime=System.currentTimeMillis();
        System.out.println("==========total time used: "+(eTime-sTime));
	}
}
