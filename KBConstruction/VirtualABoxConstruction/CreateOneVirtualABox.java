package VirtualABoxConstruction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;



import BufferReader.FileReader;
import MetaTerms.MetaRole;


public class CreateOneVirtualABox{
	
	public static void extractInd(String abox, String indPath, String mapFile, String sizeFile){
		try{
					
			List<String> tables=new ArrayList<String>();
			List<FileWriter> files=new ArrayList<FileWriter>();
			
			Map<String, Integer> claIndex=new HashMap<String, Integer>();
			Map<String, Integer> proIndex=new HashMap<String, Integer>();
			
			Map<String, Set<String>> claInd=new HashMap<String, Set<String>>();
			Map<String, Set<String>> proInd=new HashMap<String, Set<String>>();
			
			int count=0;
			BufferedReader br=FileReader.readFile(abox);
			String line="";
			while((line=br.readLine())!=null){ 	
				count=count+1;
				if(count%100000==0){
					System.out.println("count: "+count);
				}
				String[] nt=line.split("<>");
				if(nt[1].equals(MetaRole.rdf_type)){
					if(claInd.containsKey(nt[2])){
						claInd.get(nt[2]).add(nt[0]);
					}
					else{
						Set<String> set=new HashSet<String>();
						set.add(nt[0]);
						claInd.put(nt[2], set);
					}
					if(!claIndex.containsKey(nt[2])){
						tables.add("CT"+claIndex.size());
						FileWriter fw=new FileWriter(indPath+"CT"+claIndex.size()+".txt");
						files.add(fw);
						claIndex.put(nt[2], tables.size()-1);
					}
				}
				else{
					if(nt[2].contains("^^")){
						nt[2]=nt[2].substring(0, nt[2].indexOf("^^"));
					}
					nt[2]=nt[2].replace("\"", "");
					nt[2]=nt[2].replace("\\", "");
					if(nt[2].length()>1820){
						nt[2]=nt[2].substring(0, 1820);
					}
					if(nt[0].endsWith("\\")){
						nt[0]=nt[0].substring(0, nt[0].length()-1);
					}
					
					if(proInd.containsKey(nt[1])){
						proInd.get(nt[1]).add("\""+nt[0]+"\",\""+nt[2]+"\"");
					}
					else{
						Set<String> set=new HashSet<String>();
						set.add("\""+nt[0]+"\",\""+nt[2]+"\"");
						proInd.put(nt[1], set);
					}
					if(!proIndex.containsKey(nt[1])){
						tables.add("PT"+proIndex.size());
						FileWriter fw=new FileWriter(indPath+"PT"+proIndex.size()+".txt");
						files.add(fw);
						proIndex.put(nt[1], tables.size()-1);
					}
				}
				if(count%1000000==0){
					for(String c: claInd.keySet()){
						for(String ind: claInd.get(c)){
							files.get(claIndex.get(c)).write(ind+"\r\n");
						}
					}					
					for(String r: proInd.keySet()){
						for(String ind: proInd.get(r)){
							files.get(proIndex.get(r)).write(ind+"\r\n");
						}
					}
					claInd.clear();
					proInd.clear();
				}
			}
			
			for(String c: claInd.keySet()){
				for(String ind: claInd.get(c)){
					files.get(claIndex.get(c)).write(ind+"\r\n");
				}
			}
			for(String r: proInd.keySet()){
				for(String ind: proInd.get(r)){
					files.get(proIndex.get(r)).write(ind+"\r\n");
				}
			}
			claInd.clear();
			proInd.clear();
			
			
			for(int i=0; i<files.size(); i++){
				files.get(i).close();
			}
			files.clear();
			
			FileWriter fwMap=new FileWriter(mapFile);
			
			for(String c: claIndex.keySet()){
				fwMap.write(c+"<>"+tables.get(claIndex.get(c))+"\r\n");
			}
			for(String r: proIndex.keySet()){
				fwMap.write(r+"<>"+tables.get(proIndex.get(r))+"\r\n");
			}
			fwMap.close();
			tables.clear();
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
  
	
	public static void main(String[] args){
		try{
			long start=System.currentTimeMillis(); 
			
			
			CreateOneVirtualABox cva=new CreateOneVirtualABox(); 

			
			cva.extractInd("E:/Experiment/DLLitePartition/Data/dbpedia/KBPartition/SubABox_0.txt", "E:/Experiment/DLLitePartition/Data/dbpedia/KBPartition/VABox/",
					"E:/Experiment/DLLitePartition/Data/dbpedia/KBPartition/CPMap0.txt", "E:/Experiment/DLLitePartition/Data/dbpedia/KBPartition/SizeOfDB0.txt");
								
			
			long end=System.currentTimeMillis(); 
			
			System.out.println("time used (seconds): "+(end-start)/1000);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
