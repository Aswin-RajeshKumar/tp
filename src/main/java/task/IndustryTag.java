package task;

/**
 * Represents an immutable industry tag for job applications (e.g., Tech, Finance).
 */
public class IndustryTag {
    private final String tagName;

    /**
     * Creates an industry tag with normalized name (uppercase, trimmed).
     * @param tagName Name of the industry tag (e.g., "tech")
     */
    public IndustryTag(String tagName) {
        assert tagName != null && !tagName.trim().isEmpty() : "Tag name cannot be null/empty";
        this.tagName = tagName.trim().toUpperCase();
    }

    /**
     * Gets the normalized tag name.
     * @return Uppercase tag name
     */
    public String getTagName() {
        return tagName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndustryTag that = (IndustryTag) o;
        return tagName.equals(that.tagName);
    }

    @Override
    public int hashCode() {
        return tagName.hashCode();
    }

    @Override
    public String toString() {
        return tagName;
    }
}