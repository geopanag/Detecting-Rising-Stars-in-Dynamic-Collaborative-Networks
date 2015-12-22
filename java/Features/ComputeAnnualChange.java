/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Features;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Giwrgos
 */
public class ComputeAnnualChange {

    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
       /*
        The change datasets are the direct difference between the features of a year and its previous one.
        The features datasets are derived from AnnualFeatures.R
        */
        
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String connectionURL = "";
        Connection connection = DriverManager.getConnection(connectionURL);
        Statement ola = connection.createStatement();
        ResultSet au = null;
        au = ola.executeQuery("SELECT authors FROM papers where year<=2005 and year>=1998");
        Set<String> authors = new HashSet<String>();
        while (au.next()) {
            String[] s = au.getString("authors").split(",");
            for (String k : s) {
                if (k.contains("---")) {
                    String[] h = k.split("---");
                    if (h.length > 0 && !h.equals(null)) {
                        if (h[1].matches(".*\\d+.*")) {
                            authors.add(h[1]);
                        } else if (h[0].matches(".*\\d+.*")) {
                            authors.add(h[0]);
                        }
                    }
                }
            }
        }
            for (int i = 1999; i <=2005; i++) {
                BufferedReader brFeatNew = new BufferedReader(new InputStreamReader(
                        new FileInputStream("\\data\\yearly graphs and datasets\\"+i+"\\features" + i + ".csv")));

                BufferedReader brFeatOld = new BufferedReader(new InputStreamReader(
                        new FileInputStream("\\data\\yearly graphs and datasets\\"+i+"\\features" + (i - 1) + ".csv")));
                HashMap<String, String> oldFeat = new HashMap<String, String>();
                String strLine;
                String header = brFeatOld.readLine();
                //keep the old features
                while ((strLine = brFeatOld.readLine()) != null) {
                    oldFeat.put(strLine.split(",")[0], strLine);
                }
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream("\\data\\yearly graphs and datasets\\"+i+"\\change" + i + ".csv"), "utf-8"));

                writer.write(header);
                writer.newLine();
                writer.flush();
                brFeatNew.readLine();//step header
                while ((strLine = brFeatNew.readLine()) != null) {
                    String name = strLine.split(",")[0];
                    //check if the author has written in between 1998-2005
                    if(!authors.contains(name)){
                        continue;
                    }
                    //for every new author, check whether it exists in the old one
                    if (oldFeat.containsKey(name)) {
                        //if it does, keep the change between the old and the new
                        String[] n = strLine.split(",");
                        String[] o = oldFeat.get(name).split(",");
                        String newRow = name + ",";
                        for (int k = 1; k < n.length; k++) {
                            double num = Double.parseDouble(n[k].replace("\"", "")) - Double.parseDouble(o[k].replace("\"", ""));
                            newRow = newRow + num + ",";
                        }
                        newRow = newRow.substring(0, newRow.length() - 1);
                        writer.write(newRow);
                        writer.newLine();
                        writer.flush();
                    } else {
                        //if it is first time seen, store the line, as a change from 0
                        writer.write(strLine);
                        writer.newLine();
                        writer.flush();
                    }
                }
                writer.close();
            }
        }

    }
