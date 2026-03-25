//package task;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import exception.JobPilotException;
//
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//class DeleteTest {
//
//    private ArrayList<Application> applications;
//
//    @BeforeEach
//    public void setUp() {
//        // Initialize a fresh list before each test
//        applications = new ArrayList<>();
//    }
//
//    @Test
//    public void deleteApplication_validIndex_sizeDecreasesByOne() throws JobPilotException {
//        applications.add(new Application("Google", "Software Engineer", "2026-03-01"));
//        applications.add(new Application("Apple", "Data Analyst", "2026-03-02"));
//
//        Deleter.deleteApplication("delete 1", applications);
//
//        assertEquals(1, applications.size());
//    }
//
//    @Test
//    public void deleteApplication_deleteSecondApplication_correctItemRemoved() throws JobPilotException {
//        Application first = new Application("Google", "Software Engineer", "2026-03-01");
//        Application second = new Application("Apple", "Data Analyst", "2026-03-02");
//
//        applications.add(first);
//        applications.add(second);
//
//        Deleter.deleteApplication("delete 2", applications);
//
//        assertEquals(1, applications.size());
//        assertEquals(first, applications.get(0));
//    }
//
//    @Test
//    public void deleteApplication_deleteLastItem_listBecomesEmpty() throws JobPilotException {
//        Application onlyApplication = new Application("Microsoft", "UX Designer", "2026-03-03");
//        applications.add(onlyApplication);
//
//        Deleter.deleteApplication("delete 1", applications);
//
//        // List should now be empty
//        assertEquals(0, applications.size());
//    }
//
//    @Test
//    public void deleteApplication_missingIndex_throwsException() {
//        JobPilotException exception = assertThrows(JobPilotException.class, () -> {
//            Deleter.deleteApplication("delete", applications);
//        });
//
//        assertEquals("Please provide an index. Example: delete 1", exception.getMessage());
//    }
//
//    @Test
//    public void deleteApplication_invalidIndex_throwsException() {
//        applications.add(new Application("Google", "Software Engineer", "2026-03-01"));
//
//        JobPilotException exception = assertThrows(JobPilotException.class, () -> {
//            Deleter.deleteApplication("delete 5", applications);
//        });
//
//        assertEquals("Invalid application number!", exception.getMessage());
//    }
//
//    @Test
//    public void deleteApplication_nonIntegerIndex_throwsException() {
//        JobPilotException exception = assertThrows(JobPilotException.class, () -> {
//            Deleter.deleteApplication("delete abc", applications);
//        });
//
//        assertEquals("Invalid format! Use: delete INDEX", exception.getMessage());
//    }
//
//    @Test
//    public void deleteApplication_fromEmptyList_throwsException() {
//        JobPilotException exception = assertThrows(JobPilotException.class, () -> {
//            Deleter.deleteApplication("delete 1", applications);
//        });
//
//        assertEquals("Invalid application number!", exception.getMessage());
//    }
//}
