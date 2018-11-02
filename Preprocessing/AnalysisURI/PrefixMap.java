package AnalysisURI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import BufferReader.FileReader;
import MetaTerms.MetaRole;
import MetaTerms.NameSpace;
import NTFileAnalysis.TripleAnalysis;

/*******************************************
 * compute the prefixs of URIs in RDF triples and extract the owl:sameAs assertions
 * in the output file, a triple is represented as a<>b<>c where a, b, c respectively denote the subject, predict and subject
 * @author Lucy
 *
 */

public class PrefixMap{
	
	public void constructPrefixMap(String file, String newFile, String prefix, String sameAsFile){
		try{
			
			FileWriter fw=new FileWriter(newFile);
			FileWriter fw_sameAs=new FileWriter(sameAsFile);
			
			int count=0;
			Map<String, Integer> prefixIndex=new HashMap<String, Integer>();			
			BufferedReader br=FileReader.readFile(file);
			String line="";
			while((line=br.readLine())!=null){
				count=count+1;
				if(count%100000==0){
					System.out.println("count: "+count);
				}
				String[] nt=TripleAnalysis.ntTripleAnalysis(line);
				int label=0;
				if(nt[1].equals(MetaRole.owl_sameAs)){
					label=1;
				}
				for(int i=0; i<3; i++){
					if(!nt[i].startsWith("_:")&!nt[i].startsWith("\"")){
						if(!nt[i].startsWith(NameSpace.OWL)&!nt[i].startsWith(NameSpace.RDF)&!nt[i].startsWith(NameSpace.RDFS)){
							String[] pn=URIComputation.computeURI(nt[i]);
							if(prefixIndex.containsKey(pn[0])){
								nt[i]=prefixIndex.get(pn[0])+":"+pn[1];
							}
							else{
								prefixIndex.put(pn[0], prefixIndex.size()+1);
								nt[i]=prefixIndex.size()+1+":"+pn[1];
							}
						}						
					}					
				}
				fw.write(nt[0]+"<>"+nt[1]+"<>"+nt[2]+"\r\n");
				if(label==1){
					fw_sameAs.write(nt[0]+"<>"+nt[2]+"\r\n");
				}
			}
			fw.close();
			fw_sameAs.close();
			
			FileWriter index=new FileWriter(prefix);
			for(String s: prefixIndex.keySet()){
				index.write(s+"<>"+prefixIndex.get(s)+"\r\n");
			}
			index.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void constructPrefixMap(String path, String newPath){
		try{
			
			FileWriter fw=new FileWriter(newPath+"KB_new.txt");
			FileWriter fw_sameAs=new FileWriter(newPath+"SameAs.txt");
			
			List<String> tableFiles=new ArrayList<String>();      
			File file = new File(path);
			File[] Files = file.listFiles();
			for (int i = 0; i < Files.length; i++) {
			  if(!Files[i].isDirectory()){
			     tableFiles.add(Files[i].toString());
			  }
			}
			
			int count=0;
			Map<String, Integer> prefixIndex=new HashMap<String, Integer>();	
			
			for(int i=0; i<tableFiles.size(); i++){
				int index=0;
				BufferedReader br=FileReader.readFile(tableFiles.get(i));
				if(tableFiles.get(i).endsWith(".nq")){
					index=1;
				}
				String line="";
				while((line=br.readLine())!=null){
					count=count+1;
					if(count%100000==0){
						System.out.println("count: "+count);
					}
					String[] nt=null;
					if(index==1){
						nt=TripleAnalysis.nqTripleAnalysis(line);
					}
					else{
						nt=TripleAnalysis.ntTripleAnalysis(line);
					}
							
					int label=0;
					if(nt[1].equals(MetaRole.owl_sameAs)){
						label=1;
					}
					for(int j=0; j<3; j++){
						if(!nt[j].startsWith("_:")&!nt[j].startsWith("\"")){
							if(!nt[j].startsWith(NameSpace.OWL)&!nt[j].startsWith(NameSpace.RDF)&!nt[j].startsWith(NameSpace.RDFS)){
								String[] pn=URIComputation.computeURI(nt[j]);
								if(prefixIndex.containsKey(pn[0])){
									nt[j]=prefixIndex.get(pn[0])+":"+pn[1];
								}
								else{
									prefixIndex.put(pn[0], prefixIndex.size()+1);
									nt[j]=prefixIndex.size()+1+":"+pn[1];
								}
							}						
						}
						if(nt[j].length()>1820){
							nt[j]=nt[j].substring(0, 1820);
						}
					}
					fw.write(nt[0]+"<>"+nt[1]+"<>"+nt[2]+"\r\n");
					if(label==1){
						fw_sameAs.write(nt[0]+"<>"+nt[2]+"\r\n");
					}
				}
			}
			
			fw.close();
			fw_sameAs.close();
			
			FileWriter index=new FileWriter(newPath+"prefix.txt");
			for(String s: prefixIndex.keySet()){
				index.write(s+"<>"+prefixIndex.get(s)+"\r\n");
			}
			index.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){  
		PrefixMap pm=new PrefixMap();
		long start=System.currentTimeMillis();
//		pm.constructPrefixMap("E:/Experiment/DLLitePartition/Data/dbpedia/KB/KB.txt", "E:/Experiment/DLLitePartition/Data/dbpedia/KB/KB_New.txt",
//				"E:/Experiment/DLLitePartition/Data/dbpedia/KB/prefix.txt", "E:/Experiment/DLLitePartition/Data/dbpedia/KB/sameAs.txt");
		//pm.constructPrefixMap("E:/Experiment/DLLitePartition/Data/BTC/KB/BTCDataNonZip/", "E:/Experiment/DLLitePartition/Data/BTC/KB/BTCData/");
	   long end=System.currentTimeMillis();
	   System.out.println("used time (seconds): "+(end-start)/1000);
	}
}