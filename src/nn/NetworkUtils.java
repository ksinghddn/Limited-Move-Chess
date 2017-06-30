package nn;

import org.neuroph.core.data.DataSet;

public class NetworkUtils {
	public static DataSet dataSetFront = new DataSet(16, 1);
	public static DataSet dataSetMiddle = new DataSet(16, 1);
	public static DataSet dataSetBack = new DataSet(16, 1);
	
	public static Network frontNetwork;
	public static Network middleNetwork;
	public static Network backNetwork;
	
	public static void initNetworks() {
		frontNetwork = new Network();
		middleNetwork = new Network();
		backNetwork = new Network();
	}
	
	public static void trainNetworks() {
		frontNetwork.train(dataSetFront);
		middleNetwork.train(dataSetMiddle);
		backNetwork.train(dataSetBack);
	}
	
	public static void saveNetworks() {
		frontNetwork.save("./networks/FrontNetwork.nnet");
		middleNetwork.save("./networks/MiddleNetwork.nnet");
		backNetwork.save("./networks/BackNetwork.nnet");
	}
	
	public static void loadNetworks() {
		frontNetwork.load("./networks/FrontNetwork.nnet");
		middleNetwork.load("./networks/MiddleNetwork.nnet");
		backNetwork.load("./networks/BackNetwork.nnet");
	}
}
