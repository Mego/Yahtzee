package mego.yahtzee;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Controller implements ControllerInterface {
	
	private static final int NUM_ROUNDS = 1000;
	
	private HashMap<Player, Scorecard> scorecards;
	private Random rand;
	
	private Controller() {
		this.scorecards = new HashMap<Player, Scorecard>();
		this.rand = new Random(new Date().getTime());
	}
	
	private void setPlayers(Player... players) {
		for(Player p: players) {
			scorecards.put(p, new Scorecard());
		}
	}
	
	private int rollD6() {
		return rand.nextInt(6)+1;
	}
	
	private int[] rollDice(int numDice) {
		return IntStream.range(0, numDice).map(x -> rollD6()).toArray();
	}

	/* (non-Javadoc)
	 * @see mego.yahtzee.ControllerInterface#getScoreCard(mego.yahtzee.Player)
	 */
	@Override
	public ScorecardInterface getScoreCard(Player p) {
		return scorecards.get(p).clone();
	}
	
	/* (non-Javadoc)
	 * @see mego.yahtzee.ControllerInterface#getScores()
	 */
	@Override
	public int[] getScores() {
		return scorecards.values().stream().unordered().mapToInt(board -> board.getScore()).toArray();
	}
	
	private void runGame() {
		IntStream.range(0,  13).forEach(x -> this.doTurn());
	}

	private void doTurn() {
		HashMap<Player, Scorecard> newBoards = new HashMap<Player, Scorecard>();
		for(Player p: this.scorecards.keySet()) {
			TurnChoice choice = null;
			int stage = 0;
			int[] dice = null;
			while(stage < 3 && (choice == null || choice.getCategory() != null)) {
				int[] saved = {};
				if(choice != null) {
					List<Integer> diceList = Arrays.asList(Util.boxIntArray(dice));
					saved = IntStream.of(choice.getDiceSaved()).map(i -> diceList.get(i)).toArray();
				}
				dice = this.rollDice(5 - ((choice == null)?(0):(choice.getDiceSaved().length)));
				dice = IntStream.concat(IntStream.of(dice), IntStream.of(saved)).toArray();
				choice = p.turn(dice, stage);
				stage += 1;
			}
			if(choice.getCategory() == null) {
				continue;
			}
			Scorecard board = this.scorecards.get(p);
			board.setScore(choice.getCategory(), dice);
			newBoards.put(p, board);
		}
		this.scorecards = newBoards;
	}
	
	public static void main(String[] args) {
		Controller game = new Controller();
		Player[] players = {new DummyPlayer(game)};
		HashMap<Player, Integer> scores = new HashMap<Player, Integer>();
		Stream.of(players).forEach(p -> scores.put(p, 0));
		for(int i: IntStream.rangeClosed(1, NUM_ROUNDS).toArray()) {
			System.out.println("----- GAME #" + i + " -----");
			game.setPlayers(players);
			game.runGame();
			int maxScore = game.scorecards.values().stream().mapToInt(board -> board.getScore()).max().getAsInt();
			Player[] winners = game.scorecards.entrySet().stream().filter(e -> e.getValue().getScore() == maxScore).map(e -> e.getKey()).toArray(Player[]::new);
			System.out.print("Top scorers (" + maxScore + "):");
			for(Player winner: winners) {
				System.out.print(" " + winner.getName());
				System.out.println();
				scores.put(winner, scores.get(winner)+1);
			}
		}
		Player winner = scores.entrySet().stream().max(Comparator.comparingInt(a -> a.getValue())).map(e -> e.getKey()).get();
		int winningScore = scores.get(winner);
		System.out.println("Winner: " + winner.getName() + " (" + winningScore + ")");
	}
	
}
