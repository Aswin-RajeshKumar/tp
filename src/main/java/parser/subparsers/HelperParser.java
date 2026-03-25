package task;

/**
 * Represents the Help command which displays all available commands
 */
public class HelperParser {

    /**
     * Prints the help message containing all commands and formats.
     */
    public static void showHelpMessage() {
        String helpMessage = """ 
                Available Commands:
                add c/COMPANY p/POSITION d/DATE    Add a new job application
                list                               List all job applications
                delete INDEX                       Delete an application
                edit INDEX [c/COMPANY] [p/POSITION] [d/DATE] [s/STATUS]  Edit an application
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
}