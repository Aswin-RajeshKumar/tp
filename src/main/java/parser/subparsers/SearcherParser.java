package parser.subparsers;

import exception.JobPilotException;
import parser.ParsedCommand;

/**
 * Parses the search command.
 * Format: search [COMPANY_NAME]
 * Supports: exact match, negative search, multi-keyword search
 */
public class SearcherParser {

    public static ParsedCommand parse(String input) throws JobPilotException {
        if (input == null || input.trim().isEmpty()) {
            throw new JobPilotException("Please provide a company name to search. Example: search google");
        }

        String searchTerm = input.substring("search".length()).trim();

        if (searchTerm.isEmpty()) {
            throw new JobPilotException("Please provide a company name to search. Example: search google");
        }

        if (searchTerm.length() < 2) {
            throw new JobPilotException("Search term is too short! Use at least 2 characters.");
        }
        if (searchTerm.length() > 50) {
            throw new JobPilotException("Search term is too long! Maximum 50 characters allowed.");
        }

        if (containsInvalidCharacters(searchTerm)) {
            throw new JobPilotException("Search term contains invalid characters! Use letters, numbers, and spaces only.");
        }
        return new ParsedCommand(searchTerm);
    }

    private static boolean containsInvalidCharacters(String term) {
        for (int i = 0; i < term.length(); i++) {
            char c = term.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != ' ' && c != ':' && c != '!') {
                return true;
            }
        }
        return false;
    }
}