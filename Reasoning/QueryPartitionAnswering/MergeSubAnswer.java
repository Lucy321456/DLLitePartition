package QueryPartitionAnswering;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/********************************************
 * merge the certain answers of sub-queries
 * @author Lucy
 *
 */

public class MergeSubAnswer{
	public static Set<Map<String, String>> mergeSubAnswer(Set<Map<String, String>> ans1, Set<Map<String, String>> ans2){
		Set<Map<String, String>> ans_new=new HashSet<Map<String, String>>();
		for(Map<String, String> map1: ans1){
			lab: for(Map<String, String> map2: ans2){
				Map<String, String> map=new HashMap<String, String>();
				for(String s: map1.keySet()){
					if(map2.containsKey(s)){
						if(map1.get(s).equals(map2.get(s))){
							map.put(s, map1.get(s));
						}
						else{
							continue lab;
						}
					}
					else{
						map.put(s, map1.get(s));
					}
				}
				map.putAll(map2);
				ans_new.add(map);
			}
		}
		return ans_new;
	}
}