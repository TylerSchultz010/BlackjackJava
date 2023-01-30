import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * 
 */

/**
 * @author Tyler Schultz
 *
 */
public class BlackJackNoFX {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {

		System.out.println("Welcome to Blackjack!");
		Scanner input = new Scanner(System.in);
		String opponent = "";
		System.out.println("\nTo Start off, please indicate whether you would like to face other players or the computer.");
		System.out.println("\nA.) Players		B.) Computer\n");
		opponent = input.next();
		opponent = opponent.toLowerCase();
		
		while((!opponent.contains("a") && !opponent.contains("b")) || opponent.length() != 1)
		{
			System.out.println("\nInvalid input. Please just type either A for players or B for computer\n");
			opponent = input.next();
			opponent = opponent.toLowerCase();
		}
		
		if(opponent.contains("a"))
			playerGame(input);
		
		else
			computerGame(input);
	}
	
	static ArrayList<Card> deck = new ArrayList<Card>();															//an arraylist will hold our deck
	static String [] element = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
	static String [] cardName = new String[52];
	static int [] cardValue = new int[52];
	
	static String continueGame = "";
	
	public static void computerGame(Scanner input) throws InterruptedException
	{
		Card [] player = new Card[7];																				//since the game size is fixed, we don't need a player object to store the player variables
		Card [] computer = new Card[7];
		
		int playGameScore = 0,
			compGameScore = 0;
		
		while(!continueGame.contains("no"))
		{
			createCards(deck, element, cardName, cardValue);
			Collections.shuffle(deck);
			
			int playRoundScore = 0,																					//player and computer round score set to 0	
				compRoundScore = 0;
			
			/*Player goes first*/
			System.out.println("\nComputer's turn");
			compRoundScore = dealCards(deck, computer, compRoundScore);
			compRoundScore = compGame(deck, computer, compRoundScore, 2, (int) (Math.random()*10));	//random variable is exclusive to the ai, it will help them choose to hit or stand
			
			if(compRoundScore > 21)
			{
				System.out.println("Computer has busted out. The player wins via technicality");
				playGameScore++;
			}
			else
			{
				System.out.println("Computer stands with a value of " + compRoundScore + ".");
				playRoundScore = dealCards(deck, player, playRoundScore);
				playRoundScore = playGame(deck, player, playRoundScore, 2);
				if(playRoundScore > 21)
				{
					System.out.println("Player has busted out. The computer wins via technicality");
					compGameScore++;
				}
				else
				{
					System.out.println("Player stands with a value of " + playRoundScore + ".");
					
					if(playRoundScore > compRoundScore)
					{
						System.out.println("Player wins the round!");
						playGameScore++;
					}
					else if(compRoundScore > playRoundScore)
					{
						System.out.println("Computer wins the round!");
						compGameScore++;
					}
					else
						System.out.println("The round is tied!");
				}
			}
			
			System.out.println("\nWould you like to play another Round?");
			continueGame = input.next();
			continueGame = continueGame.toLowerCase();
			
			while((!continueGame.contains("no") || continueGame.length() != 2) && (!continueGame.contains("yes") || continueGame.length() != 3))									//keep looping until the user gives a valid response
			{
				System.out.print("\nInvalid input. Please just type either yes or no. ");
				continueGame = input.next();
				continueGame = continueGame.toLowerCase();
			}
		}
		
		System.out.println("Rounds Won:\nPlayer: " + playGameScore + "\nComputer: " + compGameScore);
		if(playGameScore > compGameScore)
			System.out.println("Player wins the game!");
		else if(compGameScore > playGameScore)
			System.out.println("Computer wins the game!");
		else
			System.out.println("The game is tied!");
		
		System.out.println("Thank you for playing!");
		
	}
	public static void playerGame(Scanner input)
	{
		ArrayList<Player> playerList = new ArrayList<Player>();														//an arraylist to hold all of our players
		int numOfPlayers;
		System.out.println("\nNow, how many people are playing? Note: Minimum is 2, Maximum is 7");
		numOfPlayers = input.nextInt();
		
		while(numOfPlayers < 2 || numOfPlayers > 7)																	//keep looping until user enters a valid number
		{
			System.out.print("\nInvalid input. Please just type a number that is 2 or greater but 7 or less. ");
			numOfPlayers = input.nextInt();
		}
		
		/*Create our players*/
		for(int i = 0; i < numOfPlayers; i++)
		{
			Card [] hand = new Card[7];
			Player player = new Player(0, 0, i+1, hand);															//see Player class for more info
			playerList.add(player);
		}
		
		while(!continueGame.contains("no"))																			//while loop will allow us to play multiple rounds
		{
			createCards(deck, element, cardName, cardValue);
			Collections.shuffle(deck);																				//shuffle the deck
		
			/*deal out cards to all players*/
			
			for(int i = 0; i < numOfPlayers; i++)
			{
				Player currentPlayer = playerList.get(i);															//the current player
				currentPlayer.roundScore = 0;																		//reset the score every round
				System.out.println("\nPlayer " + currentPlayer.id + "'s turn");
				currentPlayer.roundScore = dealCards(deck, currentPlayer.hand, currentPlayer.roundScore);
				currentPlayer.roundScore = playGame(deck, currentPlayer.hand, currentPlayer.roundScore, 2);
				System.out.println("\n" + currentPlayer.roundScore);
				if(currentPlayer.roundScore > 21)																	//score can't go over 21
					System.out.println("Player " + currentPlayer.id + " has busted out");
				else
					System.out.println("Player " + currentPlayer.id + " stands with a value of " + currentPlayer.roundScore + ".");
			}
			
			findWinner(playerList, "round");																		//find the winner of the round
			
			System.out.println("\nWould you like to play another Round? Note: If anybody leaves or joins your game, please relaunch so we can reset our game size. ");
			continueGame = input.next();
			continueGame = continueGame.toLowerCase();
			
			while((!continueGame.contains("no") || continueGame.length() != 2) && (!continueGame.contains("yes") || continueGame.length() != 3))									//keep looping until the user gives a valid response
			{
				System.out.print("\nInvalid input. Please just type either yes or no. ");
				continueGame = input.next();
				continueGame = continueGame.toLowerCase();
			}
		}
		
		findWinner(playerList, "game");																				//find the winner of the game
		System.out.println("Thank you for playing!");
	}
	
