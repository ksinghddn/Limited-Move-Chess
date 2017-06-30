package genetics;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import jenes.algorithms.CrowdingGA;
import jenes.chromosome.DoubleChromosome;
import jenes.population.Fitness;
import jenes.population.Individual;
import jenes.population.Population;
import jenes.stage.operator.common.MultiNicheCrowder;
import jenes.stage.operator.common.OnePointCrossover;
import jenes.stage.operator.common.SimpleMutator;
import jenes.stage.operator.common.TournamentSelector;
//import main.innerclass;
import java.util.concurrent.atomic.AtomicInteger;
import  guibase.NoOfMatch;
import  tui.TUIGame;
import java.lang.Object;
public class PhoenixGA extends CrowdingGA<DoubleChromosome> {

	/** This is a list of players who compete in the tournament */
	private ArrayList<GeneticPlayer> players;
	private static final int ELITES = 1;

	// private PhoenixFitness maximize = new PhoenixFitness(true);

	@SuppressWarnings("rawtypes")
	public PhoenixGA(int popsize, int generations,DoubleChromosome initialChromosome) {
		super(new PhoenixFitness(true), new MultiNicheCrowder(),
				new Population<DoubleChromosome>(
						new Individual<DoubleChromosome>(initialChromosome),
						popsize), generations);

		this.setElitism(ELITES);
		this.setElitismStrategy(ElitismStrategy.WORST);

		this.addStage(new TournamentSelector<DoubleChromosome>(2));
		this.addStage(new OnePointCrossover<DoubleChromosome>(0.8));
		this.addStage(new SimpleMutator<DoubleChromosome>(0.02));
	}

	@SuppressWarnings("deprecation")
	private void playTournament() {
		Population<DoubleChromosome> pop = this.getCurrentPopulation();
		List<Individual<DoubleChromosome>> individuals = pop.getIndividuals();
		Random random = new Random();
		double score = 0.0;
		int numOfPlayers = individuals.size();
		players = new ArrayList<GeneticPlayer>();

		for (int playerIndex = 0; playerIndex < numOfPlayers; playerIndex++) {
			players.add(new GeneticPlayer(individuals.get(playerIndex).getChromosome().getValues()));
		}
		int threads = Runtime.getRuntime().availableProcessors();
		System.out.println("no. of threads = "+threads);
		  ExecutorService service = Executors.newFixedThreadPool(threads);
		for (int playerIndex = 0; playerIndex < numOfPlayers; playerIndex++) {
			
			service.submit(new innerclass(playerIndex,individuals));
		
			/*GeneticPlayer player1 = players.get(playerIndex);
		//	int opponentIndex;
			while (player1.getNumOfGames() < 3) {
			//	while ((opponentIndex = random.nextInt(numOfPlayers)) == playerIndex) Kuldeep
				//	; Kuldeep
				//GeneticPlayer player2 = players.get(opponentIndex);Kuldeep
				GeneticPlayer player2 = new GeneticPlayer(Genes.defaultGenes); // added by kuldeep
				player1.setNumOfGames(player1.getNumOfGames() + 1);
				player2.setNumOfGames(player2.getNumOfGames() + 1);
				TUIGame game = new TUIGame(player1.getPlayer(),
						player2.getPlayer());
				try {
					
							NoOfMoves move = new NoOfMoves();  // added by kuldeep
							move.no_of_moves=0;	// added by kuldeep				
							System.out.println("Match no. = "+NoOfMatch.count++);// added by kuldeep
					score = getScore(game.play(false));
				} catch (IOException e) {
					e.printStackTrace();
				}
				player1.setNumOfWins(player1.getNumOfWins() + score);
				player2.setNumOfWins(player2.getNumOfWins() + (1 - score));
			}
			// Set fitness value for the individual*/
			//individuals.get(playerIndex).setScore(player1.getNumOfWins() / player1.getNumOfGames()); //done by kuldeep
			
		}
		service.shutdown();
	/*	 try
		 {
		 service.awaitTermination(100000000, TimeUnit.MINUTES);
		 }
		 catch (InterruptedException e) 
		 {
			  System.out.println("some issue with the termination function");
		}*/
		while (!service.isTerminated()) {   }
		 System.out.println("generation ended");
	}
	public class innerclass implements Runnable
	{
		int playerIndex;
		List<Individual<DoubleChromosome>> individuals;
		public innerclass(int player,List<Individual<DoubleChromosome>> individuals)
		{
			this.playerIndex=player	;
			this.individuals=individuals;
		}
		public void run()
		{
			GeneticPlayer player1 = players.get(playerIndex);
			GeneticPlayer player2 = new GeneticPlayer(Genes.defaultGenes); // added by kuldeep
			double score = 0.0;
			int ccount=0;
				while (ccount < 3) {
					
				//	while ((opponentIndex = random.nextInt(numOfPlayers)) == playerIndex) Kuldeep
					//	; Kuldeep
					//GeneticPlayer player2 = players.get(opponentIndex);Kuldeep
					
					player1.setNumOfGames(player1.getNumOfGames() + 1);
					TUIGame game = new TUIGame(player1.getPlayer(),	player2.getPlayer());
					try {
							
								System.out.println("player no.= "+playerIndex+" Match no. = "+ccount+"started");// added by kuldeep
						score = getScore(game.play(false));
						System.out.println("player no.= "+playerIndex+" Match no. = "+(ccount++)+"completed");
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					player1.setNumOfWins(player1.getNumOfWins() + score);
					//player2.setNumOfWins(player2.getNumOfWins() + (1 - score));
				}
				// Set fitness value for the individual
				individuals.get(playerIndex).setScore(player1.getNumOfWins() / player1.getNumOfGames());
		}
	}

	@Override
	protected void onGeneration(long time) {
		super.onGeneration(time);

		playTournament();
		double[] wins = new double[players.size()];
		for (int i = 0; i < players.size(); i++) {
			wins[i] = players.get(i).getNumOfWins();
		}
		Arrays.sort(wins);

		double highestScore = wins[wins.length - 1];
		for (GeneticPlayer player : players) {
			if (player.getNumOfWins() == highestScore&& this.getGeneration()%2==0) {
				
				System.out.println(this.getGeneration() + " : "	+ prettyPrint(player.getGenes()));
				
					
					
					System.out.println(this.getGeneration() + " : "	+ prettyPrint(player.getGenes()));
				
			}
		}
	}

	private String prettyPrint(double[] genes) {
		StringBuilder sb = new StringBuilder();
		for (double d : genes)
			sb.append(d + " ");
		return sb.toString();
	}

	private double getScore(String finalScore) {
		try {
			return Double.parseDouble(finalScore.split("-")[0]);
		} catch (Exception e) {
			System.out.println("Error converting to double. PhoenixGA -> getScore");
			System.exit(-1);
		}
		return 0.0;
	}
}

class PhoenixFitness extends Fitness<DoubleChromosome> {
	PhoenixFitness(boolean maximize) {
		super(maximize);
	}

	@Override
	public void evaluate(Individual<DoubleChromosome> individual) {
		// Fitness values are already set during the Tournament
	}
}
