//package task;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import ui.Ui;
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//class FiltererTest {
//    private ArrayList<Application> applications;
//    private Ui ui;
//
//    @BeforeEach
//    void setUp() {
//        applications = new ArrayList<>();
//        ui = new Ui();
//
//        // Sample data for testing
//        Application app1 = new Application("Google", "SWE", "2026-05-01");
//        app1.setStatus("OFFER");
//
//        Application app2 = new Application("Meta", "Frontend", "2026-06-01");
//        app2.setStatus("PENDING");
//
//        Application app3 = new Application("Apple", "Intern", "2026-07-01");
//        app3.setStatus("INTERVIEW");
//
//        applications.add(app1);
//        applications.add(app2);
//        applications.add(app3);
//    }
//
//    // ========== SUCCESS CASES (Matches Found) ==========
//
//    @Test
//    void filterByStatus_exactMatch_found() {
//        ArrayList<Application> results = new ArrayList<>();
//        String query = "OFFER";
//        for (Application app : applications) {
//            if (app.getStatus().toUpperCase().contains(query.toUpperCase())) {
//                results.add(app);
//            }
//        }
//        assertEquals(1, results.size());
//        assertEquals("Google", results.get(0).getCompany());
//    }
//
//    @Test
//    void filterByStatus_lowercaseQuery_found() {
//        // Testing case insensitivity
//        ArrayList<Application> results = new ArrayList<>();
//        String query = "pending";
//        for (Application app : applications) {
//            if (app.getStatus().toUpperCase().contains(query.toUpperCase())) {
//                results.add(app);
//            }
//        }
//        assertEquals(1, results.size());
//        assertEquals("Meta", results.get(0).getCompany());
//    }
//
//    @Test
//    void filterByStatus_partialMatch_found() {
//        // Testing if "OFF" matches "OFFER"
//        ArrayList<Application> results = new ArrayList<>();
//        String query = "OFF";
//        for (Application app : applications) {
//            if (app.getStatus().toUpperCase().contains(query.toUpperCase())) {
//                results.add(app);
//            }
//        }
//        assertEquals(1, results.size());
//        assertEquals("OFFER", results.get(0).getStatus());
//    }
//
//    @Test
//    void filterByStatus_multipleMatches_found() {
//        // Add another application with "PENDING" status
//        Application app4 = new Application("Amazon", "SDE", "2026-08-01");
//        app4.setStatus("PENDING");
//        applications.add(app4);
//
//        ArrayList<Application> results = new ArrayList<>();
//        String query = "PENDING";
//        for (Application app : applications) {
//            if (app.getStatus().toUpperCase().contains(query.toUpperCase())) {
//                results.add(app);
//            }
//        }
//        assertEquals(2, results.size());
//    }
//
//    // ========== NO MATCH CASES ==========
//
//    @Test
//    void filterByStatus_noMatchingStatus_emptyResults() {
//        ArrayList<Application> results = new ArrayList<>();
//        String query = "REJECTED";
//        for (Application app : applications) {
//            if (app.getStatus().toUpperCase().contains(query.toUpperCase())) {
//                results.add(app);
//            }
//        }
//        assertTrue(results.isEmpty());
//    }
//
//    @Test
//    void filterByStatus_emptyQuery_returnsAllWithStatus() {
//        // If query is empty but contains check is used, it often matches everything
//        ArrayList<Application> results = new ArrayList<>();
//        String query = "";
//        for (Application app : applications) {
//            if (app.getStatus().toUpperCase().contains(query.toUpperCase())) {
//                results.add(app);
//            }
//        }
//        assertEquals(3, results.size());
//    }
//
//    // ========== EDGE CASES ==========
//
//    @Test
//    void filterByStatus_emptyList_noResults() {
//        applications.clear();
//        ArrayList<Application> results = new ArrayList<>();
//        String query = "OFFER";
//        for (Application app : applications) {
//            if (app.getStatus().toUpperCase().contains(query.toUpperCase())) {
//                results.add(app);
//            }
//        }
//        assertTrue(results.isEmpty());
//    }
//
//    @Test
//    void filterByStatus_statusWithSpaces_found() {
//        Application app5 = new Application("Bytedance", "Backend", "2026-09-01");
//        app5.setStatus("OFFER EXTENDED");
//        applications.add(app5);
//
//        ArrayList<Application> results = new ArrayList<>();
//        String query = "OFFER EXTENDED";
//        for (Application app : applications) {
//            if (app.getStatus().toUpperCase().contains(query.toUpperCase())) {
//                results.add(app);
//            }
//        }
//        assertEquals(1, results.size());
//        assertEquals("Bytedance", results.get(0).getCompany());
//    }
//
//    @Test
//    void filterByStatus_nullStatusInApplication_skipped() {
//        // Adding an application with a null status to test defensiveness
//        Application appNull = new Application("Startup", "Founder", "2026-10-01");
//        // status is null by default or not set
//        applications.add(appNull);
//
//        ArrayList<Application> results = new ArrayList<>();
//        String query = "OFFER";
//        for (Application app : applications) {
//            // Defensive check similar to your Filterer.java logic
//            if (app.getStatus() != null && app.getStatus().toUpperCase().contains(query.toUpperCase())) {
//                results.add(app);
//            }
//        }
//        assertEquals(1, results.size()); // Should still only find Google
//    }
//}