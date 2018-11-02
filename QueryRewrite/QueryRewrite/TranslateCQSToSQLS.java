package QueryRewrite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Query.Atom;
import Query.Query;

/********************************************************************
 * translate a conjunctive query into a SQL query
 * @author Lucy
 *
 */
public class TranslateCQSToSQLS{
	
	public static String translateCQ(Query CQ, Map<String, String> ClaMap, Map<String, String> ProMap){
		String SQL="";
		
		String select="select ";
		String from="from ";
		String where="where ";
		
		List<String> tables=new ArrayList<String>();
		Map<String, String> varToAttrMap=new HashMap<String, String>();
		Map<String, List<Atom>> varToAtom=new HashMap<String, List<Atom>>();
		List<Atom> boundAtom=new ArrayList<Atom>();
		
		for(int i=0; i<CQ.body.size(); i++){
			Atom at=CQ.body.get(i);
			if(at.var2.length()==0){
                 if(!ClaMap.containsKey(at.con)){
                	 return SQL;
                 }				
				 if(!tables.contains(ClaMap.get(at.con))){
	        		  tables.add(ClaMap.get(at.con));
	        	  }
	        	  if(at.var1.startsWith("?")){
	        		  if(!varToAttrMap.containsKey(at.var1)){
	        			  varToAttrMap.put(at.var1, ClaMap.get(at.con)+".P");
	        		  }
	        		  if(varToAtom.containsKey(at.var1)){
	        			  varToAtom.get(at.var1).add(at);
	        		  }
	        		  else{
	        			  List<Atom> list=new ArrayList<Atom>();
	        			  list.add(at);
	        			  varToAtom.put(at.var1, list);
	        		  }
	        	  }
	        	  else{
	        		  boundAtom.add(at);
	        	  }
	          }
			else{
				if(!ProMap.containsKey(at.con)){
					return SQL;
				}				
				if(!tables.contains(ProMap.get(at.con))){
	        		  tables.add(ProMap.get(at.con));
	        	  }      	  
	        	  if(at.var1.startsWith("?")){
	        		  if(!varToAttrMap.containsKey(at.var1)){
	        			  varToAttrMap.put(at.var1, ProMap.get(at.con)+".P1");
	        		  }
	        		  if(varToAtom.containsKey(at.var1)){
	        			  varToAtom.get(at.var1).add(at);
	        		  }
	        		  else{
	        			  List<Atom> list=new ArrayList<Atom>();
	        			  list.add(at);
	        			  varToAtom.put(at.var1, list);
	        		  }
	        	  }
	        	  if(at.var2.startsWith("?")){
	        		  if(!varToAttrMap.containsKey(at.var2)){
	        			  varToAttrMap.put(at.var2, ProMap.get(at.con)+".P2");
	        		  }
	        		  if(varToAtom.containsKey(at.var2)){
	        			  varToAtom.get(at.var2).add(at);
	        		  }
	        		  else{
	        			  List<Atom> list=new ArrayList<Atom>();
	        			  list.add(at);
	        			  varToAtom.put(at.var2, list);
	        		  }
	        	  }
	        	  if(!at.var1.startsWith("?")&!at.var2.startsWith("?")){
	        		  boundAtom.add(at);
	        	  }
				
			}
			
			}
		
		if(CQ.heads.size()==0){ // boolean query
			select=select+" 'a' ";
		}
		else{
			for(String v: CQ.heads){
				if(v.startsWith("?")){
					select=select+varToAttrMap.get(v)+",";
				}
				else{
					select=select+"'"+v+"'"+",";
				}
			}
			select=select.substring(0, select.length()-1);
		}
		
		
		for(String t: tables){
			from=from+t+",";
		}
		from=from.substring(0, from.length()-1);
		
		for(Atom a: boundAtom){
			if(a.var2.length()==0){
				if(!a.var1.equals("-")){
					where=where+ClaMap.get(a.con)+".P="+"\""+a.var1+"\""+" and ";
				}				
			}
			else{
				if(a.var1.equals("-")&a.var2.equals("-")){
				    continue;
				}
				else if(a.var1.equals("-")){
					where=where+ProMap.get(a.con)+".P2="+"\""+a.var2+"\""+" and ";
				}
				else if(a.var2.equals("-")){
					where=where+ProMap.get(a.con)+".P1="+"\""+a.var1+"\""+" and ";
				}
				else{
					where=where+ProMap.get(a.con)+".P1="+"\""+a.var1+"\""+" and "+ProMap.get(a.con)+".P2="+"\""+a.var2+"\""+" and ";
				}				
			}
		}
		
		for(String v: varToAtom.keySet()){
			if(varToAtom.get(v).size()==1){
				Atom at=varToAtom.get(v).get(0);		
				if(at.var2.length()>0){ //A(?x) does not has any condition
					if(!at.var1.startsWith("?")){
						if(!at.var1.equals("-")){
							where=where+ProMap.get(at.con)+".P1="+"\""+at.var1+"\""+" and ";
						}						
					}
					if(!at.var2.startsWith("?")){
						if(!at.var2.equals("-")){
							where=where+ProMap.get(at.con)+".P2="+"\""+at.var2+"\""+" and ";
						}						
					}
				}
			}
			else{
				String condition="";
				Atom at=varToAtom.get(v).get(0);
				if(at.var2.length()==0){
					condition=ClaMap.get(at.con)+".P";
				}
				else{
					if(at.var1.equals(v)){
						condition=ProMap.get(at.con)+".P1";
						if(!at.var2.startsWith("?")&!at.var2.equals("-")){
							where=where+ProMap.get(at.con)+".P2="+"\""+at.var2+"\""+" and ";
						}
					}
					else{
						condition=ProMap.get(at.con)+".P2";
						if(!at.var1.startsWith("?")&!at.var1.equals("-")){
							where=where+ProMap.get(at.con)+".P1="+"\""+at.var1+"\""+" and ";
						}
					}
				}
				for(int i=1; i<varToAtom.get(v).size(); i++){
					Atom at1=varToAtom.get(v).get(i);
					if(at1.var2.length()==0){
						where=where+condition+"="+ClaMap.get(at1.con)+".P"+" and ";
					}
					else{
						if(at1.var1.equals(v)){
							where=where+condition+"="+ProMap.get(at1.con)+".P1"+" and ";
							if(!at1.var2.startsWith("?")&!at1.var2.equals("-")){
								where=where+ProMap.get(at1.con)+".P2="+"\""+at1.var2+"\""+" and ";
							}
						}
						else{
							where=where+condition+"="+ProMap.get(at1.con)+".P2"+" and ";
							if(!at1.var1.startsWith("?")&!at1.var1.equals("-")){
								where=where+ProMap.get(at1.con)+".P1="+"\""+at1.var1+"\""+" and ";
							}
						}
					}
				}
			}
		}
		if(where.length()>6){
			where=where.substring(0, where.length()-5);
			SQL=select+" "+from+" "+where;
		}
		else{
			SQL=select+" "+from;
		}

		return SQL;
	}
	
