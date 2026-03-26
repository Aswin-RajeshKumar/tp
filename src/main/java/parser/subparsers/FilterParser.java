package parser.subparsers;

import exception.JobPilotException;
import parser.CommandType;
import parser.ParsedCommand;

public class FilterParser {
    public static ParsedCommand parse(String input) throws JobPilotException {
        // Extract arguments after the word "filter"
        String arguments = input.substring("filter".length()).trim();

        if (arguments.isEmpty()) {
            throw new JobPilotException("Filter command is missing arguments! Use: filter status/STATUS");
        }

        if (!arguments.contains("status/")) {
            throw new JobPilotException("Invalid filter format! Expected: filter status/STATUS");
        }

        String statusQuery = arguments.replace("status/", "").trim();

        if (statusQuery.isEmpty()) {
            throw new JobPilotException("Status value cannot be empty!");
        }

        // Uses the NEW constructor we just added above
        return new ParsedCommand(CommandType.FILTER, statusQuery);
    }
}