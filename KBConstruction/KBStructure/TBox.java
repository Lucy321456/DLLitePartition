package KBStructure;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import BufferReader.FileReader;
import MetaTerms.MetaClass;
import MetaTerms.MetaRole;

public class TBox{
	public Map<String, Set<String>> hasSubCla;
	public Map<String, Set<String>> hasSubPro;
	public Map<String, Set<String>> inverseOf;

	
	public Set<String> funRole=new HashSet<String>();
	public Set<String> claDisj=new HashSet<String>();
	public Set<String> proDisj=new HashSet<String>();
	
	public TBox(String TBox){
		try{
			
			hasSubCla=new HashMap<String, Set<String>>();
			hasSubPro=new HashMap<String, Set<String>>();
			inverseOf=new HashMap<String, Set<String>>();
			Map<String, String> someValuesFrom=new HashMap<String, String>();
			Map<String, String> onProperty=new HashMap<String, String>();
			Map<String, String> bNode=new HashMap<String, String>();
			
			funRole=new HashSet<String>();
			claDisj=new HashSet<String>();
		    Set<String> claDisj_extra=new HashSet<String>();
			proDisj=new HashSet<String>();
			
			BufferedReader br=FileReader.readFile(TBox);
			String line="";
			while((line=br.readLine())!=null){
				//String[] nt=TripleAnalysis.ntTripleAnalysis(line);
				String[] nt=line.split("<>");
				if(nt[1].equals(MetaRole.rdf_type)){
					if(nt[2].equals(MetaClass.owl_FunctionalProperty)){
						funRole.add(nt[0]);
					}
					else if(nt[2].equals(MetaClass.owl_InverseFunctionalProperty)){
						funRole.add("inverse:"+nt[0]);
					}
				}
				else if(nt[1].equals(MetaRole.rdfs_subClassOf)){
					if(nt[0].equals(nt[2])){
						continue;
					}
					if(nt[2].startsWith("_:")){
						bNode.put(nt[2], nt[0]);
					}
					else{
						if(hasSubCla.containsKey(nt[2])){
							hasSubCla.get(nt[2]).add(nt[0]);
						}
						else{
							Set<String> set=new HashSet<String>();
							set.add(nt[0]);
							hasSubCla.put(nt[2], set);
						}
					}					
				}
				else if(nt[1].equals(MetaRole.owl_equivalentClass)){
					if(nt[0].equals(nt[2])){
						continue;
					}
					if(hasSubCla.containsKey(nt[0])){
						hasSubCla.get(nt[0]).add(nt[2]);
					}
					else{
						Set<String> set=new HashSet<String>();
						set.add(nt[2]);
						hasSubCla.put(nt[0], set);
					}
					if(hasSubCla.containsKey(nt[2])){
						hasSubCla.get(nt[2]).add(nt[0]);
					}
					else{
						Set<String> set=new HashSet<String>();
						set.add(nt[0]);
						hasSubCla.put(nt[2], set);
					}
				}
				else if(nt[1].equals(MetaRole.owl_someValuesFrom)){
					someValuesFrom.put(nt[0], nt[2]);				
				}
				else if(nt[1].equals(MetaRole.owl_onProperty)){
					onProperty.put(nt[0], nt[2]);
				}
				else if(nt[1].equals(MetaRole.rdfs_subPropertyOf)){
					if(nt[0].equals(nt[2])){
						continue;
					}
					if(hasSubPro.containsKey(nt[2])){
						hasSubPro.get(nt[2]).add(nt[0]);
					}
					else{
						Set<String> set=new HashSet<String>();
						set.add(nt[0]);
						hasSubPro.put(nt[2], set);
					}					
				}
				else if(nt[1].equals(MetaRole.owl_equivalentProperty)){
					if(nt[0].equals(nt[2])){
						continue;
					}
					
					if(hasSubPro.containsKey(nt[0])){
						hasSubPro.get(nt[0]).add(nt[2]);
					}
					else{
						Set<String> set=new HashSet<String>();
						set.add(nt[2]);
						hasSubPro.put(nt[0], set);
					}
					if(hasSubPro.containsKey(nt[2])){
						hasSubPro.get(nt[2]).add(nt[0]);
					}
					else{
						Set<String> set=new HashSet<String>();
						set.add(nt[0]);
						hasSubPro.put(nt[2], set);
					}					
				}
				else if(nt[1].equals(MetaRole.owl_inverseOf)){//nt[0] inverse nt[1]
					if(inverseOf.containsKey(nt[0])){
						inverseOf.get(nt[0]).add(nt[2]);
					}
					else{
						Set<String> set=new HashSet<String>();
						set.add(nt[2]);
						inverseOf.put(nt[0], set);
					}
					if(inverseOf.containsKey(nt[2])){
						inverseOf.get(nt[2]).add(nt[0]);
					}
					else{
						Set<String> set=new HashSet<String>();
						set.add(nt[0]);
						inverseOf.put(nt[2], set);
					}
				}
				else if(nt[1].equals(MetaRole.rdfs_domain)){
					if(hasSubCla.containsKey(nt[2])){
						hasSubCla.get(nt[2]).add("exist:"+nt[0]);
					}
					else{
						Set<String> set=new HashSet<String>();
						set.add("exist:"+nt[0]);
						hasSubCla.put(nt[2], set);
					}
				}
				else if(nt[1].equals(MetaRole.rdfs_range)){
					if(hasSubCla.containsKey(nt[2])){
						hasSubCla.get(nt[2]).add("exist:inverse:"+nt[0]);
					}
					else{
						Set<String> set=new HashSet<String>();
						set.add("exist:inverse:"+nt[0]);
						hasSubCla.put(nt[2], set);
					}
				}
				else if(nt[1].equals(MetaRole.owl_disjointWith)){
					if(nt[2].startsWith("_:")){
						claDisj_extra.add(nt[0]+"<>"+nt[2]);
					}
					else{
						claDisj.add(nt[0]+"<>"+nt[2]);
					}					
				}
				else if(nt[1].equals(MetaRole.owl_propertyDisjointWith)){
					proDisj.add(nt[0]+"<>"+nt[2]);
				}
			}
			
			for(String s: claDisj_extra){
				String[] nt=s.split("<>");
				if(onProperty.containsKey(nt[1]) & someValuesFrom.containsKey(nt[1])){
					claDisj.add(nt[0]+"<>"+"exist:"+onProperty.get(nt[1]));
				}
			}
			
			int num=0;
			
			for(String b: someValuesFrom.keySet()){
				if(onProperty.containsKey(b)& bNode.containsKey(b)){
					String p=onProperty.get(b);
					String c=someValuesFrom.get(b);
					String p_new=p+"_"+num;
					num=num+1;
					String subc=bNode.get(b);
					
					Set<String> set=new HashSet<String>();
					set.add(subc);
					hasSubCla.put("exist:"+p_new, set);
					if(hasSubPro.containsKey(p)){
						hasSubPro.get(p).add(p_new);
					}
					else{
						Set<String> set_new=new HashSet<String>();
						set_new.add(p_new);
						hasSubPro.put(p, set);
					}
					if(hasSubCla.containsKey(c)){
						hasSubCla.get(c).add("exist:inverse:"+p_new);
					}
					else{
						Set<String> set_new=new HashSet<String>();
						set_new.add("exist:inverse:"+p_new);
						hasSubCla.put(c, set_new);
					}
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Set<String> computeSubClaClosure(String cla){
		Set<String> subCla=new HashSet<String>();
		Set<String> subCla_new=new HashSet<String>();
		subCla_new.add(cla);
		while(subCla_new.size()>0){
			Set<String> subCla_added=new HashSet<String>();
			for(String c: subCla_new){
				if(hasSubCla.containsKey(c)){
					subCla_added.addAll(hasSubCla.get(c));
				}
				if(c.startsWith("exist:inverse:")){
					String p=c.substring(14);
					if(hasSubPro.containsKey(p)){
						for(String p1: hasSubPro.get(p)){
							subCla_added.add("exist:inverse:"+p1);
						}
					}
					if(inverseOf.containsKey(p)){
						for(String p1: inverseOf.get(p)){
							subCla_added.add("exist:"+p1);
						}
					}
				}
				else if(c.startsWith("exist:")){
					String p=c.substring(6);
					if(hasSubPro.containsKey(p)){
						for(String p1: hasSubPro.get(p)){
							subCla_added.add("exist:"+p1);
						}
					}
					if(inverseOf.containsKey(p)){
						for(String p1: inverseOf.get(p)){
							subCla_added.add("exist:inverse:"+p1);
						}
					}
				}
			}
			subCla_added.removeAll(subCla);
			subCla.addAll(subCla_added);
			subCla_new.clear();
			subCla_new.addAll(subCla_added);
		}
		return subCla;
	}
	
	public Set<String> computeSubProClosure(String pro){
		Set<String> subPro=new HashSet<String>();
		
		Set<String> subPro_new=new HashSet<String>();
		subPro_new.add(pro);
		while(subPro_new.size()>0){
			Set<String> subPro_added=new HashSet<String>();
			for(String p: subPro_new){
				if(!p.startsWith("inverse:")){
					if(hasSubPro.containsKey(p)){
						subPro_added.addAll(hasSubPro.get(p));
					}
					if(inverseOf.containsKey(p)){
						for(String p1: inverseOf.get(p)){
							subPro_added.add("inverse:"+p1);
						}
					}
				}
				else{
					p=p.substring(8);
					if(hasSubPro.containsKey(p)){
						for(String p1: hasSubPro.get(p)){
							subPro.add("inverse:"+p1);
						}				
					}
					if(inverseOf.containsKey(p)){
						for(String p1: inverseOf.get(p)){
							subPro.add(p1);
						}
					}
				}
			}
			
			subPro_added.removeAll(subPro);
			subPro.addAll(subPro_added);
			subPro_new.clear();
			subPro_new.addAll(subPro_added);
		}
		
		
		
		return subPro;
	}
	
	public static void main(String[] args){
		try{
			TBox onto=new TBox("E:/Experiment/DLLitePartition/Data/BTC/KB/TBox.txt");
			Set<String> cla=new HashSet<String>();
			
			 BufferedReader br=FileReader.readFile("E:/Experiment/DLLitePartition/Data/BTC/KB/Cla.txt");
			 String line="";
			 while((line=br.readLine())!=null){
				 cla.add(line);
			 }	
			
			
			Map<Integer, Set<String>> index_cla=new HashMap<Integer, Set<String>>();
			for(String s: cla){
				int ct=onto.computeSubClaClosure(s).size();
				if(index_cla.containsKey(ct)){
					index_cla.get(ct).add(s);
				}
				else{
					Set<String> set=new HashSet<String>();
					set.add(s);
					index_cla.put(ct, set);
				}
			}

			List<Integer> list=new ArrayList<Integer>();
			list.addAll(index_cla.keySet());
			
			Collections.sort(list);			
			
			FileWriter fw=new FileWriter("E:/Experiment/DLLitePartition/Data/BTC/KB/ClaDepthRecord.txt");
			for(int i=list.size()-1; i>=0;i--){
			   int n=list.get(i);
			   for(String s: index_cla.get(n)){
				   fw.write(s+"<>"+n+"\r\n");
			   }
			}
			fw.close(); 
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}

}