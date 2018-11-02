package QueryPartition;


import Query.Query;
import java.util.Set;
import java.util.HashSet;

/******************************************
 * compute partitions of conjunctive queries
 * @author Lucy
 *
 */

public class PartitionQuery{
	public static Set<Set<Query>> partitionQuery(Query query){
		Set<Set<Query>> partitions=new HashSet<Set<Query>>();
		if(CheckQueryReducible.checkReducible(query)){
			partitions.add(PartitionReducibleQuery.queryPartition(query));
		}
		else{
			partitions.addAll(PartitionNonReducibleQuery.queryPartition(query));
		}
		return partitions;
	}
}