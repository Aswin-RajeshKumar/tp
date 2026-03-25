package parser;

import task.IndustryTag;

/**
 * Represents a parsed command with its type and associated data.
 */
public class ParsedCommand {
    public final CommandType type;

    public final String company;
    public final String position;
    public final String date;

    public final int index;

    public final String newCompany;
    public final String newPosition;
    public final String newDate;
    public final String newStatus;

    public final String searchTerm;

    public final String statusValue;
    public final String note;

    public final IndustryTag tag;
    public final boolean isAddTag;

    public ParsedCommand(String company, String position, String date) {
        this.type = CommandType.ADD;
        this.company = company;
        this.position = position;
        this.date = date;
        this.index = -1;
        this.newCompany = null;
        this.newPosition = null;
        this.newDate = null;
        this.newStatus = null;
        this.searchTerm = null;
        this.statusValue = null;
        this.note = null;
        this.tag = null;
        this.isAddTag = false;
    }

    public ParsedCommand(int index) {
        this.type = CommandType.DELETE;
        this.index = index;
        this.company = null;
        this.position = null;
        this.date = null;
        this.newCompany = null;
        this.newPosition = null;
        this.newDate = null;
        this.newStatus = null;
        this.searchTerm = null;
        this.statusValue = null;
        this.note = null;
        this.tag = null;
        this.isAddTag = false;
    }

    public ParsedCommand(int index, String company, String position, String date, String status) {
        this.type = CommandType.EDIT;
        this.index = index;
        this.newCompany = company;
        this.newPosition = position;
        this.newDate = date;
        this.newStatus = status;
        this.company = null;
        this.position = null;
        this.date = null;
        this.searchTerm = null;
        this.statusValue = null;
        this.note = null;
        this.tag = null;
        this.isAddTag = false;
    }

    public ParsedCommand(String searchTerm) {
        this.type = CommandType.SEARCH;
        this.searchTerm = searchTerm;
        this.index = -1;
        this.company = null;
        this.position = null;
        this.date = null;
        this.newCompany = null;
        this.newPosition = null;
        this.newDate = null;
        this.newStatus = null;
        this.statusValue = null;
        this.note = null;
        this.tag = null;
        this.isAddTag = false;
    }

    public ParsedCommand(int index, String status, String note) {
        this.type = CommandType.STATUS;
        this.index = index;
        this.statusValue = status;
        this.note = note;
        this.company = null;
        this.position = null;
        this.date = null;
        this.newCompany = null;
        this.newPosition = null;
        this.newDate = null;
        this.newStatus = null;
        this.searchTerm = null;
        this.tag = null;
        this.isAddTag = false;
    }

    public ParsedCommand(int index, IndustryTag tag, boolean isAdd) {
        this.type = CommandType.TAG;
        this.index = index;
        this.tag = tag;
        this.isAddTag = isAdd;
        this.company = null;
        this.position = null;
        this.date = null;
        this.newCompany = null;
        this.newPosition = null;
        this.newDate = null;
        this.newStatus = null;
        this.searchTerm = null;
        this.statusValue = null;
        this.note = null;
    }

    public ParsedCommand(CommandType type) {
        this.type = type;
        this.index = -1;
        this.company = null;
        this.position = null;
        this.date = null;
        this.newCompany = null;
        this.newPosition = null;
        this.newDate = null;
        this.newStatus = null;
        this.searchTerm = null;
        this.statusValue = null;
        this.note = null;
        this.tag = null;
        this.isAddTag = false;
    }
}