package QueryPartition;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Query.Atom;
import Query.Query;

/*********************************************
 * partition simple-query reducible conjunctive queries
 * @author Lucy
 *
 */

public class PartitionReducibleQuery{
	
	public static Set<Query> queryPartition_star(Query query){
		Set<Query> partition=new HashSet<Query>();
		Map<String, Set<Atom>> varIndex=new HashMap<String, Set<Atom>>();
		for(Atom at: query.body){
			if(varIndex.containsKey(at.var1)){
				varIndex.get(at.var1).add(at);
			}
			else{
				Set<Atom> set=new HashSet<Atom>();
				set.add(at);
				varIndex.put(at.var1, set);
			}
			if(at.var2.length()>0){
				if(varIndex.containsKey(at.var2)){
					varIndex.get(at.var2).add(at);
				}
				else{
					Set<Atom> set=new HashSet<Atom>();
					set.add(at);
					varIndex.put(at.var2, set);
				}
			}
		}		
		
		for(String v: varIndex.keySet()){
			Query q=new Query();
            for(Atom at: varIndex.get(v)){
            	q.body.add(at);
            	if(at.var1.startsWith("?")&q.heads.contains(at.var1)){
            		q.heads.add(at.var1);
            	}
            	if(at.var2.startsWith("?")&q.heads.contains(at.var2)){
            		q.heads.add(at.var2);
            	}
            }
            partition.add(q);
		}
		return partition;
	}
	
	public static Set<Query> queryPartition(Query query){
		Set<Query> partition=new HashSet<Query>();
		Set<String> nonDisVar=new HashSet<String>();
		Map<String, List<Atom>> varIndex=new HashMap<String, List<Atom>>();
		for(Atom at: query.body){
			if(at.var1.startsWith("?")&!query.heads.contains(at.var1)){
				nonDisVar.add(at.var1);
			}
			if(varIndex.containsKey(at.var1)){
				varIndex.get(at.var1).add(at);
			}
			else{
				List<Atom> list=new ArrayList<Atom>();
				list.add(at);
				varIndex.put(at.var1, list);
			}
			if(at.var2.length()>0){
				if(at.var2.startsWith("?")&!query.heads.contains(at.var2)){
					nonDisVar.add(at.var2);
				}
				if(varIndex.containsKey(at.var2)){
					varIndex.get(at.var2).add(at);
				}
				else{
					List<Atom> set=new ArrayList<Atom>();
					set.add(at);
					varIndex.put(at.var2, set);
				}
			}
		}
		
		while(nonDisVar.size()>0){
			String var="";
			int max=0;
			for(String v: nonDisVar){
				if(varIndex.get(v).size()>max){
					max=varIndex.get(v).size();
					var=v;
				}
			}
			
			if(var.length()==0){
				for(String v: nonDisVar){
					varIndex.remove(v);
				}
				break;
			}
			Query q=new Query();
			for(Atom at: varIndex.get(var)){
				q.body.add(at);
				if(at.var1.startsWith("?")){
					if(query.heads.contains(at.var1)&!q.heads.contains(at.var1)){
						q.heads.add(at.var1);
					}
				}
				if(at.var2.startsWith("?")){
					if(query.heads.contains(at.var2)&!q.heads.contains(at.var2)){
						q.heads.add(at.var2);
					}
				}
			}
			partition.add(q);
			nonDisVar.remove(var);			
			for(String v: varIndex.keySet()){
				if(!v.equals(var)){
					varIndex.get(v).removeAll(varIndex.get(var));
				}				
			}
			varIndex.remove(var);
		}
		
		for(Atom at: query.body){
			String var="";
			if(at.var2.length()==0){
				if(varIndex.containsKey(at.var1)){
					var=at.var1;
				}
			}
			else{
				if(varIndex.containsKey(at.var1)&varIndex.containsKey(at.var2)){
					if(varIndex.get(at.var1).size()>=varIndex.get(at.var2).size()){
						var=at.var1;
					}
					else{
						var=at.var2;
					}
				}
			}
			if(var.length()>0){
				if(varIndex.get(var).size()==0){
					continue;
				}
				Query q=new Query();
				for(Atom at1: varIndex.get(var)){
					q.body.add(at1);
					if(at1.var1.startsWith("?")&query.heads.contains(at1.var1)){
						if(!q.heads.contains(at1.var1)){
							q.heads.add(at1.var1);
						}					
					}
					if(at1.var2.startsWith("?")&query.heads.contains(at1.var2)){
						if(!q.heads.contains(at1.var2)){
							q.heads.add(at1.var2);
						}						
					}
				}
				partition.add(q);
				for(String v: varIndex.keySet()){
					if(!v.equals(var)){
						varIndex.get(v).removeAll(varIndex.get(var));
					}
				}
				varIndex.remove(var);
			}
		}
		

		return partition;
	}
	
	public static void main(String[] args){
		try{
			FileWriter fw=new FileWriter("d:/write.txt");
			PartitionReducibleQuery prq=new PartitionReducibleQuery(); 
			Query query=new Query("q(?x,?y,?z)<- 1:Person(?x), 1:thumbnail(?x,?y) , http://www.w3.org/2000/01/rdf-schema#label(?x, Alan Turing@en) , 32:page(?x,?z)");
			Set<Query> partition=prq.queryPartition(query);
			fw.write("partition size: "+partition.size()+"\r\n"); 
			for(Query q: partition){ 
				fw.write(q.toString()+"\r\n");
			}
			fw.close();
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
}