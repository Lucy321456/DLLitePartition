package OneQueryAnswering;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import KBStructure.KB;
import Query.Atom;
import Query.Query;
import QueryRewrite.RewriteCQS;
import QueryRewrite.TranslateCQSToSQLS;



public class AnswerAQueryOverKBWithTransferedValue{
	
	public static Set<Map<String, String>> queryAnswer(Query query, KB kb, Set<Map<String, String>> binds){
		long start=System.currentTimeMillis();
		Set<Map<String, String>> answers=new HashSet<Map<String, String>>();
		Set<Query> rewrites=RewriteCQS.rewriteCQ(kb.tbox, query);
		System.out.println("over "+kb.id+" number of rewriting queries: "+rewrites.size());
		int ct=0;
		if(binds.size()==0){
			Set<String> sqls=TranslateCQSToSQLS.translateCQS(rewrites, kb.ClaMap, kb.ProMap);
			for(String sql: sqls){
				answers.addAll(AnswerSQL.answerSQL(sql, kb.st, query.heads));
			}
		} 
		System.out.println("over "+kb.id+" number of processing sqls: "+(rewrites.size()*binds.size()));
		for(Map<String, String> bind: binds){			
			Set<Map<String, String>> subAns=new HashSet<Map<String, String>>();
			Set<Query> query_binds=transferBinds(rewrites, bind);
			Set<String> sqls=TranslateCQSToSQLS.translateCQS(query_binds, kb.ClaMap, kb.ProMap);
			List<String> headVars=new ArrayList<String>();
			for(Query q: query_binds){
				headVars.addAll(q.heads);
				break;
			}
			for(String sql: sqls){
				subAns.addAll(AnswerSQL.answerSQL(sql, kb.st, headVars));
			}
			for(Map<String, String> map: subAns){
				map.putAll(bind);
				answers.add(map);
			}
		}
		System.out.println("over "+kb.id+" number of answers: "+answers.size());
		long end=System.currentTimeMillis();
		System.out.println("over "+kb.id+" time used: "+(end-start));
		return answers;
	}
	
	public static Set<Query> transferBinds(Set<Query> queries, Map<String, String> bind){
		Set<Query> queries_new=new HashSet<Query>();
		for(Query q: queries){
			Query q_new=new Query(q);
			q_new.heads.removeAll(bind.keySet());
			for(Atom at: q_new.body){
				if(bind.containsKey(at.var1)){
					at.var1=bind.get(at.var1);
				}
				if(bind.containsKey(at.var2)){
					at.var2=bind.get(at.var2);
				}
			}
			queries_new.add(q_new);
		}
		return queries_new;
	}
}