	/*Find the winner of the round or the game*/
	public static void findWinner(ArrayList<Player> playerList, String context)
	{
		Player winner = new Player(0, 0, 0, null);																	//winner variable will be overwritten once proven
		int score,																									//either the round score or the game score. same for winnerScore
			winnerScore;
		
		/*Find the winning score*/
		for(int i = 0; i < playerList.size(); i++)
		{
			Player currentPlayer = playerList.get(i);
			
			if(context.contains("round"))																			//depending on the context, we are either finding the round winner or game winner
				score = currentPlayer.roundScore;
			else
				score = currentPlayer.gameScore;
			
			if(score == currentPlayer.roundScore && currentPlayer.roundScore <= 21)									//only print their value if they haven't busted
			{
				System.out.println("Player " + currentPlayer.id + " Value: " + score);
				if(score > winner.roundScore)																		//if the current player has a better hand then the current winner, currentPlayer becomes the round winner
					winner = currentPlayer;
			}
			else if (score == currentPlayer.gameScore)
			{
				System.out.println("Player " + currentPlayer.id + " Score: " + score);
				if(score > winner.gameScore)																		//if the current player has a better score then the current winner, currentPlayer becomes the game winner														
					winner = currentPlayer;
			}
		}
		
		/*Find which player was the winner*/
		for(int i = 0; i < playerList.size(); i++)
		{
			Player currentPlayer = playerList.get(i);
			
			if(context.contains("round"))
			{
				score = currentPlayer.roundScore;
				winnerScore = winner.roundScore;
			}
			else
			{
				score = currentPlayer.gameScore;
				winnerScore = winner.gameScore;
			}
			
			if(score == winnerScore)
			{
				System.out.println("Player " + currentPlayer.id + " wins the " + context + "!");
				if(context.contains("round"))
				currentPlayer.gameScore++;																			//if player wins a round, increase their score
			}
		}
	}
	
	public static int compGame(ArrayList<Card> deck, Card [] comp, int compScore, int nextCard, int compChance) throws InterruptedException
	{
		if(compScore == 21)																	//computer has already won if its score is 21 or greater than player score
			return compScore;
		
		else if (compScore > 21)																						//computer will search to see if there is an ace in case the user goes over
		{
			for(int i = 0; comp[i] != null; i++)
			{
				if(comp[i].cardName.contains("Ace") && comp[i].cardValue == 11)									//if an ace is valued at 11, value is reduced to 1 and the round continues
				{
					compScore = compScore - 10;
					comp[i].cardValue = 1;
					return compGame(deck, comp, compScore, nextCard, compChance);
				}
			}
			return compScore;																							//if there's no ace, return the bust
		}
		
		System.out.println("\nComputer is thinking...");
		TimeUnit.SECONDS.sleep(5);
		
		int hitCriteria = 21 - compScore;
		
		if(compChance < hitCriteria)
		{
			comp[nextCard] = deck.get(0);																			//a card is drawn
			System.out.println("\nThe computer has decided to hit and has drawn " + comp[nextCard].cardName);
			compScore += comp[nextCard].cardValue;
			deck.remove(0);																							//remove card from the deck
			return compGame(deck, comp, compScore, nextCard++, compChance);
		}
		else
			return compScore;
	}
	
