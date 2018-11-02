package SameAsMaterialization;

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

/*******************************************
 * compute the equivalent classes of the equivalent relations represented by owl:sameAs
 * an equivalent relation is represented as a<>b
 * @author Lucy
 *
 */

public class ComputeEquivalentClass{
	
	public static void upDate(String line, List<NodeSet> conCom, Map<String, Integer> nodeIndex, Map<NodeSet, Set<NodeSet>> absourbNS){
		String[] t=line.split("<>");
		
		if(!nodeIndex.containsKey(t[0])&&!nodeIndex.containsKey(t[1])){
			NodeSet ns=new NodeSet();
			ns.node.add(t[0]);
			ns.node.add(t[1]);
			conCom.add(ns);
			nodeIndex.put(t[0], conCom.size()-1);
			nodeIndex.put(t[1], conCom.size()-1);
		}
		else if(nodeIndex.containsKey(t[0]) && !nodeIndex.containsKey(t[1])){
		    int index=nodeIndex.get(t[0]);
			conCom.get(index).node.add(t[1]);
			nodeIndex.put(t[1], index);
			
		}
		else if(!nodeIndex.containsKey(t[0]) && nodeIndex.containsKey(t[1])){
			 int index=nodeIndex.get(t[1]);
			 conCom.get(index).node.add(t[0]);
			 nodeIndex.put(t[0], index);
		}
		else if(nodeIndex.containsKey(t[0]) && nodeIndex.containsKey(t[1])){
			 int index=nodeIndex.get(t[0]); 
			 int index1=nodeIndex.get(t[1]);
			 
			 if(conCom.get(index).equals(conCom.get(index1))){
				
			 }
			 else{
				 if(absourbNS.containsKey(conCom.get(index))&&!absourbNS.containsKey(conCom.get(index1))){
					 absourbNS.get(conCom.get(index)).add(conCom.get(index1));
					 conCom.set(index1, conCom.get(index));
				 }
				 else if(absourbNS.containsKey(conCom.get(index1))&&!absourbNS.containsKey(conCom.get(index))){
					 absourbNS.get(conCom.get(index1)).add(conCom.get(index));
					 conCom.set(index, conCom.get(index1));
				 }
				 else if(absourbNS.containsKey(conCom.get(index)) && absourbNS.containsKey(conCom.get(index1))){
					 absourbNS.get(conCom.get(index)).addAll(absourbNS.get(conCom.get(index1)));
					 absourbNS.get(conCom.get(index)).add(conCom.get(index1));
					 absourbNS.remove(conCom.get(index1));
					 Collections.replaceAll(conCom, conCom.get(index1), conCom.get(index));					 
				 }
				 else{
					 Set<NodeSet> NS=new HashSet<NodeSet>();
					 NS.add(conCom.get(index1));
					 absourbNS.put(conCom.get(index), NS);
					 conCom.set(index1, conCom.get(index));
				 }
			 }
			 
		}
	}
	
	public List<NodeSet> computeEquivalentClass(String file){
		
		List<NodeSet> conCom=new ArrayList<NodeSet>();
		Map<String, Integer> nodeIndex=new HashMap<String, Integer>();
		
		Map<NodeSet, Set<NodeSet>> absourbNS=new HashMap<NodeSet, Set<NodeSet>>();			
		
		try{
			
			BufferedReader br=FileReader.readFile(file);
			String line="";
			while((line=br.readLine())!=null){
				upDate(line, conCom, nodeIndex, absourbNS);
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		conCom.removeAll(absourbNS.keySet());
		for(NodeSet ns: absourbNS.keySet()){
			for(NodeSet ns1: absourbNS.get(ns)){
				ns.node.addAll(ns1.node);
			}
		}
		conCom.addAll(absourbNS.keySet());
		return conCom;
	}
	
	public void computeRepElement(String file, String fileRep){
		try{
			FileWriter fw=new FileWriter(fileRep);
						
			List<NodeSet> nss=computeEquivalentClass(file);
			
			
			int count=0;
			
			for(NodeSet ns: nss){
				count=count+ns.node.size();
				int min=0;
				String rep="";
				for(String s: ns.node){
					if(rep.length()==0){
						rep=s;
						min=s.length();
					}
					else{
						if(s.length()<min){
							rep=s;
							min=s.length();
						}
					}
				}
				ns.node.remove(rep);
				
				for(String s: ns.node){
					fw.write(s+"<>"+rep+"\r\n");
				}
			}
			
			System.out.println(count);
			
			fw.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	} 
	
	public static void main(String[] args){
		ComputeEquivalentClass cec=new ComputeEquivalentClass();
		long start=System.currentTimeMillis();
		System.out.println("start");
		cec.computeRepElement("E:/Experiment/DLLitePartition/Data/BTC/KB/SameAs.txt", "E:/Experiment/DLLitePartition/Data/BTC/KB/repElement.txt");
		long end=System.currentTimeMillis();
		System.out.println("time used (seconds): "+(end-start)/1000);
	}
}

class NodeSet{ 
	public Set<String> node;
	
	public NodeSet(){
		node=new HashSet<String>();
	}
}