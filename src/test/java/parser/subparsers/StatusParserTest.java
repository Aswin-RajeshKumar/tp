package parser.subparsers;

import exception.JobPilotException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StatusParserTest {
    @Test
    void parse_junkBetweenIndexAndPrefix_throwsException() {
        assertThrows(JobPilotException.class, () -> StatusParser.parse("status 1 randomtext note/ok"));
    }

    @Test
    void parse_duplicatePrefix_throwsException() {
        // Verifies your duplicate prefix protection
        assertThrows(JobPilotException.class, () ->
                StatusParser.parse("status 1 set/OFFER set/REJECTED"));
    }

    @Test
    void parse_junkBetweenArgs_throwsException() {
        // Verifies the "Junk Zone" protection
        assertThrows(JobPilotException.class, () ->
                StatusParser.parse("status 1 randomlyText set/OFFER"));
    }
}
