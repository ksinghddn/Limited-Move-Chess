package nn;

import java.io.IOException;

import  engine.ComputerPlayer;
import  genetics.Genes;
import  tui.TUIGame;

public class NetworkTrainer {
	public static String playMatch(double[] genes, boolean isTraining) {
		boolean verbose = false;
		NetworkUtils.initNetworks();

		ComputerPlayer whitePlayer = new ComputerPlayer(verbose, genes,
				isTraining);
		ComputerPlayer blackPlayer = new ComputerPlayer(verbose,
				Genes.defaultGenes, false);
		whitePlayer.setTTLogSize(10);
		blackPlayer.setTTLogSize(10);
		TUIGame game = new TUIGame(whitePlayer, blackPlayer);
		try {
			return game.play(verbose);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void playTournament(double[] genes, boolean isTraining) {
		int numOfMatches = 50;
		double whiteScore = 0;

		NetworkUtils.initNetworks();
		NetworkUtils.loadNetworks();

		for (int i = 0; i < numOfMatches; i++)
			whiteScore += Double.parseDouble(playMatch(genes, isTraining)
					.split("-")[0]);
		System.out.println("Genetic player won " + 100
				* (whiteScore / numOfMatches) + "% of the matches");

		if (isTraining) {
			System.out.println("Starting network training...");
			NetworkUtils.trainNetworks();
			System.out.println("Finished network training. Saving networks...");
			NetworkUtils.saveNetworks();
			System.out.println("Networks saved");
		}
	}

	public static void main(String[] args) {
		boolean isTraining = false;
		playTournament(Genes.mngenes4, isTraining);
	}
}
