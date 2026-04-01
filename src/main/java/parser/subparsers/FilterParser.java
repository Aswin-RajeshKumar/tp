package parser.subparsers;

import exception.JobPilotException;
import parser.CommandType;
import parser.ParsedCommand;

// @@author Aswin-RajeshKumar
/**
 * Parses the filter command to extract status search queries.
 * Logic originally implemented for v2.0 milestone.
 */
public class FilterParser {
    public static ParsedCommand parse(String input) throws JobPilotException {
        // Extract arguments after the word "filter"
        String arguments = input.substring("filter".length()).trim();

        if (arguments.isEmpty()) {
            throw new JobPilotException("Filter command is missing arguments! Use: filter status/STATUS");
        }

        if (!arguments.contains("s/")) {
            throw new JobPilotException("Invalid filter format! Expected: filter status/STATUS");
        }

        String statusQuery = arguments.replace("s/", "").trim();

        if (statusQuery.isEmpty()) {
            throw new JobPilotException("Status value cannot be empty!");
        }

        // Uses the NEW constructor we just added above
        return new ParsedCommand(CommandType.FILTER, statusQuery);
    }
}
// @@author
