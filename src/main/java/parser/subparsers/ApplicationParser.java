package parser.subparsers;

import exception.JobPilotException;
import parser.ParsedCommand;

/**
 * Parses the add command.
 * Format: add c/COMPANY p/POSITION d/DATE
 */
public class ApplicationParser {

    public static ParsedCommand parse(String input) throws JobPilotException {
        try {
            int cIndex = input.indexOf("c/");
            int pIndex = input.indexOf("p/");
            int dIndex = input.indexOf("d/");

            if (cIndex == -1 || pIndex == -1 || dIndex == -1) {
                throw new JobPilotException("Missing required fields! Use: add c/COMPANY p/POSITION d/DATE");
            }

            if (cIndex > pIndex || pIndex > dIndex) {
                throw new JobPilotException("Wrong order! Use: c/COMPANY then p/POSITION then d/DATE");
            }

            String company = input.substring(cIndex + 2, pIndex).trim();
            String position = input.substring(pIndex + 2, dIndex).trim();
            String dateStr = input.substring(dIndex + 2).trim();

            if (company.isEmpty() || position.isEmpty() || dateStr.isEmpty()) {
                throw new JobPilotException("Fields cannot be empty!");
            }

            return new ParsedCommand(company, position, dateStr);

        } catch (StringIndexOutOfBoundsException e) {
            throw new JobPilotException("Error parsing add command!");
        }
    }
}