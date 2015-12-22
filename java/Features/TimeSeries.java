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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Giwrgos
 */
public class TimeSeries {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, Exception {

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String connectionURL = "";
        Connection connection = DriverManager.getConnection(connectionURL);
        Statement ola = connection.createStatement();
        ResultSet au = null;
        //keep the authors active from 1998 to 2005
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
        String strLine;
        HashMap<String, String> timeline = new HashMap<String, String>();

        BufferedReader brFeat = new BufferedReader(new InputStreamReader(
                new FileInputStream("")));
        while ((strLine = brFeat.readLine()) != null) {
            String[] line = strLine.split(",");
            String name = line[0];
            if (authors.contains(name)) {
                float feat = Float.parseFloat(line[6]);
                timeline.put(name, Float.toString(feat));
            }
        }
        for (int i = 1999; i < 2006; i++) {
            brFeat = new BufferedReader(new InputStreamReader(
                    new FileInputStream("\\data\\yearly graphs and datasets\\"+i+"\\features" + i + ".csv")));
            while ((strLine = brFeat.readLine()) != null) {
                String[] line = strLine.split(",");
                String name = line[0];
                if (authors.contains(name)) {
                    //keep the new value for an existing author
                    float feat = Float.parseFloat(line[6]);
                    if (timeline.containsKey(name)) {
                        timeline.put(name, timeline.get(name) + "," + feat);
                    } else {
                    //initilize a time series for the new author, in such a way to leave uneffected the dynamic time warping algorithm
                        String series = "NA";
                        for (int j = 0; j < i - 1999; j++) {
                            series = series.concat(",NA");
                        }
                        timeline.put(name, series.concat("," + feat));
                    }
                }
            }
        }      
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream("\\data\\finals\\timeSeries.csv"), "utf-8"));

        for (Map.Entry<String, String> s : timeline.entrySet()) {
            writer.write(s.getKey()+","+s.getValue());
            writer.newLine();
            writer.flush();
        }
    }

}

