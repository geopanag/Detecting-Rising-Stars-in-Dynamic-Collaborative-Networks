/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataProcessing;

import com.google.common.primitives.Doubles;
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
import java.util.HashSet;
import java.util.List;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;

/**
 *
 * @author Giwrgos
 */
public class EvaluateClusterCoherence {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, SQLException, InterruptedException, IOException, ClassNotFoundException {
      
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String connectionURL = "";
        Connection connection = DriverManager.getConnection(connectionURL);

        Statement getCitsPubs = connection.createStatement();
        int j = 0;
        //for every year, create a new dataset, one row for each cluster, with citations, publications and coauthors
    
        ArrayList<PrintWriter> wr06 = new ArrayList<PrintWriter>();
        ArrayList<PrintWriter> wr07 = new ArrayList<PrintWriter>();
        ArrayList<PrintWriter> wr08 = new ArrayList<PrintWriter>();
        ArrayList<PrintWriter> wr09 = new ArrayList<PrintWriter>();
        ArrayList<PrintWriter> wr10 = new ArrayList<PrintWriter>();
        ArrayList<PrintWriter> wr11 = new ArrayList<PrintWriter>();
        ArrayList<PrintWriter> wr12 = new ArrayList<PrintWriter>();
        ArrayList<PrintWriter> wr13 = new ArrayList<PrintWriter>();
        for (int i = 1; i <=7; i++) {
            wr06.add(new PrintWriter("\\data\\evaluation\\coherence\\2006\\cluster" + i + ".csv", "UTF-8"));
            wr06.get(i-1).println("id,publications,citations,coauthors");
            wr06.get(i-1).flush();
            wr07.add(new PrintWriter("\\data\\evaluation\\coherence\\2007\\cluster" + i + ".csv", "UTF-8"));
            wr07.get(i-1).println("id,publications,citations,coauthors");
            wr07.get(i-1).flush();
            wr08.add(new PrintWriter("\\data\\evaluation\\coherence\\2008\\cluster" + i + ".csv", "UTF-8"));
            wr08.get(i-1).println("id,publications,citations,coauthors");
            wr08.get(i-1).flush();
            wr09.add(new PrintWriter("\\data\\evaluation\\coherence\\2009\\cluster" + i + ".csv", "UTF-8"));
            wr09.get(i-1).println("id,publications,citations,coauthors");
            wr09.get(i-1).flush();
            wr10.add(new PrintWriter("\\data\\evaluation\\coherence\\2010\\cluster" + i + ".csv", "UTF-8"));
            wr10.get(i-1).println("id,publications,citations,coauthors");
            wr10.get(i-1).flush();
            wr11.add(new PrintWriter("\\data\\evaluation\\coherence\\2011\\cluster" + i + ".csv", "UTF-8"));
            wr11.get(i-1).println("id,publications,citations,coauthors");
            wr11.get(i-1).flush();
            wr12.add(new PrintWriter("C\\data\\evaluation\\coherence\\2012\\cluster" + i + ".csv", "UTF-8"));
            wr12.get(i-1).println("id,publications,citations,coauthors");
            wr12.get(i-1).flush();
            wr13.add(new PrintWriter("\\data\\evaluation\\coherence\\2013\\cluster" + i + ".csv", "UTF-8"));
            wr13.get(i-1).println("id,publications,citations,coauthors");
            wr13.get(i-1).flush();
        }

