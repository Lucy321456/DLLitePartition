package NTFileAnalysis;

/*************************************************
 * analyze the subject, predict and object of a triple
 * @author Lucy
 *
 */
public class TripleAnalysis{	
	public static String[] ntTripleAnalysis(String lin){
		
		String sub=""; String prop=""; String obj="";
		int indd1=lin.indexOf(" <");
		sub=lin.substring(0, indd1);
		if(sub.startsWith("<")){
			sub=sub.substring(1, sub.length()-1);
		}
		lin=lin.substring(indd1+1);
		int indd2=lin.indexOf("> ");
		prop=lin.substring(0, indd2);
		if(prop.startsWith("<")){
			prop=prop.substring(1);
		}
		if(prop.endsWith("<")){
			prop=prop.substring(0, prop.length()-1);
		}
		obj=lin.substring(indd2+2);
		if(obj.startsWith("<")){
			obj=obj.substring(1, obj.length()-3);
		}
		else{
			obj=obj.substring(0, obj.length()-2);
		}
			
		String[] array = {sub, prop, obj};
		return array;
	}
	
	public static String[] nqTripleAnalysis(String lin){
		
		String sub=""; String prop=""; String obj="";
		int indd1=lin.indexOf(" <");
		sub=lin.substring(0, indd1);
		if(sub.startsWith("<")){
			sub=sub.substring(1, sub.length()-1);
		}
		lin=lin.substring(indd1+1);
		int indd2=lin.indexOf("> ");
		prop=lin.substring(0, indd2);
		if(prop.startsWith("<")){
			prop=prop.substring(1);
		}
		if(prop.endsWith("<")){
			prop=prop.substring(0, prop.length()-1);
		}
		obj=lin.substring(indd2+2);
		obj=obj.substring(0, obj.indexOf(" <")+2);
		if(obj.startsWith("<")){
			obj=obj.substring(1, obj.length()-3);
		}
		else{
			obj=obj.substring(0, obj.length()-2);
		}
			
		String[] array = {sub, prop, obj};
		return array;
	}
	
	public static void main(String[] args){
		String s="<http://dbpedia.org/resource/Ford_Popular> <http://dbpedia.org/property/engine> \"30.0\"^^<http://dbpedia.org/datatype/brake horsepower> .";
		String[] nt=ntTripleAnalysis(s);
		System.out.println(nt[0]+"<>"+nt[1]+"<>"+nt[2]);  
	}
	
}