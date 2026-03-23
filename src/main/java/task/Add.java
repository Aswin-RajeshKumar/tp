package task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

/**
 * Represents a job application and provides methods to manage its data.
 */

public class Add implements Comparable<Add> {
    private String company;
    private String position;
    private LocalDate date;
    private String status;
    private String notes;

    public Add(String company, String position, String date) throws DateTimeParseException {
        assert company != null: "Company cannot be null" ;
        assert position != null: "Position cannot be null";
        assert date != null: "Date cannot be null";

        assert !company.trim().isEmpty(): "Company cannot be empty string!";
        assert !position.trim().isEmpty(): "Position cannot be empty string!";
        assert !date.trim().isEmpty(): "Date cannot be empty string!";

        this.company = company;
        this.position = position;
        this.date = LocalDate.parse(date);
        this.status = "Pending";
        this.notes = "";
    }

    @Override
    public int compareTo(Add other) {
        assert other != null: "Cannot compare with null object";
        assert other.date != null: "Other applications has null date";

        return this.date.compareTo(other.date);
    }

    public String getCompany() {
        assert company != null: "Company should not be null";
        return company;
    }

    public String getPosition() {
        assert position != null: "Position should not be null";
        return position;
    }

    public String getDate() {
        assert date != null: "Date should not be null";
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public void setStatus(String status) {
        assert status != null: "Status should not be null";
        assert !status.trim().isEmpty() : "Status cannot be empty";
        this.status = status;
    }

    public String getStatus() {
        assert status != null: "Status should not be null";
        return status;
    }

    public void setNotes(String notes) {
        assert notes != null: "Notes should not be null (empty allowed)";
        this.notes = notes;
    }

    public String getNotes() {
        assert notes != null: "Notes should not be null";
        return notes;
    }

    @Override
    public String toString() {
        assert company != null: "Company cannot be null" ;
        assert position != null: "Position cannot be null";
        assert date != null: "Date cannot be null";
        assert status != null: "Status cannot be null";
        assert notes != null: "Notes cannot be null";

        String tagDisplay = industryTags.isEmpty() ? "" : " | Tags: " + industryTags;
        String noteDisplay = notes.isEmpty() ? "" : " (Note: " + notes + ")";

        return company + " | " + position + " | " + getDate() + " | " + status + noteDisplay + tagDisplay;
    }
    private Set<IndustryTag> industryTags = new HashSet<>();

    public void addIndustryTag(IndustryTag tag) {
        assert tag != null : "Tag cannot be null";
        industryTags.add(tag);
    }

    public void removeIndustryTag(IndustryTag tag) {
        assert tag != null : "Tag cannot be null";
        industryTags.remove(tag);
    }

    public Set<IndustryTag> getIndustryTags() {
        return Collections.unmodifiableSet(industryTags);
    }
}