        BufferedReader reader = new BufferedReader(new FileReader("\\data\\finals\\author_clusters.csv"));
        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] h = line.split(",");//take id and cluster
            j++;
            System.out.println(h[0] + "," + j);
                ResultSet rs1 = getCitsPubs.executeQuery("SELECT * FROM papers where authors like '%" + h[0] + "%'");
            int cits06 = 0;
            int cits07 = 0;
            int cits08 = 0;
            int cits09 = 0;
            int cits10 = 0;
            int cits11 = 0;
            int cits12 = 0;
            int cits13 = 0;
            int c06 = 0;
            int c07 = 0;
            int c08 = 0;
            int c09 = 0;
            int c10 = 0;
            int c11 = 0;
            int c12 = 0;
            int c13 = 0;
            HashSet<String> authors06 = new HashSet<String>();
            HashSet<String> authors07 = new HashSet<String>();
            HashSet<String> authors08 = new HashSet<String>();
            HashSet<String> authors09 = new HashSet<String>();
            HashSet<String> authors10 = new HashSet<String>();
            HashSet<String> authors11 = new HashSet<String>();
            HashSet<String> authors12 = new HashSet<String>();
            HashSet<String> authors13 = new HashSet<String>();
            while (rs1.next()) {
                cits06 += rs1.getInt("2006");
                cits07 += rs1.getInt("2007");
                cits08 += rs1.getInt("2008");
                cits09 += rs1.getInt("2009");
                cits10 += rs1.getInt("2010");
                cits11 += rs1.getInt("2011");
                cits12 += rs1.getInt("2012");
                cits13 += rs1.getInt("2013");
                int year = rs1.getInt("year");
                if (year == 2006) {
                    authors06.addAll(new HashSet<String>(Arrays.asList(rs1.getString("authors").split(","))));
                    c06++;
                } else if (year == 2007) {
                    authors07.addAll(new HashSet<String>(Arrays.asList(rs1.getString("authors").split(","))));
                    c07++;
                } else if (year == 2008) {
                    authors08.addAll(new HashSet<String>(Arrays.asList(rs1.getString("authors").split(","))));
                    c08++;
                } else if (year == 2009) {
                    authors09.addAll(new HashSet<String>(Arrays.asList(rs1.getString("authors").split(","))));
                    c09++;
                } else if (year == 2010) {
                    authors10.addAll(new HashSet<String>(Arrays.asList(rs1.getString("authors").split(","))));
                    c10++;
                } else if (year == 2011) {
                    authors11.addAll(new HashSet<String>(Arrays.asList(rs1.getString("authors").split(","))));
                    c11++;
                } else if (year == 2012) {
                    authors12.addAll(new HashSet<String>(Arrays.asList(rs1.getString("authors").split(","))));
                    c12++;
                } else if (year == 2013) {
                    authors13.addAll(new HashSet<String>(Arrays.asList(rs1.getString("authors").split(","))));
                    c13++;
                }
            }
            int cluster=Integer.parseInt(h[1])-1;
            wr06.get(cluster).println(h[0]+","+c06+","+cits06+","+authors06.size());
            wr06.get(cluster).flush();
            wr07.get(cluster).println(h[0]+","+c07+","+cits07+","+authors07.size());
            wr07.get(cluster).flush();
            wr08.get(cluster).println(h[0]+","+c08+","+cits08+","+authors08.size());
            wr08.get(cluster).flush();
            wr09.get(cluster).println(h[0]+","+c09+","+cits09+","+authors09.size());
            wr09.get(cluster).flush();
            wr10.get(cluster).println(h[0]+","+c10+","+cits10+","+authors10.size());
            wr10.get(cluster).flush();
            wr11.get(cluster).println(h[0]+","+c11+","+cits11+","+authors11.size());
            wr11.get(cluster).flush();
            wr12.get(cluster).println(h[0]+","+c12+","+cits12+","+authors12.size());
            wr12.get(cluster).flush();
            wr13.get(cluster).println(h[0]+","+c13+","+cits13+","+authors13.size());
            wr13.get(cluster).flush();
  
        }
        for (int i = 1; i <=7; i++) {
            wr06.get(i-1).close();
            wr07.get(i-1).close();
            wr08.get(i-1).close();
            wr09.get(i-1).close();
            wr10.get(i-1).close();
            wr11.get(i-1).close();
            wr12.get(i-1).close();
            wr13.get(i-1).close();
        }
 
    }
}
