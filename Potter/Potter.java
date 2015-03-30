package scripts;

import java.awt.Point;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Skills;
import org.tribot.api2007.types.RSItem;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.MouseActions;

@ScriptManifest(authors = "Lmfaoown", category = "Tools", name = "Potter", description = "Drinks Prayer and Overload potions when you don't want to.", gameMode = 1, version = 1.0)
public class Potter extends Script implements Ending, MouseActions {
	
	private String[] PRAYERPOTS = { "Prayer potion(1)", "Prayer potion(2)", "Prayer potion(3)", "Prayer potion(4)" };
	private String[] OVERLOADS = { "Overload (1)", "Overload (2)", "Overload (3)", "Overload (4)" };
	
	private int nextDrink = General.random(9, 26);
	private int varyAmount = General.random(-4, 0);
	private int DrankAmount = 0;
	
	@Override
	public void onEnd() {
		println("Script is ending.");
	}

	@Override
	public void run() {
		println("Script is starting.");
		Mouse.move((int)Mouse.getPos().getX(), (int)Mouse.getPos().getY() + 1);
		boolean isLooping = true;
		boolean usingPrayerPot = true;
		boolean usingOverload = true;
		General.useAntiBanCompliance(false);
		setAIAntibanState(false);

		while(isLooping) {
			println("---------------------------");
			if(Inventory.find(PRAYERPOTS).length <= 0) {
				println("Inventory doesn't contain any prayer potions.  Disabling PrayerPot method.");
				usingPrayerPot = false;
			} else {
				usingPrayerPot = true;
			}
			if(Inventory.find(OVERLOADS).length <= 0) {
				println("Inventory doesn't contain any overloads.  Disabling Overload method.");
				usingOverload = false;
			} else {
				usingOverload = true;
			}
			
			if(usingPrayerPot) {
				println("Drinking at.. " + (nextDrink + varyAmount));
				if(Skills.getCurrentLevel(Skills.SKILLS.PRAYER) < (nextDrink + varyAmount)) {
					if(Skills.getCurrentLevel(Skills.SKILLS.PRAYER) < 9) {
						DrinkPrayerPot(2);
					} else if(Skills.getCurrentLevel(Skills.SKILLS.PRAYER) < 26) {
						DrinkPrayerPot(1);
					} else {
						println("Prayer level is acceptable.");
					}
				}
			}
			if(usingOverload) {
				if(Inventory.find(OVERLOADS).length <= 0)
					usingOverload = false;
				if(!(Skills.getCurrentLevel(Skills.SKILLS.HITPOINTS) < 51)) {
					println("Overload won't kill us, checking stats..");
					if(Skills.getCurrentLevel(Skills.SKILLS.ATTACK) <= Skills.getActualLevel(Skills.SKILLS.ATTACK)
						&& Skills.getCurrentLevel(Skills.SKILLS.STRENGTH) <= Skills.getActualLevel(Skills.SKILLS.STRENGTH)
						&& Skills.getCurrentLevel(Skills.SKILLS.DEFENCE) <= Skills.getActualLevel(Skills.SKILLS.DEFENCE)
						&& Skills.getCurrentLevel(Skills.SKILLS.RANGED) <= Skills.getActualLevel(Skills.SKILLS.RANGED)
						&& Skills.getCurrentLevel(Skills.SKILLS.MAGIC) <= Skills.getActualLevel(Skills.SKILLS.MAGIC)) {
						println("Consuming an overload...");
						DrinkOverload();
					} else {
						println("Some of your combat stats are above your actual level, skipping overloads.");
					}
				}
				
			}
			if(usingPrayerPot)
				println("Prayer potion drinking at... " + (ChangeNextDrink() + ChangeVaryAmount()));
			println("Sleeping for 6 seconds..");
			sleep(6000);
		}
	}
	
	private void DrinkPrayerPot(int DrinkAmount) {
		if(Inventory.find(PRAYERPOTS).length > 0) {
			while(DrankAmount < DrinkAmount) {
				if(DrankAmount == 0) {
					RSItem[] Pots = Inventory.find(PRAYERPOTS);
					if(Inventory.find(PRAYERPOTS).length > 0) {
						int CapturedPotID = Pots[0].getID();
						int waitTime = 0;
						
						while(waitTime < 4) {
							sleep(10);
							if(Inventory.find(PRAYERPOTS).length > 0)
								break;
							if(Inventory.find(PRAYERPOTS)[0].getID() != CapturedPotID) {
								DrankAmount++;
								break;
							}
							Inventory.find(PRAYERPOTS)[0].click("Drink");
							println("Clicked drink.");
							waitTime++;
							sleep(1500, 2050);
						}
					}
				}
				println("Drank = " + DrankAmount);
				println("Drink = " + DrinkAmount);
				if(DrankAmount >= DrinkAmount) {
					DrankAmount = 0;
					break;
				}
				if(DrankAmount == 1) {
					RSItem[] Pots = Inventory.find(PRAYERPOTS);
					if(Inventory.find(PRAYERPOTS).length > 0) {
						int CapturedPotID = Pots[0].getID();
						int waitTime = 0;
						
						while(waitTime < 4) {
							sleep(10);
							if(Inventory.find(PRAYERPOTS).length > 0)
								break;
							if(Inventory.find(PRAYERPOTS)[0].getID() != CapturedPotID) {
								println("INSIDE BREAK");
								DrankAmount++;
								break;
							}
							Inventory.find(PRAYERPOTS)[0].click("Drink");
							println("Clicked drink.");
							waitTime++;
							sleep(1000, 1550);
						}
					}
				}
			}
			DrankAmount = 0;
		} else {
			println("We're out of prayer pots.");
		}
 	}
	
	private void DrinkOverload() {
		RSItem[] OverloadPot = Inventory.find(OVERLOADS);
		if(OverloadPot.length > 0)
			if(OverloadPot[0] != null) {
				println("Sending click to Overload pot.");
				OverloadPot[0].click("Drink");
				println("Click sent.");
			}
	}
	
	private int ChangeNextDrink() {
		nextDrink = General.random(8, 26);
		return nextDrink;
	}
	
	private int ChangeVaryAmount() {
		varyAmount = General.random(-4, 0);
		return varyAmount;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void mouseClicked(Point p, int button, boolean isBot) {
		if(isBot) {
			println("Completely stopping mouseClicked thread...");
			Thread.currentThread().stop();
			println("Thread killed unsuccessfully...  Be careful, the bot may move still.");
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void mouseDragged(Point p, int button, boolean isBot) {
		if(isBot) {
			println("Completely stopping mouseDragged thread...");
			Thread.currentThread().stop();
			println("Thread killed unsuccessfully...  Be careful, the bot may move still.");
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void mouseMoved(Point p, boolean isBot) {
		if(isBot) {
			println("Completely stopping mouseMoved thread...");
			Thread.currentThread().stop();
			println("Thread killed unsuccessfully...  Be careful, the bot may move still.");
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void mouseReleased(Point p, int button, boolean isBot) {
		if(isBot) {
			println("Completely stopping mouseReleased thread...");
			Thread.currentThread().stop();
			println("Thread killed unsuccessfully...  Be careful, the bot may move still.");
		}
	}
}