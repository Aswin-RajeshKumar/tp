package parser;

import exception.JobPilotException;
import parser.subparsers.*;

/**
 * Main parser that routes commands to appropriate subparsers.
 */
public class Parser {

    public static ParsedCommand parse(String input) throws JobPilotException {
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
            return ApplicationParser.parse(trimmed);
        } else if (trimmed.startsWith("delete")) {
            return DeleterParser.parse(trimmed);
        } else if (trimmed.startsWith("edit")) {
            return EditorParser.parse(trimmed);
        } else if (trimmed.startsWith("search")) {
            return SearcherParser.parse(trimmed);
        } else if (trimmed.startsWith("status")) {
            return StatusParser.parse(trimmed);
        } else if (trimmed.startsWith("tag")) {
            return TaggerParser.parse(trimmed);
        } else {
            return new ParsedCommand(CommandType.UNKNOWN);
        }
    }
}