package scripts.woodcutter.nodes;

import org.tribot.api.General;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import scripts.woodcutter.api.Node;

public class Bank extends Node {

	public static int BSTATUS;

	public void execute() {
		BSTATUS = 1;
		while(Inventory.isFull()) {
			if(!Banking.isBankScreenOpen()) {
				Banking.openBank();
			} else {
				Banking.depositAll();
			}
			General.sleep(1000, 2500);
		}
		BSTATUS = 0;
	}

	@Override
	public boolean validate() {
		return WalkToBank.bankArea.contains(Player.getPosition()) && Inventory.isFull();
	}

}
