package task;

import app.CommandRunner;
import parser.ParsedCommand;
import parser.CommandType;

import org.junit.jupiter.api.*;

import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ListTest {

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
    void listApplications_emptyList_printsNoApplicationMessage() {
        ParsedCommand cmd = new ParsedCommand(CommandType.LIST);
        runner.run(cmd);

        assertTrue(outContent.toString().contains("There is no application yet."));
    }

    @Test
    void listApplications_singleApplication_printsApplication() {
        applications.add(new Application("Google", "Software Engineer", "2025-01-10"));

        ParsedCommand cmd = new ParsedCommand(CommandType.LIST);
        runner.run(cmd);

        String output = outContent.toString();
        assertTrue(output.contains("Here are your applications:"));
        assertTrue(output.contains("Google"));
    }
}
