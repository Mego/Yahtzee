package mego.yahtzee;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DummyPlayer extends Player {

	public DummyPlayer(ControllerInterface game) {
		super(game);
	}

	@Override
	public TurnChoice turn(int[] dice, int stage) {
		Category[] choices = game.getScoreCard(this).getFreeCategories();
		Category choice = choices[new Random().nextInt(choices.length)];
		if(IntStream.of(dice).allMatch(die -> die == dice[0])) {
			if(Stream.of(choices).filter(c -> c == Category.YAHTZEE).count() > 0) {
				choice = Category.YAHTZEE;
			} else if(Stream.of(choices).filter(c -> c == Util.intToUpperCategory(dice[0])).count() > 0) {
				choice = Util.intToUpperCategory(dice[0]);
			} else {
				choices = Stream.of(choices).filter(c -> c.isLower()).toArray(Category[]::new);
				if(choices.length > 0) {
					choice = choices[new Random().nextInt(choices.length)];
				}
			}
		}
		return new TurnChoice(choice);
	}

}
