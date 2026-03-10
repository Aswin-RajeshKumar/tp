package task;

/**
 * Class for adding an application to the list
 */
public class Add {
    private String company;
    private String position;
    private String date;
    private String status;

    public Add (String company, String position, String date) {
        this.company = company;
        this.position = position;
        this.date = date;
        this.status = "Pending";
    }

    public String getCompany() {
        return company;
    }

    public String getPosition() {
        return position;
    }

    public String getDate() {
        return date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return company + " | " + position + " | " + date + " | " + status;
    }
}

