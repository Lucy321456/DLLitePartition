package TBoxABox;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;

import BufferReader.FileReader;
import MetaTerms.MetaClass;
import MetaTerms.MetaClassForAxiom;
import MetaTerms.MetaRole;
import MetaTerms.MetaRoleForAxiom;
import MetaTerms.NameSpace;
import NTFileAnalysis.TripleAnalysis;

/*********************************
 * extract the TBox and ABox of an ontology. In the input and the output ontology, a triple is represented as a<>b<>c where a, b and c respectively denote subject, predict and object
 * @author Lucy
 */
public class ExtractTBoxABox{ 
	
	public void extractTboxAbox(String ontoFile, String tboxFile, String aboxFile){ 
		
		try{
			int aboxCount=0;
			Set<String> metaTerm=new HashSet<String>(); 

			MetaRoleForAxiom mra=new MetaRoleForAxiom();
			MetaClassForAxiom mca=new MetaClassForAxiom();			
			
			FileWriter abox=new FileWriter(aboxFile);
			FileWriter tbox=new FileWriter(tboxFile);
			
			int count=0;
			BufferedReader br=FileReader.readFile(ontoFile);
			String line="";
			while((line=br.readLine())!=null){
				count=count+1;
				if(count%100000==0){
					System.out.println("count: "+count);
				}
				String[] nt=line.split("<>");	
				
				if(nt[1].equals(MetaRole.rdf_type)){
					if(mca.metaClass.contains(nt[2])){
						tbox.write(nt[0]+"<>"+nt[1]+"<>"+nt[2]+"\r\n");
					}
					else{
						abox.write(nt[0]+"<>"+nt[1]+"<>"+nt[2]+"\r\n");
						aboxCount=aboxCount+1;
					}
				}
				else if(mra.metaRole.contains(nt[1])){
					tbox.write(nt[0]+"<>"+nt[1]+"<>"+nt[2]+"\r\n");
				}
				else{
					abox.write(nt[0]+"<>"+nt[1]+"<>"+nt[2]+"\r\n");
					aboxCount=aboxCount+1;
				}
				
			}
			
			abox.close();
			tbox.close();
			
			System.out.println("SizeOfTheOriginalABox: "+aboxCount);
			System.out.println("MetaTermsUsed: ");
			for(String s: metaTerm){
				System.out.println(s+"\r\n"); 
			}
			
		}catch(Exception e){  
			e.printStackTrace(); 
		}
	}
	
	public static void main(String[] args){     
		long start=System.currentTimeMillis();
		ExtractTBoxABox eta=new ExtractTBoxABox();
		eta.extractTboxAbox("E:/Experiment/DLLitePartition/Data/BTC/KB/KB_withoutRepeat.txt", "E:/Experiment/DLLitePartition/Data/BTC/KB/TBox.txt", "E:/Experiment/DLLitePartition/Data/BTC/KB/ABox.txt"); 
	    long end=System.currentTimeMillis();
	    System.out.println("used time (seconds): "+(end-start)/1000);
	}
}