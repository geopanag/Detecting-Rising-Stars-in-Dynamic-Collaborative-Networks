package Crawling;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import DataProcessing.Author;
import java.util.ArrayList;

/**
 *
 * @author Giorgos
 */
public class Paper {
    ArrayList <Author> authors;
    String title;
    boolean more;//more authors in Scopus csv than in Scopus web site
    String journal;
    String cited_by;

    public Paper(ArrayList<Author> authors, String title, boolean more, String journal, String cited_by) {
        this.authors = authors;
        this.title = title;
        this.more = more;
        this.journal = journal;
        this.cited_by = cited_by;
    }

    public String getCited_by() {
        return cited_by;
    }

    public void setCited_by(String cited_by) {
        this.cited_by = cited_by;
    }

    

    public ArrayList<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<Author> authors) {
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }
   
    
}
