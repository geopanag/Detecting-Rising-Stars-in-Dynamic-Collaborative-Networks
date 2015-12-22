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
public class CrawlCollabFilterUnidentified {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, ParserConfigurationException, IOException, XPatherException, IllegalAccessException, InstantiationException {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String connectionURL = "jdbc:mysql://127.0.0.1:3306/ptuxiakh?useUnicode=yes&characterEncoding=UTF-8&user=root&password=";
        Connection connection = DriverManager.getConnection(connectionURL);
        Statement papersQuery = connection.createStatement();
        Statement papers = connection.createStatement();
        Statement nodes = connection.createStatement();
        ResultSet rspapers = null;
        ResultSet rsnodes = null;
        ResultSet rsbig = null;
        rsnodes = nodes.executeQuery("SELECT * FROM nodes");
        Map<String, String> map = new HashMap<String, String>();
        Set<String> skip = new HashSet<String>();
        while (rsnodes.next()) {
            map.put(rsnodes.getString("id"), rsnodes.getString("author").split("---")[0]);
        }
        //papers with unidentified authors
        rspapers = papersQuery.executeQuery("SELECT *FROM `papers` WHERE authors not REGEXP '[0-9]' and authors!=''");
        while (rspapers.next()) {

            Map<String, String> lista = new HashMap<String, String>();
            String authors = rspapers.getString("authors").replace(", ", ",");
            int articleid = rspapers.getInt("id");
            String title = rspapers.getString("title");
            String[] auth = authors.split(",");
            ArrayList<String> kena = new ArrayList();

            //search each author in the database, and if one and only one exists, tally him to that id
            for (int i = 0; i < auth.length; i++) {
                ArrayList<String> small = new ArrayList();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (auth[i].equalsIgnoreCase(entry.getValue())) {
                        small.add(entry.getKey());
                    }
                }
                if (small.size() == 1) {
                    lista.put(small.get(0), auth[i]);
                } else {
                    kena.add(auth[i]);
                }
            }

            boolean adeia = false;
            if (lista.isEmpty()) {
                adeia = true;
            }
            for (int j = 0; j < kena.size(); j++) {
                if (skip.contains(kena.get(j))) {
                    continue;
                }
                //assign the first author id from scopus
                if (adeia) {
                    String[] mal = kena.get(j).split(" ");
                    Author A;
                    if (mal.length >= 3) {
                        String s = "";
                        int k = 0;
                        for (k = 0; k < mal.length - 1; k++) {
                            s = s + "+" + mal[k];
                        }
                         A = SelectiveAuthorSearch.fetch(s, mal[k]).get(0);
                    } else {
                        if (mal.length > 1) {
                            A = SelectiveAuthorSearch.fetch(mal[0], mal[1]).get(0);
                        } else {
                            A = null;
                        }
                    }
                    if (A != null) {
                        if (!map.containsKey(A.getId())) {
                            try {
                                nodes.executeUpdate("INSERT INTO `nodes`(`author`,`id`,`affiliation`,`field`,`city`) VALUES (\"" + kena.get(j) + "---" + A.getName() + "\",\"" + A.getId() + "\",\"" + A.getAffiliation() + "\",\"" + A.getField() + "\",\"" + A.getCity() + "\")");
                                map.put(A.getId(), kena.get(j));
                            } catch (Exception exc) {
                                System.out.println(kena.get(j) + " not in the database");
                            }
                        }
                        lista.put(A.getId(), kena.get(j));
                    }
                } else {//if there are many candidate ids, run collaborative filtering
                    String[] mal = kena.get(j).split(" ");
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
                        if (A.size() == 1) {
                            eklektos = A.get(0);
                        } else {
                            int max = 0;
                            int deikths = 0;
                            for (int l = 0; l < A.size(); l++) {
                                String query = "SELECT count(*) as c from papers where authors like '%" + A.get(l).getId() + "%' and (authors like";
                                int count = 0;
                                for (String value : lista.values()) {
                                    count++;
                                    if (count != lista.size()) {
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
                                if (c > max) {
                                    max = c;
                                    deikths = l;
                                }
                            }
                            eklektos = A.get(deikths);
                        }
                        if (!map.containsKey(eklektos.getId())) {
                            try {
                                nodes.executeUpdate("INSERT INTO `nodes`(`author`,`id`,`affiliation`,`field`,`city`) VALUES (\"" + kena.get(j) + "---" + eklektos.getName() + "\",\"" + eklektos.getId() + "\",\"" + eklektos.getAffiliation() + "\",\"" + eklektos.getField() + "\",\"" + eklektos.getCity() + "\")");
                                map.put(eklektos.getId(), kena.get(j));
                            } catch (Exception exc) {
                                System.out.println(kena.get(j) + " not in the database");
                            }
                        }
                        lista.put(kena.get(j), eklektos.getId());
                    }else {
                    skip.add(kena.get(j));
                }
                } 
            }
            String newAuthors = "";
            int p = 0;
            for (Map.Entry<String, String> entry : lista.entrySet()) {
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
