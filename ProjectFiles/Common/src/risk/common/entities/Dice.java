package risk.common.entities;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.io.Serializable;

/**
 * Represents a dice used in the game for generating random outcomes.
 * <p>
 * This class encapsulates the functionality of a dice, allowing the rolling of a specified number of dice and returning the results in a sorted order. It also provides a method for prompting the user to input their dice rolls, validating the input, and sorting the results. The class uses {@link Random} for generating random numbers and {@link Scanner} for reading user input.
 */
public class Dice{
    private Random random;
    private Scanner scanner;

    /**
     * Constructs a Dice instance with a new Random object for generating random numbers and a new Scanner object for reading user input from the console.
     * <p>
     * This constructor initializes the {@code random} field with a new instance of {@link Random}, which is used to generate random dice rolls. Additionally, the {@code scanner} field is initialized with a new instance of {@link Scanner}, specifically for reading dice roll inputs from the user through the standard input stream (console).
     */
    public Dice() {
        this.random = new Random();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Rolls a specified number of dice and returns the results in descending order.
     * <p>
     * This method simulates rolling a given number of dice, each producing a random result between 1 and 6 (inclusive). The results are then sorted in descending order, with the highest values first, to facilitate comparisons or further processing as required by game logic.
     *
     * @param numDice The number of dice to roll. Must be a positive integer.
     * @return An array of {@link Integer} objects representing the results of the dice rolls, sorted in descending order.
     */
    public Integer[] rollDice(int numDice) {
        Integer[] results = new Integer[numDice];
        Random random = new Random();

        for (int i = 0; i < numDice; i++) {
            results[i] = random.nextInt(6) + 1;
        }

        Arrays.sort(results, Collections.reverseOrder());
        return results;

    }

    /**
     * Prompts the user for dice rolls, validates the input, and returns the results in ascending order.
     * <p>
     * This method displays a message prompting the user to input their dice rolls. It expects the user to enter a series of numbers, separated by spaces, corresponding to the results of rolling a specified number of dice. Each number must be between 1 and 6 (inclusive) to represent a valid dice roll. The method validates the input to ensure it meets these criteria. If the input is valid, it sorts the results in ascending order and returns them. This sorted array can be used for further processing, such as comparing dice rolls in a game.
     *
     * @param message The message displayed to the user, prompting them to input their dice rolls.
     * @param numberOfRolls The number of dice rolls the user is expected to input. This determines the size of the returned array.
     * @return An array of integers representing the validated and sorted dice rolls input by the user.
     * @throws IllegalArgumentException if any of the input rolls are outside the range of 1 to 6.
     */
    public int[] promptDiceRolls(String message,int numberOfRolls) {
        int[] rolls = new int[numberOfRolls];
        System.out.println(message);
        String input = scanner.nextLine();
        String [] inputArray = input.split(" ");
        for (int i = 0; i < numberOfRolls; i++) {
            int roll =  Integer.parseInt(inputArray[i]);
            if (roll < 1 || roll > 6) {
                throw new IllegalArgumentException("Invalid roll: " + roll + ". Must be between 1 and 6.");
            } else {
                rolls[i] = roll;
            }
        }
        Arrays.sort(rolls);
        return rolls;
    }

    /**
     * Generates a random number between 1 and 6, inclusive.
     * <p>
     * This method creates a new instance of {@link Random} and uses it to generate a random integer between 1 and 6. This simulates the roll of a single dice, which is a common requirement in dice-based games. The method ensures that the result is always within the specified range, making it suitable for use wherever a dice roll is needed.
     *
     * @return A random integer between 1 and 6, inclusive, simulating a dice roll.
     */
    private int randomNumber() {
        Random random = new Random();
        return random.nextInt(6) + 1;
    }
}