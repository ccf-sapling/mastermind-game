/**
 * Brayden McMahon 3057663
 */

import java.util.Random;
import java.util.Scanner;
public class Game {
    private static final int SECRET_CODE_LENGTH = 4; // 4 different colours
    private static final int MAX_GUESSES = 11; // 11 is max guess count
    private int guessCount = 1;
    Scanner kb = new Scanner(System.in);
    ArrayList<Peg> secretCode; // array to place coloured pegs
    ArrayList<Peg> playerGuess;

    public Game() {
        //generateSecretCode();
        playerGuess = new ArrayList<>();
    }

    private void generateSecretCode() {
        String[] pegColours = {"blue", "red", "green", "yellow", "silver", "gold"};
        Random rand = new Random();
        secretCode = new ArrayList<>(SECRET_CODE_LENGTH);

        for (int i = 0; i < SECRET_CODE_LENGTH; i++) {
            int randColour = rand.nextInt(pegColours.length); // get index from 0 to pegColour length
            // select a random colour from the list and assign it to next index
            secretCode.add(i, new Peg(pegColours[randColour]));
        }
        System.out.println("code: " + this.secretCode);
    }

    private void getPlayerGuess() {
        for (int i = 0; i < SECRET_CODE_LENGTH; i++) {
            String guess = kb.next();
            // if guessCount is 1, add the pegs, otherwise, set them instead
            if (guessCount == 1) {
                playerGuess.add(i, new Peg(guess)); // add player's guess to playerGuess list
            }
            else {
                playerGuess.set(i, new Peg(guess));
            }
        }
        guessCount++;

    }

    /**
     * Searches through the secret code and the player's guess array lists, then if there's
     * an exact match, the count variable is incremented by 1 to show another exact match
     * @return int the number of exact matches (colour and position)
     */
    private int completeMatchCount() {
        int count = 0;
        for (int i = 0; i < SECRET_CODE_LENGTH; i++) {
            if (secretCode.get(i).equals(playerGuess.get(i))) {
                count++;
            }
        }
        return count;
    }

    /**
     * Count the number of partial matches that appear in the player's guess. Correct colour, but
     * wrong position.
     * @return int the number of partial matches found
     */
    private int partialMatch() {
        int count = 0;
        for (int i = 0; i < SECRET_CODE_LENGTH; i++) {
            for (int j = 0; j < SECRET_CODE_LENGTH; j++) {
                if (i != j && playerGuess.get(i).equals(secretCode.get(j))) {
                    count++; // simply need to know the number of partial matches
                }
            }
        }
        return Math.min(count, SECRET_CODE_LENGTH);
    }

    /**
     * Displays the system output for the game. This method implements the logic of the system.
     * @param completeMatch the number of complete matches given as an int
     * @param partialMatch the number of partial matches given as an int
     * @return String of the different symbols needed to play the game
     */
    private String displaySystemOutput(int completeMatch, int partialMatch) {
        StringBuilder sb = new StringBuilder();
        if (!isCodeBroken()) {
            for (int i = 0; i < completeMatch; i++) {
                if (i == 2) {
                    sb.append("\n           "); // if there's a third complete match, add a new line to output
                }
                sb.append("x").append(" ");
            }
            for (int j = 0; j < partialMatch; j++) {
                // if sb's length is 4 (2 Xs and 2 spaces), or if j is 2, append a new line
                if (sb.length() == 4 || j == 3 && sb.length() < 5) {
                    sb.append("\n           ");
                }
                sb.append("o").append(" ");
            }
            for (int k = 0; k < SECRET_CODE_LENGTH - (completeMatch + partialMatch); k++) {
                if (sb.length() == 4 || k == 3 && sb.length() < 5) {
                    sb.append("\n           ");
                }
                sb.append("-").append(" ");
            }
        }
        return sb.toString();
    }

    /**
     * Displays the output for the game.
     */
    private void displayOutput() {
        System.out.println("System:    Guess #" + guessCount + ":");
        System.out.println("Player:    " + playerGuess);
        int completeMatches = completeMatchCount();
        int partialMatches = partialMatch();
        if (isCodeBroken()) {
            System.out.println("System:    " + "You cracked the code!");
        }
        else {
            // message if max guesses + 1 reached
            if (guessCount == MAX_GUESSES + 1) {
                System.out.println("System:    " + "You have reached the max number of guesses, game over");
            }
            else {
                System.out.println("System:    " + displaySystemOutput(completeMatches, partialMatches));
            }
        }
    }

    /**
     * Check if the secret code has been broken.
     * @return boolean true if code was broken, false otherwise
     */
    private boolean isCodeBroken() {
        return secretCode.equals(playerGuess);
    }

    public static void main(String[] args) {
        Game mastermind = new Game();
        mastermind.generateSecretCode();

        // while player guess count <= max guesses and both array lists aren't equal
        while(mastermind.guessCount <= MAX_GUESSES){
            if (mastermind.isCodeBroken()) {
                break;
            }
            mastermind.getPlayerGuess();
            mastermind.displayOutput();
        }
    }
}
