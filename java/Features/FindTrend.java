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
import org.apache.commons.math3.stat.regression.SimpleRegression;

/**
 *
 * @author Giwrgos
 */
public class FindTrend {

    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream("\\data\\yearly graphs and datasets\\2005\\changes2005.csv")));
        String strLine;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String connectionURL = "";
        
        Connection connection = DriverManager.getConnection(connectionURL);
        Statement ola = connection.createStatement();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("\\data\\finals\\trends.csv"), "utf-8"));
        ResultSet val = null;
        ResultSet papers = null;
        br.readLine();//step headers
        while ((strLine = br.readLine()) != null) {
            String name = strLine.split(",")[0];
  
            val = ola.executeQuery("SELECT count(*)as pap,sum(`1998`+`1999`+`2000`+`2001`+`2002`+`2003`+`2004`+`2005`) as cit,year FROM `olabig1_2` WHERE authors like '%" + name + "%\' and year<=2005 and year>=1998 group by year");
  
            SimpleRegression trendPap = new SimpleRegression();
            SimpleRegression trendCit = new SimpleRegression();
            while (val.next()) {
                trendPap.addData(val.getInt("year"), val.getInt("pap"));
                trendCit.addData(val.getInt("year"), val.getInt("cit"));
            }
            double p = Math.atan(trendPap.getSlope());//atan of the slope gives the angle in rads
            if (Double.isNaN(p)) {
                p = 0;
            }
            double c = Math.atan(trendCit.getSlope());//the same applies for citation
            if (Double.isNaN(c)) {
                c = 0;
            }
            p = p + Math.PI / 4;//refrain 0
            c = c + Math.PI / 4;
            writer.write(name+","+p+","+c);
            writer.newLine();
            writer.flush();
        }

    }
}
