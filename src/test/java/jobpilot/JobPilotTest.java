package jobpilot;

import org.junit.jupiter.api.Test;
import task.Add;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JobPilotTest {

    @Test
    public void listApplications_emptyList_noApplication() {
        ArrayList<Add> applications = new ArrayList<>();

        JobPilot.listApplications(applications);

        assertTrue(applications.isEmpty());
    }

    @Test
    public void listApplications_oneApplication_sizeOne() {
        ArrayList<Add> applications = new ArrayList<>();
        applications.add(new Add("Google", "Software Engineer", "2024-09-10"));

        JobPilot.listApplications(applications);

        assertEquals(1, applications.size());
    }

    @Test
    public void listApplications_multipleApplications_sizeCorrect() {
        ArrayList<Add> applications = new ArrayList<>();

        applications.add(new Add("Google", "Software Engineer", "2024-09-10"));
        applications.add(new Add("Amazon", "Data Analyst", "2024-09-12"));

        JobPilot.listApplications(applications);

        assertEquals(2, applications.size());
    }
}