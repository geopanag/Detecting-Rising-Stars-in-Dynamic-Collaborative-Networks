/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataProcessing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Giwrgos
 */
public class ExamineJournalDistribution {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, SQLException, InterruptedException, IOException, ClassNotFoundException {
        // first create clusters and label them

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String connectionURL = "";
        Connection connection = DriverManager.getConnection(connectionURL);

        Statement st1 = connection.createStatement();

        // one hashmap with journal:freq for each cluster
        ArrayList<HashMap<String, String>> paper_journal = new ArrayList<HashMap<String, String>>();
        //initialize it 
        for (int i = 0; i <= 6; i++) {
            paper_journal.add(new HashMap<String, String>());
        }

        BufferedReader reader = new BufferedReader(new FileReader("\\data\\finals\\author_clusters.csv"));

        String line = null;
        // int counter = 0;
        while ((line = reader.readLine()) != null) {
            String[] h = line.split(",");//take id and cluster
            // counter += 1;
            System.out.println(h[0]);
            int cluster = Integer.parseInt(h[1]) - 1;
            ResultSet journals = st1.executeQuery("SELECT title,journal FROM papers where authors like '%" + h[0] + "%' and year=2005"); //all publications of author h[0]

            while (journals.next()) {
                String p = journals.getString("title");
                String j = journals.getString("journal");
                //int count = 1;//journals.getInt("c");
                if (!paper_journal.get(cluster).keySet().contains(p)) {
                    //journal_freq.get(cluster).put(j, journal_freq.get(cluster).get(j) + count);
                    paper_journal.get(cluster).put(p, j);
                }
            }
        }

        for (int i = 0; i < paper_journal.size(); i++) {

            //reverse from paper:journal, to journal:frequency
            Iterator it = paper_journal.get(i).entrySet().iterator();
            HashMap<String, Integer> journal_freq = new HashMap<String, Integer>();

            while (it.hasNext()) {
                Entry<String, String> pair = (Entry) it.next();
                String j = pair.getValue();
                if (journal_freq.containsKey(j)) {
                    journal_freq.put(j, journal_freq.get(j) + 1);
                } else {
                    journal_freq.put(j, 1);
                }
            }

            PrintWriter writer = new PrintWriter("\\data\\evaluation\\journal_distribution\\cluster" + (i + 1) + ".csv", "UTF-8");
            writer.println("journal;frequency");

            Iterator it2 = journal_freq.entrySet().iterator();
            while (it2.hasNext()) {
                Entry<String, Integer> pair = (Entry) it2.next();
                writer.println(pair.getKey() + ";" + pair.getValue());
            }
            writer.close();
        }
    }
}
