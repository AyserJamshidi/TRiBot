package scripts.woodcutter.nodes;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.util.ABCUtil;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.woodcutter.api.Node;
 
public class CutTree extends Node {
	public final int[] LOG_IDS = { 1511 }; // Inventory log IDs
	public final static int[] TREE_IDS = { 1276, 1278 }; // Normal tree IDs
	public final int[] TREE_STUMP_IDS = { 1342, 9661 }; // Tree stump IDs
	
	public static RSTile gTS = new RSTile(0, 0);
	public static RSTile ASSIGNED_TREE = new RSTile(0, 0);//new RSTile(3165, 3406);
	public static RSTile HOVERING_TREE = ASSIGNED_TREE;
	public static int CTSTATUS;
	public static int IATSTATUS;
	
	private boolean hover_next = false;
	private boolean waited_delay_time = false;
	private long last_chopping_time = 0L;
	
	private ABCUtil ABC = new ABCUtil();
	
	public void execute() {
		CTSTATUS = 1;
		RSObject tree = PublicCalls.findNearest(15, 0, TREE_IDS);
		if(tree != null /*&& this.ABC.BOOL_TRACKER.USE_CLOSEST.next() */ && InTreeArea(tree.getPosition())) {
			ASSIGNED_TREE = tree.getPosition();
			if(Player.getAnimation() == -1) {
				RSTile[] treeOnScreen = tree.getAllTiles();
				//TODO Make boolean that checks this array from 0-3 - TreeOnScreen()
				if(treeOnScreen[0].isOnScreen() && tree.isClickable()) {
					ASSIGNED_TREE = tree.getPosition();
					//TODO Eventually make a "If click getcolorstate red (successful click) and moving, do nothing, else, find other tree"
					DynamicClicking.clickRSObject(tree, "Chop down Tree");
					General.println("Sent click");
					int timeOut = 0;
					while (Player.getAnimation() == -1 && timeOut++ < 10) {
						if(Player.getAnimation() > 0)
							//TODO Make a "If chopping or treetile doesn't contain tree, then break
							break;
						General.sleep(300);
					}
				} else {
					General.println("Tree isn't clickable, adjusting...");
					Camera.setRotationMethod(Camera.ROTATION_METHOD.ONLY_MOUSE);
					RSTile currentTreeTile = tree.getAnimablePosition();
					int cameraAngle = Camera.getTileAngle(currentTreeTile);
					
					if(Player.getPosition().distanceTo(currentTreeTile) > 4 && !Player.isMoving()) {
						Walking.clickTileMM(currentTreeTile, 1);
						General.sleep(400,750);
					} else {
						if(!Player.isMoving()) {
							General.println("Turning to tree tile...");
							Camera.turnToTile(currentTreeTile);
						}
						General.println("Turning to tile angle...");
						Camera.setCameraAngle(cameraAngle);
					}
				}
			} else while(Player.getAnimation() > 0) {
				if(Camera.getCameraAngle() == 100 || Camera.getCameraAngle() < 85)
					Camera.setCameraAngle(100);
				General.println("Chopping..");
				//TODO Move this into its own method -- TreeStolen()
				if(Objects.isAt(ASSIGNED_TREE, TREE_STUMP_IDS)) {
					General.println("TREE WAS STOLEN!!!");
					//TODO MOVE ON TO NEXT TREE
				}
				
				RSObject[] treeHover = Objects.findNearest(15, TREE_IDS);
				if(treeHover.length > 1 && treeHover[1] != null && InTreeArea(treeHover[1].getPosition()) /*WalkToTrees.treeArea.contains(treeHover[1])*/) {
					HOVERING_TREE = treeHover[1].getPosition();
					treeHover[1].hover();
				}
				if(Inventory.getCount(LOG_IDS) >= 27) {
					//TODO Move mouse near the minimap in preparation of walking
					General.println("Inventory count >= 27..");
				}
				General.sleep(100);
			}
		} else {
			if(!WalkToTrees.treeArea.contains(tree))
				General.println("Tree isn't in the tree area, moving on...");
		}
		CTSTATUS = 0;
	}
	
	private boolean TreeStolen(RSObject arg0) {
		//TODO Fill this and make it used.
		return false;
	}

	public static boolean isAtTree() {
		IATSTATUS = 1;
		//TODO Just make it take arg0's position and check if it's in the tree area.
		//     This could be useless because we have InTreeArea
		IATSTATUS = 0;
		return false;
	}
	
	private boolean InTreeArea(RSTile args0) {
		if(WalkToTrees.treeArea.contains(args0))
			return true;
		return false;
	}
	
	private void Cut() {
		//TODO Fill this and make it used.
	}
	
	private boolean Cutting() {
		//TODO Fill this and make it used.
		return false;
	}

	public boolean validate() {
		RSObject tree = PublicCalls.findNearest(15, 0, TREE_IDS);
		if(tree != null && InTreeArea(ASSIGNED_TREE)) {
			ASSIGNED_TREE = tree.getPosition();
		} else
			ASSIGNED_TREE = new RSTile(0, 0);
		return !Inventory.isFull();    //run if inventory still has space
	}
}