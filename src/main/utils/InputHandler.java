package utils;

import java.util.Scanner;

public abstract class InputHandler {
    protected Scanner input = new Scanner(System.in);

    // REQUIRES: prompt not empty
    // EFFECTS: print prompt and return user input
    private String handlePromptInput(String prompt) {
        System.out.println(prompt);
        String userInput = input.nextLine().trim();
        return userInput;
    }

    // REQUIRES: prompt not empty
    // EFFECTS: repeatedly prompts until a valid positive double is provided; returns the double
    protected double getValidatedDoubleInput(String prompt) {
        Double input = null;
        while (input == null) {
            String userInput = handlePromptInput(prompt);
            if (userInput.isEmpty()) {
                System.out.println("No input provided. Please enter a valid number.");
                continue;
            }
            try {
                input = Double.parseDouble(userInput);
                if (input <= 0) {
                    System.out.println("Value must be positive. Please try again.");
                    input = null;  // reset if the value is not positive
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Please enter a valid number.");
            }
        }
        return input;
    }

    // REQUIRES: prompt not empty
    // EFFECTS: repeatedly prompts until a valid positive integer is provided; returns the integer
    protected int getValidatedIntegerInput(String prompt) {
        Integer input = null;
        while (input == null) {
            String userInput = handlePromptInput(prompt);
            if (userInput.isEmpty()) {
                System.out.println("No input provided. Please enter a valid number.");
                continue;
            }
            try {
                input = Integer.parseInt(userInput);
                if (input <= 0) {
                    System.out.println("Value must be positive. Please try again.");
                    input = null;  // reset if the value is not positive
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Please enter a valid number.");
            }
        }
        return input;
    }

    // REQUIRES: prompt not empty
    // EFFECTS: repeatedly prompts until a non-empty input string is provided; returns the string
    protected String getValidatedStringInput(String prompt) {
        String input = "";
        while (input.isEmpty()) {
            input = handlePromptInput(prompt);
            if (input.isEmpty()) {
                System.out.println("No input provided. Please enter a valid input.");
                return null;
            }
        }
        return input;
    }
}
