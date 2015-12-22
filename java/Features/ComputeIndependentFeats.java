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
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author Giwrgos
 */
public class ComputeIndependentFeats {

    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
       /*
        Computes the independent features.
        */
        
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String connectionURL = "";
        Connection connection = DriverManager.getConnection(connectionURL);
        Statement olaP = connection.createStatement();
        Statement olaL = connection.createStatement();
        ResultSet Pik = null;
        ResultSet Lik = null;
        for (int now = 1998; now <= 2005; now++) {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream("\\data\\yearly graphs and datasets\\" + now + "\\weightAggregated" + now + ".txt"), "utf-8"));
            
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("\\data\\yearly graphs and datasets\\" + now + "\\authDB" + now + ".csv"), "utf-8"));
            String strLine;
            HashSet<String> authors = new HashSet<String>();
            while ((strLine = br.readLine()) != null) {
                String[] s = strLine.split("\t");
                authors.add(s[1]);
                authors.add(s[0]);
            }
            for (String s : authors) {
                Pik = olaP.executeQuery("SELECT COUNT( * ) / ( 1 +" + now + " - year ) as y, count( * ) AS c,year FROM olabig1_2 WHERE authors LIKE '%" + s + "%' AND year <=" + now + " GROUP BY year");
                int Psum = 0;
                float Pyear = 0.0f;
                int Pnow = 0;
                while (Pik.next()) {
                    float y = Pik.getFloat("y");

                    Pyear += y;
                    int c = Pik.getInt("c");
                    Psum += c;
                    int year = Pik.getInt("year");
                    if (year == now) {
                        Pnow = c;
                    }
                }
                int Lsum = 0;
                float Lyear = 0.0f;
                int Lnow = 0;
                if (now <= 1998) {
                    Lik = olaL.executeQuery("SELECT SUM(`before`)/(1+" + now + "-year)+SUM(`1998`)/(1+" + now + "-1998) as y,SUM(`before`+`1998`) as c, SUM(`" + now + "`) as now from olabig1_2 where authors like '%" + s + "%' and year<=" + now);

                } else if (now == 1999) {

                    Lik = olaL.executeQuery("SELECT SUM(`before`)/(1+" + now + "-year)+SUM(`1998`)/(1+" + now + "-1998)+SUM(`1999`)/(1+" + now + "-1999) as y,SUM(`before`+`1998`+`1999`) as c, SUM(`" + now + "`) as now from olabig1_2 where authors like '%" + s + "%' and year<=" + now);

                } else if (now == 2000) {

                    Lik = olaL.executeQuery("SELECT SUM(`before`)/(1+" + now + "-year)+SUM(`1998`)/(1+" + now + "-1998)+SUM(`1999`)/(1+" + now + "-1999) + SUM(`2000`)/(1+" + now + "-2000)as y,SUM(`before`+`1998`+`1999`+`2000`) as c, SUM(`" + now + "`) as now from olabig1_2 where authors like '%" + s + "%' and year<=" + now);

                } else if (now == 2001) {

                    Lik = olaL.executeQuery("SELECT SUM(`before`)/(1+" + now + "-year)+SUM(`1998`)/(1+" + now + "-1998)+SUM(`1999`)/(1+" + now + "-1999) + SUM(`2000`)/(1+" + now + "-2000)+ SUM(`2001`)/(1+" + now + "-2001)as y,SUM(`before`+`1998`+`1999`+`2000`+`2001`) as c, SUM(`" + now + "`) as now from olabig1_2 where authors like '%" + s + "%' and year<=" + now);

                } else if (now == 2002) {

                    Lik = olaL.executeQuery("SELECT SUM(`before`)/(1+" + now + "-year)+SUM(`1998`)/(1+" + now + "-1998)+SUM(`1999`)/(1+" + now + "-1999) + SUM(`2000`)/(1+" + now + "-2000)+ SUM(`2001`)/(1+" + now + "-2001)+ SUM(`2002`)/(1+" + now + "-2002)as y,SUM(`before`+`1998`+`1999`+`2000`+`2001`+`2002`) as c, SUM(`" + now + "`) as now from olabig1_2 where authors like '%" + s + "%' and year<=" + now);

                } else if (now == 2003) {

                    Lik = olaL.executeQuery("SELECT SUM(`before`)/(1+" + now + "-year)+SUM(`1998`)/(1+" + now + "-1998)+SUM(`1999`)/(1+" + now + "-1999) + SUM(`2000`)/(1+" + now + "-2000)+ SUM(`2001`)/(1+" + now + "-2001)+ SUM(`2002`)/(1+" + now + "-2002)+ SUM(`2003`)/(1+" + now + "-2003) as y,SUM(`before`+`1998`+`1999`+`2000`+`2001`+`2002`+`2003`) as c, SUM(`" + now + "`) as now from olabig1_2 where authors like '%" + s + "%' and year<=" + now);

                } else if (now == 2004) {

                    Lik = olaL.executeQuery("SELECT SUM(`before`)/(1+" + now + "-year)+SUM(`1998`)/(1+" + now + "-1998)+SUM(`1999`)/(1+" + now + "-1999) + SUM(`2000`)/(1+" + now + "-2000)+ SUM(`2001`)/(1+" + now + "-2001)+ SUM(`2002`)/(1+" + now + "-2002)+ SUM(`2003`)/(1+" + now + "-2003)+ SUM(`2004`)/(1+" + now + "-2004) as y,SUM(`before`+`1998`+`1999`+`2000`+`2001`+`2002`+`2003`+`2004`) as c, SUM(`" + now + "`) as now from olabig1_2 where authors like '%" + s + "%' and year<=" + now);

                } else if (now == 2005) {

                    Lik = olaL.executeQuery("SELECT SUM(`before`)/(1+" + now + "-year)+SUM(`1998`)/(1+" + now + "-1998)+SUM(`1999`)/(1+" + now + "-1999) + SUM(`2000`)/(1+" + now + "-2000)+ SUM(`2001`)/(1+" + now + "-2001)+ SUM(`2002`)/(1+" + now + "-2002)+ SUM(`2003`)/(1+" + now + "-2003)+ SUM(`2004`)/(1+" + now + "-2004)+SUM(`2005`)/(1+" + now + "-2005) as y,SUM(`before`+`1998`+`1999`+`2000`+`2001`+`2002`+`2003`+`2004`+`2005`) as c, SUM(`" + now + "`) as now from olabig1_2 where authors like '%" + s + "%' and year<=" + now);

                } else if (now == 2006) {

                    Lik = olaL.executeQuery("SELECT SUM(`before`)/(1+" + now + "-year)+SUM(`1998`)/(1+" + now + "-1998)+SUM(`1999`)/(1+" + now + "-1999) + SUM(`2000`)/(1+" + now + "-2000)+ SUM(`2001`)/(1+" + now + "-2001)+ SUM(`2002`)/(1+" + now + "-2002)+ SUM(`2003`)/(1+" + now + "-2003)+ SUM(`2004`)/(1+" + now + "-2004)+SUM(`2005`)/(1+" + now + "-2005)+SUM(`2006`)/(1+" + now + "-2006) as y,SUM(`before`+`1998`+`1999`+`2000`+`2001`+`2002`+`2003`+`2004`+`2005`+`2006`) as c, SUM(`" + now + "`) as now from olabig1_2 where authors like '%" + s + "%' and year<=" + now);

                } else if (now == 2007) {

                    Lik = olaL.executeQuery("SELECT SUM(`before`)/(1+" + now + "-year)+SUM(`1998`)/(1+" + now + "-1998)+SUM(`1999`)/(1+" + now + "-1999) + SUM(`2000`)/(1+" + now + "-2000)+ SUM(`2001`)/(1+" + now + "-2001)+ SUM(`2002`)/(1+" + now + "-2002)+ SUM(`2003`)/(1+" + now + "-2003)+ SUM(`2004`)/(1+" + now + "-2004)+SUM(`2005`)/(1+" + now + "-2005)+SUM(`2006`)/(1+" + now + "-2006)+SUM(`2007`)/(1+" + now + "-2007) as y,SUM(`before`+`1998`+`1999`+`2000`+`2001`+`2002`+`2003`+`2004`+`2005`+`2006`+`2007`) as c, SUM(`" + now + "`) as now from olabig1_2 where authors like '%" + s + "%' and year<=" + now);

                } else if (now == 2008) {

                    Lik = olaL.executeQuery("SELECT SUM(`before`)/(1+" + now + "-year)+SUM(`1998`)/(1+" + now + "-1998)+SUM(`1999`)/(1+" + now + "-1999) + SUM(`2000`)/(1+" + now + "-2000)+ SUM(`2001`)/(1+" + now + "-2001)+ SUM(`2002`)/(1+" + now + "-2002)+ SUM(`2003`)/(1+" + now + "-2003)+ SUM(`2004`)/(1+" + now + "-2004)+SUM(`2005`)/(1+" + now + "-2005)+SUM(`2006`)/(1+" + now + "-2006)+SUM(`2007`)/(1+" + now + "-2007)+ SUM(`2008`)/(1+" + now + "-2008) as y,SUM(`before`+`1998`+`1999`+`2000`+`2001`+`2002`+`2003`+`2004`+`2005`+`2006`+`2007`+`2008`) as c, SUM(`" + now + "`) as now from olabig1_2 where authors like '%" + s + "%' and year<=" + now);

                }
                while (Lik.next()) {
                    Lyear = Lik.getFloat("y");
                    Lsum = Lik.getInt("c");
                    Lnow = Lik.getInt("now");
                }

                writer.write(s + "," + Psum + "," + Pyear + "," + Pnow + "," + Lsum + "," + Lyear + "," + Lnow);
                writer.flush();
                writer.newLine();
            }
        }
    }
}
