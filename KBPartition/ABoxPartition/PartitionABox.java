package ABoxPartition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.BufferedReader;
import java.io.FileWriter;

import BufferReader.FileReader;
import MetaTerms.MetaRole;
import VirtualABoxConstruction.CreateOneVirtualABox;

/*********************************************
 * partition ABox according to individual set partition
 * for each individual group G in an individual set partition, generate a subABox consisting of all the triples containing individuals in G
 * @author Lucy
 *
 */

public class PartitionABox{
	
	public void aboxPartition(String aboxFile, String indToSubABoxIndex, String subABoxPath){ 
		
		try{
			Map<String, Integer> indIndex=new HashMap<String, Integer>();
			Set<Integer> SubABoxNum=new HashSet<Integer>();
			
			int ct=0;
			BufferedReader br_IndIndex=FileReader.readFile(indToSubABoxIndex);
			String line_IndIndex="";
			while((line_IndIndex=br_IndIndex.readLine())!=null){
				ct=ct+1;
				if(ct%100000==0){
					System.out.println("ct: "+ct);
				}
				String[] nt=line_IndIndex.split("<>");
				int n=Integer.parseInt(nt[1]);
				indIndex.put(nt[0], n); 
				SubABoxNum.add(n);								
			}
			
			System.out.println("finish reading indindex!!");   
			
			Map<Integer, FileWriter> fws=new HashMap<Integer, FileWriter>();
			for(int i: SubABoxNum){ 
				FileWriter fw=new FileWriter(subABoxPath+"SubABox_"+i+".txt");
				fws.put(i, fw);
			}
			
			int count=0;
			BufferedReader br_ABox=FileReader.readFile(aboxFile);
			String line_ABox="";
			while((line_ABox=br_ABox.readLine())!=null){
				count=count+1;
				if(count%100000==0){
					System.out.println("count: "+count);
				}
				String[] nt=line_ABox.split("<>");
				if(!nt[0].startsWith("\"")){
					if(indIndex.containsKey(nt[0])){
						fws.get(indIndex.get(nt[0])).write(line_ABox+"\r\n");
					}					
				}
				if(!nt[1].equals(MetaRole.rdf_type)){
					if(!nt[2].startsWith("\"")){
						if(indIndex.containsKey(nt[2])){
							fws.get(indIndex.get(nt[2])).write(line_ABox+"\r\n");
						}						
					}
				}
			}			
			
			for(int k: fws.keySet()){
				fws.get(k).close();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){   
		try{
			long start=System.currentTimeMillis();
			PartitionABox pa=new PartitionABox();
			pa.aboxPartition("E:/Experiment/DLLitePartition/Data/BTC/KB/ABox.txt", "E:/Experiment/DLLitePartition/Data/BTC/KB/IndPartition.txt", "E:/Experiment/DLLitePartition/Data/BTC/KBPartition/"); 
            long end=System.currentTimeMillis();
            System.out.println("used time (seconds): "+(end-start)/1000);
		}catch(Exception e){
			e.printStackTrace(); 
		}

		
	}
	
}