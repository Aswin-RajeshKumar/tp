package app;

import exception.JobPilotException;
import parser.ParsedCommand;
import parser.CommandType;
import task.Application;
import task.Deleter;
import task.Editor;
import task.Filterer;
import task.IndustryTag;
import ui.Ui;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Executes parsed commands and manages application flow.
 */
public class CommandRunner {

    private final ArrayList<Application> applications;

    public CommandRunner(ArrayList<Application> applications) {
        this.applications = applications;
    }

    public boolean run(ParsedCommand cmd) {
        if (cmd == null) {
            return true;
        }

        switch (cmd.getType()) {

        case BYE:
            Ui.showGoodbye(applications.size());
            Ui.close();
            return false;

        case HELP:
            Ui.showHelp();
            break;

        case ADD:
            try {
                Application newApp = new Application(cmd.getCompany(), cmd.getPosition(), cmd.getDate());
                applications.add(newApp);
                Ui.showApplicationAdded(newApp);
            } catch (DateTimeParseException e) {
                Ui.showError("Invalid date! Please use YYYY-MM-DD");
            }
            break;

        case LIST:
            Ui.showApplicationList(applications);
            break;

        case DELETE:
            try {
                Application removed = Deleter.deleteApplication(applications, cmd.getIndex());
                Ui.showApplicationDeleted(removed, applications.size());
            } catch (JobPilotException e) {
                Ui.showError(e.getMessage());
            }
            break;

        case EDIT:
            try {
                Editor.editApplication(cmd.getIndex(), applications,
                        cmd.getNewCompany(), cmd.getNewPosition(), cmd.getNewDate(), cmd.getNewStatus());
            } catch (JobPilotException e) {
                Ui.showError(e.getMessage());
            }
            break;

        case FILTER:
            try {
                // Combined your logic with the team's Filterer signature
                Filterer.filterByStatus(applications, cmd.getSearchTerm());
            } catch (JobPilotException e) {
                Ui.showError(e.getMessage());
            }
            break;

        case SORT:
            Collections.sort(applications);
            Ui.showSortedMessage();
            Ui.showApplicationList(applications);
            break;

        case SEARCH:
            handleSearch(cmd.getSearchType(), cmd.getSearchTerm());
            break;

        case STATUS:
            handleStatusUpdate(cmd);
            break;

        case TAG:
            handleTagUpdate(cmd);
            break;

        case ERROR:
            Ui.showError(cmd.getErrorMessage());
            break;

        default:
            Ui.showError("Unknown command. Type 'help' to see all available commands.");
        }

        return true;
    }

    /**
     * Handles Status and Notes updates with defensive null checks.
     */
    private void handleStatusUpdate(ParsedCommand cmd) {
        int idx = cmd.getIndex();
        if (idx < 0 || idx >= applications.size()) {
            Ui.showError("Invalid index! Application not found.");
            return;
        }

        Application app = applications.get(idx);

        if (cmd.getStatusValue() != null) {
            app.setStatus(cmd.getStatusValue());
        }
        if (cmd.getNote() != null) {
            app.setNotes(cmd.getNote());
        }

        Ui.showStatusUpdated(app);
    }

    /**
     * Handles searching with a safety check for empty lists.
     */
    private void handleSearch(String type, String query) {
        if (applications.isEmpty()) {
            Ui.showError("No applications to search!");
            return;
        }

        ArrayList<Application> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (Application app : applications) {
            boolean isMatch = false;
            if (type == null || type.equals("c")) {
                isMatch = app.getCompany().toLowerCase().contains(lowerQuery);
            } else if (type.equals("p")) {
                isMatch = app.getPosition().toLowerCase().contains(lowerQuery);
            } else if (type.equals("s")) {
                isMatch = app.getStatus().toLowerCase().contains(lowerQuery);
            }

            if (isMatch) {
                results.add(app);
            }
        }
        Ui.showSearchResults(results, query);
    }

    /**
     * Handles Tag additions and removals.
     */
    private void handleTagUpdate(ParsedCommand cmd) {
        int idx = cmd.getIndex();
        if (idx < 0 || idx >= applications.size()) {
            Ui.showError("Invalid index! Application not found.");
            return;
        }

        Application target = applications.get(idx);
        if (cmd.isAddTag()) {
            target.addIndustryTag(cmd.getTag());
            Ui.showTagAdded(cmd.getTag(), target);
        } else {
            target.removeIndustryTag(cmd.getTag());
            Ui.showTagRemoved(cmd.getTag(), target);
        }
    }
}