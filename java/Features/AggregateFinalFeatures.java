/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Features;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 *
 * @author Giwrgos
 */
public class AggregateFinalFeatures {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream("\\data\\final\\final.csv")));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("\\data\\final\\aggregated.csv"), "utf-8"));
        String strLine;
        br.readLine();
        writer.write("id,paper_sum,paper_norm,paper_now,cit_sum,cit_norm,cit_now,eigenvector,degree_norm,edges_weight,wpn_weight,wpn_clique,spn_weight,spn_clique");
        writer.flush();
        writer.newLine();
        while ((strLine = br.readLine()) != null) {
            String[] s = strLine.split(",");
            String n = s[0];
            for (int i = 1; i < s.length; i = i + 5) {
                Double val = (Double.parseDouble(s[i]) + Double.parseDouble(s[i + 1]) + Double.parseDouble(s[i + 2]) + Double.parseDouble(s[i + 3]))*Double.parseDouble(s[i + 4]);
                n = n + "," + val;
            }
            writer.write(n);
            writer.newLine();
            writer.flush();
        }

    }
}
