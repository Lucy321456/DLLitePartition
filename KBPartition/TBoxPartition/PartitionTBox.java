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

/*****************************************
 * construct TBoxes for each subABoxes
 * @author Lucy
 *
 */

public class PartitionTBox{
	
	public void partitionTBox(String tbox, String subABoxPath, Integer subABox_num){
		
		try{

			Map<String, Set<String>> typeClass=new HashMap<String, Set<String>>();
			Map<String, Set<String>> typeRole=new HashMap<String, Set<String>>();			
			Map<String, Set<String>> subClassOf=new HashMap<String, Set<String>>();
			Map<String, Set<String>> subRoleOf=new HashMap<String, Set<String>>();
			Map<String, Set<String>> equClass=new HashMap<String, Set<String>>();
			Map<String, Set<String>> equRole=new HashMap<String, Set<String>>();
			Map<String, Set<String>> inverseOf=new HashMap<String, Set<String>>();
			Map<String, Set<String>> domain=new HashMap<String, Set<String>>();
			Map<String, Set<String>> range=new HashMap<String, Set<String>>();
			Map<String, Set<String>> classDisj=new HashMap<String, Set<String>>();
			Map<String, Set<String>> roleDisj=new HashMap<String, Set<String>>();
			Map<String, String> someValuesFrom=new HashMap<String, String>();
			Map<String, String> onProperty=new HashMap<String, String>();
			
			Set<String> funRole=new HashSet<String>();
			Set<String> inverseFunRole=new HashSet<String>();
			Set<String> symRole=new HashSet<String>();
			Set<String> aSymRole=new HashSet<String>();
			
			BufferedReader br=FileReader.readFile(tbox);
			String line="";
			while((line=br.readLine())!=null){
				String[] nt=line.split("<>");
				if(nt[1].equals(MetaRole.rdf_type)){
					if(nt[2].equals(MetaClass.owl_FunctionalProperty)){
						funRole.add(nt[0]);
					}
					else if(nt[2].equals(MetaClass.owl_InverseFunctionalProperty)){
						inverseFunRole.add(nt[0]);
					}
					else if(nt[2].equals(MetaClass.owl_SymmetricProperty)){
						symRole.add(nt[0]);
					}
					else if(nt[2].equals(MetaClass.owl_AsymmetricProperty)){
						aSymRole.add(nt[0]);
					}
					else if(nt[2].equals(MetaClass.owl_Class)||nt[2].equals(MetaClass.rdfs_Class)){
						if(typeClass.containsKey(nt[0])){
							typeClass.get(nt[0]).add(nt[2]);
						}
						else{
							Set<String> set=new HashSet<String>();
							set.add(nt[2]);
							typeClass.put(nt[0], set);
						}
					}
					else if(nt[2].equals(MetaClass.owl_ObjectProperty)||nt[2].equals(MetaClass.owl_DatatypeProperty)||nt[2].equals(MetaClass.rdf_Property)){
						if(typeRole.containsKey(nt[0])){
							typeRole.get(nt[0]).add(nt[2]);
						}
						else{
							Set<String> set=new HashSet<String>();
							set.add(nt[2]);
							typeRole.put(nt[0], set);
						}
					}
				}
				else if(nt[1].equals(MetaRole.owl_someValuesFrom)){
					someValuesFrom.put(nt[0], nt[2]);
				}
				else if(nt[1].equals(MetaRole.owl_onProperty)){
					onProperty.put(nt[0], nt[2]);
				}
				else if(nt[1].equals(MetaRole.owl_equivalentClass)){
					if(equClass.containsKey(nt[0])){
						equClass.get(nt[0]).add(nt[2]);
					}
					else{ 
						Set<String> set=new HashSet<String>();
						set.add(nt[2]);
						equClass.put(nt[0], set);
					}
					if(equClass.containsKey(nt[2])){
						equClass.get(nt[2]).add(nt[0]);
					}
					else{
						Set<String> set=new HashSet<String>();
						set.add(nt[0]);
						equClass.put(nt[2], set);
					}
				}
				else if(nt[1].equals(MetaRole.owl_equivalentProperty)){
					if(equRole.containsKey(nt[0])){
						equRole.get(nt[0]).add(nt[2]);
					}
					else{ 
						Set<String> set=new HashSet<String>();
						set.add(nt[2]);
						equRole.put(nt[0], set);
					}
					if(equRole.containsKey(nt[2])){
						equRole.get(nt[2]).add(nt[0]);
					}
					else{
						Set<String> set=new HashSet<String>();
						set.add(nt[0]);
						equRole.put(nt[2], set);
					}
				}
				else if(nt[1].equals(MetaRole.owl_disjointWith)){
					if(!nt[2].startsWith("_:")){
						if(classDisj.containsKey(nt[0])){
							classDisj.get(nt[0]).add(nt[2]);
						}
						else{ 
							Set<String> set=new HashSet<String>();
							set.add(nt[2]);
							classDisj.put(nt[0], set);
						}
						if(classDisj.containsKey(nt[2])){
							classDisj.get(nt[2]).add(nt[0]);
						}
						else{
							Set<String> set=new HashSet<String>();
							set.add(nt[0]);
							classDisj.put(nt[2], set);
						}
					}					
				}
				else if(nt[1].equals(MetaRole.owl_propertyDisjointWith)){
					if(roleDisj.containsKey(nt[0])){
						roleDisj.get(nt[0]).add(nt[2]);
					}
					else{ 
						Set<String> set=new HashSet<String>();
						set.add(nt[2]);
						roleDisj.put(nt[0], set);
					}
					if(roleDisj.containsKey(nt[2])){
						roleDisj.get(nt[2]).add(nt[0]);
					}
					else{
						Set<String> set=new HashSet<String>();
						set.add(nt[0]);
						roleDisj.put(nt[2], set);
					}
				}
				else if(nt[1].equals(MetaRole.owl_inverseOf)){
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
				else if(nt[1].equals(MetaRole.rdfs_subClassOf)){
					if(subClassOf.containsKey(nt[0])){
						subClassOf.get(nt[0]).add(nt[2]);
					}
					else{ 
						Set<String> set=new HashSet<String>();
						set.add(nt[2]);
						subClassOf.put(nt[0], set);
					}
				}
				else if(nt[1].equals(MetaRole.rdfs_subPropertyOf)){
					if(subRoleOf.containsKey(nt[0])){
						subRoleOf.get(nt[0]).add(nt[2]);
					}
					else{ 
						Set<String> set=new HashSet<String>();
						set.add(nt[2]);
						subRoleOf.put(nt[0], set);
					}
				}
				else if(nt[1].equals(MetaRole.rdfs_domain)){
					if(domain.containsKey(nt[0])){
						domain.get(nt[0]).add(nt[2]);
					}
					else{ 
						Set<String> set=new HashSet<String>();
						set.add(nt[2]);
						domain.put(nt[0], set);
					}
				}
				else if(nt[1].equals(MetaRole.rdfs_range)){
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
			
			System.out.println("finish reading axioms:");
			
			/*************generating subTbox********************************************************
			 * 
			 */
			
			for(int i=0; i<subABox_num; i++){
				System.out.println("constructing the tbox of subKb_"+i+"; ");
				Set<String> cla=new HashSet<String>();
				Set<String> role=new HashSet<String>();
				
				BufferedReader br_CPMap=FileReader.readFile(subABoxPath+"/CPMap"+i+".txt");
				String line_CPMap="";
				while((line_CPMap=br_CPMap.readLine())!=null){
					String[] nt=line_CPMap.split("<>");
					if(nt[1].startsWith("CT")){
						cla.add(nt[0]);
					}
					else{
						role.add(nt[0]);
					}
				}
				
				Set<String> subTbox=new HashSet<String>();
				
				Set<String> newCla=new HashSet<String>();
				Set<String> newRole=new HashSet<String>();
				newCla.addAll(cla);
				newRole.addAll(role);
				
				while(newCla.size()>0||newRole.size()>0){
					Set<String> set_cla=new HashSet<String>();
					Set<String> set_role=new HashSet<String>();
					
					for(String r: newRole){
						if(subRoleOf.containsKey(r)){
							for(String r1: subRoleOf.get(r)){
								set_role.add(r1);
								subTbox.add(r+"<>"+MetaRole.rdfs_subPropertyOf+"<>"+r1);
							}
						}
						if(inverseOf.containsKey(r)){
							for(String r1: inverseOf.get(r)){
								set_role.add(r1);
								subTbox.add(r+"<>"+MetaRole.owl_inverseOf+"<>"+r1);
							}
						}
						if(roleDisj.containsKey(r)){
							for(String r1: roleDisj.get(r)){
								if(role.contains(r1)){
									subTbox.add(r+"<>"+MetaRole.owl_propertyDisjointWith+"<>"+r1);
								}
							}
						}
						if(domain.containsKey(r)){
							for(String c: domain.get(r)){
								subTbox.add(r+"<>"+MetaRole.rdfs_domain+"<>"+c);
								if(!c.startsWith("_:")){
									set_cla.add(c);
								}
								else{
									subTbox.add(c+"<>"+MetaRole.owl_someValuesFrom+"<>"+someValuesFrom.get(c));
									subTbox.add(c+"<>"+MetaRole.owl_onProperty+"<>"+onProperty.get(c));
									set_cla.add(someValuesFrom.get(c));
									set_role.add(onProperty.get(c));
								}
							}
						}
						if(range.containsKey(r)){
							for(String c: range.get(r)){
								subTbox.add(r+"<>"+MetaRole.rdfs_range+"<>"+c);
								if(!c.startsWith("_:")){
									set_cla.add(c);									
								}
								else{
									subTbox.add(c+"<>"+MetaRole.owl_someValuesFrom+"<>"+someValuesFrom.get(c));
									subTbox.add(c+"<>"+MetaRole.owl_onProperty+"<>"+onProperty.get(c));
									set_cla.add(someValuesFrom.get(c));
									set_role.add(onProperty.get(c));
								}
							}
						}
						if(funRole.contains(r)){
							subTbox.add(r+"<>"+MetaRole.rdf_type+"<>"+MetaClass.owl_FunctionalProperty);
						}
						if(inverseFunRole.contains(r)){
							subTbox.add(r+"<>"+MetaRole.rdf_type+"<>"+MetaClass.owl_InverseFunctionalProperty);
						}
						if(symRole.contains(r)){
							subTbox.add(r+"<>"+MetaRole.rdf_type+"<>"+MetaClass.owl_SymmetricProperty);
						}
						if(aSymRole.contains(r)){
							subTbox.add(r+"<>"+MetaRole.rdf_type+"<>"+MetaClass.owl_AsymmetricProperty);
						}
					}
					
					for(String c: newCla){
						if(subClassOf.containsKey(c)){ 
							for(String c1: subClassOf.get(c)){
								subTbox.add(c+"<>"+MetaRole.rdfs_subClassOf+"<>"+c1);
								if(!c1.startsWith("_:")){									
									set_cla.add(c1);
								}
								else{
									subTbox.add(c1+"<>"+MetaRole.owl_someValuesFrom+"<>"+someValuesFrom.get(c1));
									subTbox.add(c1+"<>"+MetaRole.owl_onProperty+"<>"+onProperty.get(c1));
									set_cla.add(someValuesFrom.get(c1));
									set_role.add(onProperty.get(c1));
								}
							}
						}
						if(equClass.containsKey(c)){
							for(String c1: equClass.get(c)){
								subTbox.add(c+"<>"+MetaRole.owl_equivalentClass+"<>"+c1);
								if(!c1.startsWith("_:")){									
									set_cla.add(c1);
								}
								else{
									subTbox.add(c1+"<>"+MetaRole.owl_someValuesFrom+"<>"+someValuesFrom.get(c1));
									subTbox.add(c1+"<>"+MetaRole.owl_onProperty+"<>"+onProperty.get(c1));
									set_cla.add(someValuesFrom.get(c1));
									set_role.add(onProperty.get(c1));
								}
							}
						}
						if(classDisj.containsKey(c)){
							for(String c1: classDisj.get(c)){
								if(cla.contains(c1)){
									subTbox.add(c+"<>"+MetaRole.owl_disjointWith+"<>"+c1);
								}
							}
						}
					}					
					set_cla.removeAll(cla);
					set_role.removeAll(role);
					cla.addAll(set_cla);
					role.addAll(set_role);
					newCla.clear();
					newRole.clear();
					newCla.addAll(set_cla);
					newRole.addAll(set_role);
					set_cla.clear();
					set_role.clear();
				}
				
				FileWriter fw_tbox=new FileWriter(subABoxPath+"SubTBox_"+i+".txt");
				for(String s: subTbox){
					fw_tbox.write(s+"\r\n");
				}
				FileWriter fw_cla=new FileWriter(subABoxPath+"SubTBox_"+i+"_cla.txt");
				for(String s: cla){
					fw_cla.write(s+"\r\n");
				}
				FileWriter fw_role=new FileWriter(subABoxPath+"SubTBox_"+i+"_role.txt");
				for(String s: role){
					fw_role.write(s+"\r\n");
				}
				fw_tbox.close();
				fw_cla.close();
				fw_role.close();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void countAxioms(String path, int n){
		try{
			FileWriter fw=new FileWriter("d:/record_1.txt");
			for(int i=0; i<30; i++){
				int ct=0;
				BufferedReader br=FileReader.readFile(path+"SubTBox_"+i+".txt");
				String line="";
				while((line=br.readLine())!=null){
					String[] nt=line.split("<>");
					if(nt[1].equals(MetaRole.rdf_type)){
						if(nt[2].equals(MetaClass.owl_FunctionalProperty)||nt[2].equals(MetaClass.owl_InverseFunctionalProperty)||nt[2].equals(MetaClass.owl_SymmetricProperty)||nt[2].equals(MetaClass.owl_AsymmetricProperty)){
							ct=ct+1;
						}
					}
					else if(nt[1].equals(MetaRole.rdfs_subClassOf)||nt[1].equals(MetaRole.rdfs_subPropertyOf)||nt[1].equals(MetaRole.rdfs_domain)||nt[1].equals(MetaRole.rdfs_range)||nt[1].equals(MetaRole.owl_equivalentClass)||nt[1].equals(MetaRole.owl_equivalentProperty)||nt[1].equals(MetaRole.owl_disjointWith)||nt[1].equals(MetaRole.owl_propertyDisjointWith)||nt[1].equals(MetaRole.owl_inverseOf)){
						ct=ct+1;
					}
				}
				fw.write("SubTBox_"+i+" Axiom Size: "+ct+"\r\n"); 
				System.out.println("SubTBox_"+i+" Axiom Size: "+ct); 
			} 
			fw.close();
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		PartitionTBox pt=new PartitionTBox();
		//pt.partitionTBox("E:/Experiment/DLLitePartition/Data/BTC/KB/DLLiteTBox.txt", "E:/Experiment/DLLitePartition/Data/BTC/KBPartition/", 30);
		pt.countAxioms("E:/Experiment/DLLitePartition/Data/BTC/KBPartition/", 30);
	}
}