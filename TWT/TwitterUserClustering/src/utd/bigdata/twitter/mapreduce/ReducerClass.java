package utd.bigdata.twitter.mapreduce;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class ReducerClass  extends Reducer<IntWritable, Text, IntWritable, Text> {

	public void reduce(IntWritable key, Iterable<Text> values,Context context ) throws IOException, InterruptedException {
		HashMap<Integer,Integer> tweetcounter = new HashMap<Integer,Integer>();
		
		for(Text t : values){
			int hour = Integer.parseInt(t.toString().split(" ")[3].split(":")[0]);
			int index = (hour % 4) + 1;
			if(tweetcounter.containsKey(index))
				tweetcounter.put(index, (tweetcounter.get(index)+1)); 
			else
				tweetcounter.put(new Integer(index), 1);
		}
		for (Entry<Integer,Integer> entry: tweetcounter.entrySet())
			context.write(new IntWritable(key.get()),new Text(entry.getKey()+", "+ entry.getValue().toString()));
	}
	@Override
	protected void cleanup(Context context) throws IOException, InterruptedException {
		super.cleanup(context);
	}
}
