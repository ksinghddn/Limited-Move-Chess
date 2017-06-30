package  tui;

import  genetics.Genes;


public class TestTournament {

	@SuppressWarnings("unused")
	private static void playAll() {
		int numOfMatches = 1;

		for (double[] genes : Genes.allGenes) {
			double whiteScore = 0;
			for (int i = 0; i < numOfMatches; i++)
				whiteScore += Double.parseDouble(TestMatch.play(genes).split(
						"-")[0]);

			System.out.println("Genetic player won " + 100
					* (whiteScore / numOfMatches) + "% of the matches");
		}
	}

	private static void play(double[] genes) {
		int numOfMatches = 100;
		double whiteScore = 0;
		long start = System.currentTimeMillis();

		String header = "[Event \"Training Tournament\"]\n"
				+ "[White \"mgGenes4\"]\n" + "[Black \"defaultGenes\"]\n"
				+ "[Date \"2014.06.05\"]\n" + "[TimeControl \"5|0\"]\n"
				+ "[Result \"";
		String tail = "\"]\n";

		for (int i = 0; i < numOfMatches; i++) {
			
			System.out.print(header);
			String result = TestMatch.play(genes);
			System.out.print(result);
			System.out.println(tail);
			
			whiteScore +=(Double.parseDouble(result.split("-")[0]));
			System.out.println("Current score= "+whiteScore);
			System.out.println(TestMatch.pgn);
			System.out.println();
		}

		long end = System.currentTimeMillis();
		System.out.println("Genetic player won " + 100
				* (whiteScore / numOfMatches) + "% of the matches");
		System.out.println("Total execution time : " + (end - start) / 1000
				+ " seconds");
	}

	public static void main(String[] args) {
		// playAll();
		play(Genes.mngenes200);
	}
}
