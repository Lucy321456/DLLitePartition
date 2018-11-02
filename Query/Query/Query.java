package Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Query{
	public List<String> heads;
	public List<Atom> body;
	
	public Query(){
		heads=new ArrayList<String>();
		body=new ArrayList<Atom>();
	}
	
	public Query(Query q){
		heads=new ArrayList<String>();
		body=new ArrayList<Atom>();
		
		heads.addAll(q.heads);
		for(Atom at: q.body){
			Atom at_new=new Atom();
			at_new.con=at.con;
			at_new.var1=at.var1;
			at_new.var2=at.var2;
			body.add(at_new);
		}

	}
	
	public Query(String query){
		heads=new ArrayList<String>();
		body=new ArrayList<Atom>();
		
		String[] H_B=query.split("<-");
		String head=dropBlank(H_B[0]);
		String[] headVar=head.substring(head.indexOf("(")+1, head.indexOf(")")).split(",");
		for(String v: headVar){
			heads.add(dropBlank(v));
		}
		
		String[] nt=H_B[1].split(",");
		for(int i=0; i<nt.length; i++){
			Atom at=new Atom();
			String ele=dropBlank(nt[i]);
			if(ele.endsWith(")")){
				String con=ele.substring(0, ele.indexOf("("));
				at.con=dropBlank(con);
				String var=ele.substring(ele.indexOf("(")+1, ele.indexOf(")"));
				at.var1=dropBlank(var);
			}
			else{
				String con=ele.substring(0, ele.indexOf("("));
				at.con=dropBlank(con);
				String var1=ele.substring(ele.indexOf("(")+1);
				at.var1=dropBlank(var1);
				String s=dropBlank(nt[i+1]);
				String var2=s.substring(0, s.indexOf(")"));
				at.var2=dropBlank(var2);
				i=i+1;
			}
			body.add(at);
		}
		
	}
	
	public String dropBlank(String s){
		int lab=0;
		while(lab==0){
			if(s.startsWith(" ")){
				s=s.substring(1);
			}
			else if(s.endsWith(" ")){
				s=s.substring(0, s.length()-1);
			}
			else{
				lab=1;
			}
		}
		return s;
	}
	
	public Set<String> getClas( ){
		Set<String> cla=new HashSet<String>();
		for(Atom at: body){
			if(at.var2.length()==0){
				cla.add(at.con);
			}
		}
		return cla;
	}
	
	public Set<String> getPros( ){
		Set<String> pro=new HashSet<String>();
		for(Atom at: body){
			if(at.var2.length()>0){
				pro.add(at.con);
			}
		}
		return pro;
	}
	
	public Set<Integer> getClaIndex(String cla){
		Set<Integer> ind=new HashSet<Integer>();
		for(int i=0; i<body.size(); i++){
			if(body.get(i).var2.length()==0){
				if(body.get(i).con.equals(cla)){
					ind.add(i);
				}
			}
		}
		return ind;
	}
	
	public Set<Integer> getProIndex(String pro){
		Set<Integer> ind=new HashSet<Integer>();
		for(int i=0; i<body.size(); i++){
			if(body.get(i).var2.length()>0){
				if(body.get(i).con.equals(pro)){
					ind.add(i);
				}
			}
		}
		return ind;
	}
	
	public Set<String> getNonDisVar(){
		Set<String> vars=new HashSet<String>();
		for(Atom at: body){
			if(at.var1.startsWith("?")){
				if(!heads.contains(at.var1)){
					vars.add(at.var1);
				}
			}
			if(at.var2.startsWith("?")){
				if(!heads.contains(at.var2)){
					vars.add(at.var2);
				}
			}
		}
		return vars;
	}
	
	public Set<String> getInd(){
		Set<String> inds=new HashSet<String>();
		for(Atom at: body){
			if(!at.var1.startsWith("?")){
				inds.add(at.var1);
			}
			if(at.var2.length()>0 & !at.var2.startsWith("?")){
				inds.add(at.var2);
			}
		}
		return inds;
	}
	
	public void print(){
		String headVars="";
		for(String v: heads){
			headVars=headVars+v+",";
		}
		if(headVars.length()>0){
			headVars=headVars.substring(0, headVars.length()-1);
		}		
		String bodies="";
		for(Atom at: body){
			if(at.var2.length()==0){
				bodies=bodies+at.con+"("+at.var1+")"+",";
			}
			else{
				bodies=bodies+at.con+"("+at.var1+","+at.var2+")"+",";
			}
		}
		bodies=bodies.substring(0, bodies.length()-1);
		System.out.println("q("+headVars+")<-"+bodies);
	}
	
	public String toString(){
		String headVars="";
		for(String v: heads){
			headVars=headVars+v+",";
		}
		if(headVars.length()>0){
			headVars=headVars.substring(0, headVars.length()-1);
		}		
		String bodies="";
		for(Atom at: body){
			if(at.var2.length()==0){
				bodies=bodies+at.con+"("+at.var1+")"+",";
			}
			else{
				bodies=bodies+at.con+"("+at.var1+","+at.var2+")"+",";
			}
		}
		bodies=bodies.substring(0, bodies.length()-1);
		return "q("+headVars+")<-"+bodies;
	}
	
	public static void main(String[] args){
		Query query=new Query("q(?x, ?y) <- A (?x , ?y) , A(?x) ");
		query.print(); 
	}
}