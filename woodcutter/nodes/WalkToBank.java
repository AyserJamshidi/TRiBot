package scripts.woodcutter.nodes;

import org.tribot.api.General;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

import scripts.woodcutter.api.Node;

public class WalkToBank extends Node {
	
	public final static RSArea bankArea = new RSArea(new RSTile(3181, 3433, 0), new RSTile(3185, 3441, 0));
	private final RSTile bankTile = new RSTile(3183, 3439, 0);
	public static int WTBSTATUS;
	
	@Override
	public void execute() {
		WTBSTATUS = 1;
		if (bankArea.contains(Player.getPosition())) {
			General.println("Inside the bank!");
		} else {
			//WebWalking.walkToBank();
			WebWalking.walkTo(bankTile);
		}
		WTBSTATUS = 0;
	}

	@Override
	public boolean validate() {
		return Inventory.isFull() && !bankArea.contains(Player.getPosition());
	}
}