package scripts.woodcutter.nodes;

import org.tribot.api.General;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSObject;

import scripts.woodcutter.api.Node;

public class Bank extends Node {

	public static String BSTATUS;

	@Override
	public void execute() {
		BSTATUS = "1";
		General.println("Inventory is full, doing stuff...");
		while(Inventory.isFull()) {
			RSObject[] bankBooth = Objects.findNearest(20, 11748);
			int tries = 0;
			if(!Banking.isBankScreenOpen() && bankBooth.length > 0) {
				if(bankBooth.length > 0) {
					bankBooth[0].click("Bank");
					while(!Banking.isBankScreenOpen() && tries < 5) {
						tries++;
						General.sleep(400);
					}
				}
			} else {
				if(Banking.isBankScreenOpen()) {
					tries = 0;
					while(Inventory.isFull()) {
						Banking.depositAll();
						General.sleep(400,700);
					}
				}
			}
		}
		Banking.close();
		BSTATUS = "0";
	}

	@Override
	public boolean validate() {
		return WalkToBank.bankArea.contains(Player.getPosition()) && Inventory.isFull();
	}

}
