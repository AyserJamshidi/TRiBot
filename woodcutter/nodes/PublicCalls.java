package scripts.woodcutter.nodes;

import org.tribot.api2007.Objects;
import org.tribot.api2007.types.RSObject;

import scripts.woodcutter.api.Node;

public class PublicCalls extends Node {

	public static RSObject findNearest(int distance, int index, int ... ids) {
	    RSObject[] objects = Objects.findNearest(distance, ids);
	    return objects.length > index ? objects[index] : null;
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

}
