package scripts.woodcutter.nodes;

import org.tribot.api.General;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
//import org.tribot.api2007.Walking;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

import scripts.woodcutter.api.Node;

public class WalkToTrees extends Node {
	
	public final static RSArea treeArea = new RSArea(new RSTile(3154, 3375, 0), new RSTile(3173, 3411, 0));
	private final RSTile treeTile = new RSTile(3170, 3410, 0);
	
	
	@Override
	public void execute() {
		//TODO Make it go to a tree in treeArea instead of walking to treeTile
		if (treeArea.contains(Player.getPosition()) /*|| isPlayerInArea(playerArea)*/) {
			General.println("Inside the tree area!");
		} else {
			General.println("Not in the tree zone, adjusting location...");
			//if(CutTree.tree.isOnMinimap) {
				
				WebWalking.walkTo(treeTile);
			//}
		}
	}

	@Override
	public boolean validate() {
		return !Inventory.isFull() && !treeArea.contains(Player.getPosition());
	}
}