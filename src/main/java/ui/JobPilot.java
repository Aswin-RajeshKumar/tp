package ui;

import exception.JobPilotException;
import task.Application;
import task.Deleter;
import task.IndustryTag;

import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Main class for the JobPilot application.
 * Handles the user interface and coordinates application logic.
 */
public class JobPilot {
    private static final Logger LOGGER = Logger.getLogger(JobPilot.class.getName());

    public static void addApplication(ArrayList<Application> applications, String input, Ui ui)
            throws JobPilotException {
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

            Application app = new Application(company, position, dateStr);
            applications.add(app);
            ui.showApplicationAdded(app);

        } catch (DateTimeParseException e) {
            ui.showError("Invalid date! Please use YYYY-MM-DD (e.g., 2024-09-12)");
        } catch (StringIndexOutOfBoundsException e) {
            throw new JobPilotException("Error parsing command!");
        }
    }

    public static void listApplications(ArrayList<Application> applications, Ui ui) {
        assert applications != null : "Application list should not be null";
        ui.showApplicationList(applications);
    }

    public static void sortApplications(ArrayList<Application> applications, Ui ui) {
        assert applications != null : "applications list cannot be null (sort operation failed)";

        if (applications.isEmpty()) {
            ui.showMessage("No applications to sort!");
            return;
        }

        try {
            Collections.sort(applications);
            ui.showSortedMessage();
            ui.showApplicationList(applications);
        } catch (NullPointerException e) {
            ui.showError("Sort failed: Some applications have invalid submission date (null)");
        } catch (ClassCastException e) {
            ui.showError("Sort failed: Applications cannot be sorted (missing Comparable implementation)");
        }
    }

    public static void updateStatus(ArrayList<Application> applications, String input, Ui ui) {
        assert applications != null : "Applications list should not be null";
        assert input != null : "Input command string should not be null";
        assert input.startsWith("status ") : "Input must start with 'status ' prefix";

        LOGGER.log(Level.INFO, "Attempting to update status with input: " + input);

        try {
            int setIndex = input.indexOf("set/");
            int noteIndex = input.indexOf("note/");

            if (setIndex == -1 || noteIndex == -1) {
                LOGGER.log(Level.WARNING, "Invalid status update format provided: " + input);
                ui.showError("Invalid format! Use: status INDEX set/STATUS note/NOTE");
                return;
            }

            String indexStr = input.substring("status ".length(), setIndex).trim();
            int listIndex = Integer.parseInt(indexStr) - 1;

            if (listIndex < 0 || listIndex >= applications.size()) {
                LOGGER.log(Level.WARNING, "Status update failed: Index " + (listIndex + 1) + " out of bounds");
                ui.showError("Invalid index! Application not found.");
                return;
            }

            String newStatus = input.substring(setIndex + 4, noteIndex).trim().toUpperCase();
            String note = input.substring(noteIndex + 5).trim();

            Application app = applications.get(listIndex);
            assert app != null : "Retrieved application at index " + listIndex + " should not be null";

            app.setStatus(newStatus);
            app.setNotes(note);

            LOGGER.log(Level.INFO, "Successfully updated status for application at index " + listIndex);
            ui.showStatusUpdated(app);

        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Failed to parse index from input: " + input, e);
            ui.showError("Error parsing status command. Ensure index is a number.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error during status update", e);
        }
    }

    public static void tagApplication(ArrayList<Application> applications, String input, Ui ui)
            throws JobPilotException {
        assert applications != null : "Applications list should not be null";
        assert input != null : "Input command string should not be null";
        assert input.startsWith("tag ") : "Input must start with 'tag ' prefix";

        LOGGER.log(Level.INFO, "Attempting to tag application with input: " + input);

        try {
            int addIndex = input.indexOf(" add/");
            int removeIndex = input.indexOf(" remove/");
            boolean isAdd = addIndex != -1;

            if (!isAdd && removeIndex == -1) {
                throw new JobPilotException("Invalid format! Use: tag INDEX add/TAG or tag INDEX remove/TAG");
            }

            int commandIndex = isAdd ? addIndex : removeIndex;
            String indexStr = input.substring("tag ".length(), commandIndex).trim();
            int listIndex = Integer.parseInt(indexStr) - 1;

            if (listIndex < 0 || listIndex >= applications.size()) {
                throw new JobPilotException("Invalid index! Application not found.");
            }

            int tagStartIndex = commandIndex + (isAdd ? 5 : 7);
            String tagStr = input.substring(tagStartIndex).trim();
            IndustryTag tag = new IndustryTag(tagStr);

            Application app = applications.get(listIndex);
            if (isAdd) {
                app.addIndustryTag(tag);
                LOGGER.log(Level.INFO, "Added tag " + tag + " to application at index " + listIndex);
                ui.showTagAdded(tag, app);
            } else {
                app.removeIndustryTag(tag);
                LOGGER.log(Level.INFO, "Removed tag " + tag + " from application at index " + listIndex);
                ui.showTagRemoved(tag, app);
            }

        } catch (NumberFormatException e) {
            throw new JobPilotException("Invalid format! Index must be a number. Use: tag INDEX add/TAG");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error during tag operation", e);
            throw new JobPilotException("Failed to update tags: " + e.getMessage());
        }
    }

    public static void searchByCompany(ArrayList<Application> applications, String input, Ui ui) {
        assert applications != null : "Applications list should not be null";
        assert input != null : "Input command string should not be null";
        assert input.startsWith("search ") : "Input must start with 'search ' prefix";

        LOGGER.log(Level.INFO, "Attempting to search with input: " + input);

        try {
            String searchTerm = input.substring("search ".length()).trim();

            if (searchTerm.isEmpty()) {
                LOGGER.log(Level.WARNING, "Empty search term provided");
                ui.showError("Please provide a company name to search. Example: search google");
                return;
            }
            if (applications.isEmpty()) {
                ui.showMessage("No applications to search!");
                return;
            }

            ArrayList<Application> results = new ArrayList<>();
            for (Application application : applications) {
                assert application != null : "Application in list should not be null";
                String company = application.getCompany();
                assert company != null : "Company name should not be null";
                if (company.toLowerCase().contains(searchTerm.toLowerCase())) {
                    results.add(application);
                }
            }

            LOGGER.log(Level.INFO, "Search found " + results.size() + " result(s) for term: " + searchTerm);
            ui.showSearchResults(results, searchTerm);

        } catch (StringIndexOutOfBoundsException e) {
            LOGGER.log(Level.SEVERE, "Error parsing search command: " + input, e);
            ui.showError("Invalid search format! Use: search COMPANY_NAME");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error during search", e);
            ui.showError("An error occurred while searching.");
        }
    }

    public static void main(String[] args) {
        LOGGER.setLevel(Level.OFF);

        Ui ui = new Ui();
        ui.showWelcome();

        ArrayList<Application> applications = new ArrayList<>();

        while (true) {
            String input = ui.readCommand();

            if (input.equals("bye")) {
                ui.showGoodbye(applications.size());
                break;
            } else if (input.equals("help")) {
                ui.showHelp();
            } else if (input.startsWith("add")) {
                try {
                    addApplication(applications, input, ui);
                } catch (JobPilotException e) {
                    ui.showError(e.getMessage());
                }
            } else if (input.equals("list")) {
                listApplications(applications, ui);
            } else if (input.startsWith("search")) {
                searchByCompany(applications, input, ui);
            } else if (input.equals("sort")) {
                sortApplications(applications, ui);
            } else if (input.startsWith("status ")) {
                updateStatus(applications, input, ui);
            } else if (input.startsWith("tag ")) {
                try {
                    tagApplication(applications, input, ui);
                } catch (JobPilotException e) {
                    ui.showError(e.getMessage());
                }
            } else if (input.startsWith("delete")) {
                try {
                    deleteApplication(input, applications, ui);
                } catch (JobPilotException e) {
                    ui.showError(e.getMessage());
                }
            } else {
                ui.showError("Unknown command. Use 'help' to see all available commands!");
            }
        }
        ui.close();
    }

    private static void deleteApplication(String input, ArrayList<Application> applications, Ui ui)
            throws JobPilotException {
        try {
            Application removed = Deleter.deleteApplication(input, applications);
            ui.showApplicationDeleted(removed, applications.size());
        } catch (NumberFormatException e) {
            throw new JobPilotException("Invalid format! Use: delete INDEX");
        }
    }
}
