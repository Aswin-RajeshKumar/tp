/**
package task;

import exception.JobPilotException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EditTest {
    private ArrayList<Application> applications;
    private Application testApp;

    @BeforeEach
    void setUp() {
        applications = new ArrayList<>();
        testApp = new Application("Google", "Software Engineer", "2024-09-12");
        applications.add(testApp);
    }

    // ========== SUCCESS CASES ==========

    @Test
    void editApplication_updateCompanyOnly_success() throws JobPilotException {
        Edit.editApplication("edit 1 c/Microsoft", applications);
        assertEquals("Microsoft", testApp.getCompany());
        assertEquals("Software Engineer", testApp.getPosition());
        assertEquals("2024-09-12", testApp.getDate());
        assertEquals("Pending", testApp.getStatus());
    }

    @Test
    void editApplication_updatePositionOnly_success() throws JobPilotException {
        Edit.editApplication("edit 1 p/Senior Engineer", applications);
        assertEquals("Google", testApp.getCompany());
        assertEquals("Senior Engineer", testApp.getPosition());
        assertEquals("2024-09-12", testApp.getDate());
        assertEquals("Pending", testApp.getStatus());
    }

    @Test
    void editApplication_updateDateOnly_success() throws JobPilotException {
        Edit.editApplication("edit 1 d/2024-12-01", applications);
        assertEquals("Google", testApp.getCompany());
        assertEquals("Software Engineer", testApp.getPosition());
        assertEquals("2024-12-01", testApp.getDate());
        assertEquals("Pending", testApp.getStatus());
    }

    @Test
    void editApplication_updateStatusOnly_success() throws JobPilotException {
        Edit.editApplication("edit 1 s/Interview", applications);
        assertEquals("Google", testApp.getCompany());
        assertEquals("Software Engineer", testApp.getPosition());
        assertEquals("2024-09-12", testApp.getDate());
        assertEquals("Interview", testApp.getStatus());
    }

    @Test
    void editApplication_updateMultipleFields_success() throws JobPilotException {
        Edit.editApplication("edit 1 c/Microsoft p/Senior Engineer d/2024-12-01 s/Offer",
                applications);
        assertEquals("Microsoft", testApp.getCompany());
        assertEquals("Senior Engineer", testApp.getPosition());
        assertEquals("2024-12-01", testApp.getDate());
        assertEquals("Offer", testApp.getStatus());
    }

    @Test
    void editApplication_updateCompanyWithSpaces_success() throws JobPilotException {
        Edit.editApplication("edit 1 c/Amazon Web Services", applications);
        assertEquals("Amazon Web Services", testApp.getCompany());
    }

    @Test
    void editApplication_updatePositionWithSpaces_success() throws JobPilotException {
        Edit.editApplication("edit 1 p/Senior Software Engineer", applications);
        assertEquals("Senior Software Engineer", testApp.getPosition());
    }

    @Test
    void editApplication_partialUpdate_otherFieldsUnchanged() throws JobPilotException {
        Edit.editApplication("edit 1 c/Microsoft", applications);
        assertEquals("Microsoft", testApp.getCompany());
        assertEquals("Software Engineer", testApp.getPosition());
        assertEquals("2024-09-12", testApp.getDate());
        assertEquals("Pending", testApp.getStatus());
    }

    // ========== ERROR CASES ==========

    @Test
    void editApplication_missingIndex_throwsException() {
        Exception exception = assertThrows(JobPilotException.class, () -> {
            Edit.editApplication("edit", applications);
        });
        assertTrue(exception.getMessage().contains("provide an index"));
    }

    @Test
    void editApplication_invalidIndexZero_throwsException() {
        Exception exception = assertThrows(JobPilotException.class, () -> {
            Edit.editApplication("edit 0 c/Google", applications);
        });
        assertTrue(exception.getMessage().contains("Invalid application number"));
    }

    @Test
    void editApplication_indexTooLarge_throwsException() {
        Exception exception = assertThrows(JobPilotException.class, () -> {
            Edit.editApplication("edit 99 c/Google", applications);
        });
        assertTrue(exception.getMessage().contains("Invalid application number"));
    }

    @Test
    void editApplication_nonNumericIndex_throwsException() {
        Exception exception = assertThrows(JobPilotException.class, () -> {
            Edit.editApplication("edit abc c/Google", applications);
        });
        assertTrue(exception.getMessage().contains("Invalid index"));
    }

    @Test
    void editApplication_noFieldsToUpdate_throwsException() {
        Exception exception = assertThrows(JobPilotException.class, () -> {
            Edit.editApplication("edit 1", applications);
        });
        assertTrue(exception.getMessage().contains("No valid fields"));
    }

    @Test
    void editApplication_invalidDateFormat_throwsException() {
        Exception exception = assertThrows(JobPilotException.class, () -> {
            Edit.editApplication("edit 1 d/01-01-2024", applications);
        });
        assertTrue(exception.getMessage().contains("Invalid date"));
    }

    @Test
    void editApplication_invalidDateValue_throwsException() {
        Exception exception = assertThrows(JobPilotException.class, () -> {
            Edit.editApplication("edit 1 d/2024-02-30", applications);
        });
        assertTrue(exception.getMessage().contains("Invalid date"));
    }

    @Test
    void editApplication_emptyCompany_throwsException() {
        Exception exception = assertThrows(JobPilotException.class, () -> {
            Edit.editApplication("edit 1 c/", applications);
        });
        assertTrue(exception.getMessage().contains("cannot be empty"));
    }

    @Test
    void editApplication_emptyPosition_throwsException() {
        Exception exception = assertThrows(JobPilotException.class, () -> {
            Edit.editApplication("edit 1 p/", applications);
        });
        assertTrue(exception.getMessage().contains("cannot be empty"));
    }

    @Test
    void editApplication_emptyDate_throwsException() {
        Exception exception = assertThrows(JobPilotException.class, () -> {
            Edit.editApplication("edit 1 d/", applications);
        });
        assertTrue(exception.getMessage().contains("cannot be empty"));
    }

    @Test
    void editApplication_emptyStatus_throwsException() {
        Exception exception = assertThrows(JobPilotException.class, () -> {
            Edit.editApplication("edit 1 s/", applications);
        });
        assertTrue(exception.getMessage().contains("cannot be empty"));
    }

    // ========== EDGE CASES ==========

    @Test
    void editApplication_emptyList_throwsException() {
        applications.clear();
        Exception exception = assertThrows(JobPilotException.class, () -> {
            Edit.editApplication("edit 1 c/Google", applications);
        });
        assertTrue(exception.getMessage().contains("Invalid application number"));
    }

    @Test
    void editApplication_multipleApps_editSecondApp() throws JobPilotException {
        Application secondApp = new Application("Meta", "Product Manager", "2024-09-15");
        applications.add(secondApp);

        Edit.editApplication("edit 2 c/Amazon", applications);

        assertEquals("Google", applications.get(0).getCompany());
        assertEquals("Amazon", applications.get(1).getCompany());
    }

    @Test
    void editApplication_whitespaceInCommand_success() throws JobPilotException {
        Edit.editApplication("edit   1   c/Microsoft   ", applications);
        assertEquals("Microsoft", testApp.getCompany());
    }

    @Test
    void editApplication_caseSensitiveFields_preservesCase() throws JobPilotException {
        Edit.editApplication("edit 1 c/GoogleInc", applications);
        assertEquals("GoogleInc", testApp.getCompany());
    }
}

 **/