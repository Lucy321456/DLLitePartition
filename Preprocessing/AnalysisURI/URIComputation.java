package AnalysisURI;

/******************************************
 * computing the prefix of a URI
 * @author 	Lucy
 *
 */
public class URIComputation{
	
	public static String[] computeURI(String o){	
		String[] prename= new String[2];
		String pre="";
		String name="";	
		if(!o.startsWith("\"")&!o.startsWith("_:")&!o.endsWith("//")&!o.endsWith("#")&o.length()>12){
			if(o.substring(0, 12).contains("://")){
				pre=o.substring(0, o.indexOf("://")+3);
				String la="";
				if(o.endsWith("/")){
					la=o.substring(o.length()-1, o.length());
					o=o.substring(o.indexOf("://")+3, o.length()-1);
				}
				else{
					o=o.substring(o.indexOf("://")+3, o.length());
				}	
				String[] t=o.split("/");
				int size=t.length;
				if(size>4){
					for(int i=0; i<4; i++){
						pre=pre+t[i]+"/";
					}
					for(int i=4; i<size; i++){
						name=name+t[i]+"/";
					}
					name=name.substring(0, name.length()-1)+la;
				}
				else{
					if(size<=1){
						name=pre+o+la;
						pre="";
					}
					else if(la.length()>0 ){ //o endsWith /
						for(int i=0; i<size-1; i++){
							pre=pre+t[i]+"/";
						}
						name=t[size-1]+la;
					}
					else{
						for(int i=0; i<size-1; i++){
							pre=pre+t[i]+"/";
						}
						if(t[size-1].contains("#")){
							pre=pre+t[size-1].substring(0, t[size-1].lastIndexOf("#")+1);
							name=t[size-1].substring(t[size-1].lastIndexOf("#")+1);
						}
						else{
							name=t[size-1];
						}
					}					
				}			
			}
			else{
				name=o;
			}
		}
		else{
			name=o;
		}
		
		prename[0]=pre;
		prename[1]=name;
		return prename;
	}
	
}