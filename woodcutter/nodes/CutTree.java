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
	private final int[] TREE_IDS = { 1276, 1278 }; // Normal tree IDs
	
	public void execute() {
		RSObject tree = findNearest(15, TREE_IDS);
		if(tree != null) {
			if(Player.getAnimation() == -1) {
				if(tree.isOnScreen() && tree.isClickable()) {
					tree.click("Chop down");
					
					int timeout = 0;
					while (Player.getAnimation() == -1) {
						timeout++;
						General.sleep(10);
						if(timeout > 250)
							break;
					}
				} else {
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
			} else {
				/*
				 * Duplicated code from findNearest
				 * When 27 logs are in the inventory, make the mouse go to minimap.
				 */
				if(Player.getAnimation() == 875) {
					General.println("Chopping..");
					RSObject[] treeHover = Objects.findNearest(15, TREE_IDS);
					if(treeHover.length > 1)
						treeHover[1].hover();
				}
			}
		}
	}

	public boolean validate() {
		RSObject tree = findNearest(15, TREE_IDS);
		return !Inventory.isFull() && WalkToTrees.treeArea.contains(tree.getPosition());    //run if inventory still has space
	}
	
	private RSObject findNearest(int distance, int ...ids) {
		RSObject[] objs = Objects.findNearest(distance, ids);
		if (objs.length > 0)
			return objs[0];
		return null;
	}
}