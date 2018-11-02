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

/*********************************************
 * sort individual indices according to the numbers, 
 * @author Lucy
 *
 */

public class IndividualIndexSort{
	
	public static void sortIndIndex(String indIndex, String indIndex_new){
		try{
			List<Integer> num=new ArrayList<Integer>();			
			Map<Integer, Set<String>> index=new HashMap<Integer, Set<String>>();
			
			BufferedReader br=FileReader.readFile(indIndex);
			String line="";
			while((line=br.readLine())!=null){
				String[] nt=line.split("<>");
				int i=Integer.parseInt(nt[1]);
				if(!num.contains(i)){
					num.add(i);
					Set<String> set=new HashSet<String>();
					set.add(nt[0]);
					index.put(i, set);
				}
				else{
					index.get(i).add(nt[0]);
				}
			}
			System.out.println("finish reading");
			Collections.sort(num); 
			System.out.println("finish sorting"); 
			FileWriter fw=new FileWriter(indIndex_new);
			
			for(int i=num.size()-1; i>=0; i--){
				for(String s: index.get(num.get(i))){
					fw.write(s+"<>"+num.get(i)+"\r\n");
				}
			}
			
			fw.close();
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/***sorting for every large files*********************************************************************************************************/
	
	public static void filePartition_increase(String file, String path, int size){
		try{
			Map<Integer, Set<String>> map=new HashMap<Integer, Set<String>>();
			int num=0;
			int count=0;
			BufferedReader br=FileReader.readFile(file);
			String line="";
			while((line=br.readLine())!=null){
				count=count+1;
				System.out.println("count: "+count);
				String[] nt=line.split("<>");
				int n=Integer.parseInt(nt[1]);
				if(map.containsKey(n)){
					map.get(n).add(nt[0]);
				}
				else{
					Set<String> set=new HashSet<String>();
					set.add(nt[0]);
					map.put(n, set);
				}
				if(count%size==0){
					List<Integer> list=new ArrayList<Integer>();
					list.addAll(map.keySet());
					Collections.sort(list);
					FileWriter fw=new FileWriter(path+"file_"+num+".txt");
					for(int i: list){
						for(String s: map.get(i)){ 
							fw.write(s+"<>"+i+"\r\n");
						}
					}
					fw.close();
					num=num+1;
					map.clear();
				}
			}
			
			List<Integer> list=new ArrayList<Integer>();
			list.addAll(map.keySet());
			Collections.sort(list);
			FileWriter fw=new FileWriter(path+"file_"+num+".txt");
			for(int i: list){
				for(String s: map.get(i)){
					fw.write(s+"<>"+i+"\r\n");
				}
			}
			fw.close();
			num=num+1;
			map.clear();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void merge_increase(String path, String file){ 
		try{
			int total=0;
			int count=0;
			FileWriter fw=new FileWriter(file);
			Map<BufferedReader, ReadLine> map=new HashMap<BufferedReader, ReadLine>();
			File folder=new File(path);
			File[] files=folder.listFiles();
			Set<Integer> counts=new HashSet<Integer>();
			for(File f: files){
				InputStreamReader rd= new InputStreamReader(new FileInputStream(f));
				BufferedReader bw = new BufferedReader(rd);
				ReadLine rl=new ReadLine();
				String s=bw.readLine();				
				if(s==null){
					System.out.println(f.getName());
				}
				else{
					String[] nt=s.split("<>");
					count=count+1;
					rl.ind=nt[0];
					rl.count=Integer.parseInt(nt[1]);
					counts.add(Integer.parseInt(nt[1]));
					map.put(bw, rl);
				}
			}			
			while(true){ 
				int min=Collections.min(counts);
								
				for(BufferedReader br: map.keySet()){
					if(map.get(br).count==min){
						fw.write(map.get(br).ind+"<>"+map.get(br).count+"\r\n");
					}
				}
				total=total+1;
				counts.remove(min);
				
				Set<BufferedReader> deleteBR=new HashSet<BufferedReader>();
				for(BufferedReader br: map.keySet()){
					if(map.get(br).count==min){
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
							counts.add(Integer.parseInt(nt[1]));
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
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
/***sorting for every large files*********************************************************************************************************/
	
	public static void filePartition_decrease(String file, String path, int size){
		try{
			Map<Integer, Set<String>> map=new HashMap<Integer, Set<String>>();
			int num=0;
			int count=0;
			BufferedReader br=FileReader.readFile(file);
			String line="";
			while((line=br.readLine())!=null){
				count=count+1;
				System.out.println("count: "+count);
				String[] nt=line.split("<>");
				int n=Integer.parseInt(nt[1]);
				if(map.containsKey(n)){
					map.get(n).add(nt[0]);
				}
				else{
					Set<String> set=new HashSet<String>();
					set.add(nt[0]);
					map.put(n, set);
				}
				if(count%size==0){
					List<Integer> list=new ArrayList<Integer>();
					list.addAll(map.keySet());
					Collections.sort(list);
					FileWriter fw=new FileWriter(path+"file_"+num+".txt");
					for(int i=list.size()-1; i>=0; i--){
						for(String s: map.get(list.get(i))){ 
							fw.write(s+"<>"+list.get(i)+"\r\n");
						}
					}
					fw.close();
					num=num+1;
					map.clear();
				}
			}
			
			List<Integer> list=new ArrayList<Integer>();
			list.addAll(map.keySet());
			Collections.sort(list);
			FileWriter fw=new FileWriter(path+"file_"+num+".txt");
			for(int i=list.size()-1; i>=0; i--){
				for(String s: map.get(list.get(i))){
					fw.write(s+"<>"+list.get(i)+"\r\n");
				}
			}
			fw.close();
			num=num+1;
			map.clear();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void merge_decrease(String path, String file){
		try{
			int total=0;
			int count=0;
			FileWriter fw=new FileWriter(file);
			Map<BufferedReader, ReadLine> map=new HashMap<BufferedReader, ReadLine>();
			File folder=new File(path);
			File[] files=folder.listFiles();
			Set<Integer> counts=new HashSet<Integer>();
			for(File f: files){
				InputStreamReader rd= new InputStreamReader(new FileInputStream(f));//¶ÁÈ¡URIÎÄ¼þ
				BufferedReader bw = new BufferedReader(rd);
				ReadLine rl=new ReadLine();
				String s=bw.readLine();				
				if(s==null){
					System.out.println(f.getName());
				}
				else{
					String[] nt=s.split("<>");
					count=count+1;
					rl.ind=nt[0];
					rl.count=Integer.parseInt(nt[1]);
					counts.add(Integer.parseInt(nt[1]));
					map.put(bw, rl);
				}
			}			
			while(true){ 
				int max=Collections.max(counts);
								
				for(BufferedReader br: map.keySet()){
					if(map.get(br).count==max){
						fw.write(map.get(br).ind+"<>"+map.get(br).count+"\r\n");
					}
				}
				total=total+1;
				counts.remove(max);
				
				Set<BufferedReader> deleteBR=new HashSet<BufferedReader>();
				for(BufferedReader br: map.keySet()){
					if(map.get(br).count==max){
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
							counts.add(Integer.parseInt(nt[1]));
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
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String[] args){ 
		IndividualIndexSort si=new IndividualIndexSort();
		long start=System.currentTimeMillis();
		//si.sortIndIndex("E:/Experiment/DLLitePartition/Data/BTC/KB/indIndex.txt", "E:/Experiment/DLLitePartition/Data/BTC/KB/indIndex_new.txt");  
		//si.filePartition_increase("E:/Experiment/DLLitePartition/Data/BTC/KB/indIndex.txt", "E:/Experiment/DLLitePartition/Data/BTC/KB/files/", 1331799);
		si.merge_increase("E:/Experiment/DLLitePartition/Data/BTC/KB/files/", "E:/Experiment/DLLitePartition/Data/BTC/KB/indIndex_increase.txt");
		//si.filePartition_decrease("E:/Experiment/DLLitePartition/Data/BTC/KB/indIndex.txt", "E:/Experiment/DLLitePartition/Data/BTC/KB/files/", 1331799);
	    //si.merge_decrease("E:/Experiment/DLLitePartition/Data/BTC/KB/files/", "E:/Experiment/DLLitePartition/Data/BTC/KB/indIndex_increase.txt");
		long end=System.currentTimeMillis();
		System.out.println("used time (seconds): "+(end-start)/1000);
		
	}
}

class ReadLine{
	public String ind;
	public int count;
	ReadLine(){
		ind="";
		count=0;
	}
}