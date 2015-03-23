package scripts.woodcutter.nodes;

import org.tribot.api.General;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

import scripts.woodcutter.api.Node;

public class WalkToTrees extends Node {
	
	public static int WTTSTATUS;
	public final static int areaMinX = 3154; //South West X Tile
	public final static int areaMaxX = 3173; //North East X Tile
	public final static int areaMinY = 3375; //South West Y Tile
	public final static int areaMaxY = 3422; //North East Y Tile
	
	public final static RSArea treeArea = new RSArea(new RSTile(areaMinX, areaMinY, 0), new RSTile(areaMaxX, areaMaxY, 0));
	private final RSTile treeCenterTile = new RSTile(3170, 3418, 0);
	//private RSTile recordedPlayerPos;
	//private RSTile currentPlayerPos;
	
	public void derp(boolean...optionalFlag) {}
	
	@Override
	public void execute() {
		WTTSTATUS = 1;
		//TODO Make it go to a tree in treeArea instead of walking to treeCenterTile
		General.println("Not in the tree zone, adjusting location...");
		WebWalking.walkTo(treeCenterTile);
		WTTSTATUS = 0;
	}
		
	@Override
	public boolean validate() {
		return !Inventory.isFull() && !treeArea.contains(Player.getPosition());
	}
}