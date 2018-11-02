package SatisfiabilityChecking;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import BufferReader.FileReader;
import KBStructure.TBox;
import MetaTerms.MetaClass;
import MetaTerms.MetaRole;
import NTFileAnalysis.TripleAnalysis;

/******************************************************
 * compute negative inclusion closure of a DL-LiteA TBox
 * @author Lucy
 *
 */

public class ComputeNIColusure{
	
	public static List<Set<String>> computeNIColusure(TBox onto){
		List<Set<String>> NCI=new ArrayList<Set<String>>();
		

		
		Set<String> funRole=new HashSet<String>();
		funRole.addAll(onto.funRole);
		Set<String> claDisj=new HashSet<String>();
		claDisj.addAll(onto.claDisj);
		Set<String> proDisj=new HashSet<String>();
		proDisj.addAll(onto.proDisj);
		
		try{
			
			
		
			Set<String> claDisj_new=new HashSet<String>();	
			claDisj_new.addAll(claDisj);
			while(claDisj_new.size()>0){
				Set<String> claDisj_added=new HashSet<String>();			
				for(String s: claDisj_new){
					claDisj_added.addAll(computeAddedClaDisj(s, onto, claDisj));
				}
				
				claDisj_added.removeAll(claDisj);
				claDisj.addAll(claDisj_added);
				claDisj_new.clear();
				claDisj_new.addAll(claDisj_added);
			}
			
			Set<String> proDisj_new=new HashSet<String>();			
			proDisj_new.addAll(proDisj);
			while(proDisj_new.size()>0){
				Set<String> proDisj_added=new HashSet<String>();				
				for(String s: proDisj_new){
					proDisj_added.addAll(computeAddedProDisj(s, onto, proDisj));
				}

				proDisj_added.removeAll(proDisj);
				proDisj.addAll(proDisj_added);
				proDisj_new.clear();
				proDisj_new.addAll(proDisj_added);
			}
		
			
			NCI.add(claDisj);
			NCI.add(proDisj);
			NCI.add(funRole);
			
		}catch(Exception e){
			e.printStackTrace();
		}
			
		return NCI;
	}
	
	public static Set<String> computeAddedClaDisj(String disj, TBox onto, Set<String> claDisj){
		Set<String> newDisj=new HashSet<String>();
		String[] nt=disj.split("<>");
		for(String c: getSubCla(nt[0],onto)){
			if(!claDisj.contains(c+"<>"+nt[1])&!claDisj.contains(nt[1]+"<>"+c)){
				newDisj.add(c+"<>"+nt[1]);
			}
		}
		for(String c: getSubCla(nt[1],onto)){
			if(!claDisj.contains(nt[0]+"<>"+c)&!claDisj.contains(c+"<>"+nt[0])){
				newDisj.add(nt[0]+"<>"+c);
			}
		}
		return newDisj;
	}
	
	public static Set<String> computeAddedProDisj(String disj, TBox onto, Set<String> proDisj){
		Set<String> newDisj=new HashSet<String>();
		String[] nt=disj.split("<>");
		for(String p: getSubPro(nt[0],onto)){
			if(!proDisj.contains(p+"<>"+nt[1])&!proDisj.contains(nt[1]+"<>"+p)){
				newDisj.add(p+"<>"+nt[1]);
			}
		}
		for(String p: getSubPro(nt[1],onto)){
			if(!proDisj.contains(p+"<>"+nt[0])&!proDisj.contains(nt[0]+"<>"+p)){
				newDisj.add(nt[0]+"<>"+p);
			}
		}
		return newDisj;
	}
	
	public static Set<String> getSubCla(String cla, TBox onto){
		Set<String> set=new HashSet<String>();
		if(onto.hasSubCla.containsKey(cla)){
			set.addAll(onto.hasSubCla.get(cla));
		}
		
		if(cla.startsWith("exist:inverse:")){
			String p=cla.substring(14);
			if(onto.hasSubPro.containsKey(p)){
				for(String p1: onto.hasSubPro.get(p)){
					set.add("exist:inverse:"+p1);
				}
			}
			if(onto.inverseOf.containsKey(p)){
				for(String p1: onto.inverseOf.get(p)){
					set.add("exist:"+p1);
				}
			}
		}
		else if(cla.startsWith("exist:")){
			String p=cla.substring(6);
			if(onto.hasSubPro.containsKey(p)){
				for(String p1: onto.hasSubPro.get(p)){
					set.add("exist:"+p1);
				}
			}
			if(onto.inverseOf.containsKey(p)){
				for(String p1:onto.inverseOf.get(p)){
					set.add("exist:inverse:"+p1);
				}
			}
		}
		return set;
	}
	
	public static Set<String> getSubPro(String pro, TBox onto){
		Set<String> set=new HashSet<String>();
		if(pro.startsWith("inverse:")){
			String p=pro.substring(8);
			if(onto.hasSubPro.containsKey(p)){
				for(String s: onto.hasSubPro.get(p)){
					set.add("inverse:"+s);
				}
			}
			if(onto.inverseOf.containsKey(p)){
				for(String s: onto.inverseOf.get(p)){
					set.add(s);
				}
			}
		}
		else{
			if(onto.hasSubPro.containsKey(pro)){
				for(String s: onto.hasSubPro.get(pro)){
					set.add(s);
				}
			}
			if(onto.inverseOf.containsKey(pro)){
				for(String s: onto.inverseOf.get(pro)){
					set.add("inverse:"+s);
				}
			}
		}
		
		return set;
	}
}