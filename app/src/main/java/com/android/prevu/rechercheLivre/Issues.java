package com.android.prevu.rechercheLivre;

/**
 * Created by Ouerdia on 14/08/2015.
 */
public class Issues {
    private int id_issue;

    private String issuesdate;
    private int issues;
    private String ufr;
    private String matiere;
    private String semestre;
    private String year;
    private String month;
    private String semester;

    public String getMoisAnnee() {
        return moisAnnee;
    }

    public void setMoisAnnee(String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

    private String moisAnnee;


    public String getUfr() {
        return ufr;
    }

    public void setUfr(String ufr) {
        this.ufr = ufr;
    }



    public String getIssuesdate() {
        return issuesdate;
    }

    public void setIssuesdate(String issuesdate) {
        this.issuesdate = issuesdate;
    }

    public int getIssues() {
        return issues;
    }

    public void setIssues(int issues) {
        this.issues = issues;
    }

    public int getId_issue() {
        return id_issue;
    }

    public void setId_issue(int id_issue) {
        this.id_issue = id_issue;
    }

    private String description;

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Issues(int id_issue, String description, int issues, String issuesdate,
                  String ufr, String matiere, String semestre,String year, String semester, String month, String moisAnnee ) {
        this.id_issue = id_issue;
        this.description = description;
        this.ufr = ufr;
        this.matiere=matiere;
        this.issues=issues;
        this.issuesdate=issuesdate;
        this.year=year;
        this.semestre=semestre;
        this.semester=semester;
        this.month=month;
        this.moisAnnee=moisAnnee;
    }

    public String getMatiere() {
        return matiere;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Issues() {
        this.id_issue = 0;
        this.description = "";
this.matiere=null;

        this.issues=0;
        this.issuesdate=null;
        this.ufr =null;
        this.semestre=null;
        this.year=null;
        this.moisAnnee=null;
        this.semester=null;
        this.month="";



    }






    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
