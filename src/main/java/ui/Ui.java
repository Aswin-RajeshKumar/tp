package ui;

import task.Application;
import task.IndustryTag;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles all user interactions, including reading input from the console
 * and displaying formatted messages, lists, and errors to the user.
 */
public class Ui {
    private final Scanner scanner;

    /**
     * Initializes the Ui and sets up the Scanner to read from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads the next line of input from the user.
     *
     * @return The trimmed input string.
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Displays the application logo and welcome message.
     */
    public void showWelcome() {
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
    }

    /**
     * Displays the exit message and the total number of applications tracked.
     *
     * @param applicationCount The final size of the applications list.
     */
    public void showGoodbye(int applicationCount) {
        System.out.println("Bye! You added " + applicationCount + " application(s).");
    }

    /**
     * Displays a standard message to the user.
     *
     * @param message The message to print.
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays an error message to the user.
     *
     * @param error The error message to print.
     */
    public void showError(String error) {
        System.out.println(error);
    }

    /**
     * Confirms that an application was successfully added.
     *
     * @param app The application that was added.
     */
    public void showApplicationAdded(Application app) {
        System.out.println("Added: " + app);
    }

    /**
     * Confirms that an application was successfully deleted and shows the remaining count.
     *
     * @param app           The application that was removed.
     * @param remainingSize The number of applications left in the list.
     */
    public void showApplicationDeleted(Application app, int remainingSize) {
        System.out.println("Deleted application:");
        System.out.println(app);
        System.out.println("You have " + remainingSize + " application(s) left.");
    }

    /**
     * Displays the full list of job applications.
     *
     * @param applications The list of applications to display.
     */
    public void showApplicationList(ArrayList<Application> applications) {
        if (applications.isEmpty()) {
            System.out.println("There is no application yet.");
            return;
        }

        System.out.println("Here are your applications:");
        for (int i = 0; i < applications.size(); i++) {
            Application application = applications.get(i);
            if (application == null) {
                System.out.println((i + 1) + ". [Invalid application data]");
            } else {
                System.out.println((i + 1) + ". " + application);
            }
        }
    }

    /**
     * Displays search results matching a specific company name.
     *
     * @param results    The list of matching applications.
     * @param searchTerm The keyword used for the search.
     */
    public void showSearchResults(ArrayList<Application> results, String searchTerm) {
        if (results.isEmpty()) {
            System.out.println("No applications found for company: " + searchTerm);
        } else {
            System.out.println("Found " + results.size() + " application(s) matching '" + searchTerm + "':");
            for (int i = 0; i < results.size(); i++) {
                System.out.println((i + 1) + ". " + results.get(i));
            }
        }
    }

    /**
     * Confirms that the list was successfully sorted.
     */
    public void showSortedMessage() {
        System.out.println("Sorted by submission date!");
    }

    /**
     * Confirms that an application's status was successfully updated.
     *
     * @param app The updated application.
     */
    public void showStatusUpdated(Application app) {
        System.out.println("Updated Status: " + app);
    }

    /**
     * Confirms that an industry tag was successfully added.
     *
     * @param tag The tag that was added.
     * @param app The application it was added to.
     */
    public void showTagAdded(IndustryTag tag, Application app) {
        System.out.println("Added tag: " + tag + " ->" + app);
    }

    /**
     * Confirms that an industry tag was successfully removed.
     *
     * @param tag The tag that was removed.
     * @param app The application it was removed from.
     */
    public void showTagRemoved(IndustryTag tag, Application app) {
        System.out.println("Removed tag: " + tag + " ->" + app);
    }

    /**
     * Displays the comprehensive help menu containing all valid commands.
     */
    public void showHelp() {
        String helpMessage = """
                Available Commands:
                add c/COMPANY p/POSITION d/DATE    Add a new job application
                list                               List all job applications
                delete INDEX                       Delete an application
                sort                               Sort applications by date
                search COMPANY_NAME                Search applications by company name
                status INDEX set/STATUS note/NOTE  Update application status and add a note
                tag INDEX add/TAG                  Add a tag to an application
                tag INDEX remove/TAG               Remove a tag from an application
                help                               Show this message
                bye                                Exit the application
                """;
        System.out.println(helpMessage);
    }

    /**
     * Closes the underlying Scanner resource.
     */
    public void close() {
        scanner.close();
    }
}
