package scripts.woodcutter.nodes;

import org.tribot.api.General;
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
	public static RSTile ASSIGNED_TREE = new RSTile(3165, 3406);
	public static String CTSTATUS;
	
	public void execute() {
		CTSTATUS = "1";
		RSObject tree = PublicCalls.findNearest(15, 0, TREE_IDS);
		General.println("Checking if tree isn't null && in treeArea..");
		if(tree != null && WalkToTrees.treeArea.contains(tree)) {
			RSObject treeStump = PublicCalls.findNearest(15, 0, TREE_STUMP_IDS);
			if((Player.getAnimation() == -1) || (Player.getAnimation() == 875 && (treeStump.getPosition() == tree.getPosition()))) {
				General.println("Checking if tree is clickable and on screen...");
				if(tree.isOnScreen() && tree.isClickable()) {
					ASSIGNED_TREE = tree.getPosition();//new RSTile();
					tree.click("Chop down");
					General.println("Sent click");
					int timeOut = 0;
					while (Player.getAnimation() == -1 && timeOut++ <= 7 /*|| 
							(Player.getAnimation() == 875 && treeStump.getPosition() != tree.getPosition())*/) {
						General.println("Sleeping for 200ms... Timeout = " + timeOut);
						//timeOut++;
						General.sleep(200);
					}
				} else {
					General.println("Tree isn't clickable, adjusting...");
					Camera.setRotationMethod(Camera.ROTATION_METHOD.ONLY_MOUSE);
					RSTile currentTreeTile = tree.getAnimablePosition();
					//int cameraAngle = Camera.getTileAngle(currentTreeTile);
					
					General.println(Player.getPosition().distanceTo(currentTreeTile));
					if(Player.getPosition().distanceTo(currentTreeTile) > 4) {
						Walking.clickTileMM(currentTreeTile, 1);
						General.sleep(400,750);
					} else {
						Camera.turnToTile(currentTreeTile);
						General.sleep(500,800);
						//Camera.setCameraAngle(cameraAngle);
					}
				}
			} else if(Player.getAnimation() == 875) {
				General.println("Chopping..");
				RSObject[] treeHover = Objects.findNearest(15, TREE_IDS);
				if(treeHover.length >= 1)
					treeHover[1].hover();
				if(Inventory.getCount(LOG_IDS) >= 27) {
					//TODO
					General.println("Inventory count >= 27..");
				}
				General.sleep(500);
			}
		} else {
			if(tree == null)
				General.println("Tree is null, moving on...");
			if(!WalkToTrees.treeArea.contains(tree))
				General.println("Tree isn't in the tree area, moving on...");
		}
		CTSTATUS = "0";
	}
	
	public boolean isAtTree() {
		final RSObject tree = PublicCalls.findNearest(15, 0, TREE_IDS);
		
		if(tree != null) {
			if(Player.getPosition().distanceTo(tree) < 10 && WalkToTrees.treeArea.contains(tree)) {
				General.println("Player distance to tree is less than 10, and tree is in area.");
				return true;
			} else {
				General.println("Distance to tree - " + Player.getPosition().distanceTo(tree));
				General.println("Tree is in area - " + WalkToTrees.treeArea.contains(tree));
				General.sleep(10000);
			}
		}
		return false;
	}

	public boolean validate() {
		/*if(WalkToTrees.treeArea.contains(Player.getPosition())) {
			General.println("In CutTree..");
			int i = 0;
			RSObject[] tree = Objects.findNearest(15, CutTree.TREE_IDS);
			General.println("CutTree 1..");
			if(tree[i] != null) {
				General.println("Tree ISN'T null");
				if(!WalkToTrees.treeArea.contains(tree[i])) {
					General.println("Physical tree out of area... " + i);
					while(!WalkToTrees.treeArea.contains(tree[i]) && i < 5) {
						General.println("Adding 1 to i..");
						i++;
						int treeTimeOut = 0;
						General.println("1");
						General.println("Tree is null = " + tree[i] == null);
						General.println("2");
						while(tree[i] == null && treeTimeOut < 7) {
							General.println("tree is null, still in loop...");
							treeTimeOut++;
							General.sleep(400);
						}
						General.println("Past treeTimeOut loop, sleeping for .5s..");
						General.sleep(500);
					}
					General.println("Out of while loop..");
				} else {
					return true;
				}
			} else
				General.println("Physical tree is NULL");
			General.println("CutTree Done..");
		}
		return false;*/
		return !Inventory.isFull() && WalkToTrees.treeArea.contains(ASSIGNED_TREE) && !Player.isMoving();    //run if inventory still has space
	}
}
