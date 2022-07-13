package Util;

/**
 *
 * @author Kenny
 */
public class ModelPerson {
    
    private int id;
    private String first_name;
    private String last_name;
    private String dob;
    private String officer;

    public ModelPerson() {
    
    }

    public ModelPerson(int id, String first_name, String last_name, String dob, String officer) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.dob = dob;
        this.officer = officer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getOfficer() {
        return officer;
    }

    public void setOfficer(String officer) {
        this.officer = officer;
    }
    
    
}
