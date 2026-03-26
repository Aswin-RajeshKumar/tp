package ui;

import exception.JobPilotException;
import parser.Parser;
import parser.ParsedCommand;
import task.*;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class for the JobPilot application.
 * Handles the user interface and coordinates application logic.
 */
public class JobPilot {
    private static final Logger LOGGER = Logger.getLogger(JobPilot.class.getName());

    public static void main(String[] args) {
        // V2.0 Requirement: Assertions & Logging control
        LOGGER.setLevel(Level.OFF);

        String logo = """
                 _   ___   ____   ____   ___  _       ___   _____
                | | / _ \\ | __ ) |  _ \\ |_ _|| |     / _ \\ |_   _|
             _  | || | | ||  _ \\ | |_) | | | | |    | | | |  | |
            | |_| || |_| || |_) ||  __/  | | | |___ | |_| |  | |
             \\___/  \\___/ |____/ |_|    |___||_____| \\___/   |_|
            """;

        System.out.println("Hello from\n" + logo);
        System.out.println("Welcome to JobPilot!");
        System.out.println("Type 'help' to see all available commands!");

        Scanner in = new Scanner(System.in);
        ArrayList<Application> applications = new ArrayList<>();

        // Initialize the UI class to replace System.out.println loops
        Ui ui = new Ui();

        while (true) {
            String input = in.nextLine().trim();
            ParsedCommand cmd = Parser.parse(input);

            switch (cmd.type) {
            case BYE:
                ui.showMessage("Bye! You added " + applications.size() + " application(s).");
                in.close();
                return;

            case HELP:
                Helper.showHelpMessage();
                break;

            case ADD:
                try {
                    Application newApp = new Application(cmd.company, cmd.position, cmd.date);
                    applications.add(newApp);
                    ui.showMessage("Added: " + newApp);
                } catch (DateTimeParseException e) {
                    ui.showError("Invalid date! Please use YYYY-MM-DD (e.g., 2024-09-12)");
                }
                break;

            case LIST:
                // Using the new UI method for consistency
                ui.showApplicationList(applications);
                break;

            case DELETE:
                try {
                    // Updated to use the index directly if your Deleter supports it
                    Application removed = Deleter.deleteApplication("delete " + (cmd.index + 1), applications);
                    ui.showMessage("Deleted application:\n" + removed);
                    ui.showMessage("You have " + applications.size() + " application(s) left.");
                } catch (JobPilotException e) {
                    ui.showError(e.getMessage());
                }
                break;

            case EDIT:
                try {
                    Editor.editApplication(cmd.index, applications,
                            cmd.newCompany, cmd.newPosition, cmd.newDate, cmd.newStatus);
                    ui.showMessage("Application edited successfully!");
                } catch (JobPilotException e) {
                    ui.showError(e.getMessage());
                }
                break;

            case SORT:
                Collections.sort(applications);
                ui.showMessage("Sorted by submission date!");
                ui.showApplicationList(applications);
                break;

            case FILTER: // <--- INTEGRATED FILTER LOGIC
                // Ensure cmd.searchTerm contains the value from your FilterParser
                Filterer.filterByStatus(applications, cmd.searchTerm, ui);
                break;

            case SEARCH:
                // Keeping search by company logic, but using UI for output
                ArrayList<Application> searchResults = new ArrayList<>();
                for (Application app : applications) {
                    if (app.getCompany().toLowerCase().contains(cmd.searchTerm.toLowerCase())) {
                        searchResults.add(app);
                    }
                }
                if (searchResults.isEmpty()) {
                    ui.showError("No applications found for company: " + cmd.searchTerm);
                } else {
                    ui.showMessage("Found " + searchResults.size() + " matches:");
                    ui.showApplicationList(searchResults);
                }
                break;

            case STATUS:
                try {
                    if (cmd.index < 0 || cmd.index >= applications.size()) {
                        throw new JobPilotException("Invalid index! Application not found.");
                    }
                    Application appToUpdate = applications.get(cmd.index);
                    appToUpdate.setStatus(cmd.statusValue);
                    appToUpdate.setNotes(cmd.note);
                    ui.showMessage("Updated Status: " + appToUpdate);
                } catch (JobPilotException e) {
                    ui.showError(e.getMessage());
                }
                break;

            case TAG:
                try {
                    if (cmd.index < 0 || cmd.index >= applications.size()) {
                        throw new JobPilotException("Invalid index! Application not found.");
                    }
                    Application target = applications.get(cmd.index);
                    if (cmd.isAddTag) {
                        target.addIndustryTag(cmd.tag);
                        ui.showMessage("Added tag: " + cmd.tag);
                    } else {
                        target.removeIndustryTag(cmd.tag);
                        ui.showMessage("Removed tag: " + cmd.tag);
                    }
                } catch (JobPilotException e) {
                    ui.showError(e.getMessage());
                }
                break;

            case ERROR:
                ui.showError(cmd.errorMessage);
                break;

            case UNKNOWN:
            default:
                ui.showError("Unknown command. Use 'help' to see all available commands!");
                break;
            }
        }
    }
}