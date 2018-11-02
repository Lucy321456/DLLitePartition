package SatisfiabilityChecking;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*********************************************************
 * translate axioms in the negative inclusion closure into SQL queries
 * @author Lucy
 *
 */

public class ObtainCheckedQuery{
	
	public static Set<String> obtainCheckedQuery(List<Set<String>> nic, Map<String, String> claMap, Map<String, String> proMap){
		Set<String> sqls=new HashSet<String>();
		for(String s: nic.get(0)){ 
			String[] nt=s.split("<>");
			if(!nt[0].startsWith("exist")&!nt[1].startsWith("exist")){
				if(claMap.containsKey(nt[0])&&claMap.containsKey(nt[1])){
					String T1=claMap.get(nt[0]);
					String T2=claMap.get(nt[1]);
					String sql="";
					if(T1.equals(T2)){
						sql="select "+T1+".P"+" from "+T1;
					}
					else{
						sql="select "+T1+".P" +" from "+T1+", "+T2+" where "+T1+".P"+"="+T2+".P";
					}
					sqls.add(sql);
				}				
			}
			else if(!nt[0].startsWith("exist")&nt[1].startsWith("exist")){
				if(claMap.containsKey(nt[0])){
					String T1=claMap.get(nt[0]);
					String T2="";
					String sql="";
					if(nt[1].startsWith("exist:inverse")){
						if(proMap.containsKey(nt[1].substring(14))){
							T2=proMap.get(nt[1].substring(14));
							sql="select "+T1+".P"+" from "+T1+", "+T2+" where "+T1+".P="+T2+".P2";
							sqls.add(sql);
						}						
					}
					else{
						if(proMap.containsKey(nt[1].substring(6))){
							T2=proMap.get(nt[1].substring(6));
							sql="select "+T1+".P"+" from "+T1+", "+T2+" where "+T1+".P="+T2+".P1";
							sqls.add(sql);
						}					
					}
					
				}				
			}
			else if(nt[0].startsWith("exist")&!nt[1].startsWith("exist")){
				String T1="";
				String T2="";
				String sql="";
				if(nt[0].startsWith("exist:inverse")){
					nt[0]=nt[0].substring(14);
					if(proMap.containsKey(nt[0])&&claMap.containsKey(nt[1])){
						T1=proMap.get(nt[0]);
						T2=claMap.get(nt[1]);
						sql="select "+T2+".P"+" from "+T2+","+T1+" where "+T2+".P="+T1+".P2";
						sqls.add(sql);
					}					
				}
				else{
					nt[0]=nt[0].substring(6);
					if(proMap.containsKey(nt[0])&&claMap.containsKey(nt[1])){
						T1=proMap.get(nt[0]);
						T2=claMap.get(nt[1]);
						sql="select "+T2+".P"+" from "+T2+","+T1+" where "+T2+".P="+T1+".P1";
						sqls.add(sql);
					}					
				}
				
			}
			else{
				String T1="";
				String T2="";
				String sql="";
				if(nt[0].startsWith("exist:inverse")&!nt[1].startsWith("exist:inverse")){
					nt[0]=nt[0].substring(14);
					nt[1]=nt[1].substring(6);
					if(proMap.containsKey(nt[0])&&proMap.containsKey(nt[1])){
						T1=proMap.get(nt[0]);
						T2=proMap.get(nt[1]);
						if(T1.equals(T2)){
							sql=" select A.P2"+" from "+T1+" as A"+", "+T1+" as B"+" where A.P2=B.P1";
						}
						else{
							sql="select "+T1+".P2"+" from "+T1+", "+T2+" where "+T1+".P2="+T2+".P1";
						}
						sqls.add(sql);
					}					
				}
				else if(!nt[0].startsWith("exist:inverse")&nt[1].startsWith("exist:inverse")){
					nt[0]=nt[0].substring(6);
					nt[1]=nt[1].substring(14);
					if(proMap.containsKey(nt[0])&&proMap.containsKey(nt[1])){
						T1=proMap.get(nt[0]);
						T2=proMap.get(nt[1]);
						if(T1.equals(T2)){
							sql="select A.P1"+" from "+T1+" as A "+", "+T2+" as B"+" where A.P1=B.P2";
						}
						else{
							sql="select "+T1+".P1"+" from "+T1+","+T2+" where "+T1+".P1="+T2+".P2";
						}
						sqls.add(sql);
					}					
				}
				else if(nt[0].startsWith("exist:inverse")&nt[1].startsWith("exist:inverse")){
					nt[0]=nt[0].substring(14);
					nt[1]=nt[1].substring(14);
					if(proMap.containsKey(nt[0])&&proMap.containsKey(nt[1])){
						T1=proMap.get(nt[0]);
						T2=proMap.get(nt[1]);
						if(T1.equals(T2)){
							sql="select A.P2"+" from "+T1+" as A, "+T2+" as B"+" where A.P2=B.P2";
						}
						else{
							sql="select "+T1+".P2"+" from "+T1+" , "+T2+" where "+T1+".P2="+T2+".P2";
						}
						sqls.add(sql);
					}					
				}
				else{
					nt[0]=nt[0].substring(6);
					nt[1]=nt[1].substring(6);
					if(proMap.containsKey(nt[0])&&proMap.containsKey(nt[1])){
						T1=proMap.get(nt[0]);
						T2=proMap.get(nt[1]);					
						if(T1.equals(T2)){
							sql="select A.P1"+" from "+T1+" as A, "+T2+" as B"+" where A.P1=B.P1";
						}
						else{
							sql="select "+T1+".P1"+" from "+T1+","+T2+" where "+T1+".P1="+T2+".P2";
						}
						sqls.add(sql);
					}					
				}
			}
		}
		
		for(String s: nic.get(1)){ // property disjoint axioms
			String[] nt=s.split("<>");
			if(!nt[0].startsWith("inverse")&!nt[1].startsWith("inverse")){
				if(proMap.containsKey(nt[0])&&proMap.containsKey(nt[1])){
					String T1=proMap.get(nt[0]);
					String T2=proMap.get(nt[1]);
					String sql="";
					if(T1.equals(T2)){
						sql="select "+T1+".P1"+" , "+T1+".P2"+" from "+T1;
					}
					else{
						sql="select "+T1+".P1"+" , "+T1+".P2" +" from "+T1+", "+T2+" where "+T1+".P1="+T2+".P1"+" and "+T1+".P2"+"="+T2+".P2";
					}			    
				    sqls.add(sql);
				}
				
			}
			else if(nt[0].startsWith("inverse")&!nt[1].startsWith("inverse")){
				nt[0]=nt[0].substring(8);
				if(proMap.containsKey(nt[0])&&proMap.containsKey(nt[1])){
					String T1=proMap.get(nt[0]);
					String T2=proMap.get(nt[1]);
					String sql="";
					if(T1.equals(T2)){
						sql="select A.P1, A.P2"+" from "+T1+" as A, "+T2+" as B"+" where "+"A.P1=B.P2 and A.P2=B.P1";
					}
					else{
						sql="select "+T1+".P2"+" , "+T1+".P1" +" from "+T1+", "+T2+" where "+T1+".P1="+T2+".P2"+" and "+T1+".P2"+"="+T2+".P1";
					}
				    sqls.add(sql);
				}				
			}
			else if(!nt[0].startsWith("inverse")& nt[1].startsWith("inverse")){
				nt[1]=nt[1].substring(8);
				if(proMap.containsKey(nt[0])&&proMap.containsKey(nt[1])){
					String T1=proMap.get(nt[0]);
					String T2=proMap.get(nt[1]);
					String sql="";
					if(T1.equals(T2)){
						sql="select A.P2, A.P1 from "+T1+" as A"+", "+T2+" as B where A.P1=B.P2 and A.P2=B.P1";
					}
					else{
						sql="select "+T1+".P2"+" , "+T1+".P1" +" from "+T1+", "+T2+" where "+T1+".P1="+T2+".P2"+" and "+T1+".P2"+"="+T2+".P1";
					}
				    sqls.add(sql);
				}
				
			}
			else if(nt[0].startsWith("inverse")&nt[1].startsWith("inverse")){
				nt[0]=nt[0].substring(8);
				nt[1]=nt[1].substring(8);
				if(proMap.containsKey(nt[0])&&proMap.containsKey(nt[1])){
					String T1=proMap.get(nt[0]);
					String T2=proMap.get(nt[1]);
					String sql="";
					if(T1.equals(T2)){
						sql="select "+T1+".P1"+" , "+T1+".P2"+" from "+T1;
					}
					else{
						sql="select "+T1+".P1"+" , "+T1+".P2" +" from "+T1+", "+T2+" where "+T1+".P1="+T2+".P1"+" and "+T1+".P2"+"="+T2+".P2";
					}
				    sqls.add(sql);
				}
				
			}
		}
		
		for(String p: nic.get(2)){ // functional property axioms
			if(!p.startsWith("inverse:")){
				if(proMap.containsKey(p)){
					String T=proMap.get(p);
					String sql="select T1.P1"+" from "+T+" as T1"+", "+T+" as T2 "+" where T1.P1=T2.P1 and T1.P2<>T2.P2";
					sqls.add(sql);
				}				
			}
			else{
				p=p.substring(8);
				if(proMap.containsKey(p)){
					String sql="select T1.P2"+" from "+proMap.get(p)+" as T1"+", "+proMap.get(p)+" as T2 "+" where T1.P1<>T2.P1 and T1.P2=T2.P2";
					sqls.add(sql);
				}
				
			}
		}
		return sqls;
	}
}