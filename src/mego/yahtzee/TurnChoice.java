package mego.yahtzee;
import java.util.stream.IntStream;

public class TurnChoice {
	
	private int[] diceSaved;
	private Category categoryChosen;
	
	// save the dice with the specified indexes (0-4 inclusive)
	public TurnChoice(int[] diceIndexes) {
		for(int index: diceIndexes) {
			if(IntStream.of(diceIndexes).filter(i -> i == index).count() > 1) {
				throw new IllegalArgumentException();
			}
			if(index < 0 || index > 4) {
				throw new IllegalArgumentException();
			}
		}
		this.diceSaved = diceIndexes.clone();
		this.categoryChosen = null;
	}
	
	// use the current dice for specified category
	public TurnChoice(Category categoryChosen) {
		this.categoryChosen = categoryChosen;
		this.diceSaved = new int[0];
	}
	
	public int[] getDiceSaved() {
		return diceSaved.clone();
	}
	
	public Category getCategory() {
		return categoryChosen;
	}
	
}
