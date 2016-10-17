package mego.yahtzee;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public enum Category {
	ACES,
	TWOS,
	THREES,
	FOURS,
	FIVES,
	SIXES,
	THREE_OF_A_KIND,
	FOUR_OF_A_KIND,
	FULL_HOUSE,
	SMALL_STRAIGHT,
	LARGE_STRAIGHT,
	YAHTZEE,
	CHANCE;
	
	public boolean isUpper() {
		switch(this) {
		case ACES:
		case TWOS:
		case THREES:
		case FOURS:
		case FIVES:
		case SIXES:
			return true;
		default:
			return false;
		}
	}
	
	public boolean isLower() {
		return !this.isUpper();
	}
	
	public boolean matches(int[] dice) {
		switch(this) {
			case THREE_OF_A_KIND:
				return IntStream.of(dice).map(die -> Util.count(Util.boxIntArray(dice), die)).max().getAsInt() >= 3;
			case FOUR_OF_A_KIND:
				return IntStream.of(dice).map(die -> Util.count(Util.boxIntArray(dice), die)).max().getAsInt() >= 4;
			case YAHTZEE:
				return IntStream.of(dice).map(die -> Util.count(Util.boxIntArray(dice), die)).max().getAsInt() == 5;
			case FULL_HOUSE:
				int[] distinct = IntStream.of(dice).distinct().toArray();
				int[] counts = IntStream.of(distinct).map(die -> Util.count(Util.boxIntArray(dice), die)).sorted().toArray();
				return counts[0] == 2 && counts[1] == 3;
			case SMALL_STRAIGHT:
				int[][] small_straights = {{1,2,3,4},{2,3,4,5},{3,4,5,6}};
				return Stream.of(small_straights).anyMatch(s -> Collections.indexOfSubList(Arrays.asList(Stream.of(Util.boxIntArray(dice)).distinct().sorted().toArray()), Arrays.asList(Stream.of(Util.boxIntArray(s)).toArray())) != -1);
			case LARGE_STRAIGHT:
				int[][] large_straights = {{1,2,3,4,5},{2,3,4,5,6}};
				return Stream.of(large_straights).anyMatch(s -> Collections.indexOfSubList(Arrays.asList(Stream.of(Util.boxIntArray(dice)).distinct().sorted().toArray()), Arrays.asList(Stream.of(Util.boxIntArray(s)).toArray())) != -1);
			default:
				return true;
			}
	}
	
	public int getScore(int[] dice) {
		if(this.matches(dice)) {
			switch(this) {
				case FULL_HOUSE:
					return 25;
				case SMALL_STRAIGHT:
					return 30;
				case LARGE_STRAIGHT:
					return 40;
				case YAHTZEE:
					return 50;
				case THREE_OF_A_KIND:
				case FOUR_OF_A_KIND:
				case CHANCE:
					return IntStream.of(dice).sum();
				case ACES:
				case TWOS:
				case THREES:
				case FOURS:
				case FIVES:
				case SIXES:
					return IntStream.of(dice).filter(die -> die == Util.upperCategoryToInt(this)).sum();
				default: // this shouldn't be necessary but Java is dumb and can't tell that all the cases are covered
					throw new IllegalArgumentException();
			}
		} else {
			return 0;
		}
	}
	
	public static Category[] getMatchingCategories(int[] dice) {
		return Stream.of(Category.values()).filter(c -> c.matches(dice)).toArray(Category[]::new);
	}
}
