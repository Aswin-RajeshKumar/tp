package task;

import app.CommandRunner;
import parser.CommandType;
import parser.ParsedCommand;

import org.junit.jupiter.api.*;

import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the search functionality.
 * Verifies case-insensitivity and partial matching logic.
 */
public class SearchTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private ArrayList<Application> applications;
    private CommandRunner runner;

    @BeforeEach
    void setUp() {
        // Redirect System.out to capture console output for verification
        System.setOut(new PrintStream(outContent));
        applications = new ArrayList<>();
        runner = new CommandRunner(applications);
    }

    @AfterEach
    void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
        outContent.reset();
    }

    @Test
    void search_noMatches_displaysErrorMessage() {
        applications.add(new Application("Google", "SE", "2025-01-10"));

        runner.run(new ParsedCommand("c", "microsoft"));

        String output = outContent.toString();
        assertTrue(output.contains("No applications found"), "Should notify user when no match exists");
    }

    @Test
    void search_partialMatch_findsRelevantApplications() {
        applications.add(new Application("Google", "SE", "2025-01-10"));
        applications.add(new Application("GoGo", "PM", "2025-02-10"));

        runner.run(new ParsedCommand("c", "go"));

        String output = outContent.toString();
        assertTrue(output.contains("Google"), "Should find Google for query 'Go'");
        assertTrue(output.contains("GoGo"), "Should find GoGo for query 'Go'");
    }

    @Test
    void search_caseInsensitive_ignoresCasing() {
        applications.add(new Application("Google", "SE", "2025-01-10"));

        runner.run(new ParsedCommand("c", "GOOGLE"));

        String output = outContent.toString();
        assertTrue(output.contains("Google"), "Search should be case-insensitive");
    }

    @Test
    void search_byPosition() {
        applications.add(new Application("Google", "Intern", "2025-01-10"));
        applications.add(new Application("Amazon", "Manager", "2025-02-10"));

        runner.run(new ParsedCommand("p", "intern"));

        String output = outContent.toString();
        assertTrue(output.contains("Google"));
        assertFalse(output.contains("Amazon"));
    }

    @Test
    void search_byStatus() {
        Application app1 = new Application("Google", "SE", "2025-01-10");
        app1.setStatus("Accepted");

        Application app2 = new Application("Amazon", "SE", "2025-02-10");
        app2.setStatus("Pending");

        applications.add(app1);
        applications.add(app2);

        runner.run(new ParsedCommand("s", "Accepted"));

        String output = outContent.toString();
        assertTrue(output.contains("Google"));
        assertFalse(output.contains("Amazon"));
    }
}
