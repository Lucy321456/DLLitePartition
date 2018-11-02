package QueryPartitionAnswering;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/****************************************
 * compute the variable bindings of a subquery
 * @author Lucy
 *
 */

public class ComputeVarBinds{
	
	public static Set<Map<String, String>> computeVarBinds(Set<Map<String, String>> ans, List<String> headVars){
		Set<Map<String, String>> binds=new HashSet<Map<String, String>>();
		for(Map<String, String> a: ans){
			Map<String, String> map=new HashMap<String, String>();
			int lab=0;
			for(String v: headVars){
				if(a.containsKey(v)){
					map.put(v, a.get(v));
					lab=1;
				}
			}
			if(lab==0){
				return binds;
			}
			binds.add(map);
		}
		return binds;
	}
}