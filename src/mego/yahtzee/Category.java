package mego.yahtzee;

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
}
