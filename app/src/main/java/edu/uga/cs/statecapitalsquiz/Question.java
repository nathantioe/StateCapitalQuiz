package edu.uga.cs.statecapitalsquiz;

/**
 * This class (a POJO) represents a single question, including the question id, state name,
 * capital city, second city, and third city.
 * The id is -1 if the object has not been persisted in the database yet, and
 * the db table's primary key value, if it has been persisted.
 */
public class Question {

    private long   questionID;
    private String stateName;
    private String capitalCity;
    private String secondCity;
    private String thirdCity;

    public Question()
    {
        this.questionID = -1;
        this.stateName = null;
        this.capitalCity = null;
        this.secondCity = null;
        this.thirdCity = null;
    }

    public Question( String stateName, String capitalCity, String secondCity, String thirdCity ) {
        this.questionID = -1;  // the primary key id will be set by a setter method
        this.stateName = stateName;
        this.capitalCity = capitalCity;
        this.secondCity = secondCity;
        this.thirdCity = thirdCity;
    }

    public long getQuestionIDId()
    {
        return questionID;
    }

    public void setQuestionID(long id)
    {
        this.questionID = id;
    }

    public String getStateName()
    {
        return stateName;
    }

    public void setStateName(String stateName)
    {
        this.stateName = stateName;
    }

    public String getCapitalCity()
    {
        return capitalCity;
    }

    public void setCapitalCity(String capitalCity)
    {
        this.capitalCity = capitalCity;
    }

    public String getSecondCity()
    {
        return secondCity;
    }

    public void setSecondCity(String secondCity)
    {
            this.secondCity = secondCity;
    }

    public String getThirdCity()
    {
        return thirdCity;
    }

    public void setThirdCity(String thirdCity)
    {
        this.thirdCity = thirdCity;
    }

    public String toString()
    {
        return questionID + ": " + stateName + " " + capitalCity + " " + secondCity + " " + thirdCity;
    }
}
