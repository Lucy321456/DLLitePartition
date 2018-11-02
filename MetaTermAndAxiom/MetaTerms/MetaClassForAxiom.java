package MetaTerms;

import java.io.BufferedReader;
import java.util.Set;
import java.util.HashSet;

import BufferReader.FileReader;

public class MetaClassForAxiom{
	public static Set<String> metaClass;
	
	public MetaClassForAxiom(){ 
		try{
			metaClass=new HashSet<String>();
			BufferedReader br=FileReader.readFile("MetaClassForAxiom.txt"); 
			String line="";
			while((line=br.readLine())!=null){
				metaClass.add(line);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}