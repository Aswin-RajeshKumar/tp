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
import java.util.Comparator;

/**
 * Executes parsed commands and manages application flow.
 * Synchronized with v2.0 advanced search and status logic.
 */
public class CommandRunner {

    private final ArrayList<Application> applications;

    public CommandRunner(ArrayList<Application> applications) {
        this.applications = applications;
    }

    /**
     * Main execution loop for commands.
     * @param cmd The parsed command to execute.
     * @return false if the command is BYE, true otherwise.
     */
    public boolean run(ParsedCommand cmd) {
        if (cmd == null) {
            return true;
        }

        switch (cmd.getType()) {

        case BYE:
            try {
                Ui.showGoodbye(applications.size());
                Ui.close();
            } catch (IllegalStateException e) {
                Ui.showError("Scanner was already closed.");
            }
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
                Ui.showApplicationEdited(applications.get(cmd.getIndex()));
            } catch (JobPilotException e) {
                Ui.showError(e.getMessage());
            }
            break;

        case FILTER:
            try {
                Filterer.filterByStatus(applications, cmd.getSearchTerm());
            } catch (JobPilotException e) {
                Ui.showError(e.getMessage());
            }
            break;

        case SORT:
            handleSort(cmd.getSearchTerm());
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
     * Handles advanced sorting by date, company, or status with optional reverse flag.
     */
    private void handleSort(String rawSortTerm) {
        if (applications.isEmpty()) {
            Ui.showError("There is no application yet.");
            return;
        }

        String sortType = rawSortTerm != null ? rawSortTerm.trim().toLowerCase() : "";
        boolean reverse = sortType.contains("reverse");

        Comparator<Application> comparator;
        if (sortType.startsWith("company")) {
            comparator = Comparator.comparing(a -> a.getCompany().toLowerCase());
        } else if (sortType.startsWith("status")) {
            comparator = Comparator.comparing(a -> a.getStatus().toLowerCase());
        } else {
            // Default to sorting by Date
            comparator = Comparator.naturalOrder();
        }

        if (reverse) {
            applications.sort(Collections.reverseOrder(comparator));
        } else {
            applications.sort(comparator);
        }

        Ui.showSortedMessage();
        Ui.showApplicationList(applications);
    }

    /**
     * Handles Status and Notes updates with defensive index checks.
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
     * Synchronized with team prefix requirements (c/, p/, s/).
     */
    private void handleSearch(String type, String query) {
        if (applications.isEmpty()) {
            Ui.showError("Application list is empty!");
            return;
        }

        ArrayList<Application> results = new ArrayList<>();
        String lowerQuery = (query != null) ? query.toLowerCase() : "";

        for (Application app : applications) {
            boolean isMatch = false;
            String searchType = (type != null) ? type : "c";

            switch (searchType) {
            case "c":
                isMatch = app.getCompany().toLowerCase().contains(lowerQuery);
                break;
            case "p":
                isMatch = app.getPosition().toLowerCase().contains(lowerQuery);
                break;
            case "s":
                isMatch = app.getStatus().toLowerCase().contains(lowerQuery);
                break;
            default:
                isMatch = app.getCompany().toLowerCase().contains(lowerQuery);
            }

            if (isMatch) {
                results.add(app);
            }
        }

        Collections.sort(results);
        // Formatting search result output string to match EXPECTED.TXT requirements
        String searchLabel = (type != null ? type : "c") + "/" + query;
        Ui.showSearchResults(results, searchLabel);
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
        try {
            if (cmd.isAddTag()) {
                target.addIndustryTag(cmd.getTag());
                Ui.showTagAdded(cmd.getTag(), target);
            } else {
                target.removeIndustryTag(cmd.getTag());
                Ui.showTagRemoved(cmd.getTag(), target);
            }
        } catch (Exception e) {
            Ui.showError(e.getMessage());
        }
    }
}