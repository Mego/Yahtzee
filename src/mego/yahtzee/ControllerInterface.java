package mego.yahtzee;

public interface ControllerInterface {

	// returns the player's scorecard (cloned copy, so don't try any funny business)
	ScorecardInterface getScoreCard(Player p);

	// returns the current scores for all players, in no particular order
	int[] getScores();

}