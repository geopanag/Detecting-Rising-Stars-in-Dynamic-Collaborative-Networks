/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataProcessing;

import static com.google.common.math.DoubleMath.mean;
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
import org.apache.commons.math3.stat.descriptive.rank.Percentile;

/**
 *
 * @author Giwrgos
 */
public class EvaluateClusterValidity {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, SQLException, InterruptedException, IOException, ClassNotFoundException {
        // first create clusters and label them

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String connectionURL = "";
        Connection connection = DriverManager.getConnection(connectionURL);

        Statement all_papers = connection.createStatement();

        for (int cluster = 1; cluster < 8; cluster++) {
            System.out.println("CLUSTER :" + cluster);

            ArrayList<Integer> current_citations = new ArrayList<Integer>();
            ArrayList<Double> normalized_productivity = new ArrayList<Double>();
            ArrayList<Integer> productivity = new ArrayList<Integer>();
            ArrayList<Integer> degree = new ArrayList<Integer>();

            BufferedReader reader = new BufferedReader(new FileReader("\\data\\final\\ptuxiakh_zibragala\\author_clusters.csv"));

            String line = null;
            // int counter = 0;
            while ((line = reader.readLine()) != null) {
                String[] h = line.split(",");//take id and cluster
                if (Integer.parseInt(h[1]) == cluster) {
                    ResultSet papers = all_papers.executeQuery("SELECT `year`,`authors`,`2013` FROM papers where authors like '%" + h[0] + "%'"); //all publications of author h[0]

                    ArrayList<Double> years = new ArrayList<Double>();
                    HashSet<String> hs = new HashSet<String>();//set of h[0] coauthors
                    int cits = 0;
                    while (papers.next()) {
                        years.add((double) (2015 - papers.getInt("year")));
                        hs.addAll(new HashSet<String>(Arrays.asList(papers.getString("authors").split(","))));
                        cits += papers.getInt("2013");
                    }

                    degree.add(hs.size());//number of author's coauthors
                    productivity.add(years.size());//number of papers 
                    double normalized_prod = 0.0;
                    for (int i = 0; i < years.size(); i++) {
                        normalized_prod += 1 / (double) (2015 - years.get(i));//normalize each paper with its oldness
                    }
                    normalized_productivity.add(normalized_prod);
                    current_citations.add(cits);//
                }
            }

            Percentile p = new Percentile();

            p.setData(Doubles.toArray(productivity));
            double productivity25 = p.evaluate(25);
            double productivity50 = p.evaluate(50);
            double productivity75 = p.evaluate(75);
            double productivity90 = p.evaluate(90);
            double sum = 0;
            for (double d : productivity) {
                sum += d;
            }
            double productivityAVG = sum / (double) productivity.size();

            p.setData(Doubles.toArray(normalized_productivity));
            double normalized_productivity25 = p.evaluate(25);
            double normalized_productivity50 = p.evaluate(50);
            double normalized_productivity75 = p.evaluate(75);
            double normalized_productivity90 = p.evaluate(90);
            sum = 0;
            for (double d : normalized_productivity) {
                sum += d;
            }
            double normalized_productivityAVG = sum / normalized_productivity.size();

            p.setData(Doubles.toArray(current_citations));
            double current_citations25 = p.evaluate(25);
            double current_citations50 = p.evaluate(50);
            double current_citations75 = p.evaluate(75);
            double current_citations90 = p.evaluate(90);
            sum = 0;
            for (double d : current_citations) {
                sum += d;
            }
            double current_citationsAVG = sum / current_citations.size();

            p.setData(Doubles.toArray(degree));
            double degree25 = p.evaluate(25);
            double degree50 = p.evaluate(50);
            double degree75 = p.evaluate(75);
            double degree90 = p.evaluate(90);

            sum = 0;
            for (double d : degree) {
                sum += d;
            }
            double degreeAVG = sum / (double) degree.size();

            PrintWriter writer = new PrintWriter("\\data\\evaluation\\validity\\validity" + cluster + ".csv", "UTF-8");
            writer.println("normalized_productivity25,normalized_productivity50,normalized_productivity75,normalized_productivity90,normalized_productivityAVG,current_citations25,current_citations50,current_citations75,current_citations90,current_citationsAVG,degree25,degree50,degree75,degree90,degreeAVG,productivity25,productivity50,productivity75,productivity90,productivityAVG");
            writer.println(normalized_productivity25 + "," + normalized_productivity50 + "," + normalized_productivity75 + "," + normalized_productivity90 + "," + normalized_productivityAVG + "," + current_citations25 + "," + current_citations50 + "," + current_citations75 + "," + current_citations90 + "," + current_citationsAVG + "," + degree25 + "," + degree50 + "," + degree75 + "," + degree90 + "," + degreeAVG + "," + productivity25 + "," + productivity50 + "," + productivity75 + "," + productivity90 + "," + productivityAVG);
            writer.close();
        }
    }
}
