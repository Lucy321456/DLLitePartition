package QueryPartition;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import Query.Atom;
import Query.Query;

/*********************************************************
 * check whether a conjunctive query is simple-query reducible
 * @author Lucy
 *
 */

public class CheckQueryReducible{
	
	public static boolean checkReducible(Query query){
		boolean lab=true;
		Map<String, Set<Atom>> varToAtom=new HashMap<String, Set<Atom>>();
		Set<Atom> proAtom=new HashSet<Atom>();
		
		for(int i=0; i<query.body.size(); i++){
			Atom at=query.body.get(i);
			if(at.var2.length()==0){
				if(!query.heads.contains(at.var1)){
					if(varToAtom.containsKey(at.var1)){
						varToAtom.get(at.var1).add(at);
					}
					else{
						Set<Atom> set=new HashSet<Atom>();
						set.add(at);
						varToAtom.put(at.var1, set);
					}
				}
			}
			else if(!query.heads.contains(at.var1)&query.heads.contains(at.var2)){
				if(varToAtom.containsKey(at.var1)){
					varToAtom.get(at.var1).add(at);
				}
				else{
					Set<Atom> set=new HashSet<Atom>();
					set.add(at);
					varToAtom.put(at.var1, set);
				}
			}
			else if(query.heads.contains(at.var1)&!query.heads.contains(at.var2)){
				if(varToAtom.containsKey(at.var2)){
					varToAtom.get(at.var2).add(at);
				}
				else{
					Set<Atom> set=new HashSet<Atom>();
					set.add(at);
					varToAtom.put(at.var2, set);
				}
			}
			else if(!query.heads.contains(at.var1)&!query.heads.contains(at.var2)){
				proAtom.add(at);
			}
		}
		for(Atom at: proAtom){
			if(varToAtom.containsKey(at.var1)&varToAtom.containsKey(at.var2)){ 
				if(!varToAtom.get(at.var1).equals(varToAtom.get(at.var2))){
					lab=false;
					return lab;
				}
			}
		}
		return lab;
	}
	
	public static void main(String[] args){
		CheckQueryReducible cqr=new CheckQueryReducible();
		Query q=new Query("q()<-A(?x), P(?x,?y),B(?y)");
		boolean b=cqr.checkReducible(q); 
		System.out.println(b);
	}
	
}