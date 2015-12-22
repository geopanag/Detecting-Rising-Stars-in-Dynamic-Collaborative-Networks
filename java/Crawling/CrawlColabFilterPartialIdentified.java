/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crawling;

import Crawling.SelectiveAuthorSearch;
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
import javax.xml.parsers.ParserConfigurationException;
import org.htmlcleaner.XPatherException;
import DataProcessing.Author;

/**
 *
 * @author Administrator
 */
public class CrawlColabFilterPartialIdentified {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, ParserConfigurationException, IOException, XPatherException, IllegalAccessException, InstantiationException {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String connectionURL = "jdbc:mysql://127.0.0.1:3306/ptuxiakh?useUnicode=yes&characterEncoding=UTF-8&user=root&password=";
        Connection connection = DriverManager.getConnection(connectionURL);
        Statement papersQuery = connection.createStatement();
        Statement papers = connection.createStatement();
        Statement authors = connection.createStatement();
        ResultSet rspapers = null;
        ResultSet rsauthors = null;
        ResultSet rsbig = null;
        rsauthors = authors.executeQuery("SELECT * FROM authors");
        Map<String, String> map = new HashMap<String, String>();
        Set<String> skip = new HashSet<String>();
        while (rsauthors.next()) {
            map.put(rsauthors.getString("id"), rsauthors.getString("author").split("---")[0]);
        }
        //pare ta arthra pou den exoun id oi author tous
        rspapers = papersQuery.executeQuery("SELECT *FROM `papers` WHERE authors like '%!%'");
        while (rspapers.next()) {
            Map<String, String> paper = new HashMap<String, String>();
            String authors = rspapers.getString("authors").replace(", ", ",");
            int articleid = rspapers.getInt("id");
            String title = rspapers.getString("title");
            String[] auth = authors.split(",");
            ArrayList<String> kena = new ArrayList();
            //list ith identified authors
            for (int i = 0; i < auth.length; i++) {
                if (!auth[i].contains("!")) {
                    paper.put(auth[i].split("---")[0], auth[i].split("---")[1]);
                } else {//list with unidintified authors
                    kena.add(auth[i].split("---")[0]);
                }
            }
            for (int i = 0; i < kena.size(); i++) {
                //check if the name can be cralwed
                if (skip.contains(kena.get(i))) {
                    continue;
                }
                String[] mal = kena.get(i).split(" ");
                ArrayList<Author> A = new ArrayList();
                if (mal.length >= 3) {
                    String s = "";
                    int k = 0;
                    for (k = 0; k < mal.length - 1; k++) {
                        s = s + "+" + mal[k];
                    }
                    A = SelectiveAuthorSearch.fetch(s, mal[k]);

                } else {
                    if (mal.length > 1) {
                        A = SelectiveAuthorSearch.fetch(mal[0], mal[1]);
                    } else {
                        A = null;
                    }
                }
                if (A != null && !A.isEmpty()) {
                    Author eklektos;
                    //if there is only one, it is kept
                    if (A.size() == 1) {
                        eklektos = A.get(0);
                    } else {
                        //run the databaset search for every candidate author of the search results
                        int max = 0;
                        int deikths = 0;
                        for (int l = 0; l < A.size(); l++) {
                            String query = "SELECT count(*) as c from papers where authors like '%" + A.get(l).getId() + "%' and (authors like";
                            int count = 0;
                            //find collaborations in the databaset with that candidate id
                            for (String value : paper.values()) {
                                count++;
                                if (count != paper.size()) {
                                    query = query.concat(" '%" + value + "%' or authors like ");
                                } else {
                                    query = query.concat(" '%" + value + "%')");
                                }
                            }
                            rsbig = papers.executeQuery(query);
                            int c = 0;
                            while (rsbig.next()) {
                                c = rsbig.getInt("c");
                            }
                            //keep the candidate with the most collaborations
                            if (c > max) {
                                max = c;
                                deikths = l;
                            }
                        }
                        eklektos = A.get(deikths);
                    }
                    if (!map.containsKey(eklektos.getId())) {
                        try {
                            authors.executeUpdate("INSERT INTO `authors`(`author`,`id`,`affiliation`,`field`,`city`) VALUES (\"" + kena.get(i) + "---" + eklektos.getName() + "\",\"" + eklektos.getId() + "\",\"" + eklektos.getAffiliation() + "\",\"" + eklektos.getField() + "\",\"" + eklektos.getCity() + "\")");
                            map.put(eklektos.getId(), kena.get(i));
                        } catch (Exception exc) {
                            System.out.println(kena.get(i) + " out of the databas");
                        }
                    }
                    paper.put(eklektos.getId(), kena.get(i));
                } else {
                    //keep the ones that can not be identified to refrain from searching them again
                    skip.add(kena.get(i));
                }
            }
            String newAuthors = "";
            int p = 0;
            //assign the retrieved author ids to the paper
            for (Map.Entry<String, String> entry : paper.entrySet()) {
                p++;
                String key = entry.getKey();
                String value = entry.getValue();
                if (p == map.size()) {
                    newAuthors = newAuthors.concat(value + "---" + key);
                } else {
                    newAuthors = newAuthors.concat(value + "---" + key + ",");
                }
            }
            papers.executeUpdate("UPDATE papers SET authors=\"" + newAuthors + "\" where id=" + articleid);
            
        }
    }
}
