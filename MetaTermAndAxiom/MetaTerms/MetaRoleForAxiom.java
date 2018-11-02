package MetaTerms;

import java.io.BufferedReader;
import java.util.Set;
import java.util.HashSet;

import BufferReader.FileReader;

public class MetaRoleForAxiom{
	public static Set<String> metaRole;
	
	public MetaRoleForAxiom(){
		try{
			metaRole=new HashSet<String>();
			BufferedReader br=FileReader.readFile("MetaRoleForAxiom.txt");
			String line="";
			while((line=br.readLine())!=null){
				metaRole.add(line);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}