/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graph;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 *
 * @author Giwrgos
 */
public class AggregateEdges {

    public static void main(String[] args) {
        try {
            int i = 0;
            int now = 2001;
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream("\\data\\yearly graphs and datasets\\" + now + "\\PowerGraph\\" + now + "\\sorted" + now + ".txt")));
            BufferedWriter writerW = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("\\data\\yearly graphs and datasets\\" + now + "\\PowerGraph\\" + now + "\\weightAggregated" + now + ".edg"), "utf-8"));

            BufferedWriter writerS = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("\\data\\yearly graphs and datasets\\" + now + "\\PowerGraph\\" + now + "\\simpleAggregated" + now + ".edg"), "utf-8"));
            //read line by line
            String strLine;
            String[] s = br.readLine().split("\t");
            String id1 = s[1];
            String id2 = s[2];
            int cit = Integer.parseInt(s[3]);
            int year = Integer.parseInt(s[4]);
            int coos = Integer.parseInt(s[5]);
            double index = ((0.7 * cit + 0.3) / coos) / (1 + now - year);
            int CoAuthorships = 1;
            while ((strLine = br.readLine()) != null) {
                s = strLine.split("\t");
                //if the ne line is between the same authors, aggregate it
                if (s[1].equalsIgnoreCase(id1) && s[2].equalsIgnoreCase(id2)) {
                    double a = ((0.7 * Integer.parseInt(s[3]) + 0.3) / Integer.parseInt(s[5])) / (1 + now - Integer.parseInt(s[4]));//+1 gt now=year
                    index += a;
                    //keep the number of coauthorships
                    CoAuthorships++;
                    //when a new collaboration is read, store the old one and begin calculating the weight of new one
                } else {
                    float weight = (float) index;
                    //the weighted graph
                    String wr = "EDGE" + "\t" + id1 + "\t" + id2 + "\t" + weight;
                    writerW.write(wr);
                    writerW.flush();
                    writerW.newLine();
                    //the simple graph
                    wr = "EDGE" + "\t" + id1 + "\t" + id2 + "\t" + CoAuthorships;
                    writerS.write(wr);
                    writerS.flush();
                    writerS.newLine();
                    //ftiaxe thn nea
                    id1 = s[1];
                    id2 = s[2];
                    cit = Integer.parseInt(s[3]);
                    year = Integer.parseInt(s[4]);
                    coos = Integer.parseInt(s[5]);
                    //start new edge weight index
                    index = ((0.7 * cit + 0.3) / coos) / (1 + now - year);
                    CoAuthorships = 1;
                }
            }
            writerW.close();
            writerS.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
