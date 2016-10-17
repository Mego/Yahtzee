package mego.yahtzee;

public interface ScorecardInterface {

	// returns an array of unused categories
	Category[] getFreeCategories();

	// returns the current total score
	int getScore();

	// returns the current Yahtzee bonus
	int getYahtzeeBonus();

	// returns the current Upper Section bonus
	int getUpperBonus();

	// returns the current Upper Section total
	int getUpperScore();

}