	public static String translateCQ_new(Query CQ, Map<String, String> ClaMap, Map<String, String> ProMap){
		String SQL="";
		
		String select="select ";
		String from="from ";
		String where="where ";
		
		Map<String, List<String>> varToAttrMap=new HashMap<String, List<String>>();
		Set<String> cla_pro=new HashSet<String>();
		List<String> vars=new ArrayList<String>();
		
		for(int i=0; i<CQ.body.size(); i++){
			Atom at=CQ.body.get(i);			
			if(at.var2.length()==0){ 
				if(!ClaMap.containsKey(at.con)){
		           	 return SQL;
		            }
                 String T="";
                 if(!cla_pro.contains(at.con)){
                	 T=ClaMap.get(at.con);
                	 from=from+T+", ";
                	 cla_pro.add(at.con);
                 }
                 else{
                	 from=from+ClaMap.get(at.con)+" as "+"A"+i+", ";
                	 T="A"+i;               	 
                 }
                 if(varToAttrMap.containsKey(at.var1)){
                	 varToAttrMap.get(at.var1).add(T+".P");
                 }
                 else{
                	 List<String> list=new ArrayList<String>();
                	 list.add(T+".P");
                	 varToAttrMap.put(at.var1, list);
                	 vars.add(at.var1);
                 }
	          }
			else{
				if(!ProMap.containsKey(at.con)){ 
					return SQL;
				}
				String T="";
				if(!cla_pro.contains(at.con)){
					T=ProMap.get(at.con);
					from=from+T+", ";
					cla_pro.add(at.con);
				}
				else{
					T="A"+i;
					from=from+ProMap.get(at.con)+" as "+"A"+i+", "; 
				}
				if(varToAttrMap.containsKey(at.var1)){
					varToAttrMap.get(at.var1).add(T+".P1");
				}
				else{
					List<String> list=new ArrayList<String>();
					list.add(T+".P1");
					varToAttrMap.put(at.var1, list);
					vars.add(at.var1);
				}
				if(varToAttrMap.containsKey(at.var2)){
					varToAttrMap.get(at.var2).add(T+".P2");
				}
				else{
					List<String> list=new ArrayList<String>();
					list.add(T+".P2");
					varToAttrMap.put(at.var2, list);
					vars.add(at.var2);
				}
			}		
		}
		
		from=from.substring(0, from.length()-2);
		
		if(CQ.heads.size()==0){
			select=select+" 'a' ";
		}
		else{
			for(String v: CQ.heads){
				if(v.startsWith("?")){
					select=select+varToAttrMap.get(v).get(0)+",";
				}
				else{
					select=select+"'"+v+"'"+","; 
				}
			}
			select=select.substring(0, select.length()-1);
		}
		
		for(String v: varToAttrMap.keySet()){
			if(v.startsWith("?")){
				if(varToAttrMap.get(v).size()>1){
					for(int i=1; i<varToAttrMap.get(v).size(); i++){
						where=where+varToAttrMap.get(v).get(i-1)+"="+varToAttrMap.get(v).get(i)+" and ";
					}
				}
			}
			else if(!v.equals("-")){
				for(String c: varToAttrMap.get(v)){
					where=where+c+"="+"'"+v+"'"+" and ";
				}
			}
		}

		if(where.length()>6){
			where=where.substring(0, where.length()-5);
			SQL=select+" "+from+" "+where;
		}
		else{
			SQL=select+" "+from;
		}

		return SQL;
	}
	
	public static Set<String> translateCQS(Set<Query> cqs, Map<String, String> ClaMap, Map<String, String> ProMap){
		Set<String> queries=new HashSet<String>();
		for(Query q: cqs){
			String sql=translateCQ_new(q, ClaMap, ProMap);
			if(sql.length()>0){
				queries.add(sql);
			}			
		}
		return queries;
	}
}
