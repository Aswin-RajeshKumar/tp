package parser.subparsers;

import exception.JobPilotException;
import parser.ParsedCommand;

/**
 * Parses the status command.
 * Format: status INDEX set/STATUS note/NOTE
 */
public class StatusParser {

    public static ParsedCommand parse(String input) throws JobPilotException {
        int setIndex = input.indexOf("set/");
        int noteIndex = input.indexOf("note/");

        if (setIndex == -1 || noteIndex == -1) {
            throw new JobPilotException("Invalid format! Use: status INDEX set/STATUS note/NOTE");
        }

        String indexStr = input.substring("status".length(), setIndex).trim();
        int listIndex;
        try {
            listIndex = Integer.parseInt(indexStr) - 1;
        } catch (NumberFormatException e) {
            throw new JobPilotException("Invalid index! Use a number.");
        }

        String newStatus = input.substring(setIndex + 4, noteIndex).trim().toUpperCase();
        String note = input.substring(noteIndex + 5).trim();

        return new ParsedCommand(listIndex, newStatus, note);
    }
}
