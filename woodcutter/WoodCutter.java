/*
 * Script framework example provided by Worthy
 * http://bit.ly/1M1w4sp
 * 1
 * Paint example provided by Deltac0
 * http://bit.ly/1F0WwAj
 * 
 * RSArea example provided by Bradsta
 * http://bit.ly/1CjKO65
 * 
 * Banking example provided by Bradsta
 * http://bit.ly/1BPXzWu
 */

package scripts.woodcutter;

import java.util.ArrayList;
import java.util.Collections;

import org.tribot.api.General;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.MessageListening07;

import scripts.woodcutter.api.Node;
import scripts.woodcutter.nodes.Bank;
import scripts.woodcutter.nodes.CutTree;
import scripts.woodcutter.nodes.WalkToTrees;
//import scripts.woodcutter.nodes.FailSafe;
import scripts.woodcutter.nodes.WalkToBank;

@ScriptManifest (authors = {"Lmfaoown"}, category = "Framework Example", name = "WoodCutter")
public class WoodCutter extends Script implements MessageListening07 {
	public boolean loopStatus = true;
	public static ArrayList<Node> nodes = new ArrayList<>();
	
	
	
	public void run() {
		Collections.addAll(nodes,
				new CutTree(), 
				new WalkToBank(),
				new Bank(),
				new WalkToTrees());    //add all nodes to the ArrayList
		loop(20, 40);
	}
	
	private void loop(int min, int max) {
		while (loopStatus) {
			for (final Node node : nodes) {
				if (node.validate()) {
					node.execute();
					sleep(General.random(min, max));
				}
			}
		}
	}

	@Override
	public void clanMessageReceived(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void duelRequestReceived(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void personalMessageReceived(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerMessageReceived(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serverMessageReceived(String arg0) {
		// TODO Auto-generated method stub
		println(arg0);
		if(arg0.toLowerCase().contains("an axe to chop")) {
			println("No axe, stopping and logging out.");
			loopStatus = false;
		}
	}

	@Override
	public void tradeRequestReceived(String arg0) {
		// TODO Auto-generated method stub
		
	}
}
