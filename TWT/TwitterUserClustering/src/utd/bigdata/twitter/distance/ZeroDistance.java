package utd.bigdata.twitter.distance;

public class ZeroDistance {

	public int measureDistance(int cCenter, int tweetTime)
	{
		if(cCenter>tweetTime)
			return cCenter-tweetTime;
		else
			return tweetTime-cCenter;
	}
}
