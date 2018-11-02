package QueryPartitionAnswering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Query.Query;

/*******************************************************
 * order the queries in a query partition
 * @author Lucy
 *
 */

public class OrderPartition{
	public static List<Query> orderPartition(Set<Query> partition){
		List<Query> partition_new=new ArrayList<Query>();
		Map<Integer, Set<Query>> ind_index=new HashMap<Integer, Set<Query>>();
		if(partition.size()==1){
			partition_new.addAll(partition);
			return partition_new;
		}
		for(Query q: partition){
			int n=q.getInd().size();
			if(ind_index.containsKey(n)){
				ind_index.get(n).add(q);
			}
			else{
				Set<Query> set=new HashSet<Query>();
				set.add(q);
				ind_index.put(n, set);
			}
		}
		List<Integer> list=new ArrayList<Integer>();
		list.addAll(ind_index.keySet());
		Collections.sort(list);
		for(int i=list.size()-1; i>=0; i--){ 
			if(ind_index.get(i).size()==1){
				partition_new.addAll(ind_index.get(list.get(i)));
			}
			else{
				Map<Integer, Set<Query>> index_length=new HashMap<Integer, Set<Query>>();
				for(Query q: ind_index.get(list.get(i))){
					int length=q.body.size();
					if(index_length.containsKey(length)){
						index_length.get(length).add(q);
					}
					else{
						Set<Query> set=new HashSet<Query>();
						set.add(q);
						index_length.put(length, set);
					}
				}
				List<Integer> list_new=new ArrayList<Integer>();
				list_new.addAll(index_length.keySet());
				Collections.sort(list_new);
				for(int j=list_new.size()-1; j>=0; j--){
					partition_new.addAll(index_length.get(list_new.get(j)));
				}
				
			}
			
		}
		return partition_new;
	}
}