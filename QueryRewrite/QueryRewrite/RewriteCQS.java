package QueryRewrite;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import KBStructure.TBox;
import Query.Atom;
import Query.Query;

/*********************************************
 * rewrite a conjunctive query into multiple conjunctive queries according to a TBox
 * @author Lucy
 *
 */

public class RewriteCQS{
	
	public static Set<Query> rewriteCQ(TBox onto, Query query){
		queryPreprocess(query);
		Query query1=new Query(query);
		Set<Query> queries=new HashSet<Query>();
		queries.add(query1);
		Set<Set<String>> bodies=new HashSet<Set<String>>();
		
		Set<Query> query_new=new HashSet<Query>();
		query_new.add(query1);
		while(query_new.size()>0){
			Set<Query> query_added=new HashSet<Query>();			
			for(Query q: query_new){
				for(int i=0; i<q.body.size();i++){
					Atom at=q.body.get(i);
					if(at.var2.length()==0){
						if(onto.hasSubCla.containsKey(at.con)){
							for(String c: onto.hasSubCla.get(at.con)){
								Query q_new=new Query(q);	
								changeClaAtom(at, q_new, c, i);
								Set<String> body=getBodyElement(q_new);
								if(!bodies.contains(body)){
									query_added.add(q_new);
									bodies.add(body);
								}
							}
						}												
					}
					else{
						if(onto.hasSubPro.containsKey(at.con)){							
							for(String p: onto.hasSubPro.get(at.con)){								
								Query q_new=new Query(q);	
								changeProAtom(at, q_new, p, i);
								Set<String> body=getBodyElement(q_new);
								if(!bodies.contains(body)){
									query_added.add(q_new);
									bodies.add(body);
								}
							}
						}
						if(onto.inverseOf.containsKey(at.con)){
							for(String p: onto.inverseOf.get(at.con)){
								Query q_new=new Query(q);	
								changeProAtom(at, q_new, "inverse:"+p, i);
								Set<String> body=getBodyElement(q_new);
								if(!bodies.contains(body)){
									query_added.add(q_new);
									bodies.add(body);
								}
							}
						}

						if(at.var2.equals("-")){
							if(onto.hasSubCla.containsKey("exist:"+at.con)){
								for(String c: onto.hasSubCla.get("exist:"+at.con)){
									Query q_new=new Query(q);	
									changeClaAtom(at, q_new, c, i);
									Set<String> body=getBodyElement(q_new);
									if(!bodies.contains(body)){
										query_added.add(q_new);
										bodies.add(body);
									}
								}
							}													
						}
						if(at.var1.equals("-")){
							if(onto.hasSubCla.containsKey("exist:inverse:"+at.con)){
								for(String c: onto.hasSubCla.get("exist:inverse:"+at.con)){
									Query q_new=new Query(q);	
									changeClaAtomExtra(at, q_new, c, i);
									Set<String> body=getBodyElement(q_new);
									if(!bodies.contains(body)){
										query_added.add(q_new);
										bodies.add(body);
									}
								}
							}														
						}
					}
				}
			}
			queries.addAll(query_added);
			query_new.clear();
			query_new.addAll(query_added);
		}
		return queries;
	}
	
	public static void queryPreprocess(Query query){//
		Map<String, Set<Integer>> map=new HashMap<String, Set<Integer>>();
		for(int i=0;i<query.body.size();i++){
			Atom at=query.body.get(i);
			if(at.var1.startsWith("?")&!query.heads.contains(at.var1)){
				if(map.containsKey(at.var1)){
					map.get(at.var1).add(i);
				}
				else{
					Set<Integer> set=new HashSet<Integer>();
					set.add(i);
					map.put(at.var1, set);
				}
			}
			if(at.var2.startsWith("?")&!query.heads.contains(at.var2)){
				if(map.containsKey(at.var2)){
					if(!at.var2.equals(at.var1)){
						map.get(at.var2).add(i);
					}
					else{
						map.get(at.var2).add(i+10);
					}
				}
				else{
					Set<Integer> set=new HashSet<Integer>();
					set.add(i);
					map.put(at.var2, set);
				}
			}
		}
		
		for(String s: map.keySet()){
			if(map.get(s).size()==1){
				for(int i: map.get(s)){
					if(query.body.get(i).var1.equals(s)){
						query.body.get(i).var1="-";
					}
					else{
						query.body.get(i).var2="-";
					}
				}
			}
		}
	}
	
