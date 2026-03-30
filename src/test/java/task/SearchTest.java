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

        runner.run(new ParsedCommand("Microsoft"));

        assertTrue(outContent.toString().contains("No applications found"));
    }

    @Test
    void search_partialMatch() {
        applications.add(new Application("Google", "SE", "2025-01-10"));
        applications.add(new Application("GoGo", "PM", "2025-02-10"));

        runner.run(new ParsedCommand("Go"));

        String output = outContent.toString();
        assertTrue(output.contains("Google"));
        assertTrue(output.contains("GoGo"));
    }

    @Test
    void search_caseInsensitive() {
        applications.add(new Application("Google", "SE", "2025-01-10"));

        runner.run(new ParsedCommand("GOOGLE"));

        assertTrue(outContent.toString().contains("Google"));
    }
}
