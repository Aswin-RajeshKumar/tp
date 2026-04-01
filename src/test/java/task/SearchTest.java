package task;

import app.CommandRunner;
import parser.ParsedCommand;

import org.junit.jupiter.api.*;

import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SearchTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private ArrayList<Application> applications;
    private CommandRunner runner;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        applications = new ArrayList<>();
        runner = new CommandRunner(applications);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        outContent.reset();
    }

    @Test
    void search_noMatches() {
        applications.add(new Application("Google", "SE", "2025-01-10"));

        runner.run(new ParsedCommand("c", "microsoft"));

        assertTrue(outContent.toString().contains("No applications found"));
    }

    @Test
    void search_partialMatch() {
        applications.add(new Application("Google", "SE", "2025-01-10"));
        applications.add(new Application("GoGo", "PM", "2025-02-10"));

        runner.run(new ParsedCommand("c", "go"));

        String output = outContent.toString();
        assertTrue(output.contains("Google"));
        assertTrue(output.contains("GoGo"));
    }

    @Test
    void search_caseInsensitive() {
        applications.add(new Application("Google", "SE", "2025-01-10"));

        runner.run(new ParsedCommand("c", "GOOGLE"));

        assertTrue(outContent.toString().contains("Google"));
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

        runner.run(new ParsedCommand("s", "accepted"));

        String output = outContent.toString();
        assertTrue(output.contains("Google"));
        assertFalse(output.contains("Amazon"));
    }
}
