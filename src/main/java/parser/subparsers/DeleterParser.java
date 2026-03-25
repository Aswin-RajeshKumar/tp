package parser.subparsers;

import exception.JobPilotException;
import parser.ParsedCommand;

/**
 * Parses the delete command.
 * Format: delete INDEX
 */
public class DeleterParser {

    public static ParsedCommand parse(String input) throws JobPilotException {
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            throw new JobPilotException("Please provide an index. Example: delete 1");
        }

        try {
            int index = Integer.parseInt(parts[1]) - 1;
            return new ParsedCommand(index);
        } catch (NumberFormatException e) {
            throw new JobPilotException("Invalid index! Use a number: delete 1");
        }
    }
}