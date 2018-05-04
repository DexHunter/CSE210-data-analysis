package cse210;

import java.util.Arrays;

/**
 * A helper class for storing Researcher related information
 * 
 * @author Dixing Xu
 * @since 0.1.0
 */
public class Researcher {

    /**
     * vairables of the Researcher
     */
    private String name;
    private String university;
    private String department;
    private String[] topics;
    private String[] skills;
    private String[] interest; // topics + skills

    /**
     * Researcher class constructor.
     */
    public Researcher() {
    }

    /**
     * Researcher class constructor.
     * 
     * @param name: the full name of Researcher
     * @param university: the university of Researcher
     * @param department: the department of Researcher
     * @param interest: the research interests of Researcher
     */
    public Researcher(String name, String university, String department, String[] interest) {
        this.name = name;
        this.university = university;
        this.department = department;
        this.interest = interest;
    }

    /**
     * getter for name
     * 
     * @return name of Researcher
     */
    public String getName() {
        return name;
    }

    /**
     * setter for name
     * 
     * @param name of Researcher
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter for university
     * 
     * @return univeristy of Researcher
     */
    public String getUniversity() {
        return university;
    }

    /**
     * setter for university
     * 
     * @param university of Researcher
     */
    public void setUniversity(String university) {
        this.university = university;
    }

    /**
     * getter for university
     * 
     * @return department of Researcher
     */
    public String getDeparment() {
        return department;
    }

    /**
     * setter for department
     * 
     * @param department of Researcher
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * getter for topics
     * 
     * @return topics of Researcher
     */
    public String[] getTopic() {
        return topics;
    }

    /**
     * setter for topics
     * 
     * @param topics of Researcher
     */
    public void setTopic(String[] topics) {
        this.topics = topics;
    }

    /**
     * getter for skills
     * 
     * @return skills of Researcher
     */
    public String[] getSkill() {
        return skills;
    }

    /**
     * setter for skills
     * 
     * @param skills of Researcher
     */
    public void setSkill(String[] skills) {
        this.skills = skills;
    }

    /**
     * getter for interests
     * 
     * @return interest of Researcher
     */
    public String[] getInterest() {
        return interest;
    }

    /**
     * setter for interest
     * 
     * @param interest of Researcher
     */
    public void setInterest(String[] interest) {
        this.interest = interest;
    }

    /**
     * Stringfy the Researcher
     */
    @Override
    public String toString() {
        return "Name: " + name + "\nUniversity: " + university + "\n" + department + "\nInterests: "
                + Arrays.toString(interest);
    }

}