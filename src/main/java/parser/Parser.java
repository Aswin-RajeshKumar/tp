package parser;

import exception.JobPilotException;
import parser.subparsers.*;

/**
 * Main parser that routes commands to appropriate subparsers.
 */
public class Parser {

    public static ParsedCommand parse(String input) {
        String trimmed = input.trim();

        if (trimmed.equals("bye")) {
            return new ParsedCommand(CommandType.BYE);
        } else if (trimmed.equals("list")) {
            return new ParsedCommand(CommandType.LIST);
        } else if (trimmed.equals("sort")) {
            return new ParsedCommand(CommandType.SORT);
        } else if (trimmed.equals("help")) {
            return new ParsedCommand(CommandType.HELP);
        } else if (trimmed.startsWith("add")) {
            try {
                return ApplicationParser.parse(trimmed);
            } catch (JobPilotException e) {
                return new ParsedCommand(CommandType.ERROR, e.getMessage());
            }
        } else if (trimmed.startsWith("delete")) {
            try {
                return DeleterParser.parse(trimmed);
            } catch (JobPilotException e) {
                return new ParsedCommand(CommandType.ERROR, e.getMessage());
            }
        } else if (trimmed.startsWith("edit")) {
            try {
                return EditorParser.parse(trimmed);
            } catch (JobPilotException e) {
                return new ParsedCommand(CommandType.ERROR, e.getMessage());
            }
        } else if (trimmed.startsWith("search")) {
            try {
                return SearcherParser.parse(trimmed);
            } catch (JobPilotException e) {
                return new ParsedCommand(CommandType.ERROR, e.getMessage());
            }
        } else if (trimmed.startsWith("status")) {
            try {
                return StatusParser.parse(trimmed);
            } catch (JobPilotException e) {
                return new ParsedCommand(CommandType.ERROR, e.getMessage());
            }
        } else if (trimmed.startsWith("filter")) {
            try {
                return FilterParser.parse(trimmed);
            } catch (JobPilotException e) {
                return new ParsedCommand(CommandType.ERROR, e.getMessage());
            }
        } else if (trimmed.startsWith("tag")) {
            try {
                return TaggerParser.parse(trimmed);
            } catch (JobPilotException e) {
                return new ParsedCommand(CommandType.ERROR, e.getMessage());
            }
        } else {
            return new ParsedCommand(CommandType.UNKNOWN);
        }
    }
}