package coms309.people;

public class Student extends Person{
    private String school;

    private String year;

    public Student(){

    }

    public Student(String firstName, String lastName, String address, String telephone, String school, String year){
        super(firstName, lastName, address, telephone);

        this.school = school;
        this.year = year;
    }

    public String getSchool() {
        return this.school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getYear() {
        return this.year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return super.toString() + " "
                + school + " "
                + year;
    }
}
