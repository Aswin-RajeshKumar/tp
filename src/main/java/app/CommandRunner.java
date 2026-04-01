package app;

import exception.JobPilotException;
import parser.ParsedCommand;
import task.*;
import ui.Ui;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Executes parsed commands and applies them to the application list.
 */
public class CommandRunner {

    private final ArrayList<Application> applications;

    /**
     * Initializes the command runner with the application list.
     */
    public CommandRunner(ArrayList<Application> applications) {
        this.applications = applications;
    }

    /**
     * Runs a parsed command.
     *
     * @return true to continue, false to exit the program
     */
    public boolean run(ParsedCommand cmd) {
        switch (cmd.type) {

        case BYE:
            Ui.showGoodbye(applications.size());
            Ui.close();
            return false;

        case HELP:
            Ui.showHelp();
            break;

        case ADD:
            try {
                Application newApp = new Application(cmd.company, cmd.position, cmd.date);
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
                Application removed = Deleter.deleteApplication(applications, cmd.index);
                Ui.showApplicationDeleted(removed, applications.size());
            } catch (JobPilotException e) {
                Ui.showError(e.getMessage());
            }
            break;

        case EDIT:
            try {
                Editor.editApplication(cmd.index, applications,
                        cmd.newCompany, cmd.newPosition, cmd.newDate, cmd.newStatus);
            } catch (JobPilotException e) {
                Ui.showError(e.getMessage());
            }
            break;

        case FILTER:
            Filterer.filterByStatus(applications, cmd.searchTerm, null);
            break;

        case SORT:
            Collections.sort(applications);
            Ui.showSortedMessage();
            Ui.showApplicationList(applications);
            break;

        case SEARCH:
            case SEARCH:
                if (applications.isEmpty()) {
                    Ui.showError("No applications to search!");
                    break;
                }

                String rawSearchTerm = cmd.searchTerm.trim();
                if (rawSearchTerm.isEmpty()) {
                    Ui.showError("Please provide a company name to search. Example: search google");
                    break;
                }

                boolean isExactMatch = rawSearchTerm.startsWith("exact:");
                boolean isNegativeSearch = rawSearchTerm.startsWith("!");
                String[] searchKeywords;

                if (isExactMatch) {
                    searchKeywords = new String[]{rawSearchTerm.substring("exact:".length()).trim().toLowerCase()};
                } else if (isNegativeSearch) {
                    searchKeywords = new String[]{rawSearchTerm.substring(1).trim().toLowerCase()};
                } else {
                    searchKeywords = rawSearchTerm.split("\\s+");
                    for (int i = 0; i < searchKeywords.length; i++) {
                        searchKeywords[i] = searchKeywords[i].trim().toLowerCase();
                    }
                }

                ArrayList<Application> results = new ArrayList<>();
                for (Application app : applications) {
                    String companyLower = app.getCompany().toLowerCase();
                    boolean matches = true;

                    for (String keyword : searchKeywords) {
                        if (keyword.isEmpty()) continue;

                        if (isExactMatch) {
                            if (!companyLower.equals(keyword)) {
                                matches = false;
                                break;
                            }
                        } else if (isNegativeSearch) {
                            if (companyLower.contains(keyword)) {
                                matches = false;
                                break;
                            }
                        } else {
                            if (!companyLower.contains(keyword)) {
                                matches = false;
                                break;
                            }
                        }
                    }

                    if (matches) {
                        results.add(app);
                    }
                }

                Collections.sort(results);

                Ui.showSearchResults(results, rawSearchTerm);
                break;

        case STATUS:
            if (cmd.index < 0 || cmd.index >= applications.size()) {
                Ui.showError("Invalid index! Application not found.");
                break;
            }
            Application app = applications.get(cmd.index);
            app.setStatus(cmd.statusValue);
            app.setNotes(cmd.note);
            Ui.showStatusUpdated(app);
            break;

        case TAG:
            if (cmd.index < 0 || cmd.index >= applications.size()) {
                Ui.showError("Invalid index! Application not found.");
                break;
            }
            Application target = applications.get(cmd.index);
            if (cmd.isAddTag) {
                target.addIndustryTag(cmd.tag);
                Ui.showTagAdded(cmd.tag, target);
            } else {
                target.removeIndustryTag(cmd.tag);
                Ui.showTagRemoved(cmd.tag, target);
            }
            break;

        case ERROR:
            Ui.showError(cmd.errorMessage);
            break;

        default:
            Ui.showError("Unknown command. Type 'help' to see all available commands.");
        }

        return true;
    }
}