	/*Deal cards until player opts to stand or the total value goes above 21*/
	public static int playGame(ArrayList<Card> deck, Card [] player, int value, int nextCard)
	{
		if(value == 21)
			return value;
		
		else if (value > 21)																						//computer will search to see if there is an ace in case the user goes over
		{
			for(int i = 0; player[i] != null; i++)
			{
				if(player[i].cardName.contains("Ace") && player[i].cardValue == 11)									//if an ace is valued at 11, value is reduced to 1 and the round continues
				{
					value = value - 10;
					player[i].cardValue = 1;
					return playGame(deck, player, value, nextCard);
				}
			}
			return value;																							//if there's no ace, return the bust
		}
			
		String userInput = "";
		Scanner input = new Scanner(System.in);
		
		System.out.print("\nPlease type in whether you would like to Hit or Stand? ");
		userInput = input.next();
		userInput = userInput.toLowerCase();																		//force the string to be lower case so our input doesn't need to be case sensitive
		
		while((!userInput.contains("hit") || userInput.length() != 3) && (!userInput.contains("stand") || userInput.length() != 5))
		{
			System.out.print("\nInvalid input. Please just type either hit or stand. ");
			userInput = input.next();
			userInput = userInput.toLowerCase();
		}
		
		if(userInput.contains("hit"))
		{
			player[nextCard] = deck.get(0);																			//a card is drawn
			System.out.println("\nYou have drawn " + player[nextCard].cardName);
			value += player[nextCard].cardValue;
			deck.remove(0);																							//remove card from the deck
			return playGame(deck, player, value, nextCard++);
		}
			
		else																										//if the user enters stand
			return value;
	}
	
	/*Deal out an initial hand to a player to start a round with them*/
	public static int dealCards(ArrayList<Card> deck, Card [] player, int value)
	{
		for(int i = 0; i < 2; i++)																					//each player gets 2 cards to start
		{
			player[i] = deck.get(0);
			value += player[i].cardValue;																			//add the drawn cards value to the total value
			deck.remove(0);
		}
		
		System.out.println("\nUser currently holds:");
		for(int i = 0; i < 2; i++)
			System.out.println(player[i].cardName);
		return value;
	}
	
	static void createCards(ArrayList<Card> deck, String [] element, String [] cardName, int [] cardValue)
	{
		int j = 0;																									//j is an iterator like i but will reset with every suit
		for(int i = 0; i < 52; i++)
		{
			/*First lets fill our cardName array with different card properties*/
			if (j == 13)																							//reset j once it goes through a whole suit
			{
				j = 0;
			}
			
			if (i < 13)
				cardName[i] = element[j] + " of Spades";
			
			else if (i < 26)
				cardName[i] = element[j] + " of Hearts";
			
			else if (i < 39)
				cardName[i] = element[j] + " of Diamonds";
			
			else
				cardName[i] = element[j] + " of Clubs";
			
			
			/*Now lets fill our cardValue array with their value*/
			
			if(j > 9)
				cardValue[i] = 10;																					//Face cards are worth 10
			
			else if(j < 1)
				cardValue[i] = 11;																					//Ace cards are worth 11 or 1
			
			else
				cardValue[i] = j+1;																					//Otherwise the card is worth its number
			
			j++;
			
			Card card = new Card(cardName[i], cardValue[i]);														//Make an object for the card that has its name and value
			deck.add(card);																							//Add that object to the deck
		}
	}
}

class Card{
	String cardName;
	int cardValue;																									//the value of the card in a blackjack game
	
	public Card(String cardName, int cardValue)
	{
		this.cardName = cardName;
		this.cardValue = cardValue;
	}
}

class Player{
  	int roundScore,																									//the players score in a round
  		gameScore,																									//the players score in a game
  		id;																											//an ID to distinguish the player
  	Card [] hand;																									//the players hand of cards

  	public Player(int roundScore, int gameScore, int id, Card[] hand)
  	{
  		this.roundScore = roundScore;
  		this.gameScore = gameScore;
  		this.id = id;
  		this.hand = hand;
  	}
}