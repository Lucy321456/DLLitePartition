package ABoxPartition;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

import BufferReader.FileReader;

/***************************************************
 * partition individuals according to the sorted individual indices.
 * @author Lucy
 *
 */


public class IndividualPartition{
	
	
	
	public void partitionIndividual(String indIndex, String partition, int size){
		try{
			List<Integer> index=new ArrayList<Integer>();
			List<String> ind=new ArrayList<String>();
			
			BufferedReader br=FileReader.readFile(indIndex);
			String line="";
			while((line=br.readLine())!=null){
				String[] nt=line.split("<>");
				ind.add(nt[0]);
				index.add(Integer.parseInt(nt[1]));
			}
			
			System.out.println("finish reading individual index!");
			FileWriter fw=new FileWriter(partition);
			
			int label=0;
			int num=0;
			while(label==0){ 
				int subSize=0;

				Set<Integer> deleteIndex=new HashSet<Integer>();
				
				label=1;
				for(int i=0; i<index.size(); i++){
					if(index.get(i)>0){
						label=0;
						if(subSize+index.get(i)<=size+1000000){ 
							subSize=subSize+index.get(i);
							fw.write(ind.get(i)+"<>"+num+"\r\n");
							deleteIndex.add(i);
						}
						else{
							for(int j=index.size()-1; j>i; j--){
								if(index.get(j)>0){
									if(subSize+index.get(j)<=size+1000000){
										subSize=subSize+index.get(j);
										fw.write(index.get(j)+"<>"+num+"\r\n");
										deleteIndex.add(index.get(j));
									}
									else{
										break;
									}
								}															
							}
							break;
						}
					}															
				}
				
				System.out.println(num+"<>"+subSize);
				
				num=num+1;
				
				for(int d: deleteIndex){
					index.set(d, 0);
					ind.set(d, "");
				}

			}
			
			fw.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//individual partition for large files
	
	public void partitionIndividual_new(String indIndex_decrease, String indIndex_increase, String partition, int size){
		try{ 
			FileWriter fw=new FileWriter(partition);
			int subSize=0;
			int num=0;
			
			int count=2;
			BufferedReader br_d=FileReader.readFile(indIndex_decrease);
			BufferedReader br_i=FileReader.readFile(indIndex_increase);
			String line_d=br_d.readLine();
			String line_i=br_i.readLine();
			lab1: while(line_d!=null){	
				while(subSize<=size){
					if(line_d!=null){
						if(line_d.equals(line_i)){
							System.out.println("over||over");
							String[] nt=line_d.split("<>");
							fw.write(nt[0]+"<>"+num+"\r\n");
							line_d=null;
							System.out.println(num+"<>"+subSize); 
							continue lab1;
						}
						else{
							String[] nt=line_d.split("<>");
							int n=Integer.parseInt(nt[1]);
							if(subSize+n<=size){
								fw.write(nt[0]+"<>"+num+"\r\n");
								subSize=subSize+n;
								line_d=br_d.readLine();
								count=count+1; 
							}
							else{
								break;
							}
						}
					}					
				}
				if(line_d!=null){
					while(subSize<=size){
						if(line_i.equals(line_d)){  
							System.out.println("over--over");
							String[] nt=line_i.split("<>");
							fw.write(nt[0]+"<>"+num+"\r\n");
							line_d=null;
							System.out.println(num+"<>"+subSize); 
							continue lab1;
						}
						else{
							String[] nt=line_i.split("<>");
							int n=Integer.parseInt(nt[1]);
							if(subSize+n<=size){
								fw.write(nt[0]+"<>"+num+"\r\n");
								subSize=subSize+n;
								line_i=br_i.readLine();
								count=count+1;
							}
							else{
								break;
							}
						}						
					}
				}
				System.out.println(num+"<>"+subSize);  
				subSize=0;
				num=num+1; 
			}
			System.out.println("count: "+count);
			fw.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){ 
		IndividualPartition IP=new IndividualPartition();  
		long start=System.currentTimeMillis();
		//IP.partitionIndividual("E:/Experiment/DLLitePartition/Data/dbpedia/KB/IndIndex_new.txt", "E:/Experiment/DLLitePartition/Data/dbpedia/KB/IndPartition.txt", 27571044);
		IP.partitionIndividual_new("E:/Experiment/DLLitePartition/Data/BTC/KB/indIndex_decrease.txt", "E:/Experiment/DLLitePartition/Data/BTC/KB/indIndex_increase.txt", 
				"E:/Experiment/DLLitePartition/Data/BTC/KB/indPartition.txt", 52022789);
		long end=System.currentTimeMillis();
		System.out.println("used time (seconds): "+(end-start)/1000);
	}
}
