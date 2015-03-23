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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.tribot.api.General;
import org.tribot.api.Timing;
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
@ScriptManifest (
		authors = {"Lmfaoown"},
		version = 1.0, 
		category = "Woodcutting", 
		name = "VWB Tree Killer", 
		description =	
	"<html><body><form action=\"aa\">"
		+ "<input type=\"checkbox\" name=\"debug\">Debug mode<br>"
		+ "<input type=\"checkbox\" name=\"paint\">Paint"
+ "</form></body></html>")
public class WoodCutter extends Script implements MessageListening07, Painting, Arguments {
	private static ArrayList<Node> nodes = new ArrayList<>();
	
	public static boolean isLooping;
		
	boolean debug;
	boolean paint;
	long startTime, runTime, timeRan;
	//String custommode = "false";
	
	@Override
	public void run() {
		General.useAntiBanCompliance(true);
		Collections.addAll(nodes,
				new CutTree(), 
				new WalkToBank(),
				new Bank(),
				new WalkToTrees(),
				new PublicCalls()
		); loop(10, 40);
	}
	
	public boolean LoggedIn() {
		if(Login.getLoginState() != Login.STATE.LOGINSCREEN)
			return true;
		return false;
	}
	
	private void loop(int min, int max) {
		isLooping = true;
		startTime = System.currentTimeMillis();

		while (isLooping)
			for (final Node node : nodes)
				if (node.validate()) {
					node.execute();
					sleep(General.random(min, max));
				}
	}
	
	@Override
	public void clanMessageReceived(String arg0, String arg1) {}

	@Override
	public void duelRequestReceived(String arg0, String arg1) {}

	@Override
	public void personalMessageReceived(String arg0, String arg1) {}

	@Override
	public void playerMessageReceived(String arg0, String arg1) {}

	@Override
	public void tradeRequestReceived(String arg0) {}
	
	@Override
	public void serverMessageReceived(String arg0) {
		//println(arg0);
		if(arg0.toLowerCase().contains("an axe to chop")) {
			println("No axe, stopping and logging out.");
			isLooping = false;
		}
	}

	public void passArguments(HashMap<String, String> arg0) {
		debug = arg0.get("debug").equals("true");
		paint = arg0.get("paint").equals("true");
	}	
	
	/*
	 * -------------------------------------------------------------------------
	 * --                                Paint                                --
	 * -------------------------------------------------------------------------
	 */
	private static void drawTile(RSTile tile, Graphics2D g) {
		if(tile.isOnScreen())
        	g.drawPolygon(Projection.getTileBoundsPoly(tile, 0));
	}
	
	/*
	 * Make selections for which part of paint should be used.
	 * e.g.
	 * If paint is checked -> Branch off to floor polygons, tree polygons, etc
	 */
	@Override
	public void onPaint(Graphics g) {
		if(LoggedIn()) {
			runTime = System.currentTimeMillis();
			timeRan = runTime - startTime;
			g.drawString("Time ran: " + Timing.msToString(timeRan), 100, 300);
			if(debug) {
				int x = 20;
				int y = 20;
				int bump = 12;
				g.setColor(Color.GREEN);
				g.drawString("Animation:   " + Player.getAnimation(), 	x, y+(bump*0));
				g.drawString("Position:    " + Player.getPosition(), 	x, y+(bump*1));
				
				g.drawString("Tree Pos:    " + CutTree.ASSIGNED_TREE, 	x, y+(bump*2));
				
				g.drawString("WalkToTrees(): " + WalkToTrees.WTTSTATUS, x, y+(bump*3));
				g.drawString("CutTree():           " + CutTree.CTSTATUS, 		x, y+(bump*4));
				g.drawString("WalkToBank():  " + WalkToBank.WTBSTATUS,	x, y+(bump*5));
				g.drawString("Bank():                " + Bank.BSTATUS,			x, y+(bump*6));
				g.drawString("isAtTree():          " + CutTree.IATSTATUS,		x, y+(bump*7));
				//g.drawString("a is.......... " + CutTree.a, 			x, y+(bump*8));
			}
			
			if(paint) {
				int treeX = CutTree.ASSIGNED_TREE.getX();
				int treeY = CutTree.ASSIGNED_TREE.getY();
				int hTreeX = CutTree.HOVERING_TREE.getX();
				int hTreeY = CutTree.HOVERING_TREE.getY();
				int sTreeX = CutTree.gTS.getX();
				int sTreeY = CutTree.gTS.getY();
				
				g.setColor(Color.BLUE);
				// Draw RSArea polygon
				for(int x = WalkToTrees.areaMinX; x<= WalkToTrees.areaMaxX; x++)
					for(int y = WalkToTrees.areaMinY; y<= WalkToTrees.areaMaxY;y++)
						drawTile(new RSTile(x,y,0),(Graphics2D)g);
				
				// Mouse hovering tree
				if(new RSTile(hTreeX, hTreeY).isOnScreen()) {
					Polygon hTree1 = Projection.getTileBoundsPoly(new RSTile(hTreeX, hTreeY), 0);
					Polygon hTree2 = Projection.getTileBoundsPoly(new RSTile(hTreeX+1, hTreeY), 0);
					Polygon hTree3 = Projection.getTileBoundsPoly(new RSTile(hTreeX, hTreeY+1), 0);
					Polygon hTree4 = Projection.getTileBoundsPoly(new RSTile(hTreeX+1, hTreeY+1), 0);
					g.setColor(Color.RED);
					g.drawPolygon(hTree1); g.drawPolygon(hTree2); g.drawPolygon(hTree3); g.drawPolygon(hTree4);	
				}
				
				// Current chopping tree
				if(new RSTile(treeX, treeY).isOnScreen()) {
					Polygon tree1 = Projection.getTileBoundsPoly(new RSTile(treeX, treeY), 0);
					Polygon tree2 = Projection.getTileBoundsPoly(new RSTile(treeX+1, treeY), 0);
					Polygon tree3 = Projection.getTileBoundsPoly(new RSTile(treeX, treeY+1), 0);
					Polygon tree4 = Projection.getTileBoundsPoly(new RSTile(treeX+1, treeY+1), 0);
					
					g.setColor(Color.GREEN);
					g.drawPolygon(tree1); g.drawPolygon(tree2); g.drawPolygon(tree3); g.drawPolygon(tree4);
				}
				
				// Nearest tree stump

				if(new RSTile(treeX, treeY).isOnScreen()) {
					Polygon stump1 = Projection.getTileBoundsPoly(new RSTile(sTreeX, sTreeY), 0);
					Polygon stump2 = Projection.getTileBoundsPoly(new RSTile(sTreeX+1, sTreeY), 0);
					Polygon stump3 = Projection.getTileBoundsPoly(new RSTile(sTreeX, sTreeY+1), 0);
					Polygon stump4 = Projection.getTileBoundsPoly(new RSTile(sTreeX+1, sTreeY+1), 0);
					
					g.setColor(Color.CYAN);
					g.drawPolygon(stump1); g.drawPolygon(stump2); g.drawPolygon(stump3); g.drawPolygon(stump4); 
				}
			}
		}
	}
}