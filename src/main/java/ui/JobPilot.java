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

        while (true) {
            String input = in.nextLine().trim();

            ParsedCommand cmd = Parser.parse(input);

            switch (cmd.type) {
                case BYE:
                    System.out.println("Bye! You added " + applications.size() + " application(s).");
                    in.close();
                    return;

                case HELP:
                    Helper.showHelpMessage();
                    break;

                case ADD:
                    try {
                        Application newApp = new Application(cmd.company, cmd.position, cmd.date);
                        applications.add(newApp);
                        System.out.println("Added: " + newApp);
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date! Please use YYYY-MM-DD (e.g., 2024-09-12)");
                    }
                    break;

                case LIST:
                    if (applications.isEmpty()) {
                        System.out.println("There is no application yet.");
                    } else {
                        System.out.println("Here are your applications:");
                        for (int i = 0; i < applications.size(); i++) {
                            System.out.println((i + 1) + ". " + applications.get(i));
                        }
                    }
                    break;

                case DELETE:
                    try {
                        String deleteCommand = "delete " + (cmd.index + 1);
                        Application removed = Deleter.deleteApplication(deleteCommand, applications);
                        System.out.println("Deleted application:");
                        System.out.println(removed);
                        System.out.println("You have " + applications.size() + " application(s) left.");
                    } catch (JobPilotException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case EDIT:
                    try {
                        Editor.editApplication(cmd.index, applications,
                                cmd.newCompany, cmd.newPosition, cmd.newDate, cmd.newStatus);
                    } catch (JobPilotException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case SORT:
                    Collections.sort(applications);
                    System.out.println("Sorted by submission date!");
                    if (applications.isEmpty()) {
                        System.out.println("There is no application yet.");
                    } else {
                        System.out.println("Here are your applications:");
                        for (int i = 0; i < applications.size(); i++) {
                            System.out.println((i + 1) + ". " + applications.get(i));
                        }
                    }
                    break;

                case SEARCH:
                    ArrayList<Application> results = new ArrayList<>();
                    for (Application app : applications) {
                        if (app.getCompany().toLowerCase().contains(cmd.searchTerm.toLowerCase())) {
                            results.add(app);
                        }
                    }
                    if (results.isEmpty()) {
                        System.out.println("No applications found for company: " + cmd.searchTerm);
                    } else {
                        System.out.println("Found " + results.size() + " application(s) matching '" + cmd.searchTerm + "':");
                        for (int i = 0; i < results.size(); i++) {
                            System.out.println((i + 1) + ". " + results.get(i));
                        }
                    }
                    break;

                case STATUS:
                    if (cmd.index < 0 || cmd.index >= applications.size()) {
                        System.out.println("Invalid index! Application not found.");
                        break;
                    }
                    Application app = applications.get(cmd.index);
                    app.setStatus(cmd.statusValue);
                    app.setNotes(cmd.note);
                    System.out.println("Updated Status: " + app);
                    break;

                case TAG:
                    if (cmd.index < 0 || cmd.index >= applications.size()) {
                        System.out.println("Invalid index! Application not found.");
                        break;
                    }
                    Application target = applications.get(cmd.index);
                    if (cmd.isAddTag) {
                        target.addIndustryTag(cmd.tag);
                        System.out.println("Added tag: " + cmd.tag + " -> " + target);
                    } else {
                        target.removeIndustryTag(cmd.tag);
                        System.out.println("Removed tag: " + cmd.tag + " -> " + target);
                    }
                    break;

                case ERROR:
                    System.out.println(cmd.errorMessage);
                    break;

                case UNKNOWN:
                default:
                    System.out.println("Unknown command. Use 'help' to see all available commands!");
                    break;
            }
        }
    }
}