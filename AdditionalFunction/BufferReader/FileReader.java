package BufferReader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class FileReader{
	
	public static BufferedReader readFile(String file){
		BufferedReader bw =null;
		try{
			InputStreamReader rdf= new InputStreamReader(new FileInputStream(file));//先由inclusion axiom计算一个基本的extension
			bw= new BufferedReader(rdf);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return bw;
	}
}