package mego.yahtzee;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.IntStream;

public final class Util {

	public static <T> int count(Collection<T> haystack, T needle) {
		return haystack.stream().mapToInt(item -> (item.equals(needle)?1:0)).sum();
	}
	
	public static <T> int count(T[] haystack, T needle) {
		return Util.count(Arrays.asList(haystack), needle);
	}
	
	public static Integer[] boxIntArray(int[] arr) {
		return IntStream.of(arr).boxed().toArray(Integer[]::new);
	}
	
	public static Category intToUpperCategory(int face) {
		switch(face) {
		case 1:
			return Category.ACES;
		case 2:
			return Category.TWOS;
		case 3:
			return Category.THREES;
		case 4:
			return Category.FOURS;
		case 5:
			return Category.FIVES;
		case 6:
			return Category.SIXES;
		default:
			throw new IllegalArgumentException();
		}
	}
	
	public static int upperCategoryToInt(Category c) {
		switch(c) {
		case ACES:
			return 1;
		case TWOS:
			return 2;
		case THREES:
			return 3;
		case FOURS:
			return 4;
		case FIVES:
			return 5;
		case SIXES:
			return 6;
		default:
			throw new IllegalArgumentException();
		}
	}
	
}
