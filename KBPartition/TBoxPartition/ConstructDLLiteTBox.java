package TBoxPartition;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import BufferReader.FileReader;
import MetaTerms.MetaClass;
import MetaTerms.MetaRole;

/*********************************
 * construct DL-LiteA axioms from a TBox
 * @author Lucy
 *
 */

public class ConstructDLLiteTBox{
	
	public void constructDLLiteTBox(String tbox, String newTbox){
		try{
			FileWriter fw=new FileWriter(newTbox);
			
			int axiomDLLite=0;			
			int axiomRDFS=0;
			int axiomAll=0;
			int axiomClass=0;
			int axiomFun=0;
			
			int axiomClass_SUB=0;
			int axiomClass_DSJ=0;
			int axiomClass_SVF=0;
			int axiomClass_DR=0;
			int axiomRole=0;
			int axiomRole_RDFS=0;
			int axiomRole_Inv=0;
			int axiomRole_DSJ=0;
			int axiomRole_FUN=0;
			int axiomRole_SYM=0;
			
		
			Map<String, Set<String>> subClassOf=new HashMap<String, Set<String>>();
			Map<String, Set<String>> equClass=new HashMap<String, Set<String>>();
			Map<String, Set<String>> domain=new HashMap<String, Set<String>>();
			Map<String, Set<String>> range=new HashMap<String, Set<String>>();
			Map<String, Set<String>> classDisj=new HashMap<String, Set<String>>();
			Map<String, String> someValuesFrom=new HashMap<String, String>();
			Map<String, String> onProperty=new HashMap<String, String>();			
			Set<String> funRole=new HashSet<String>();
			Set<String> inverseFunRole=new HashSet<String>();
			
			Map<String, Set<String>> bNodes=new HashMap<String, Set<String>>();
			
            Set<String> supRole=new HashSet<String>();
			
			BufferedReader br=FileReader.readFile(tbox);
			String line="";
			while((line=br.readLine())!=null){
				String[] nt=line.split("<>");
				if(nt[0].startsWith("_:")){
					if(bNodes.containsKey(nt[0])){
						bNodes.get(nt[0]).add(nt[2]);
					}
					else{
						Set<String> set=new HashSet<String>();
						set.add(nt[2]);
						bNodes.put(nt[0], set);
					}
				}
				if(nt[1].equals(MetaRole.rdf_type)){
					if(nt[2].equals(MetaClass.owl_FunctionalProperty)){
						funRole.add(nt[0]);
						axiomAll=axiomAll+1;
						axiomFun=axiomFun+1;
						
					}
					else if(nt[2].equals(MetaClass.owl_InverseFunctionalProperty)){
						inverseFunRole.add(nt[0]);
						axiomAll=axiomAll+1;
						axiomFun=axiomFun+1;
					}
					else if(nt[2].equals(MetaClass.owl_SymmetricProperty)){
						fw.write(line+"\r\n");
						axiomAll=axiomAll+1;
						axiomDLLite=axiomDLLite+1;
						axiomRole=axiomRole+1;
						axiomRole_SYM=axiomRole_SYM+1;
						supRole.add(nt[0]);
					}
					else if(nt[2].equals(MetaClass.owl_AsymmetricProperty)){
						fw.write(line+"\r\n");
						axiomAll=axiomAll+1;
						axiomDLLite=axiomDLLite+1;
						axiomRole=axiomRole+1;
						axiomRole_SYM=axiomRole_SYM+1;
						supRole.add(nt[0]);
					}
					else if(nt[2].equals(MetaClass.owl_IrreflexiveProperty)||nt[2].equals(MetaClass.owl_TransitiveProperty)){
						axiomAll=axiomAll+1;
					}
					else if(nt[2].equals(MetaClass.owl_Class)||nt[2].equals(MetaClass.rdfs_Class)){
						fw.write(line+"\r\n");
					}
					else if(nt[2].equals(MetaClass.owl_ObjectProperty)||nt[2].equals(MetaClass.owl_DatatypeProperty)||nt[2].equals(MetaClass.rdf_Property)){
						fw.write(line+"\r\n");
					}
				}
				else if(nt[1].equals(MetaRole.owl_someValuesFrom)){
					if(!nt[2].startsWith("_:")){
						someValuesFrom.put(nt[0], nt[2]);
					}
				}
				else if(nt[1].equals(MetaRole.owl_onProperty)){
					onProperty.put(nt[0], nt[2]);
				}
				else if(nt[1].equals(MetaRole.owl_equivalentClass)){
					if(!nt[2].startsWith("_:")){
						fw.write(line+"\r\n");
						axiomAll=axiomAll+1;
						axiomDLLite=axiomDLLite+1;
						axiomRDFS=axiomRDFS+1;
						axiomClass=axiomClass+1;
						axiomClass_SUB=axiomClass_SUB+1;
					}
					else{ //absubtion subjects are not black nodes
						if(equClass.containsKey(nt[0])){
							equClass.get(nt[0]).add(nt[2]);
						}
						else{ 
							Set<String> set=new HashSet<String>();
							set.add(nt[2]);
							equClass.put(nt[0], set);
						}
					}					
				}
				else if(nt[1].equals(MetaRole.owl_equivalentProperty)){
					fw.write(line+"\r\n");
					axiomAll=axiomAll+1;
					axiomDLLite=axiomDLLite+1;
					axiomRDFS=axiomRDFS+1;
					axiomRole=axiomRole+1;
					axiomRole_RDFS=axiomRole_RDFS+1;
					supRole.add(nt[0]);
					supRole.add(nt[2]);
				}
				else if(nt[1].equals(MetaRole.owl_disjointWith)){					
					if(!nt[2].startsWith("_:")){
						fw.write(line+"\r\n");
						axiomAll=axiomAll+1;
						axiomDLLite=axiomDLLite+1;
						axiomClass=axiomClass+1;
						axiomClass_SUB=axiomClass_SUB+1;
						axiomClass_DSJ=axiomClass_DSJ+1;
					}
					else{
						if(classDisj.containsKey(nt[0])){
							classDisj.get(nt[0]).add(nt[2]);
						}
						else{ 
							Set<String> set=new HashSet<String>();
							set.add(nt[2]);
							classDisj.put(nt[0], set);
						}
					}				
				}
				else if(nt[1].equals(MetaRole.owl_propertyDisjointWith)){
					fw.write(line+"\r\n");
					axiomAll=axiomAll+1;
					axiomDLLite=axiomDLLite+1;
					axiomRole=axiomRole+1;
					axiomRole_DSJ=axiomRole_DSJ+1;
				}
				else if(nt[1].equals(MetaRole.owl_inverseOf)){
					fw.write(line+"\r\n");
					axiomAll=axiomAll+1;
					axiomDLLite=axiomDLLite+1;
					axiomRole=axiomRole+1;
					axiomRole_Inv=axiomRole_Inv+1;
					supRole.add(nt[0]);
					supRole.add(nt[2]);
				}
				else if(nt[1].equals(MetaRole.rdfs_subClassOf)){
					if(!nt[2].startsWith("_:")){
						fw.write(line+"\r\n");
						axiomAll=axiomAll+1;
						axiomDLLite=axiomDLLite+1;
						axiomRDFS=axiomRDFS+1;
						axiomClass=axiomClass+1;
						axiomClass_SUB=axiomClass_SUB+1;
					}
					else{
						if(subClassOf.containsKey(nt[0])){
							subClassOf.get(nt[0]).add(nt[2]);
						}
						else{ 
							Set<String> set=new HashSet<String>();
							set.add(nt[2]);
							subClassOf.put(nt[0], set);
						}
					}					
				}
				else if(nt[1].equals(MetaRole.rdfs_subPropertyOf)){
					axiomAll=axiomAll+1;
					axiomDLLite=axiomDLLite+1;
					axiomRDFS=axiomRDFS+1;
					axiomRole=axiomRole+1;
					axiomRole_RDFS=axiomRole_RDFS+1;
					fw.write(line+"\r\n");
					supRole.add(nt[2]);
				}
				else if(nt[1].equals(MetaRole.rdfs_domain)){
					if(!nt[2].startsWith("_:")){
						fw.write(line+"\r\n");
						axiomAll=axiomAll+1;
						axiomDLLite=axiomDLLite+1;
						axiomRDFS=axiomRDFS+1;
						axiomClass=axiomClass+1;
						axiomClass_DR=axiomClass_DR+1;
						//axiomClass_RDFS=axiomClass_RDFS+1;
					}
					else{
						if(domain.containsKey(nt[0])){
							domain.get(nt[0]).add(nt[2]);
						}
						else{ 
							Set<String> set=new HashSet<String>();
							set.add(nt[2]);
							domain.put(nt[0], set);
						}
					}					
				}
				else if(nt[1].equals(MetaRole.rdfs_range)){
					if(!nt[2].startsWith("_:")){
						fw.write(line+"\r\n");
						axiomAll=axiomAll+1;
						axiomDLLite=axiomDLLite+1;
						axiomRDFS=axiomRDFS+1;
						axiomClass=axiomClass+1;
						axiomClass_DR=axiomClass_DR+1;
						//axiomClass_RDFS=axiomClass_RDFS+1;
					}
					else{
						if(range.containsKey(nt[0])){
							range.get(nt[0]).add(nt[2]);
						}
						else{ 
							Set<String> set=new HashSet<String>();
							set.add(nt[2]);
							range.put(nt[0], set);
						}
					}					
				}
			}
			
			for(String c: subClassOf.keySet()){
				for(String c1: subClassOf.get(c)){
					if(someValuesFrom.containsKey(c1)&onProperty.containsKey(c1)){
						axiomDLLite=axiomDLLite+1;
						axiomClass=axiomClass+1;
						axiomClass_SUB=axiomClass_SUB+1;
						axiomClass_SVF=axiomClass_SVF+1;
						fw.write(c+"<>"+MetaRole.rdfs_subClassOf+"<>"+c1+"\r\n");
						fw.write(c1+"<>"+MetaRole.owl_someValuesFrom+"<>"+someValuesFrom.get(c1)+"\r\n");
						fw.write(c1+"<>"+MetaRole.owl_onProperty+"<>"+onProperty.get(c1)+"\r\n");
						supRole.add(onProperty.get(c1));
					}
					if(bNodes.containsKey(c1)){
						axiomAll=axiomAll+1;
					}
				}
			}
			for(String p: domain.keySet()){
				for(String c1:domain.get(p)){
					if(someValuesFrom.containsKey(c1)&onProperty.containsKey(c1)){
						axiomDLLite=axiomDLLite+1;
						axiomClass=axiomClass+1;
						axiomClass_SUB=axiomClass_SUB+1;
						axiomClass_SVF=axiomClass_SVF+1;
						fw.write(p+"<>"+MetaRole.rdfs_domain+"<>"+c1+"\r\n");
						fw.write(c1+"<>"+MetaRole.owl_someValuesFrom+"<>"+someValuesFrom.get(c1)+"\r\n");
						fw.write(c1+"<>"+MetaRole.owl_onProperty+"<>"+onProperty.get(c1)+"\r\n");
						supRole.add(onProperty.get(c1));
					}
					if(bNodes.containsKey(c1)){
						axiomAll=axiomAll+1;
					}
				}
			}
			for(String p: range.keySet()){
				for(String c1:range.get(p)){
					if(someValuesFrom.containsKey(c1)&onProperty.containsKey(c1)){
						axiomDLLite=axiomDLLite+1;
						axiomClass=axiomClass+1;
						axiomClass_SUB=axiomClass_SUB+1;						
						axiomClass_SVF=axiomClass_SVF+1;
						fw.write(p+"<>"+MetaRole.rdfs_range+"<>"+c1+"\r\n");
						fw.write(c1+"<>"+MetaRole.owl_someValuesFrom+"<>"+someValuesFrom.get(c1)+"\r\n");
						fw.write(c1+"<>"+MetaRole.owl_onProperty+"<>"+onProperty.get(c1)+"\r\n");
						supRole.add(onProperty.get(c1));
					}
					if(bNodes.containsKey(c1)){
						axiomAll=axiomAll+1;
					}
				}
			}
			for(String c: classDisj.keySet()){
				for(String c1:classDisj.get(c)){
					if(someValuesFrom.containsKey(c1)&onProperty.containsKey(c1)){
						if(someValuesFrom.get(c1).equals(MetaClass.owl_Thing)||someValuesFrom.get(c1).equals(MetaClass.rdfs_Resource)){
							axiomDLLite=axiomDLLite+1;
							axiomClass=axiomClass+1;
							axiomClass_SUB=axiomClass_SUB+1;
							axiomClass_SVF=axiomClass_SVF+1;
							fw.write(c+"<>"+MetaRole.owl_disjointWith+"<>"+c1+"\r\n");
							fw.write(c1+"<>"+MetaRole.owl_someValuesFrom+"<>"+someValuesFrom.get(c1)+"\r\n");
							fw.write(c1+"<>"+MetaRole.owl_onProperty+"<>"+onProperty.get(c1)+"\r\n");
							supRole.add(onProperty.get(c1));
						}						
					}
					if(bNodes.containsKey(c1)){
						axiomAll=axiomAll+1;
					}
				}
			}
			
			for(String p: funRole){
				if(!supRole.contains(p)){
					fw.write(p+"<>"+MetaRole.rdf_type+"<>"+MetaClass.owl_FunctionalProperty+"\r\n");
					axiomDLLite=axiomDLLite+1;
					axiomRole_FUN=axiomRole_FUN+1;
				}
			}
			
			for(String p: inverseFunRole){
				if(!supRole.contains(p)){
					fw.write(p+"<>"+MetaRole.rdf_type+"<>"+MetaClass.owl_InverseFunctionalProperty+"\r\n");
					axiomDLLite=axiomDLLite+1;
					axiomRole_FUN=axiomRole_FUN+1;
				}
			}
			
			fw.close();
			System.out.println("axiomDLLite: "+axiomDLLite);			
			System.out.println("axiomRDFS: "+axiomRDFS);
			System.out.println("axiomFun: "+axiomFun);
			System.out.println("axiomAll: "+axiomAll);
			System.out.println("axiomClass: "+axiomClass);
			System.out.println("axiomClass_SUB: "+axiomClass_SUB);
			System.out.println("axiomClass_DR: "+axiomClass_DR); 
			System.out.println("axiomClass_DSJ: "+axiomClass_DSJ);
			System.out.println("axiomClass_SVF: "+axiomClass_SVF);
			System.out.println("axiomRole: "+axiomRole);
			System.out.println("axiomRole_RDFS: "+axiomRole_RDFS);
			System.out.println("axiomRole_Inv: "+axiomRole_Inv);
			System.out.println("axiomRole_DSJ: "+axiomRole_DSJ);
			System.out.println("axiomRole_FUN: "+axiomRole_FUN);
			System.out.println("axiomRole_SYM: "+axiomRole_SYM);
			
			
		}catch(Exception e){   
			e.printStackTrace(); 
		}
	}
	
	public static void main(String[] args){ 
		long start=System.currentTimeMillis();
		ConstructDLLiteTBox cdt=new ConstructDLLiteTBox();
		cdt.constructDLLiteTBox("E:/Experiment/DLLitePartition/Data/BTC/KB/TBox.txt", "E:/Experiment/DLLitePartition/Data/BTC/KB/DLLiteTBox.txt");
	    long end=System.currentTimeMillis();
	    System.out.println("used time (seconds): "+(end-start)/1000);
	}
}