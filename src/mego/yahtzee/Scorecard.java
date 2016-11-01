package mego.yahtzee;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Scorecard implements ScorecardInterface {
	
	private HashMap<Category, Integer> scores;
	private int upperBonus;
	private int yahtzeeBonus;
	private int upperScore;
	
	public Scorecard() {
		this.scores = new HashMap<Category, Integer>();
		this.upperBonus = 0;
		this.yahtzeeBonus = 0;
		this.upperScore = 0;
	}
	
	@Override
	public ScorecardInterface clone() {
		Scorecard other = new Scorecard();
		for(Category c: this.scores.keySet()) {
			other.scores.put(c, this.scores.get(c));
		}
		other.upperBonus = this.upperBonus;
		other.yahtzeeBonus = this.yahtzeeBonus;
		other.upperScore = this.upperScore;
		return other;
	}
	
	/* (non-Javadoc)
	 * @see mego.yahtzee.ScorecardInterface#getFreeCategories()
	 */
	@Override
	public Category[] getFreeCategories() {
		return Stream.of(Category.values()).filter(c -> !scores.containsKey(c)).toArray(Category[]::new);
	}

	/* (non-Javadoc)
	 * @see mego.yahtzee.ScorecardInterface#getScore()
	 */
	@Override
	public int getScore() {
		return this.scores.values().stream().mapToInt(x -> x).sum() + upperBonus + yahtzeeBonus;
	}
	
	@Override
	public int getYahtzeeBonus() {
		return this.yahtzeeBonus;
	}
	
	@Override
	public int getUpperBonus() {
		return this.upperBonus;
	}
	
	@Override
	public int getUpperScore() {
		return this.upperScore;
	}
	
	public void setScore(Category c, int[] dice) {
		boolean joker = false;
		if(scores.containsKey(Category.YAHTZEE) && IntStream.of(dice).distinct().count() == 1) {
			// thanks to @TNT from PPCG.SE for help with this logic (http://chat.stackexchange.com/transcript/message/33233446#33233446)
			int face = dice[0];
			Category upper = Util.intToUpperCategory(face);
			boolean isUpperTaken = scores.containsKey(upper);
			if(!isUpperTaken && c != upper) {
				throw new IllegalArgumentException();
			} else if(isUpperTaken && c.isUpper() && Stream.of(this.getFreeCategories()).filter(x -> x.isLower()).count() > 0) {
				throw new IllegalArgumentException();
			}
			this.yahtzeeBonus += 100;
			joker = true;
		}
		if(scores.containsKey(c)) {
			throw new IllegalArgumentException();
		}
		switch(c) {
		case SMALL_STRAIGHT:
			int[][] small_straights = {{1,2,3,4},{2,3,4,5},{3,4,5,6}};
			if(joker || Stream.of(small_straights).anyMatch(s -> Collections.indexOfSubList(Arrays.asList(Stream.of(Util.boxIntArray(dice)).distinct().sorted().toArray()), Arrays.asList(Stream.of(Util.boxIntArray(s)).toArray())) != -1)) {
				this.scores.put(c, 30);
			} else {
				this.scores.put(c,  0);
			}
			break;
		case LARGE_STRAIGHT:
			int[][] large_straights = {{1,2,3,4,5},{2,3,4,5,6}};
			if(joker || Stream.of(large_straights).anyMatch(s -> Collections.indexOfSubList(Arrays.asList(Stream.of(Util.boxIntArray(dice)).distinct().sorted().toArray()), Arrays.asList(Stream.of(Util.boxIntArray(s)).toArray())) != -1)) {
				this.scores.put(c, 40);
			} else {
				this.scores.put(c,  0);
			}
			break;
		case FULL_HOUSE:
			int[] distinct = IntStream.of(dice).distinct().toArray();
			int[] counts = IntStream.of(distinct).map(die -> Util.count(Util.boxIntArray(dice), die)).sorted().toArray();
			if(joker || counts[0] == 2 && counts[1] == 3) {
				this.scores.put(c, 25);
			} else {
				this.scores.put(c, 0);
			}
			break;
		case THREE_OF_A_KIND:
			if(joker || IntStream.of(dice).map(die -> Util.count(Util.boxIntArray(dice), die)).max().getAsInt() >= 3) {
				this.scores.put(c, IntStream.of(dice).sum());
			} else {
				this.scores.put(c, 0);
			}
			break;
		case FOUR_OF_A_KIND:
			if(joker || IntStream.of(dice).map(die -> Util.count(Util.boxIntArray(dice), die)).max().getAsInt() >= 4) {
				this.scores.put(c, IntStream.of(dice).sum());
			} else {
				this.scores.put(c, 0);
			}
			break;
		case YAHTZEE:
			if(IntStream.of(dice).map(die -> Util.count(Util.boxIntArray(dice), die)).max().getAsInt() == 5) {
				this.scores.put(c, 50);
			} else {
				this.scores.put(c, 0);
			}
			break;
		case CHANCE:
			this.scores.put(c, IntStream.of(dice).sum());
			break;
		case ACES:
		case TWOS:
		case THREES:
		case FOURS:
		case FIVES:
		case SIXES:
			int face = Util.upperCategoryToInt(c);
			this.scores.put(c, IntStream.of(dice).filter(die -> die == face).sum());
			this.upperScore += this.scores.get(c);
			break;
		}
		if(this.upperScore >= 63) {
			this.upperBonus = 35;
		}
	}
	
}
