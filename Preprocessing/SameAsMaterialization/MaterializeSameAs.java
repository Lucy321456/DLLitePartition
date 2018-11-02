package SameAsMaterialization;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import BufferReader.FileReader;
import MetaTerms.MetaRole;

/********************************************
 * materialize sameAs relations by replacing equivalent terms with a delegate
 * @author Lucy
 *
 */

public class MaterializeSameAs{
	
	public void materializeSameAs(String onto, String onto_new, String sameAs){
		try{
			
			Map<String, String> rep=new HashMap<String, String>();			
			
			BufferedReader br_sameAs=FileReader.readFile(sameAs);  // a<>b, a will be replaced by b;
			String line_sameAs="";
			while((line_sameAs=br_sameAs.readLine())!=null){
				String[] nt=line_sameAs.split("<>");
				rep.put(nt[0], nt[1]);
			}
			System.out.println("Size of repElements: "+rep.size());
			int count=0;
			
			FileWriter fw=new FileWriter(onto_new);
			BufferedReader br_onto=FileReader.readFile(onto);
			String line_onto="";
			while((line_onto=br_onto.readLine())!=null){
				String[] nt=line_onto.split("<>");
				if(nt[1].equals(MetaRole.owl_sameAs)){
					continue;
				}
				count=count+1;
			    if(count%100000==0){
			    	System.out.println("count: "+count);
			    }
			    if(rep.containsKey(nt[0])){
		    		nt[0]=rep.get(nt[0]);
		    	}
			    if(rep.containsKey(nt[1])){
			    	nt[1]=rep.get(nt[1]);
			    }
			    if(!nt[2].startsWith("\"")){
			    	if(rep.containsKey(nt[2])){
			    		nt[2]=rep.get(nt[2]);
			    	}
			    }			    
				fw.write(nt[0]+"<>"+nt[1]+"<>"+nt[2]+"\r\n");
			}
			fw.close(); 
			
			
		}catch(Exception e){  
			e.printStackTrace();
		}
	}
	
	public void materializeSameAsNew(String onto, String onto_new, String sameAs){ 
		try{
			
			Map<String, Map<String, String>> index=new HashMap<String, Map<String, String>>();
			
			BufferedReader br_sameAs=FileReader.readFile(sameAs);  // a<>b, a will be replaced by b;
			String line_sameAs="";
			while((line_sameAs=br_sameAs.readLine())!=null){
				String[] nt=line_sameAs.split("<>");
				String s=nt[0].substring(0, nt[0].indexOf(":"));
				if(index.containsKey(s)){
					index.get(s).put(nt[0], nt[1]);
				}
				else{
					Map<String, String> map=new HashMap<String, String>();
					map.put(nt[0], nt[1]);
					index.put(s, map);
				}
			}
			System.out.println("Size of repElements: "+index.size());
			int count=0;
			
			FileWriter fw=new FileWriter(onto_new);
			BufferedReader br_onto=FileReader.readFile(onto);
			String line_onto="";
			while((line_onto=br_onto.readLine())!=null){
				String[] nt=line_onto.split("<>");
				if(nt[1].equals(MetaRole.owl_sameAs)){
					continue;
				}
				count=count+1;
			    if(count%100000==0){
			    	System.out.println("count: "+count);
			    }
			    
			    for(int i=0; i<3; i++){
			    	if(!nt[i].startsWith("\"")){
			    		String s=nt[i].substring(0, nt[i].indexOf(":"));
			    		if(index.containsKey(s)){
			    			if(index.get(s).containsKey(nt[i])){
			    				nt[i]=index.get(s).get(nt[i]);
			    			}
			    		}
			    	}
			    }			    
				fw.write(nt[0]+"<>"+nt[1]+"<>"+nt[2]+"\r\n");
			}
			fw.close(); 
			
			
		}catch(Exception e){  
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args){     
		long start=System.currentTimeMillis();
		MaterializeSameAs msa=new MaterializeSameAs();
		msa.materializeSameAsNew("E:/Experiment/DLLitePartition/Data/BTC/KB/KB_new.txt", "E:/Experiment/DLLitePartition/Data/BTC/KB/KB_noSameAs.txt", "E:/Experiment/DLLitePartition/Data/BTC/KB/repElement.txt");
		long end=System.currentTimeMillis(); 
		System.out.println("Time used (seconds): "+(end-start)/1000);
	}
}