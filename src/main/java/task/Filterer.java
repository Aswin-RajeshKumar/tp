package task;

import ui.Ui;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Filterer {
    // V2.0 REQUIREMENT: Logging
    private static final Logger LOGGER = Logger.getLogger(Filterer.class.getName());

    /**
     * Filters applications by the given status string.
     *
     * @param applications The master list of applications.
     * @param statusQuery  The status to search for (e.g., "OFFER").
     * @param ui           The UI instance to handle output.
     */
    public static void filterByStatus(ArrayList<Application> applications, String statusQuery, Ui ui) {
        // V2.0 REQUIREMENT: Assertions
        assert applications != null : "The applications list should not be null";
        assert ui != null : "The UI component should be initialized";

        LOGGER.log(Level.INFO, "Filtering applications for status: " + statusQuery);

        ArrayList<Application> filteredResults = new ArrayList<>();

        for (Application app : applications) {
            // Defensive check: ensure the app and its status aren't null
            if (app != null && app.getStatus() != null) {
                // Using contains and toUpperCase makes it more flexible for the user
                if (app.getStatus().toUpperCase().contains(statusQuery.toUpperCase())) {
                    filteredResults.add(app);
                }
            }
        }

        if (filteredResults.isEmpty()) {
            LOGGER.log(Level.WARNING, "No matches found for status: " + statusQuery);
            ui.showMessage("No applications found with status matching: " + statusQuery);
        } else {
            ui.showMessage("Found " + filteredResults.size() + " matching application(s):");
            ui.showApplicationList(filteredResults);
        }
    }
}