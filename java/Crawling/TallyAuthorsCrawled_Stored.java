/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crawling;

import Crawling.AuthorSearchWithoutId;
import Crawling.AuthorSearchWithId;
import Crawling.AuthorPageFetcher;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import DataProcessing.Author;

/**
 *
 * @author Giorgos
 */
public class TallyAuthorsCrawled_Stored {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, SQLException, InterruptedException, IOException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String connectionURL = "";
        Connection connection = DriverManager.getConnection(connectionURL);
        Statement ola = connection.createStatement();
        Statement olaUpdate = connection.createStatement();
        Statement big = connection.createStatement();
        Statement nodes = connection.createStatement();
        
        ResultSet rsola = null;
        ResultSet rsnodes = null;
        ResultSet rsbig = null;
        rsnodes = nodes.executeQuery("SELECT * FROM nodes");
        Map<String, Author> map = new HashMap<String, Author>();
        ArrayList<String> Names = new ArrayList();
        ArrayList<String> Ids = new ArrayList();
        while (rsnodes.next()) {
            map.put(rsnodes.getString("id"), new Author(rsnodes.getString("author"), rsnodes.getString("id"), rsnodes.getString("affiliation"), rsnodes.getString("field"), rsnodes.getString("city"), "", false));
            Names.add(rsnodes.getString("author").split("---")[0]);
            Ids.add(rsnodes.getString("id"));
        }
        rsola = ola.executeQuery("SELECT *FROM ola where done=0 and more=1");///first  more=0 than more=1
        Set<String> skip = new HashSet<String>();

        while (rsola.next()) {
            //take an author with id
            String authors = rsola.getString("authors");
            int articleid = rsola.getInt("articleid");
            String title = rsola.getString("title");
            authors = authors.replaceAll(", ", " ");
            Map<String, String> paper = new HashMap<String, String>();
            String[] auth = authors.split(",");
            for (int i = 0; i < auth.length; i++) {
                String id;
                String name;
                if (auth[i].contains("\\.-")) {
                    id = auth[i].split("\\.-")[1];
                    name = auth[i].split("\\.-")[0];
                } else {
                    id = auth[i].split("-")[1];
                    name = auth[i].split("-")[0];
                }
                Author au = new Author(name, id, null, null, null, null, false);
                if (!map.containsKey(id) && id.matches(".*\\d.*")) {
                    ArrayList<String> stoixeia = AuthorPageFetcher.fetch(id);
                    if (stoixeia.get(1) == null && stoixeia.get(3) == null) {
                        //if the id is not found
                        au = AuthorSearchWithId.fetch(au);
                        if (au != null) {
                            au.setCity(au.getCity() + " " + au.getCountry());
                        }
                    } else {
                        au.setName(au.getName() + "---" + stoixeia.get(0));
                        au.setAffiliation(stoixeia.get(1));
                        au.setCity(stoixeia.get(2));
                        au.setField(stoixeia.get(3));
                    }
                    if (au != null) {
                        map.put(id, au);
                        paper.put(au.getName(), au.getId());
                        System.out.println("id " + au.getId() + " author " + au.getName());
                        try {
                            nodes.executeUpdate("INSERT INTO `nodes`(`author`,`id`,`affiliation`,`field`,`city`) VALUES (\"" + au.getName() + "\",\"" + au.getId() + "\",\"" + au.getAffiliation() + "\",\"" + au.getField() + "\",\"" + au.getCity() + "\")");
                        } catch (Exception exc) {
                          }
                    }
                }
            }

            if (articleid != 0) {
                String newAuthors = "";
                rsbig = big.executeQuery("Select authors from papers where id=" + articleid);
                while (rsbig.next()) {
                    //assign author id to an author in the database, if found with one to one name correspondance  
                    String authorsBig = rsbig.getString("authors");
                    String[] authBig = authorsBig.split(", ");
                    for (int i = 0; i < authBig.length; i++) {
                        if (skip.contains(authBig[i])) {
                            continue;
                        }
                        if (paper.containsKey(authBig[i])) {
                            if (i != (authBig.length - 1)) {
                                newAuthors = newAuthors.concat(authBig[i] + "-" + paper.get(authBig[i]) + ",");
                            } else {
                                newAuthors = newAuthors.concat(authBig[i] + "-" + paper.get(authBig[i]) );
                            }
                            continue;
                        }
                        ArrayList<Integer> small = new ArrayList();
                        for (int k = 0; k < Names.size(); k++) {//search for papers with that name
                            if (Names.get(k).equalsIgnoreCase(authBig[i])) {
                                small.add(k);
                            }
                        }
                        if (small.size() == 1) {//if there is only one name, assign the id
                            if (i != (authBig.length - 1)) {
                                newAuthors = newAuthors.concat(authBig[i] + "-" + Ids.get(small.get(0)) + ",");
                            } else {
                                newAuthors = newAuthors.concat(authBig[i] + "-" + Ids.get(small.get(0)));
                            }
                        } else {//if more than one, fetch from scopus
                            String[] mal = authBig[i].split(" ");
                            ArrayList<String> temp = new ArrayList();
                            if (mal.length >= 3) {
                                String s = "";
                                int j = 0;
                                for (j = 0; j < mal.length - 1; j++) {
                                    s = s + "+" + mal[j];
                                }
                                temp = AuthorSearchWithoutId.fetch(s, mal[j]);
                            } else {
                                if (mal.length > 1) {
                                    temp = AuthorSearchWithoutId.fetch(mal[0], mal[1]);
                                } else {
                                    temp = null;
                                }
                            }
                            if (temp != null) {//if the search results in one, it is considered the right author
                                String city = "";
                                try {
                                    city = temp.get(3).concat(" " + temp.get(4));
                                } catch (Exception e) {
                                    System.out.println("error in city field");
                                }
                                if (!map.containsKey(temp.get(0))) {
                                    try {
                                        nodes.executeUpdate("INSERT INTO `nodes`(`author`,`id`,`affiliation`,`field`,`city`) VALUES (\"" + authBig[i] + "\",\"" + temp.get(0) + "\",\"" + temp.get(2) + "\",\"" + temp.get(1) + "\",\"" + city + "\")");
                                        map.put(temp.get(0), new Author(authBig[i], temp.get(0), temp.get(2), temp.get(1), city, "", false));
                                    } catch (Exception exc) {
                                        System.out.println(authBig[i] + " not in the database " +exc.getMessage());
                                    }
                                }
                                if (i != (authBig.length - 1)) {
                                    //fix the format the authors will be assigned in the paper
                                    newAuthors = newAuthors.concat(authBig[i] + "-" + temp.get(0) + ",");
                                } else {
                                    newAuthors = newAuthors.concat(authBig[i] + "-" + temp.get(0));
                                }
                            } else {//the unidientified authorsa re left with a ! for the second passage
                                skip.add(authBig[i]);
                                if (i != (authBig.length - 1)) {
                                    newAuthors = newAuthors.concat(authBig[i] + "!" + ",");
                                } else {
                                    newAuthors = newAuthors.concat(authBig[i] + "!");
                                }
                            }
                        }
                    }
                }
                big.executeUpdate("UPDATE papers SET authors=\"" + newAuthors + "\" where id=" + articleid);
            }
            olaUpdate.executeUpdate("UPDATE papersCrawled SET done=1 where title=\"" + title + "\"");
           
        }
        rsola.close();
    }
}
