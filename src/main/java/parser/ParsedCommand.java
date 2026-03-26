package parser;

import task.IndustryTag;

/**
 * Represents a parsed command with its type and associated data.
 */
public class ParsedCommand {
    public final CommandType type;
    public final String errorMessage;

    // Add command fields
    public final String company;
    public final String position;
    public final String date;

    // Delete/Edit/Status/Tag command fields
    public final int index;

    // Edit command fields (all optional)
    public final String newCompany;
    public final String newPosition;
    public final String newDate;
    public final String newStatus;

    // Search command field
    public final String searchTerm;

    // Status command fields
    public final String statusValue;
    public final String note;

    // Tag command fields
    public final IndustryTag tag;
    public final boolean isAddTag;

    // ========== CONSTRUCTORS ==========

    /**
     * General Constructor for commands requiring a Type and a single String argument.
     * This resolves the conflict between FILTER and ERROR constructors.
     */
    public ParsedCommand(CommandType type, String stringValue) {
        this.type = type;
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

        // Route the string value based on the CommandType
        if (type == CommandType.FILTER || type == CommandType.SEARCH) {
            this.searchTerm = stringValue;
            this.errorMessage = null;
        } else if (type == CommandType.ERROR) {
            this.errorMessage = stringValue;
            this.searchTerm = null;
        } else {
            this.searchTerm = stringValue;
            this.errorMessage = null;
        }
    }

    // Constructor for ADD
    public ParsedCommand(String company, String position, String date) {
        this.type = CommandType.ADD;
        this.company = company;
        this.position = position;
        this.date = date;
        this.errorMessage = null;
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

    // Constructor for DELETE
    public ParsedCommand(int index) {
        this.type = CommandType.DELETE;
        this.index = index;
        this.errorMessage = null;
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

    // Constructor for EDIT
    public ParsedCommand(int index, String company, String position, String date, String status) {
        this.type = CommandType.EDIT;
        this.index = index;
        this.newCompany = company;
        this.newPosition = position;
        this.newDate = date;
        this.newStatus = status;
        this.errorMessage = null;
        this.company = null;
        this.position = null;
        this.date = null;
        this.searchTerm = null;
        this.statusValue = null;
        this.note = null;
        this.tag = null;
        this.isAddTag = false;
    }

    // Constructor for SEARCH (Legacy support - can also use the general constructor)
    public ParsedCommand(String searchTerm) {
        this.type = CommandType.SEARCH;
        this.searchTerm = searchTerm;
        this.errorMessage = null;
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

    // Constructor for STATUS
    public ParsedCommand(int index, String status, String note) {
        this.type = CommandType.STATUS;
        this.index = index;
        this.statusValue = status;
        this.note = note;
        this.errorMessage = null;
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

    // Constructor for TAG
    public ParsedCommand(int index, IndustryTag tag, boolean isAdd) {
        this.type = CommandType.TAG;
        this.index = index;
        this.tag = tag;
        this.isAddTag = isAdd;
        this.errorMessage = null;
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

    // Constructor for simple commands (BYE, LIST, SORT, HELP, UNKNOWN)
    public ParsedCommand(CommandType type) {
        this.type = type;
        this.errorMessage = null;
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