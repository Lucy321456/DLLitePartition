package ABoxPartition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import BufferReader.FileReader;
import MetaTerms.MetaRole;
import Structure.Count;

/**********************************************************
 * compute individual index, Ind<>Num, i.e., compute the number Num of the assertions using Ind as individual
 * @author Lucy
 *
 */

public class IndividualIndex{
	
	/************for small files******************************************************/
	public void indexIndividual(String aboxFile, String indFile){
		
		try{
			Map<String, Count> indIndex=new HashMap<String, Count>();
			
			int count=0;
			
			BufferedReader br=FileReader.readFile(aboxFile);
			String line="";
			while((line=br.readLine())!=null){
				count=count+1;
				if(count%100000==0){ 
					System.out.println("count: "+count);
				}
				String[] nt=line.split("<>");
				if(!nt[0].startsWith("\"")){
					if(indIndex.containsKey(nt[0])){
						indIndex.get(nt[0]).count=indIndex.get(nt[0]).count+1;
					}
					else{
						Count ct=new Count();
						ct.count=1;
						indIndex.put(nt[0], ct);
					}
					if(!nt[1].equals(MetaRole.rdf_type)){
						if(!nt[2].startsWith("\"")){
							if(indIndex.containsKey(nt[2])){
								indIndex.get(nt[2]).count=indIndex.get(nt[2]).count+1;
							}
							else{
								Count ct=new Count();
								ct.count=1;
								indIndex.put(nt[2], ct);
							}
						}
					}
				}
			}
			
			FileWriter fw=new FileWriter(indFile);
			int max=0;
			
			for(String s: indIndex.keySet()){
				fw.write(s+"<>"+indIndex.get(s).count+"\r\n");
				if(indIndex.get(s).count>max){
					max=indIndex.get(s).count;
				}
			}
			
			System.out.println("IndividualSize: "+indIndex.size());
			System.out.println("MaxSiz: "+max);
			
			fw.close();
			
			
		}catch(Exception e){
			e.printStackTrace();
		}		
	}  
	
	/*************************for very large files*******************************************************************/
	
	public void indexIndividualForLargeFile(String abox, int size, String path){
		try{
			Map<String, Count> index=new HashMap<String, Count>();
			int count=0;
			int num=0;
			BufferedReader br=FileReader.readFile(abox);
			String line="";
			while((line=br.readLine())!=null){
				count=count+1;
				String[] nt=line.split("<>");
				if(index.containsKey(nt[0])){
					index.get(nt[0]).count=index.get(nt[0]).count+1;
				}
				else{
					Count ct=new Count();
					ct.count=1;
					index.put(nt[0], ct);
				}
				if(!nt[1].equals(MetaRole.rdf_type)&!nt[2].startsWith("\"")){
					if(index.containsKey(nt[2])){
						index.get(nt[2]).count=index.get(nt[2]).count+1;
					}
					else{
						Count ct=new Count();
						ct.count=1;
						index.put(nt[2], ct);
					}
				}
				if(count%size==0){
					System.out.println("count: "+count);
					List<String> inds=new ArrayList<String>();
					inds.addAll(index.keySet());
					Collections.sort(inds);
					FileWriter fw=new FileWriter(path+"file"+num+".txt");
					for(int i=0; i<inds.size(); i++){
						fw.write(inds.get(i)+"<>"+index.get(inds.get(i)).count+"\r\n");
					}
					fw.close();
					num=num+1;
					System.out.println("finish writing!");
					inds.clear();
					index.clear();
				}
			}
			
			List<String> inds=new ArrayList<String>();
			inds.addAll(index.keySet());
			Collections.sort(inds);
			FileWriter fw=new FileWriter(path+"file"+num+".txt");
			for(int i=0; i<inds.size(); i++){
				fw.write(inds.get(i)+"<>"+index.get(inds.get(i)).count+"\r\n");
			}
			fw.close();
			num=num+1;
			System.out.println("finish writing!");
			index.clear();
			inds.clear();
			
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	public void mergeIndIndex(String path, String index){
		try{
			int total=0;
			int count=0;
			FileWriter fw=new FileWriter(index);
			Map<BufferedReader, readIndex> map=new HashMap<BufferedReader, readIndex>();
			File folder=new File(path);
			File[] files=folder.listFiles();
			Set<String> lines=new HashSet<String>();
			for(File f: files){
				InputStreamReader rd= new InputStreamReader(new FileInputStream(f));//¶ÁÈ¡URIÎÄ¼þ
				BufferedReader bw = new BufferedReader(rd);
				readIndex ri=new readIndex();
				String s=bw.readLine();				
				if(s==null){
					System.out.println(f.getName());
				}
				else{
					String[] nt=s.split("<>");
					count=count+1;
					ri.ind=nt[0];
					ri.count=Integer.parseInt(nt[1]);
					lines.add(nt[0]);
					map.put(bw, ri);
				}
			}			
			while(true){ 
				String min=Collections.min(lines);
				int num=0;
				for(BufferedReader br: map.keySet()){
					if(map.get(br).ind.equals(min)){
						num=num+map.get(br).count;
					}
				}
				fw.write(min+"<>"+num+"\r\n");
				total=total+1;
				lines.remove(min);
				Set<BufferedReader> deleteBR=new HashSet<BufferedReader>();
				for(BufferedReader br: map.keySet()){
					if(map.get(br).ind.equals(min)){
						String s=br.readLine();
						count=count+1;
						if(count%1000==0){
							System.out.println("count: "+count);
						}
						if(s==null){
							deleteBR.add(br);
						}
						else{
							String[] nt=s.split("<>");
							map.get(br).ind=nt[0];
							map.get(br).count=Integer.parseInt(nt[1]);
							lines.add(nt[0]);
						}
					}
				}	
				for(BufferedReader br: deleteBR){
					map.remove(br);
				}				
				if(map.size()==0){
					break;
				}
			}
			fw.close();
			
			System.out.println("the total number of different individuals: "+total);
		}catch(Exception e){ 
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){  
		IndividualIndex II=new IndividualIndex();  
		long start=System.currentTimeMillis();
		//II.indexIndividualForLargeFile("E:/Experiment/DLLitePartition/Data/BTC/KB/ABox.txt", 5000000, "E:/Experiment/DLLitePartition/Data/BTC/KB/files/");		
		II.mergeIndIndex("E:/Experiment/DLLitePartition/Data/BTC/KB/files/", "E:/Experiment/DLLitePartition/Data/BTC/KB/indIndex.txt"); 
		long end=System.currentTimeMillis();
		System.out.println("used time (seconds): "+(end-start)/1000);
	}
}

class readIndex{ 
	public String ind;
	public int count;
	public readIndex(){
		ind="";
		count=0;
	}
}