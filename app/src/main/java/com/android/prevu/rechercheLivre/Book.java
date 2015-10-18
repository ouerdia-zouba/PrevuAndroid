package com.android.prevu.rechercheLivre;

import android.graphics.Bitmap;

/**
 * Created by Ouerdia on 11/08/2015.
 */
public class Book {
    private int id_notice;
    private int id_author;
    private String title;
    private String isbn;
    private int issues;
    private String author_nom;
    private String author_prenom;
    private String publicationyear;
    private int biblionumber;
    private String image;
    private Bitmap thumbImg;

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    private String annee;



    public String getDatenaissance() {
        return datenaissance;
    }

    public void setDatenaissance(String datenaissance) {
        this.datenaissance = datenaissance;
    }

    private String datenaissance;

    public String getDatedeces() {
        return datedeces;
    }

    public void setDatedeces(String datedeces) {
        this.datedeces = datedeces;
    }

    private String datedeces;


    //private Categorie categorie;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIssues() {
        return issues;
    }

    public void setIssues(int issues) {
        this.issues = issues;
    }










    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getId_notice() {
        return id_notice;
    }

    public void setId_notice(int id_notice) {
        this.id_notice = id_notice;
    }

    public int getId_author() {
        return id_author;
    }

    public void setId_author(int id_author) {
        this.id_author = id_author;
    }

    public String getIsbn() {
        return isbn;
    }

    public Book() {
        super();

        this.id_notice = 0;
        this.id_author = 0;
        this.title = "";
        this.isbn = "";
        this.description="";
        this.issues = 0;
        this.author_nom = "";
        this.author_prenom = "";
        this.publicationyear = "";
        this.biblionumber = 0;
        this.datenaissance="";
        this.datedeces="";
        this.annee="";


    }

    public String getAuthor_nom() {
        return author_nom;
    }

    public void setAuthor_nom(String author_nom) {
        this.author_nom = author_nom;
    }

    public String getAuthor_prenom() {
        return author_prenom;
    }

    public void setAuthor_prenom(String author_prenom) {
        this.author_prenom = author_prenom;
    }

    public String getPublicationyear() {
        return publicationyear;
    }

    public void setPublicationyear(String publicationyear) {
        this.publicationyear = publicationyear;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getBiblionumber() {
        return biblionumber;
    }

    public void setBiblionumber(int biblionumber) {
        this.biblionumber = biblionumber;
    }

    public Bitmap getThumbImg() {
        return thumbImg;
    }

    public void setThumbImg(Bitmap thumbImg) {
        this.thumbImg = thumbImg;
    }

    public Book(int idNotice, String title, String isbn, int issues, String description,
                String author_nom, String author_prenom, String publicationyear, int biblionumber,
                String datenaissance, String datedeces, int id_notice, int id_author, String annee) {
        super();
        this.id_notice = 0;
        this.id_author=0;
        this.title = title;
        this.isbn = isbn;
        this.description=description;
        this.issues = issues;
        this.author_nom = author_nom;
        this.author_prenom = author_prenom;
        this.publicationyear = publicationyear;
        this.biblionumber = biblionumber;
        this.datenaissance=datenaissance;
        this.datedeces=datedeces;
        this.annee=annee;

    }

}
