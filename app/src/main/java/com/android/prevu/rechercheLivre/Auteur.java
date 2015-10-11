package com.android.prevu.rechercheLivre;

/**
 * Created by Ouerdia on 10/09/2015.
 */
public class Auteur {
    private int id_author;
    private String author_nom;
    private String author_prenom;
    private String datenaissance;
    private String datedeces;

    public Auteur(int id_author, String author_nom, String author_prenom, String datenaissance, String datedeces) {
        this.id_author = id_author;
        this.author_nom = author_nom;
        this.author_prenom = author_prenom;
        this.datenaissance = datenaissance;
        this.datedeces = datedeces;
    }
    public Auteur() {
        this.id_author = 0;
        this.author_nom = "";
        this.author_prenom = "";
        this.datenaissance = "";
        this.datedeces = "";
    }


    public String getDatedeces() {

        return datedeces;
    }

    public void setDatedeces(String datedeces) {
        this.datedeces = datedeces;
    }

    public String getDatenaissance() {
        return datenaissance;
    }

    public void setDatenaissance(String datenaissance) {
        this.datenaissance = datenaissance;
    }

    public String getAuthor_prenom() {
        return author_prenom;
    }

    public void setAuthor_prenom(String author_prenom) {
        this.author_prenom = author_prenom;
    }

    public String getAuthor_nom() {
        return author_nom;
    }

    public void setAuthor_nom(String author_nom) {
        this.author_nom = author_nom;
    }

    public int getId_author() {
        return id_author;
    }

    public void setId_author(int id_author) {
        this.id_author = id_author;
    }

}
