package QueryRewrite;

import java.util.ArrayList;
import java.util.List;

import Query.Query;
import edu.ntua.isci.common.lp.Atom;
import edu.ntua.isci.common.lp.Clause;
import edu.ntua.isci.common.lp.ClauseParser;
import edu.ntua.isci.qa.algorithm.ComputedRewriting;
import edu.ntua.isci.qa.algorithm.Engine;


public class RewriteCQByRapid{
	public List<Query> rewrite(Engine engine, String q){
		List<Query> rew=new ArrayList<Query>();
		
		ClauseParser cp = new ClauseParser(); 
		Clause query=cp.parseClause(q);
		query.reset();
		ComputedRewriting res = engine.computeRewritings(query, true);
		for(Clause cla: res.getFilteredRewritings()){
			Query q_new=new Query(cla.toString());
			rew.add(q_new);
		}
		
		return rew;
	}
}
	