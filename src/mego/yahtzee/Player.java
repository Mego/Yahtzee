package mego.yahtzee;

public abstract class Player {
	
	protected ControllerInterface game;
	
	public Player(ControllerInterface game) {
		this.game = game;
	}
	
	public String getName() {
		return this.getClass().getSimpleName();
	}

	public abstract TurnChoice turn(int[] dice, int stage);
	
}
