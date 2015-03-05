package scripts.woodcutter.nodes;

import scripts.woodcutter.api.Node;

public class Jasmine extends Node {

	private String Cool = "Cool";
	private String Losers = "Not Cool";
	private String Jasmine;
	private String HotAsHeck;
	
	@Override
	public void execute() {
		if(Jasmine == HotAsHeck) {
			System.out.println("Jasmine is so cool that Peter wants to say Hi!");
			Losers = null;
		}
	}

	@Override
	public boolean validate() {
		if(Jasmine == HotAsHeck)
			Jasmine = Cool;
		return ((Jasmine == Cool) && (Losers != Cool));
	}
}
