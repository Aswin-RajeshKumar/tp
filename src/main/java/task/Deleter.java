package task;

import exception.JobPilotException;
import java.util.ArrayList;

/**
 * Handles the deletion of a given application from the list.
 */
public class Deleter {
    /**
     * Deletes an application from the list by parsing the index provided by the user.
     *
     * @param input        The full user command (e.g., "delete 2").
     * @param applications The list storing all job applications.
     * @return The application that was successfully removed.
     * @throws JobPilotException If the index provided is not a valid integer or is out of range.
     */
    public static Application deleteApplication(String input, ArrayList<Application> applications)
            throws JobPilotException {
        try {
            String[] parts = input.split(" ");

            if (parts.length < 2) {
                throw new JobPilotException("Please provide an index. Example: delete 1");
            }

            assert applications != null : "The application list is empty!";

            int deleteIndex = Integer.parseInt(parts[1]) - 1;

            if (deleteIndex < 0 || deleteIndex >= applications.size()) {
                throw new JobPilotException("Invalid application number!");
            }

            Application removed = applications.remove(deleteIndex);
            assert removed != null : "The application was not successfully deleted. Please try again!";

            return removed;
        } catch (NumberFormatException e) {
            throw new JobPilotException("Invalid format! Use: delete INDEX");
        }
    }
}
