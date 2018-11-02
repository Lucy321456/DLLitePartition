package QueryPartition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Query.Atom;
import Query.Query;


/****************************************************************************
 * partition non simple-query reducible conjunctive queries
 * @author Lucy
 *
 */
public class PartitionNonReducibleQuery{
	
	public static Set<Set<Query>> queryPartition(Query query){
		Set<Set<Query>> partitions=new HashSet<Set<Query>>();
		Set<String> nonDisVars=query.getNonDisVar();
		Set<Set<String>> subSets=computeSubSet(nonDisVars);
		for(Set<String> s: subSets){
			partitions.add(computeOnePartition(s, query));
		}
		return partitions;
	}
	
	public static Set<Set<String>> computeSubSet(Set<String> set){
		Set<Set<String>> subSets=new HashSet<Set<String>>();
		Set<String> empty=new HashSet<String>();
		subSets.add(empty);
		if(set.size()<=1){
			subSets.add(set);
			return subSets;
		}
		else{
			List<Set<Set<String>>> list=new ArrayList<Set<Set<String>>>();
			Set<Set<String>> size_0=new HashSet<Set<String>>();
			list.add(size_0);
			Set<Set<String>> size_1=new HashSet<Set<String>>();
			for(String a: set){
				Set<String> s=new HashSet<String>();
				s.add(a);
				size_1.add(s);
			}
			list.add(size_1);
			for(int i=2; i<=set.size();i++){
				Set<Set<String>> size_i=new HashSet<Set<String>>();
				for(Set<String> s: list.get(i-1)){
					for(String a: set){
						Set<String> set_new=new HashSet<String>();
						set_new.addAll(s);
						set_new.add(a);
						size_i.add(set_new);
					}
				}
				list.add(size_i);
			}
			for(int i=1; i<list.size(); i++){
				subSets.addAll(list.get(i));
			}
		}
		
		return subSets;
	}
	
	public static Set<Query> computeOnePartition(Set<String> vars, Query query){
		Set<Query> partition=new HashSet<Query>();
		Query q=new Query(query);
		Map<String, Set<Atom>> varIndex=new HashMap<String, Set<Atom>>();
		Map<String, Set<String>> varConnected=new HashMap<String, Set<String>>();
		for(Atom at: q.body){
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
				if(vars.contains(at.var1)&vars.contains(at.var2)){
					if(varConnected.containsKey(at.var1)){
						varConnected.get(at.var1).add(at.var2);
					}
					else{
						Set<String> set=new HashSet<String>();
						set.add(at.var2);
						varConnected.put(at.var1, set);
					}
					if(varConnected.containsKey(at.var2)){
						varConnected.get(at.var2).add(at.var1);
					}
					else{
						Set<String> set=new HashSet<String>();
						set.add(at.var1);
						varConnected.put(at.var2, set);
					}
				}
			}
		}
		Set<Set<String>> conComponent=computeConnectedComponnet(varConnected);
		
		for(Set<String> set: conComponent){
			Set<Atom> atoms=new HashSet<Atom>();
			if(set.size()>0){
				for(String v: set){
					atoms.addAll(varIndex.get(v));
				}
			}
			else{
				for(String v: set){
					if(varIndex.get(v).size()>0){
						atoms.addAll(varIndex.get(v));
					}
				}
			}
			if(atoms.size()>0){
				Query q_new=new Query();
				for(Atom at: atoms){
					q_new.body.add(at);
					if(at.var1.startsWith("?")){
						if(query.heads.contains(at.var1)|| (!set.contains(at.var1)&varIndex.get(at.var1).size()>1)){
							q_new.heads.add(at.var1);
						}
					}
					if(at.var2.startsWith("?")){
						if(query.heads.contains(at.var2)||(!set.contains(at.var2)&varIndex.get(at.var2).size()>1)){
							q_new.heads.add(at.var2);
						}
					}
				}
				partition.add(q_new);
				for(String v: set){
					varIndex.remove(v);
				}
				for(String v: varIndex.keySet()){
					varIndex.get(v).removeAll(atoms);
				}
			}
		}
		
		while(varIndex.size()>0){
			String var="";
			int max=0;
			for(String v: varIndex.keySet()){
				if(varIndex.get(v).size()>max){
					max=varIndex.get(v).size();
					var=v;
				}
			}
			if(max>0){
				
			}
		}
		
		
		return partition;
	}
	
	public static Set<Set<String>> computeConnectedComponnet(Map<String, Set<String>> connectedRelation){
		Set<Set<String>> conComponent=new HashSet<Set<String>>();
		List<String> ele=new ArrayList<String>();
		ele.addAll(connectedRelation.keySet());
		while(ele.size()>0){
			Set<String> set=new HashSet<String>();
			set.add(ele.get(0));
			Set<String> set_new=new HashSet<String>();
			set_new.add(ele.get(0));
			while(set_new.size()>0){
				Set<String> set_added=new HashSet<String>();
				for(String v: set_new){
					if(connectedRelation.containsKey(v)){
						set_added.addAll(connectedRelation.get(v));
					}
				}
				set_added.removeAll(set);
				set.addAll(set_added);
				set_new.clear();
				set_new.addAll(set_added);
			}
			conComponent.add(set);
		}
		return conComponent;
	}
	
	public static void main(String[] args){  
		Query query=new Query("q(?y)<-P(?y,?x),A(?x),Q(?y,?z),R(?d,?z),B(?d)");
		Set<Set<Query>> partitions=PartitionNonReducibleQuery.queryPartition(query);
		System.out.println("partition number: "+partitions.size());
		for(Set<Query> set: partitions){
			System.out.println("partition: ");
			for(Query q: set){
				q.print();
			}
		}
		
	}
}