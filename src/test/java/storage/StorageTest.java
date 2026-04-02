package storage;

import org.junit.jupiter.api.*;
import task.Application;
import task.IndustryTag;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class StorageTest {

    private Storage storage;
    private File defaultFile;

    @BeforeEach
    void setUp() throws Exception {
        storage = new Storage();

        // Keep track of the default file path
        defaultFile = new File(Storage.CURRENT_WORKING_DIRECTORY + "/data/JobPilotData.txt");

        // Make sure file is empty before each test
        if (defaultFile.exists()) {
            defaultFile.delete();
        }
        defaultFile.getParentFile().mkdirs();
        defaultFile.createNewFile();
    }

    @AfterEach
    void tearDown() {
        // Clean up file after each test
        if (defaultFile.exists()) {
            defaultFile.delete();
        }
    }

    @Test
    void saveAndLoadSingleApplication_shouldPreserveData() {
        ArrayList<Application> appsToSave = new ArrayList<>();
        Application app = new Application("Google", "SE Manager", "2025-03-10");
        app.setStatus("INTERVIEW");
        app.setNotes("Bring portfolio");
        app.addIndustryTag(new IndustryTag("Tech"));
        appsToSave.add(app);

        storage.saveToFile(appsToSave);

        ArrayList<Application> loadedApps = storage.loadFromFile();

        assertEquals(1, loadedApps.size());
        Application loadedApp = loadedApps.get(0);
        assertEquals("Google", loadedApp.getCompany());
        assertEquals("SE Manager", loadedApp.getPosition());
        assertEquals("2025-03-10", loadedApp.getDate());
        assertEquals("INTERVIEW", loadedApp.getStatus());
        assertEquals("Bring portfolio", loadedApp.getNotes());
        assertTrue(loadedApp.getIndustryTags().stream()
                .anyMatch(tag -> tag.getTagName().equals("TECH")));
    }

    @Test
    void saveAndLoadMultipleApplications_shouldPreserveAllData() {
        ArrayList<Application> appsToSave = new ArrayList<>();

        Application app1 = new Application("Google", "SE Manager", "2025-03-10");
        app1.setStatus("INTERVIEW");
        app1.addIndustryTag(new IndustryTag("Tech"));

        Application app2 = new Application("Amazon", "Data Scientist", "2025-04-01");
        app2.setStatus("APPLIED");
        app2.setNotes("Follow up in 2 weeks");
        app2.addIndustryTag(new IndustryTag("E-COMMERCE"));

        appsToSave.add(app1);
        appsToSave.add(app2);

        storage.saveToFile(appsToSave);
        ArrayList<Application> loadedApps = storage.loadFromFile();

        assertEquals(2, loadedApps.size());

        // Verify first application
        Application loadedApp1 = loadedApps.get(0);
        assertEquals("Google", loadedApp1.getCompany());
        assertEquals("SE Manager", loadedApp1.getPosition());
        assertEquals("INTERVIEW", loadedApp1.getStatus());
        assertTrue(loadedApp1.getIndustryTags().stream()
                .anyMatch(tag -> tag.getTagName().equals("TECH")));

        // Verify second application
        Application loadedApp2 = loadedApps.get(1);
        assertEquals("Amazon", loadedApp2.getCompany());
        assertEquals("Data Scientist", loadedApp2.getPosition());
        assertEquals("APPLIED", loadedApp2.getStatus());
        assertEquals("Follow up in 2 weeks", loadedApp2.getNotes());
        assertTrue(loadedApp2.getIndustryTags().stream()
                .anyMatch(tag -> tag.getTagName().equals("E-COMMERCE")));
    }

    @Test
    void loadFromEmptyFile_shouldReturnEmptyList() {
        // Default file is empty
        ArrayList<Application> loadedApps = storage.loadFromFile();
        assertTrue(loadedApps.isEmpty());
    }

    @Test
    void loadFromFile_withCorruptedLines_shouldSkipCorrupted() throws Exception {
        // Write valid and invalid lines directly to the default file
        FileWriter writer = new FileWriter(defaultFile, false);
        writer.write("Google | SE Manager | 2025-03-10 | INTERVIEW | Notes | TECH\n"); // valid
        writer.write("CORRUPTED_LINE_WITH_TOO_FEW_FIELDS\n"); // invalid
        writer.write("Amazon | Data Scientist | 2025-04-01 | APPLIED | Follow up | E-COMMERCE\n"); // valid
        writer.close();

        ArrayList<Application> loadedApps = storage.loadFromFile();

        // Only valid applications should be loaded
        assertEquals(2, loadedApps.size());
        assertEquals("Google", loadedApps.get(0).getCompany());
        assertEquals("Amazon", loadedApps.get(1).getCompany());
    }

    @Test
    void saveToFile_emptyApplicationList_shouldCreateEmptyFile() {
        ArrayList<Application> emptyList = new ArrayList<>();

        storage.saveToFile(emptyList);
        ArrayList<Application> loadedApps = storage.loadFromFile();

        assertTrue(loadedApps.isEmpty());
    }

    @Test
    void loadFromFile_withOnlyCorruptedLines_shouldReturnEmptyList() throws Exception {
        FileWriter writer = new FileWriter(defaultFile, false);
        writer.write("INVALID_LINE\n");
        writer.write("Another bad line\n");
        writer.close();

        ArrayList<Application> loadedApps = storage.loadFromFile();

        assertTrue(loadedApps.isEmpty());
    }

    @Test
    void loadFromFile_withMixedValidAndCorruptedLines_shouldLoadOnlyValidApplications() throws Exception {
        FileWriter writer = new FileWriter(defaultFile, false);
        writer.write("Google | SE Manager | 2025-03-10 | INTERVIEW | Notes | TECH\n");
        writer.write("INVALID_LINE\n");
        writer.write("Meta | Product Manager | 2025-05-20 | OFFER | Accepted | TECH,AI\n");
        writer.close();

        ArrayList<Application> loadedApps = storage.loadFromFile();

        assertEquals(2, loadedApps.size());
        assertEquals("Google", loadedApps.get(0).getCompany());
        assertEquals("Meta", loadedApps.get(1).getCompany());
    }

}
