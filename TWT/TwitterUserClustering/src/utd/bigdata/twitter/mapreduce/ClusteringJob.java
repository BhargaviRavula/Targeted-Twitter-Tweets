package utd.bigdata.twitter.mapreduce;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class ClusteringJob {

	//Driver Program
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();		// get all args
		if (otherArgs.length != 2) {
			System.err.println("Tweets: CountTweets <in> <out>");
			System.exit(2);
		}
		
		Job job = Job.getInstance(conf);
		job.setJobName("KMeans Clustering");
		job.setJarByClass(ClusteringJob.class);
					   
		job.setMapperClass(MapperClass.class);
		job.setReducerClass(ReducerClass.class);
		job.setJarByClass(MapperClass.class);
		job.setNumReduceTasks(6);
		
		// set output key type 
		job.setOutputKeyClass(IntWritable.class);
		// set output value type
		job.setOutputValueClass(Text.class);
		
		//set the HDFS path of the input data
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		// set the HDFS path for the output 
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		
		//Wait till job completion
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
