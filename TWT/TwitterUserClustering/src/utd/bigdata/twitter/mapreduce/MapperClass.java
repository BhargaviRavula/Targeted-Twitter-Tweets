package utd.bigdata.twitter.mapreduce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import utd.bigdata.twitter.clustering.model.ClusterCenter;
import utd.bigdata.twitter.distance.ZeroDistance;

public class MapperClass extends Mapper<LongWritable, Text, IntWritable, Text>{
	
		private List<ClusterCenter> centers = new ArrayList<ClusterCenter>();
		private ZeroDistance distanceMeasure;
	
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			//from ratings
			
			String[] mydata = value.toString().split(",");
			
			if (mydata.length == 3){
				String[] time = mydata[0].split(" ")[3].split(":");
				int iTimeInSec = (Integer.parseInt(time[2]))+ (Integer.parseInt(time[1])*60)+ (Integer.parseInt(time[0])*60*60);
				int nearestDist = Integer.MAX_VALUE;
				int nearestCenterIndex =-1;
				
				for (ClusterCenter cCenterobj : centers) {
					int iDistance = distanceMeasure.measureDistance(cCenterobj.getiCenter(), iTimeInSec);
					if (-1 == nearestCenterIndex) {
						nearestCenterIndex = cCenterobj.getiClusterIndex();
						nearestDist = iDistance;
					} else {
						if (nearestDist > iDistance) {
							nearestCenterIndex = cCenterobj.getiClusterIndex();
							nearestDist = iDistance;
						}
					}
				}
				context.write(new IntWritable(nearestCenterIndex),new Text(mydata[0]));
			}						
		}
	
		@Override
		protected void setup(Context context)
				throws IOException, InterruptedException {
			int splitfactor = (24*60*60)/6;
			for(int i =0; i <6;i++)
			{
				ClusterCenter cCenter = new ClusterCenter();
				cCenter.setiCenter(((splitfactor*i)+ (splitfactor*(i+1)))/2);
				cCenter.setiClusterIndex(i+1);
				centers.add(cCenter);
			}
			distanceMeasure = new ZeroDistance();
		}
	}