	public static void changeClaAtom(Atom at, Query q, String cla, int index){
		
        if(!cla.startsWith("exist")){
        	for(int n: q.getClaIndex(cla)){
        		if(q.body.get(n).var1.equals(at.var1)){
        			q.body.remove(index);
        			return ;
        		}
        	}
        	Atom at_new=new Atom();
        	at_new.con=cla;
        	at_new.var1=at.var1;
        	q.body.set(index, at_new);
        	return ;
        }
        else if(cla.startsWith("exist:inverse")){
        	String p=cla.substring(14);
        	for(int n: q.getProIndex(p)){
        		if(q.body.get(n).var2.equals(at.var1)){
        			q.body.remove(index);
        			return ;
        		}
        	}
        	Atom at_new=new Atom();
        	at_new.con=p;
        	at_new.var1="-";
        	at_new.var2=at.var1;
        	q.body.set(index, at_new);
        	return;
        }
        else{
        	String p=cla.substring(6);
        	for(int n: q.getProIndex(p)){
        		if(q.body.get(n).var1.equals(at.var1)){
        			q.body.remove(index);
        			return;
        		}
        	}
        	Atom at_new=new Atom();
        	at_new.con=p;
        	at_new.var1=at.var1;
        	at_new.var2="-";
        	q.body.set(index, at_new);
        	return;
        }

	}
	
	public static void changeClaAtomExtra(Atom at, Query q, String cla, int index){
		for(int i: q.getClaIndex(cla)){
			if(q.body.get(i).var1.equals(at.var2)){
				q.body.remove(index);
				return;
			}
		}
		Atom at_new=new Atom();
		at_new.con=cla;
		at_new.var1=at.var2;
		q.body.set(index, at_new);
		return;
	}
	
	public static void changeProAtom(Atom at, Query q, String pro, int index){

		if(!pro.startsWith("inverse")){
			for(int i: q.getProIndex(pro)){ 
				if(q.body.get(i).var1.equals("-")&q.body.get(i).var2.equals("-")){
					q.body.get(i).var1=at.var1;
					q.body.get(i).var2=at.var2;
					q.body.remove(index);
					return;
				}
				else if(q.body.get(i).var1.equals("-")& q.body.get(i).var2.equals(at.var2)){
					q.body.get(i).var1=at.var1;
					q.body.remove(index);
					return;
				}
				else if(q.body.get(i).var2.equals("-")& q.body.get(i).var1.equals(at.var1)){
					q.body.get(i).var2=at.var2;
					q.body.remove(index);
					return;
				}
				else{
					if((at.var1.equals("-")&at.var2.equals("-"))||(at.var1.equals("-")&at.var2.equals(q.body.get(i).var2))||
							(at.var1.equals(q.body.get(i).var1)&at.var2.equals("-"))||
							(at.var1.equals(q.body.get(i).var1)&at.var2.equals(q.body.get(i).var2))){
						q.body.remove(index);
						return;
					}
				}

			}
			Atom at_new=new Atom();
			at_new.con=pro;
			at_new.var1=at.var1;
			at_new.var2=at.var2;
			
			q.body.set(index, at_new);
			return;
		}
		else{
			String p=pro.substring(8);
			for(int i: q.getProIndex(p)){
				if(q.body.get(i).var1.equals("-")&q.body.get(i).var2.equals("-")){
					q.body.get(i).var1=at.var2;
					q.body.get(i).var2=at.var1;
					q.body.remove(index);
					return;
				}
				else if(q.body.get(i).var1.equals("-")&q.body.get(i).var2.equals(at.var1)){
					q.body.get(i).var1=at.var2;
					q.body.remove(index);
					return;
				}
				else if(q.body.get(i).var2.equals("-")&q.body.get(i).var1.equals(at.var2)){
					q.body.get(i).var2=at.var1;
					return;
				}
				else{
					if((at.var1.equals("-")&at.var2.equals("-"))||(at.var1.equals("-")&at.var2.equals(q.body.get(i).var1))||
							(at.var2.equals("-")&at.var1.equals(q.body.get(i).var2))||
							(at.var1.equals(q.body.get(i).var2)&at.var2.equals(q.body.get(i).var1))){
						q.body.remove(index);
						return;
					}
				}
			}
			Atom at_new=new Atom();
			at_new.con=p;
			at_new.var1=at.var2;
			at_new.var2=at.var1;
			q.body.set(index, at_new);
			return;
		}

	}
	
	public static Set<String> getBodyElement(Query q){
		Set<String> ele=new HashSet<String>();
		for(Atom at: q.body){
			if(at.var2.length()==0){
				ele.add(at.con+"<>"+at.var1);
			}
			else{
				ele.add(at.con+"<>"+at.var1+"<>"+at.var2);
			}
		}
		return ele;
	}
	
	public static void main(String[] args){
		try{
			RewriteCQS rc=new RewriteCQS();
			System.out.println("Start");
			TBox onto=new TBox("E:/Experiment/DLLitePartition/Data/dbpedia/KB/TBox.txt");   
			Query q=new Query("q(?x,?y)<- 1:Person(?x), 1:birthPlace(?x,1:China), 1:birthDate(?x,?y)");//");//1:Person(?x),1:Place(?y)"); //, 1:birthPlace(?x,1:China)
			Set<Query> queries=rc.rewriteCQ(onto, q); 
			System.out.println(queries.size());
			FileWriter fw=new FileWriter("d:/write.txt");
			for(Query q1: queries){
				fw.write(q1.toString()+"\r\n");
			}
			fw.close();

		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
}