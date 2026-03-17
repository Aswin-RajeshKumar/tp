package jobpilot;

import exception.JobPilotException;
import task.Add;
import task.Delete;

import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Main class for the JobPilot application.
 * Handles the user interface and coordinates application logic.
 */
public class JobPilot {
    private static final Logger LOGGER = Logger.getLogger(JobPilot.class.getName());

    /**
     * Adds a new job application to the list.
     *
     * @param applications The list of job applications.
     * @param input        The raw user command string.
     * @throws JobPilotException If there's an error in the command format.
     */
    public static void addApplication(ArrayList<Add> applications, String input) throws JobPilotException {
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

            Add app = new Add(company, position, dateStr);
            applications.add(app);
            System.out.println("Added: " + app);

        } catch (DateTimeParseException e) {
            System.out.println("Invalid date! Please use YYYY-MM-DD (e.g., 2024-09-12)");
        } catch (StringIndexOutOfBoundsException e) {
            throw new JobPilotException("Error parsing command!");
        }
    }

    /**
     * Lists all current job applications.
     *
     * @param applications The list of job applications to display.
     */
    public static void listApplications(ArrayList<Add> applications) {
        assert applications != null : "Application list should not be null";

        if (applications.isEmpty()) {
            System.out.println("There is no application yet.");
            return;
        }

        System.out.println("Here are your applications:");
        int index = 0;
        for (Add app : applications) {
            if (app == null) {
                System.out.println((index + 1) + ". [Invalid application data]");
            } else {
                System.out.println((index + 1) + ". " + app);
            }
            index++;
        }
    }

    /**
     * Sorts applications by submission date.
     *
     * @param applications The list of job applications to sort.
     */
    public static void sortApplications(ArrayList<Add> applications) {
        if (applications.isEmpty()) {
            System.out.println("No applications to sort!");
            return;
        }
        Collections.sort(applications);
        System.out.println("Sorted by submission date!");
        listApplications(applications);
    }

    /**
     * Updates the status and note of an existing application.
     *
     * @param applications The list containing job applications.
     * @param input        The raw user command string.
     */
    public static void updateStatus(ArrayList<Add> applications, String input) {
        assert applications != null : "Applications list should not be null";
        assert input != null : "Input command string should not be null";
        assert input.startsWith("status ") : "Input must start with 'status ' prefix";

        LOGGER.log(Level.INFO, "Attempting to update status with input: " + input);

        try {
            int setIndex = input.indexOf("set/");
            int noteIndex = input.indexOf("note/");

            if (setIndex == -1 || noteIndex == -1) {
                LOGGER.log(Level.WARNING, "Invalid status update format provided: " + input);
                System.out.println("Invalid format! Use: status INDEX set/STATUS note/NOTE");
                return;
            }

            String indexStr = input.substring("status ".length(), setIndex).trim();
            int listIndex = Integer.parseInt(indexStr) - 1;

            if (listIndex < 0 || listIndex >= applications.size()) {
                LOGGER.log(Level.WARNING, "Status update failed: Index " + (listIndex + 1) + " out of bounds");
                System.out.println("Invalid index! Application not found.");
                return;
            }

            String newStatus = input.substring(setIndex + 4, noteIndex).trim().toUpperCase();
            String note = input.substring(noteIndex + 5).trim();

            Add app = applications.get(listIndex);
            assert app != null : "Retrieved application at index " + listIndex + " should not be null";

            app.setStatus(newStatus + " (Note: " + note + ")");
            LOGGER.log(Level.INFO, "Successfully updated status for application at index " + listIndex);
            System.out.println("Updated Status: " + app);

        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Failed to parse index from input: " + input, e);
            System.out.println("Error parsing status command. Ensure index is a number.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error during status update", e);
        }
    }

    /**
     * Main entry-point for the application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        String logo = """
                 _   ___   ____   ____   ___  _       ___   _____
                | | / _ \\ | __ ) |  _ \\ |_ _|| |     / _ \\ |_   _|
             _  | || | | ||  _ \\ | |_) | | | | |    | | | |  | |
            | |_| || |_| || |_) ||  __/  | | | |___ | |_| |  | |
             \\___/  \\___/ |____/ |_|    |___||_____| \\___/   |_|
            """;

        System.out.println("Hello from\n" + logo);
        System.out.println("Welcome to JobPilot!");
        System.out.println("Commands: add | list | sort | status | delete | bye");

        Scanner in = new Scanner(System.in);
        ArrayList<Add> applications = new ArrayList<>();

        while (true) {
            String input = in.nextLine().trim();

            if (input.equals("bye")) {
                System.out.println("Bye! You added " + applications.size() + " application(s).");
                break;
            } else if (input.startsWith("add")) {
                try {
                    addApplication(applications, input);
                } catch (JobPilotException e) {
                    System.out.println(e.getMessage());
                }
            } else if (input.equals("list")) {
                listApplications(applications);
            } else if (input.equals("sort")) {
                sortApplications(applications);
            } else if (input.startsWith("status ")) {
                updateStatus(applications, input);
            } else if (input.startsWith("delete")) {
                try {
                    deleteApplication(input, applications);
                } catch (JobPilotException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("Unknown command. Use: add or list or sort or status or delete or bye");
            }
        }
        in.close();
    }

    /**
     * Helper to delete an application from the list.
     *
     * @param input        The full user command.
     * @param applications The list storing all job applications.
     * @throws JobPilotException If the index provided is invalid.
     */
    private static void deleteApplication(String input, ArrayList<Add> applications) throws JobPilotException {
        try {
            Delete.deleteApplication(input, applications);
        } catch (NumberFormatException e) {
            throw new JobPilotException("Invalid format! Use: delete INDEX");
        }
    }
}


