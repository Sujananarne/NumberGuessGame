package com.sample.game;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

/**
 * The Class NumberGame.
 * 
 * @author sujanaNarne
 */
public class NumberGame {

	private int number = 1; 
	private int maxNumber = 1000;
	private int minNumber = 0;
	private HashSet<Integer> numbers = new HashSet<Integer>();

	
	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws Exception
	 *             the exception
	 */
	public static void main(String args[]) throws Exception {
          NumberGame numberGame = new NumberGame();
          numberGame.getProperties();
          numberGame.calculateNumber();
	}

	
	/**
	 * Calculate number.
	 * 
	 * Calculate the number guessed by user and takes input from the user, 
	 * validates it.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void calculateNumber() throws Exception {
		String input = null;
		StringBuilder str = new StringBuilder("Guess a number between");
		str = str.append(minNumber+1);
		str = str.append(" and ");
		str = str.append(maxNumber);
		System.out.println(str);
		Scanner inputScanner = new Scanner(System.in);
		startProgram(inputScanner, input);
		System.out.println(generateQuestion(input));
		input = inputScanner.nextLine();
		while (!validateInput(inputScanner,input)) {
			System.out.println(generateQuestion(input));
			input = inputScanner.nextLine().trim();			
		}
	}

	/**
	 * Start program.
	 * 
	 * displays instruction to start the program and validates the
	 * user input to start guessing the number.
	 * @param inputScanner
	 *            the input scanner
	 * @param input
	 *            the input
	 * @return true, if successful
	 */
	private boolean startProgram(Scanner inputScanner, String input) {
		boolean isValidToStart = false;
		if (input == null) {
			System.out.println("To start the program give 'READY' on the prompt:");
		} else if (!input.equalsIgnoreCase("ready")) {
			System.out.println("Invalid input to start the program. Give 'READY' on the prompt:");			
		} else {
			isValidToStart = true;
		}
		if (!isValidToStart) {
			input = inputScanner.nextLine();
			startProgram(inputScanner, input);
		}
		return isValidToStart;
		
	}

	/**
	 * Validate input.
	 * 
	 * validate user input and displays message to the user.
	 * 
	 * @param inputScanner
	 *            the input scanner
	 * @param input
	 *            the input
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	private boolean validateInput(Scanner inputScanner, String input) throws Exception {
		if (!(input.equalsIgnoreCase("higher")	|| input.equalsIgnoreCase("lower") || input.equalsIgnoreCase("yes"))) {
			System.out.println("InValid Answer. Your input can only be higher or lower or yes case insensitive");
			System.out.println("Is the number "+number+" ?");
			input = inputScanner.nextLine();
			validateInput(inputScanner,input);
		}
		if (input.equalsIgnoreCase("yes")) {
			System.out.println("Your number is " + number);
			return true;
		}
		if (maxNumber-minNumber <= 1) {
			if (number > maxNumber) {
				System.out.println("Your number can not be more than "+ maxNumber);
			} else if (number < minNumber) { 
				System.out.println("Your number can not be less than "+ minNumber);
			} else {
				System.out.println("Your number can not be less than "+ number);
			}
			return true;
		}
	
		return false;
	}

	/**
	 * Generate question.
	 * 
	 * generate question depending on the user input.
	 * @param input
	 *            the input
	 * @return the string
	 */
	private String generateQuestion(String input) {
		number = getNextInt(input);	
		if (!numbers.contains(number)) {
			numbers.add(number);
			return "Is the number "+number+" ?";
		} else {
			if (maxNumber-minNumber <= 1) {
				numbers.add(number);
				return "Is the number "+number+" ?";
			}
			return generateQuestion(input);
		}		
	}	
	
	/**
	 * Gets the next int.
	 * 
	 * Set the min and max number depending on the user input
	 * and calls getRandomInt api to get the random number with in the range.
	 * 
	 * @param input
	 *            the input
	 * @return the next int
	 */
	private int getNextInt(String input) {	
		if(input != null && input.equalsIgnoreCase("higher")) {
			if (minNumber < number) {
				minNumber = number;
			}			
		}		
		if(input != null && input.equalsIgnoreCase("lower")) {
			if (maxNumber > number) {
				maxNumber = number;
			}
		}		
		return getRandomInt();
	}
	
	/**
	 * Gets the random int.
	 * 
	 * generates the random number between the min and max number.
	 * @return the random int
	 */
	private int getRandomInt() {
		if (maxNumber - minNumber <= 0){
			return number;
		}
		Random random = new Random();
		int randomNumber =  (minNumber + random.nextInt(maxNumber - minNumber)) + 1;
		return randomNumber;
	}

	/**
	 * Gets the properties.
	 * 
	 * Loads the properties file and read the range of number for 
	 * NumberGuess game.
	 * 
	 * @return the properties
	 */
	private void getProperties() {

		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "./config/game.properties";
			input = new FileInputStream(filename);

			if (input == null) {
				System.out.println("Sorry, unable to find " + filename);
				System.out.println("Setting default range for NumberGuess game");
				return;
			}

			// load a properties file.
			prop.load(input);

			// get the property value and set them for the program
			String minNumberStr = prop.getProperty("numberGame.min.number");
			String maxNumberStr = prop.getProperty("numberGame.max.number");
			try {
				if (minNumberStr != null && maxNumberStr != null) {
					minNumber = Integer.parseInt(minNumberStr.trim()) - 1;
					maxNumber = Integer.parseInt(maxNumberStr.trim());	
					number = minNumber;
				}
			} catch (NumberFormatException ne) {
				System.out.println("Setting default range for NumberGuess game");
				maxNumber = 1000;
				minNumber = 0;

			}			
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
