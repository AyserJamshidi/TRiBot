package scripts.woodcutter.nodes;

import org.tribot.api.General;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.woodcutter.api.Node;

public class WalkToTrees extends Node {
	
	public static String WTTSTATUS;
	public final static RSArea treeArea = new RSArea(new RSTile(3154, 3375, 0), new RSTile(3173, 3422, 0));
	private final RSTile treeCenterTile = new RSTile(3170, 3410, 0);
	
	@Override
	public void execute() {
		WTTSTATUS = "1";
		//TODO Make it go to a tree in treeArea instead of walking to treeTile
		General.println("Not in the tree zone, adjusting location...");
		RSObject[] physicalTree = Objects.findNearest(15, CutTree.TREE_IDS);
		
		int i = 0;
		General.println("Position to treeCenterTile is.. " + Player.getPosition().distanceTo(treeCenterTile));
		while(Player.getPosition().distanceTo(treeCenterTile) < 20) {
			if(physicalTree[i] != null) {
				if(Player.getPosition().distanceTo(treeCenterTile) < 20 && treeArea.contains(physicalTree[i])) {
					General.println("Physical tree found, number... " + i);
					Walking.walkTo(physicalTree[i].getPosition());
					i++;
					General.sleep(5);
					break;
				} else {
					i++;
					General.sleep(5);
				}
				Walking.blindWalkTo(treeCenterTile);
			} else
				General.println("Physical tree is NULL");
		}
		WebWalking.walkTo(treeCenterTile);
		WTTSTATUS = "0";
	}

	/*private RSObject findNearest(int distance, int ...ids) {
		RSObject[] objs = Objects.findNearest(distance, ids);
		if (objs.length > 0)
			return objs[0];
		return null;
	}*/
		
	@Override
	public boolean validate() {
		return !Inventory.isFull() && !treeArea.contains(Player.getPosition());
	}
}
