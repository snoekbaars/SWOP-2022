package swop.UI;

import java.util.Scanner;

import swop.Exceptions.CancelException;

/**
 * A class used to scan and get the user input
 */
public class InputScanner {
	Scanner inputScanner;

	/**
	 * initializes the inputscanner
	 * @param inputscanner a {@code Scanner}
	 */
	public InputScanner(Scanner inputscanner) {
		this.inputScanner = inputscanner;
	}
	/**
	 * Will scan next line for an integer, keeps asking for input when given string does not meet requirements.
	 * @return valid string as int
	 * @throws CancelException when the user types 'Cancel'
	 */
	public int scanNextLineOfTypeInt() throws CancelException {
		String s;
		if (inputScanner.hasNextLine()) s = this.inputScanner.nextLine();
		else s = this.inputScanner.next();
		if (s.equalsIgnoreCase("cancel")) throw new CancelException();
		try {
			return Integer.parseInt(s);
		} catch(Exception e) {
			System.out.println("Please give valid input");
			return scanNextLineOfTypeInt();
		}
		 
		
	}
	/**
	 * Will scan next line for an integer, keeps asking for input when given string does not meet requirements.
	 * @param leftborder the lowest integer possible
	 * @param rightBorder the highest integer
	 * @return valid string as int
	 * @throws CancelException when the user types 'Cancel'
	 */
	public int scanNextLineOfTypeInt(int leftborder, int rightBorder) throws CancelException {
		String s;
		if (inputScanner.hasNextLine()) s = this.inputScanner.nextLine();
		else s = this.inputScanner.next();
		if (s.equalsIgnoreCase("CANCEL")) throw new CancelException();
		try {
			int number = Integer.parseInt(s);
			while (number < leftborder || number >= rightBorder) {
				System.out.print("Please give valid input:");
				s = this.inputScanner.nextLine();
				number = Integer.parseInt(s);
			}
			return number;
		} catch(Exception e) {
			System.out.print("Please give valid input:");
			return scanNextLineOfTypeInt(leftborder, rightBorder);
		}
		
	}
	/**
	 * Will scan next line for a string, keeps asking for input when given string does not meet requirements.
	 * @return a valid string
	 * @throws CancelException when the user types 'Cancel'
	 */
	public String scanNextLineOfTypeString() throws CancelException {
		String s;
		if (inputScanner.hasNextLine()) s = this.inputScanner.nextLine();
		else s = this.inputScanner.next();
		if (s.equalsIgnoreCase("CANCEL")) throw new CancelException();
		return s;
	}
	/**
	 * Will scan next line for a string, keeps asking for input when given string does not meet requirements.
	 * @param strings array of Strings that input may be equal to.
	 * @return a valid string
	 * @throws CancelException when the user types 'Cancel'
	 */
	public String scanNextLineOfTypeString(String[] strings) throws CancelException {
		String s;
		if (inputScanner.hasNextLine()) s = this.inputScanner.nextLine();
		else s = this.inputScanner.next();
		if (s.equalsIgnoreCase("CANCEL")) throw new CancelException();
		if(strings == null) {
			System.out.println("Invalid Array");//throw error?
			return null;
		}
		for(String str : strings) {
			if(str.equals(s)) return s;
		}
		System.out.print("Please give valid input:");
		return scanNextLineOfTypeString(strings);
		
	}

	/**
	 * Assigns a new input to the inputScanner
	 */
	public void updateScanner() {	
		this.inputScanner = new Scanner(System.in);
	}
}
