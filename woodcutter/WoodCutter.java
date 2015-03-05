/*
 * Script framework example provided by Worthy
 * http://bit.ly/1M1w4sp
 * 
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

import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.tribot.api.General;
import org.tribot.api2007.Login;
import org.tribot.api2007.Player;
import org.tribot.api2007.Projection;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Arguments;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;

import scripts.woodcutter.api.Node;
import scripts.woodcutter.nodes.Bank;
import scripts.woodcutter.nodes.CutTree;
import scripts.woodcutter.nodes.WalkToTrees;
import scripts.woodcutter.nodes.WalkToBank;
import scripts.woodcutter.nodes.PublicCalls;


/*
 * Learn how to read text arguments instead of using HTML check boxes
 * for ease of client-starter
 */
@ScriptManifest (authors = {"Lmfaoown"},version = 1.0, category = "Framework Example", name = "WoodCutter", description =	
"<html><body><form action=\"aa\">"
		+ "<input type=\"checkbox\" name=\"debug\">Debug mode<br>"
		+ "<input type=\"checkbox\" name=\"paint\">Paint"
		//+ 
+ "</form></body></html>")
public class WoodCutter extends Script implements MessageListening07, Painting, Arguments {
	
	private boolean isLooping;
	private static ArrayList<Node> nodes = new ArrayList<>();
	
	boolean debug;
	boolean paint;
	//String custommode = "false";
	
	@Override
	public void run() {
		Collections.addAll(nodes,
				new CutTree(), 
				new WalkToBank(),
				new Bank(),
				new WalkToTrees(),
				new PublicCalls());    //add all nodes to the ArrayList
		loop(80, 1000);
	}
	
	private void loop(int min, int max) {
		if(Login.getLoginState() == Login.STATE.LOGINSCREEN)
			println("Logging in..."); Login.login();
			
		//debug = debug.toString();
		//paint = paint.toString();
		
		println("debug is.. " + debug);
		println("paint is.. " + paint);
		println("Does debug == true? " + (debug == true));
		println("Does paint == true? " + (paint == true));
		
		//println("custommode is... " + custommode);
		
		isLooping = true;
		
		while (isLooping)
			for (final Node node : nodes)
				if (node.validate()) {
					node.execute();
					sleep(General.random(min, max));
				}
	}

	public boolean getLoopStatus() {
		return isLooping;
	}
	
	public void setLoopStatus(boolean isLooping) {
		this.isLooping = isLooping;
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
			isLooping = false;
		}
	}

	@Override
	public void tradeRequestReceived(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void passArguments(HashMap<String, String> m) {
		debug = m.get("debug").equals("true");
		paint = m.get("paint").equals("true");
		//custommode = m.get("custommode");
	}
	
	@Override
	public void onPaint(Graphics g) {
		//RSObject tree = PublicCalls.findNearest(15, 0, CutTree.TREE_IDS);
		//ASSIGNED_TREE = new RSTile(0, 0);
		//println(paint == "true");
		//println(debug == "true");
		if(paint == true) {
			int x = CutTree.ASSIGNED_TREE.getX();
			int y = CutTree.ASSIGNED_TREE.getY();
			
			Polygon t1 = Projection.getTileBoundsPoly(new RSTile(x, y), 0);
			Polygon t2 = Projection.getTileBoundsPoly(new RSTile(x+1, y), 0);
			Polygon t3 = Projection.getTileBoundsPoly(new RSTile(x, y+1), 0);
			Polygon t4 = Projection.getTileBoundsPoly(new RSTile(x+1, y+1), 0);
			g.drawPolygon(t1); g.drawPolygon(t2); g.drawPolygon(t3); g.drawPolygon(t4);
		}
		
		if(debug == true) {
			//245, 503
			int x = 20;
			int y = 10;
			g.drawString("Animation:   " + Player.getAnimation(), 	x, y+(24*0));
			g.drawString("Position:    " + Player.getPosition(), 	x, y+(24*1));
			
			g.drawString("Tree Pos:    " + CutTree.ASSIGNED_TREE, 	x, y+(24*2));
			
			
			g.drawString("WalkToTrees: " + WalkToTrees.WTTSTATUS, 	x, y+(24*3));
			g.drawString("CutTree:     " + CutTree.CTSTATUS, 		x, y+(24*4));
			g.drawString("WalkToBank:  " + WalkToBank.WTBSTATUS,	x, y+(24*5));
			g.drawString("Bank:        " + Bank.BSTATUS,			x, y+(24*6));
			
		}
	}
}
