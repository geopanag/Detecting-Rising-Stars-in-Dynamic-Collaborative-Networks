/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graph;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Giwrgos
 */
public class Multigraphs {

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, SQLException, InterruptedException, IOException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String connectionURL = "";
        Connection connection = DriverManager.getConnection(connectionURL);
        Statement ola = connection.createStatement();//CrawledPapwers
        Statement nodes = connection.createStatement();
        ResultSet rsola = null;
        ResultSet rsnodes = null;
        Set<String> authors = new HashSet<String>();
        Set<String> miss = new HashSet<String>();
        //o sunergatikos grafos
        //UndirectedGraph g = new UndirectedSparseGraph();
        rsnodes = nodes.executeQuery("SELECT id FROM `nodes`");
        while (rsnodes.next()) {
            authors.add(rsnodes.getString("id"));

        }
    
        Writer writer98 = null;
        Writer writer99 = null;
        Writer writer00 = null;
        Writer writer01 = null;
        Writer writer02 = null;
        Writer writer03 = null;
        Writer writer04 = null;
        Writer writer05 = null;
        Writer writer06 = null;
        Writer writer07 = null;
        Writer writer08 = null;
        try {
            writer98 = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("\\data\\yearly graphs and datasets\\1998\\graph1998.txt"), "utf-8"));
            writer99 = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("\\data\\yearly graphs and datasets\\1999\\graph1999.txt"), "utf-8"));
            writer00 = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("\\data\\yearly graphs and datasets\\2000\\graph2000.txt"), "utf-8"));
            writer01 = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("\\data\\yearly graphs and datasets\\2001\\graph2001.txt"), "utf-8"));
            writer02 = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("\\data\\yearly graphs and datasets\\2002\\graph2002.txt"), "utf-8"));
            writer03 = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("\\data\\yearly graphs and datasets\\2003\\graph2003.txt"), "utf-8"));
            writer04 = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("\\data\\yearly graphs and datasets\\2004\\graph2004.txt"), "utf-8"));
            writer05 = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("\\data\\yearly graphs and datasets\\2005\\graph2005.txt"), "utf-8"));
            writer06 = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("\\data\\yearly graphs and datasets\\2006\\graph2006.txt"), "utf-8"));
            writer07 = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("\\data\\yearly graphs and datasets\\2007\\graph2007.txt"), "utf-8"));
            writer08 = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("\\data\\yearly graphs and datasets\\2008\\graph2008.txt"), "utf-8"));
             ///////////////////////////////////////////////////////////////1998
            rsola = ola.executeQuery("SELECT id,year,authors,`before`+`1998` as a98 ,`1999` as a99,`2000` as a00,`2001` as a01,`2002` as a02,`2003` as a03,`2004` as a04,`2005` as a05,`2006` as a06,`2007` as a07,`2008` as a08 from olabig1_2 where year<=1998 and (authors like '%---%' or authors like '%.-%')");
            while (rsola.next()) {
                
                String[] auth = rsola.getString("authors").split(",");
                String id = rsola.getString("id");
                int year = rsola.getInt("year");
                
                int a98 = rsola.getInt("a98");
                int a99 = rsola.getInt("a99");
                int a00 = rsola.getInt("a00");
                int a01 = rsola.getInt("a01");
                int a02 = rsola.getInt("a02");
                int a03 = rsola.getInt("a03");
                int a04 = rsola.getInt("a04");
                int a05 = rsola.getInt("a05");
                int a06 = rsola.getInt("a06");
                int a07 = rsola.getInt("a07");
                int a08 = rsola.getInt("a08");
                System.out.println("1998 " + id);
                ArrayList<String> paper = new ArrayList();
                //keep only a list of ids
                for (int i = 0; i < auth.length; i++) {
                    if (auth[i].contains("---") && auth[i].matches(".*\\d.*")) {
                        String au = auth[i].split("---")[1];
                        
                        //check if the string has only numbers
                        au = au.replace("-", "");
                        if (isNumeric(au)) {
                            paper.add(au);
                            //keep an uncrawled author
                            if (!authors.contains(au)) {
                                miss.add(au);
                            }
                        }
                    } else if (auth[i].contains(".-") && auth[i].matches(".*\\d.*")) {
                        String au = auth[i].split("\\.-")[1];
                        au = au.replace("-", "");
                        if (isNumeric(au)) {
                            paper.add(au);
                            if (!authors.contains(au)) {
                                miss.add(au);
                            }
                        }
                    }
                }
                //draw the undirected edges
                for (int i = 0; i < paper.size(); i++) {
                    long m = Long.parseLong(paper.get(i), 10);
                    for (int j = i + 1; j < paper.size(); j++) {
                        long l = Long.parseLong(paper.get(j), 10);
                        //make sure every undirected edge will have the smallest id in front
                        //to facilitate easier and faster sorting
                        //and refrain from confusion of two same edges, because the ids are reversed
                        if (m < l) {
                            int a=a98;
                            writer98.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer98.write(System.getProperty("line.separator"));
                            a=a98 + a99;
                            writer99.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer99.write(System.getProperty("line.separator"));
                            a=a98 + a99 + a00;
                            writer00.write("EDGE\t" + m + "\t" + l + "\t" + a  + "\t" + year + "\t" + auth.length);
                            writer00.write(System.getProperty("line.separator"));
                            a=a98 + a99 + a00 + a01;
                            writer01.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer01.write(System.getProperty("line.separator"));
                            a= a98 + a99 + a00 + a01 + a02 ;
                            writer02.write("EDGE\t" + m + "\t" + l + "\t" +a+ "\t" + year + "\t" + auth.length);
                            writer02.write(System.getProperty("line.separator"));
                            a=a98 + a99 + a00 + a01 + a02 + a03;
                            writer03.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer03.write(System.getProperty("line.separator"));
                            a=a98 + a99 + a00 + a01 + a02 + a03+a04;
                            writer04.write("EDGE\t" + m + "\t" + l + "\t" + a +  "\t" + year + "\t" + auth.length);
                            writer04.write(System.getProperty("line.separator"));
                            a=a98+ a99 + a00 + a01 + a02 + a03 + a04 + a05;
                            writer05.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer05.write(System.getProperty("line.separator"));
                            a=a98 + a99 + a00 + a01 + a02 + a03 + a04 + a05 + a06 ;
                            writer06.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer06.write(System.getProperty("line.separator"));
                            a=a98 + a99 + a00 + a01 + a02 + a03 + a04 + a05 + a06 + a07 ;
                            writer07.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer07.write(System.getProperty("line.separator"));
                            a=a98 + a99 + a00 + a01 + a02 + a03 + a04 + a05 + a06 + a07 +a08;
                            writer08.write("EDGE\t" + m + "\t" + l + "\t" + a+ "\t" + year + "\t" + auth.length);
                            writer08.write(System.getProperty("line.separator"));
                          } else {
                            int a=a98;
                            writer98.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer98.write(System.getProperty("line.separator"));
                            a=a98 + a99;
                            writer99.write("EDGE\t" +l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer99.write(System.getProperty("line.separator"));
                            a=a98 + a99 + a00;
                            writer00.write("EDGE\t" + l + "\t" + m+ "\t" + a  + "\t" + year + "\t" + auth.length);
                            writer00.write(System.getProperty("line.separator"));
                            a=a98 + a99 + a00 + a01;
                            writer01.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer01.write(System.getProperty("line.separator"));
                            a= a98 + a99 + a00 + a01 + a02 ;
                            writer02.write("EDGE\t" + l + "\t" + m+ "\t" + a + "\t" + year + "\t" + auth.length);
                            writer02.write(System.getProperty("line.separator"));
                            a=a98 + a99 + a00 + a01 + a02 + a03;
                            writer03.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer03.write(System.getProperty("line.separator"));
                            a=a98 + a99 + a00 + a01 + a02 + a03+a04;
                            writer04.write("EDGE\t" + l + "\t" + m + "\t" + a +  "\t" + year + "\t" + auth.length);
                            writer04.write(System.getProperty("line.separator"));
                            a=a98+ a99 + a00 + a01 + a02 + a03 + a04 + a05;
                            writer05.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer05.write(System.getProperty("line.separator"));
                            a=a98 + a99 + a00 + a01 + a02 + a03 + a04 + a05 + a06 ;
                            writer06.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer06.write(System.getProperty("line.separator"));
                            a=a98 + a99 + a00 + a01 + a02 + a03 + a04 + a05 + a06 + a07 ;
                            writer07.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer07.write(System.getProperty("line.separator"));
                            a=a98 + a99 + a00 + a01 + a02 + a03 + a04 + a05 + a06 + a07 +a08;
                            writer08.write("EDGE\t" + l + "\t" + m + "\t" + a+ "\t" + year + "\t" + auth.length);
                            writer08.write(System.getProperty("line.separator"));
                        }
                    }
                }
            }
            writer98.close();
             rsola = ola.executeQuery("SELECT id,year,authors,`1999` as a99,`2000` as a00,`2001` as a01,`2002` as a02,`2003` as a03,`2004` as a04,`2005` as a05,`2006` as a06,`2007` as a07,`2008` as a08 from olabig1_2 where year=1999 and (authors like '%---%' or authors like '%.-%')");
            
            while (rsola.next()) {
                 String[] auth = rsola.getString("authors").split(",");
                String id = rsola.getString("id");
                int year = rsola.getInt("year");
                //keep the sum of citations
                int a99 = rsola.getInt("a99");
                int a00 = rsola.getInt("a00");
                int a01 = rsola.getInt("a01");
                int a02 = rsola.getInt("a02");
                int a03 = rsola.getInt("a03");
                int a04 = rsola.getInt("a04");
                int a05 = rsola.getInt("a05");
                int a06 = rsola.getInt("a06");
                int a07 = rsola.getInt("a07");
                int a08 = rsola.getInt("a08");
                  System.out.println("1999 " + id);
                ArrayList<String> paper = new ArrayList();
                for (int i = 0; i < auth.length; i++) {
                    if (auth[i].contains("---") && auth[i].matches(".*\\d.*")) {
                        String au = auth[i].split("---")[1];
                        au = au.replace("-", "");
                        if (isNumeric(au)) {
                            paper.add(au);
                            if (!authors.contains(au)) {
                                miss.add(au);
                            }
                        }
                    } else if (auth[i].contains(".-") && auth[i].matches(".*\\d.*")) {
                        String au = auth[i].split("\\.-")[1];
                        // 
                        // 
                        //
                        au = au.replace("-", "");
                        if (isNumeric(au)) {
                            paper.add(au);
                            if (!authors.contains(au)) {
                                miss.add(au);
                            }
                        }
                    }
                }
                
                for (int i = 0; i < paper.size(); i++) {
                    long m = Long.parseLong(paper.get(i), 10);
                    for (int j = i + 1; j < paper.size(); j++) {
                        long l = Long.parseLong(paper.get(j), 10);
                         
                        
                         
                        if (m < l) {
                            
                            int a= a99;
                            writer99.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer99.write(System.getProperty("line.separator"));
                            a= a99 + a00;
                            writer00.write("EDGE\t" + m + "\t" + l + "\t" + a  + "\t" + year + "\t" + auth.length);
                            writer00.write(System.getProperty("line.separator"));
                            a= a99 + a00 + a01;
                            writer01.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer01.write(System.getProperty("line.separator"));
                            a= a99 + a00 + a01 + a02 ;
                            writer02.write("EDGE\t" + m + "\t" + l + "\t" +a+ "\t" + year + "\t" + auth.length);
                            writer02.write(System.getProperty("line.separator"));
                            a= a99 + a00 + a01 + a02 + a03;
                            writer03.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer03.write(System.getProperty("line.separator"));
                            a= a99 + a00 + a01 + a02 + a03+a04;
                            writer04.write("EDGE\t" + m + "\t" + l + "\t" + a +  "\t" + year + "\t" + auth.length);
                            writer04.write(System.getProperty("line.separator"));
                            a= a99 + a00 + a01 + a02 + a03 + a04 + a05;
                            writer05.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer05.write(System.getProperty("line.separator"));
                            a= a99 + a00 + a01 + a02 + a03 + a04 + a05 + a06 ;
                            writer06.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer06.write(System.getProperty("line.separator"));
                            a= a99 + a00 + a01 + a02 + a03 + a04 + a05 + a06 + a07 ;
                            writer07.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer07.write(System.getProperty("line.separator"));
                            a= a99 + a00 + a01 + a02 + a03 + a04 + a05 + a06 + a07 +a08;
                            writer08.write("EDGE\t" + m + "\t" + l + "\t" + a+ "\t" + year + "\t" + auth.length);
                            writer08.write(System.getProperty("line.separator"));
                          
                            
                            
                            
                            
                            
                            
                            
                            
                            
                        } else {
                            int a= a99;
                            writer99.write("EDGE\t" +l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer99.write(System.getProperty("line.separator"));
                            a= a99 + a00;
                            writer00.write("EDGE\t" + l + "\t" + m+ "\t" + a  + "\t" + year + "\t" + auth.length);
                            writer00.write(System.getProperty("line.separator"));
                            a= a99 + a00 + a01;
                            writer01.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer01.write(System.getProperty("line.separator"));
                            a=a99 + a00 + a01 + a02 ;
                            writer02.write("EDGE\t" + l + "\t" + m+ "\t" + a + "\t" + year + "\t" + auth.length);
                            writer02.write(System.getProperty("line.separator"));
                            a= a99 + a00 + a01 + a02 + a03;
                            writer03.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer03.write(System.getProperty("line.separator"));
                            a= a99 + a00 + a01 + a02 + a03+a04;
                            writer04.write("EDGE\t" + l + "\t" + m + "\t" + a +  "\t" + year + "\t" + auth.length);
                            writer04.write(System.getProperty("line.separator"));
                            a= a99 + a00 + a01 + a02 + a03 + a04 + a05;
                            writer05.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer05.write(System.getProperty("line.separator"));
                            a= a99 + a00 + a01 + a02 + a03 + a04 + a05 + a06 ;
                            writer06.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer06.write(System.getProperty("line.separator"));
                            a= a99 + a00 + a01 + a02 + a03 + a04 + a05 + a06 + a07 ;
                            writer07.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer07.write(System.getProperty("line.separator"));
                            a= a99 + a00 + a01 + a02 + a03 + a04 + a05 + a06 + a07 +a08;
                            writer08.write("EDGE\t" + l + "\t" + m + "\t" + a+ "\t" + year + "\t" + auth.length);
                            writer08.write(System.getProperty("line.separator"));
                         
                        }
                    }
                }
            }
            writer99.close();
 
            //////////////////////////////////////////////////////////////////////////////////2000
            
            rsola = ola.executeQuery("SELECT id,year,authors,`2000` as a00,`2001` as a01,`2002` as a02,`2003` as a03,`2004` as a04,`2005` as a05,`2006` as a06,`2007` as a07,`2008` as a08 from olabig1_2 where year=2000 and (authors like '%---%' or authors like '%.-%')");
            
            while (rsola.next()) {
                
                String[] auth = rsola.getString("authors").split(",");
                String id = rsola.getString("id");
                int year = rsola.getInt("year");
                
               
                
                
                int a00 = rsola.getInt("a00");
                int a01 = rsola.getInt("a01");
                int a02 = rsola.getInt("a02");
                int a03 = rsola.getInt("a03");
                int a04 = rsola.getInt("a04");
                int a05 = rsola.getInt("a05");
                int a06 = rsola.getInt("a06");
                int a07 = rsola.getInt("a07");
                int a08 = rsola.getInt("a08");
             
                
                
                
                
                System.out.println("2000 " + id);
                ArrayList<String> paper = new ArrayList();
                
                for (int i = 0; i < auth.length; i++) {
                    if (auth[i].contains("---") && auth[i].matches(".*\\d.*")) {
                        String au = auth[i].split("---")[1];
                        // 
                        
                        //  
                        au = au.replace("-", "");
                        if (isNumeric(au)) {
                            paper.add(au);
                            
                            if (!authors.contains(au)) {
                                miss.add(au);
                            }
                        }
                    } else if (auth[i].contains(".-") && auth[i].matches(".*\\d.*")) {
                        String au = auth[i].split("\\.-")[1];
                        // 
                        // 
                        //
                        au = au.replace("-", "");
                        if (isNumeric(au)) {
                            paper.add(au);
                            if (!authors.contains(au)) {
                                miss.add(au);
                            }
                        }
                    }
                }
                
                for (int i = 0; i < paper.size(); i++) {
                    long m = Long.parseLong(paper.get(i), 10);
                    for (int j = i + 1; j < paper.size(); j++) {
                        long l = Long.parseLong(paper.get(j), 10);
                         
                        
                         
                        if (m < l) {
                            
                            int a=  a00;
                            writer00.write("EDGE\t" + m + "\t" + l + "\t" + a  + "\t" + year + "\t" + auth.length);
                            writer00.write(System.getProperty("line.separator"));
                            a=  a00 + a01;
                            writer01.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer01.write(System.getProperty("line.separator"));
                            a=  a00 + a01 + a02 ;
                            writer02.write("EDGE\t" + m + "\t" + l + "\t" +a+ "\t" + year + "\t" + auth.length);
                            writer02.write(System.getProperty("line.separator"));
                            a=  a00 + a01 + a02 + a03;
                            writer03.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer03.write(System.getProperty("line.separator"));
                            a=  a00 + a01 + a02 + a03+a04;
                            writer04.write("EDGE\t" + m + "\t" + l + "\t" + a +  "\t" + year + "\t" + auth.length);
                            writer04.write(System.getProperty("line.separator"));
                            a=  a00 + a01 + a02 + a03 + a04 + a05;
                            writer05.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer05.write(System.getProperty("line.separator"));
                            a=  a00 + a01 + a02 + a03 + a04 + a05 + a06 ;
                            writer06.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer06.write(System.getProperty("line.separator"));
                            a=  a00 + a01 + a02 + a03 + a04 + a05 + a06 + a07 ;
                            writer07.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer07.write(System.getProperty("line.separator"));
                            a=  a00 + a01 + a02 + a03 + a04 + a05 + a06 + a07 +a08;
                            writer08.write("EDGE\t" + m + "\t" + l + "\t" + a+ "\t" + year + "\t" + auth.length);
                            writer08.write(System.getProperty("line.separator"));
                           
                            
                            
                            
                            
                            
                            
                            
                            
                            
                        } else {
                             int a=  a00;
                            writer00.write("EDGE\t" + l + "\t" + m+ "\t" + a  + "\t" + year + "\t" + auth.length);
                            writer00.write(System.getProperty("line.separator"));
                            a=  a00 + a01;
                            writer01.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer01.write(System.getProperty("line.separator"));
                            a= a00 + a01 + a02 ;
                            writer02.write("EDGE\t" + l + "\t" + m+ "\t" + a + "\t" + year + "\t" + auth.length);
                            writer02.write(System.getProperty("line.separator"));
                            a= a00 + a01 + a02 + a03;
                            writer03.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer03.write(System.getProperty("line.separator"));
                            a= a00 + a01 + a02 + a03+a04;
                            writer04.write("EDGE\t" + l + "\t" + m + "\t" + a +  "\t" + year + "\t" + auth.length);
                            writer04.write(System.getProperty("line.separator"));
                            a= a00 + a01 + a02 + a03 + a04 + a05;
                            writer05.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer05.write(System.getProperty("line.separator"));
                            a= a00 + a01 + a02 + a03 + a04 + a05 + a06 ;
                            writer06.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer06.write(System.getProperty("line.separator"));
                            a= a00 + a01 + a02 + a03 + a04 + a05 + a06 + a07 ;
                            writer07.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer07.write(System.getProperty("line.separator"));
                            a= a00 + a01 + a02 + a03 + a04 + a05 + a06 + a07 +a08;
                            writer08.write("EDGE\t" + l + "\t" + m + "\t" + a+ "\t" + year + "\t" + auth.length);
                            writer08.write(System.getProperty("line.separator"));
                        
                        }
                    }
                }
            }
            writer00.close();
            //////////////////////////////////////////////////////////////////2001
            
            rsola = ola.executeQuery("SELECT id,year,authors,`2001` as a01,`2002` as a02,`2003` as a03,`2004` as a04,`2005` as a05,`2006` as a06,`2007` as a07,`2008` as a08 from olabig1_2 where year=2001 and (authors like '%---%' or authors like '%.-%')");
            
            while (rsola.next()) {
                
                String[] auth = rsola.getString("authors").split(",");
                String id = rsola.getString("id");
                int year = rsola.getInt("year");
                
                
                
               
                int a01 = rsola.getInt("a01");
                int a02 = rsola.getInt("a02");
                int a03 = rsola.getInt("a03");
                int a04 = rsola.getInt("a04");
                int a05 = rsola.getInt("a05");
                int a06 = rsola.getInt("a06");
                int a07 = rsola.getInt("a07");
                int a08 = rsola.getInt("a08");
             
                
                
                
                
                System.out.println("2001 " + id);
                ArrayList<String> paper = new ArrayList();
                
                for (int i = 0; i < auth.length; i++) {
                    if (auth[i].contains("---") && auth[i].matches(".*\\d.*")) {
                        String au = auth[i].split("---")[1];
                        // 
                        
                        //  
                        au = au.replace("-", "");
                        if (isNumeric(au)) {
                            paper.add(au);
                            
                            if (!authors.contains(au)) {
                                miss.add(au);
                            }
                        }
                    } else if (auth[i].contains(".-") && auth[i].matches(".*\\d.*")) {
                        String au = auth[i].split("\\.-")[1];
                        // 
                        // 
                        //
                        au = au.replace("-", "");
                        if (isNumeric(au)) {
                            paper.add(au);
                            if (!authors.contains(au)) {
                                miss.add(au);
                            }
                        }
                    }
                }
                
                for (int i = 0; i < paper.size(); i++) {
                    long m = Long.parseLong(paper.get(i), 10);
                    for (int j = i + 1; j < paper.size(); j++) {
                        long l = Long.parseLong(paper.get(j), 10);
                         
                        
                         
                        if (m < l) {
                            
                            int a= a01;
                            writer01.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer01.write(System.getProperty("line.separator"));
                            a=  a01 + a02 ;
                            writer02.write("EDGE\t" + m + "\t" + l + "\t" +a+ "\t" + year + "\t" + auth.length);
                            writer02.write(System.getProperty("line.separator"));
                            a=  a01 + a02 + a03;
                            writer03.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer03.write(System.getProperty("line.separator"));
                            a=  a01 + a02 + a03+a04;
                            writer04.write("EDGE\t" + m + "\t" + l + "\t" + a +  "\t" + year + "\t" + auth.length);
                            writer04.write(System.getProperty("line.separator"));
                            a=  a01 + a02 + a03 + a04 + a05;
                            writer05.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer05.write(System.getProperty("line.separator"));
                            a=  a01 + a02 + a03 + a04 + a05 + a06 ;
                            writer06.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer06.write(System.getProperty("line.separator"));
                            a=   a01 + a02 + a03 + a04 + a05 + a06 + a07 ;
                            writer07.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer07.write(System.getProperty("line.separator"));
                            a=   a01 + a02 + a03 + a04 + a05 + a06 + a07 +a08;
                            writer08.write("EDGE\t" + m + "\t" + l + "\t" + a+ "\t" + year + "\t" + auth.length);
                            writer08.write(System.getProperty("line.separator"));
                          
                            
                            
                            
                            
                            
                            
                            
                            
                        } else {
                            int a=a01;
                            writer01.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer01.write(System.getProperty("line.separator"));
                            a= a01 + a02 ;
                            writer02.write("EDGE\t" + l + "\t" + m+ "\t" + a + "\t" + year + "\t" + auth.length);
                            writer02.write(System.getProperty("line.separator"));
                            a= a01 + a02 + a03;
                            writer03.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer03.write(System.getProperty("line.separator"));
                            a= a01 + a02 + a03+a04;
                            writer04.write("EDGE\t" + l + "\t" + m + "\t" + a +  "\t" + year + "\t" + auth.length);
                            writer04.write(System.getProperty("line.separator"));
                            a= a01 + a02 + a03 + a04 + a05;
                            writer05.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer05.write(System.getProperty("line.separator"));
                            a= a01 + a02 + a03 + a04 + a05 + a06 ;
                            writer06.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer06.write(System.getProperty("line.separator"));
                            a= a01 + a02 + a03 + a04 + a05 + a06 + a07 ;
                            writer07.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer07.write(System.getProperty("line.separator"));
                            a= a01 + a02 + a03 + a04 + a05 + a06 + a07 +a08;
                            writer08.write("EDGE\t" + l + "\t" + m + "\t" + a+ "\t" + year + "\t" + auth.length);
                            writer08.write(System.getProperty("line.separator"));
                        
                        }
                    }
                }
            }
            writer01.close();
            
            
            //////////////////////////////////////////////////////////////////////////////2002
             
            rsola = ola.executeQuery("SELECT id,year,authors,`2002` as a02,`2003` as a03,`2004` as a04,`2005` as a05,`2006` as a06,`2007` as a07,`2008` as a08 from olabig1_2 where year=2002 and (authors like '%---%' or authors like '%.-%')");
            
            while (rsola.next()) {
                
                String[] auth = rsola.getString("authors").split(",");
                String id = rsola.getString("id");
                int year = rsola.getInt("year");
                
                
                
                int a02 = rsola.getInt("a02");
                int a03 = rsola.getInt("a03");
                int a04 = rsola.getInt("a04");
                int a05 = rsola.getInt("a05");
                int a06 = rsola.getInt("a06");
                int a07 = rsola.getInt("a07");
                int a08 = rsola.getInt("a08");
             
                
                
                
                
                System.out.println("2002 " + id);
                ArrayList<String> paper = new ArrayList();
                
                for (int i = 0; i < auth.length; i++) {
                    if (auth[i].contains("---") && auth[i].matches(".*\\d.*")) {
                        String au = auth[i].split("---")[1];
                        // 
                        
                        //  
                        au = au.replace("-", "");
                        if (isNumeric(au)) {
                            paper.add(au);
                            
                            if (!authors.contains(au)) {
                                miss.add(au);
                            }
                        }
                    } else if (auth[i].contains(".-") && auth[i].matches(".*\\d.*")) {
                        String au = auth[i].split("\\.-")[1];
                        // 
                        // 
                        //
                        au = au.replace("-", "");
                        if (isNumeric(au)) {
                            paper.add(au);
                            if (!authors.contains(au)) {
                                miss.add(au);
                            }
                        }
                    }
                }
                
                for (int i = 0; i < paper.size(); i++) {
                    long m = Long.parseLong(paper.get(i), 10);
                    for (int j = i + 1; j < paper.size(); j++) {
                        long l = Long.parseLong(paper.get(j), 10);
                         
                        
                         
                        if (m < l) {
                            
                            int a= a02 ;
                            writer02.write("EDGE\t" + m + "\t" + l + "\t" +a+ "\t" + year + "\t" + auth.length);
                            writer02.write(System.getProperty("line.separator"));
                            a= a02 + a03;
                            writer03.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer03.write(System.getProperty("line.separator"));
                            a= a02 + a03+a04;
                            writer04.write("EDGE\t" + m + "\t" + l + "\t" + a +  "\t" + year + "\t" + auth.length);
                            writer04.write(System.getProperty("line.separator"));
                            a= a02 + a03 + a04 + a05;
                            writer05.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer05.write(System.getProperty("line.separator"));
                            a= a02 + a03 + a04 + a05 + a06 ;
                            writer06.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer06.write(System.getProperty("line.separator"));
                            a= a02 + a03 + a04 + a05 + a06 + a07 ;
                            writer07.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer07.write(System.getProperty("line.separator"));
                            a= a02 + a03 + a04 + a05 + a06 + a07 +a08;
                            writer08.write("EDGE\t" + m + "\t" + l + "\t" + a+ "\t" + year + "\t" + auth.length);
                            writer08.write(System.getProperty("line.separator"));
                         
                            
                            
                            
                            
                            
                            
                            
                            
                            
                        } else {
                            int a=  a02 ;
                            writer02.write("EDGE\t" + l + "\t" + m+ "\t" + a + "\t" + year + "\t" + auth.length);
                            writer02.write(System.getProperty("line.separator"));
                            a= a02 + a03;
                            writer03.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer03.write(System.getProperty("line.separator"));
                            a= a02 + a03+a04;
                            writer04.write("EDGE\t" + l + "\t" + m + "\t" + a +  "\t" + year + "\t" + auth.length);
                            writer04.write(System.getProperty("line.separator"));
                            a= a02 + a03 + a04 + a05;
                            writer05.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer05.write(System.getProperty("line.separator"));
                            a= a02 + a03 + a04 + a05 + a06 ;
                            writer06.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer06.write(System.getProperty("line.separator"));
                            a= a02 + a03 + a04 + a05 + a06 + a07 ;
                            writer07.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer07.write(System.getProperty("line.separator"));
                            a= a02 + a03 + a04 + a05 + a06 + a07 +a08;
                            writer08.write("EDGE\t" + l + "\t" + m + "\t" + a+ "\t" + year + "\t" + auth.length);
                            writer08.write(System.getProperty("line.separator"));
                       
                        }
                    }
                }
            }
            writer02.close();
            
            
            ////////////////////////////////////////////////////////////////////////////////////2003
            rsola = ola.executeQuery("SELECT id,year,authors,`2003` as a03,`2004` as a04,`2005` as a05,`2006` as a06,`2007` as a07,`2008` as a08 from olabig1_2 where year=2003 and (authors like '%---%' or authors like '%.-%')");
            
            while (rsola.next()) {
                
                String[] auth = rsola.getString("authors").split(",");
                String id = rsola.getString("id");
                int year = rsola.getInt("year");
                
                
                
                int a03 = rsola.getInt("a03");
                int a04 = rsola.getInt("a04");
                int a05 = rsola.getInt("a05");
                int a06 = rsola.getInt("a06");
                int a07 = rsola.getInt("a07");
                int a08 = rsola.getInt("a08");
             
                
                
                
                
                System.out.println("2003 " + id);
                ArrayList<String> paper = new ArrayList();
                
                for (int i = 0; i < auth.length; i++) {
                    if (auth[i].contains("---") && auth[i].matches(".*\\d.*")) {
                        String au = auth[i].split("---")[1];
                        // 
                        
                        //  
                        au = au.replace("-", "");
                        if (isNumeric(au)) {
                            paper.add(au);
                            
                            if (!authors.contains(au)) {
                                miss.add(au);
                            }
                        }
                    } else if (auth[i].contains(".-") && auth[i].matches(".*\\d.*")) {
                        String au = auth[i].split("\\.-")[1];
                        // 
                        // 
                        //
                        au = au.replace("-", "");
                        if (isNumeric(au)) {
                            paper.add(au);
                            if (!authors.contains(au)) {
                                miss.add(au);
                            }
                        }
                    }
                }
                
                for (int i = 0; i < paper.size(); i++) {
                    long m = Long.parseLong(paper.get(i), 10);
                    for (int j = i + 1; j < paper.size(); j++) {
                        long l = Long.parseLong(paper.get(j), 10);
                         
                        
                         
                        if (m < l) {
                            
                            int a= a03;
                            writer03.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer03.write(System.getProperty("line.separator"));
                            a= a03 + a04;
                            writer04.write("EDGE\t" + m + "\t" + l + "\t" + a +  "\t" + year + "\t" + auth.length);
                            writer04.write(System.getProperty("line.separator"));
                            a= a03 + a04 + a05;
                            writer05.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer05.write(System.getProperty("line.separator"));
                            a= a03 + a04 + a05 + a06 ;
                            writer06.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer06.write(System.getProperty("line.separator"));
                            a=  a03 + a04 + a05 + a06 + a07 ;
                            writer07.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer07.write(System.getProperty("line.separator"));
                            a= a03 + a04 + a05 + a06 + a07 +a08;
                            writer08.write("EDGE\t" + m + "\t" + l + "\t" + a+ "\t" + year + "\t" + auth.length);
                            writer08.write(System.getProperty("line.separator"));
                         
                            
                            
                            
                            
                            
                            
                            
                            
                            
                        } else {
                            int a= a03;
                            writer03.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer03.write(System.getProperty("line.separator"));
                            a= a03+a04;
                            writer04.write("EDGE\t" + l + "\t" + m + "\t" + a +  "\t" + year + "\t" + auth.length);
                            writer04.write(System.getProperty("line.separator"));
                            a= a03 + a04 + a05;
                            writer05.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer05.write(System.getProperty("line.separator"));
                            a= a03 + a04 + a05 + a06 ;
                            writer06.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer06.write(System.getProperty("line.separator"));
                            a= a03 + a04 + a05 + a06 + a07 ;
                            writer07.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer07.write(System.getProperty("line.separator"));
                            a= a03 + a04 + a05 + a06 + a07 +a08;
                            writer08.write("EDGE\t" + l + "\t" + m + "\t" + a+ "\t" + year + "\t" + auth.length);
                            writer08.write(System.getProperty("line.separator"));
                        
                        }
                    }
                }
            }
            writer03.close();
            
            //////////////////////////////////////////////////////////////////////2004
            rsola = ola.executeQuery("SELECT id,year,authors,`2004` as a04,`2005` as a05,`2006` as a06,`2007` as a07,`2008` as a08 from olabig1_2 where year=2004 and (authors like '%---%' or authors like '%.-%')");
            
            while (rsola.next()) {
                
                String[] auth = rsola.getString("authors").split(",");
                String id = rsola.getString("id");
                int year = rsola.getInt("year");
                
                
                
                int a04 = rsola.getInt("a04");
                int a05 = rsola.getInt("a05");
                int a06 = rsola.getInt("a06");
                int a07 = rsola.getInt("a07");
                int a08 = rsola.getInt("a08");
             
                
                
                
                
                System.out.println("2004 " + id);
                ArrayList<String> paper = new ArrayList();
                
                for (int i = 0; i < auth.length; i++) {
                    if (auth[i].contains("---") && auth[i].matches(".*\\d.*")) {
                        String au = auth[i].split("---")[1];
                        // 
                        
                        //  
                        au = au.replace("-", "");
                        if (isNumeric(au)) {
                            paper.add(au);
                            
                            if (!authors.contains(au)) {
                                miss.add(au);
                            }
                        }
                    } else if (auth[i].contains(".-") && auth[i].matches(".*\\d.*")) {
                        String au = auth[i].split("\\.-")[1];
                        // 
                        // 
                        //
                        au = au.replace("-", "");
                        if (isNumeric(au)) {
                            paper.add(au);
                            if (!authors.contains(au)) {
                                miss.add(au);
                            }
                        }
                    }
                }
                
                for (int i = 0; i < paper.size(); i++) {
                    long m = Long.parseLong(paper.get(i), 10);
                    for (int j = i + 1; j < paper.size(); j++) {
                        long l = Long.parseLong(paper.get(j), 10);
                         
                        
                         
                        if (m < l) {
                            
                            int a= a04;
                            writer04.write("EDGE\t" + m + "\t" + l + "\t" + a +  "\t" + year + "\t" + auth.length);
                            writer04.write(System.getProperty("line.separator"));
                            a= a04 + a05;
                            writer05.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer05.write(System.getProperty("line.separator"));
                            a= a04 + a05 + a06 ;
                            writer06.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer06.write(System.getProperty("line.separator"));
                            a= a04 + a05 + a06 + a07 ;
                            writer07.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer07.write(System.getProperty("line.separator"));
                            a= a04 + a05 + a06 + a07 +a08;
                            writer08.write("EDGE\t" + m + "\t" + l + "\t" + a+ "\t" + year + "\t" + auth.length);
                            writer08.write(System.getProperty("line.separator"));
                     
                            
                            
                            
                         
                            
                            
                        } else {
                             int a= a04;
                             writer04.write("EDGE\t" + l + "\t" + m + "\t" + a +  "\t" + year + "\t" + auth.length);
                            writer04.write(System.getProperty("line.separator"));
                            a= a04 + a05;
                            writer05.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer05.write(System.getProperty("line.separator"));
                            a=  a04 + a05 + a06 ;
                            writer06.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer06.write(System.getProperty("line.separator"));
                            a= a04 + a05 + a06 + a07 ;
                            writer07.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer07.write(System.getProperty("line.separator"));
                            a= a04 + a05 + a06 + a07 +a08;
                            writer08.write("EDGE\t" + l + "\t" + m + "\t" + a+ "\t" + year + "\t" + auth.length);
                            writer08.write(System.getProperty("line.separator"));
                    
                        }
                    }
                }
            }
            writer04.close();
            
               //////////////////////////////////////////////////////////////////////2005
            rsola = ola.executeQuery("SELECT id,year,authors,`2005` as a05,`2006` as a06,`2007` as a07,`2008` as a08 from olabig1_2 where year=2005 and (authors like '%---%' or authors like '%.-%')");
            
            while (rsola.next()) {
                
                String[] auth = rsola.getString("authors").split(",");
                String id = rsola.getString("id");
                int year = rsola.getInt("year");
                
                
                
                int a05 = rsola.getInt("a05");
                int a06 = rsola.getInt("a06");
                int a07 = rsola.getInt("a07");
                int a08 = rsola.getInt("a08");
             
                
                
                
                
                System.out.println("2005 " + id);
                ArrayList<String> paper = new ArrayList();
                
                for (int i = 0; i < auth.length; i++) {
                    if (auth[i].contains("---") && auth[i].matches(".*\\d.*")) {
                        String au = auth[i].split("---")[1];
                        // 
                        
                        //  
                        au = au.replace("-", "");
                        if (isNumeric(au)) {
                            paper.add(au);
                            
                            if (!authors.contains(au)) {
                                miss.add(au);
                            }
                        }
                    } else if (auth[i].contains(".-") && auth[i].matches(".*\\d.*")) {
                        String au = auth[i].split("\\.-")[1];
                        // 
                        // 
                        //
                        au = au.replace("-", "");
                        if (isNumeric(au)) {
                            paper.add(au);
                            if (!authors.contains(au)) {
                                miss.add(au);
                            }
                        }
                    }
                }
                
                for (int i = 0; i < paper.size(); i++) {
                    long m = Long.parseLong(paper.get(i), 10);
                    for (int j = i + 1; j < paper.size(); j++) {
                        long l = Long.parseLong(paper.get(j), 10);
                         
                        
                         
                        if (m < l) {
                            
                            int a= a05;
                            writer05.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer05.write(System.getProperty("line.separator"));
                            a= a05 + a06 ;
                            writer06.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer06.write(System.getProperty("line.separator"));
                            a= a05 + a06 + a07 ;
                            writer07.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer07.write(System.getProperty("line.separator"));
                            a= a05 + a06 + a07 +a08;
                            writer08.write("EDGE\t" + m + "\t" + l + "\t" + a+ "\t" + year + "\t" + auth.length);
                            writer08.write(System.getProperty("line.separator"));
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                        } else {
                            int a= a05;
                            writer05.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer05.write(System.getProperty("line.separator"));
                            a=  a05 + a06 ;
                            writer06.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer06.write(System.getProperty("line.separator"));
                            a= a05 + a06 + a07 ;
                            writer07.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer07.write(System.getProperty("line.separator"));
                            a=  a05 + a06 + a07 +a08;
                            writer08.write("EDGE\t" + l + "\t" + m + "\t" + a+ "\t" + year + "\t" + auth.length);
                            writer08.write(System.getProperty("line.separator"));
                        
                        }
                    }
                }
            }
            writer05.close();
            //////////////////////////////////////////////////////////2006
            rsola = ola.executeQuery("SELECT id,year,authors,`2006` as a06,`2007` as a07,`2008` as a08 from olabig1_2 where year=2006 and (authors like '%---%' or authors like '%.-%')");
            
            while (rsola.next()) {
                
                String[] auth = rsola.getString("authors").split(",");
                String id = rsola.getString("id");
                int year = rsola.getInt("year");
                
                
                
                int a06 = rsola.getInt("a06");
                int a07 = rsola.getInt("a07");
                int a08 = rsola.getInt("a08");
             
                
                
                
                
                System.out.println("2006 " + id);
                ArrayList<String> paper = new ArrayList();
                
                for (int i = 0; i < auth.length; i++) {
                    if (auth[i].contains("---") && auth[i].matches(".*\\d.*")) {
                        String au = auth[i].split("---")[1];
                        // 
                        
                        //  
                        au = au.replace("-", "");
                        if (isNumeric(au)) {
                            paper.add(au);
                            
                            if (!authors.contains(au)) {
                                miss.add(au);
                            }
                        }
                    } else if (auth[i].contains(".-") && auth[i].matches(".*\\d.*")) {
                        String au = auth[i].split("\\.-")[1];
                        // 
                        // 
                        //
                        au = au.replace("-", "");
                        if (isNumeric(au)) {
                            paper.add(au);
                            if (!authors.contains(au)) {
                                miss.add(au);
                            }
                        }
                    }
                }
                
                for (int i = 0; i < paper.size(); i++) {
                    long m = Long.parseLong(paper.get(i), 10);
                    for (int j = i + 1; j < paper.size(); j++) {
                        long l = Long.parseLong(paper.get(j), 10);
                         
                        
                         
                        if (m < l) {
                            
                            int a= a06 ;
                            writer06.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer06.write(System.getProperty("line.separator"));
                            a= a06 + a07 ;
                            writer07.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer07.write(System.getProperty("line.separator"));
                            a= a06 + a07 +a08;
                            writer08.write("EDGE\t" + m + "\t" + l + "\t" + a+ "\t" + year + "\t" + auth.length);
                            writer08.write(System.getProperty("line.separator"));
                           
                            
                            
                            
                            
                            
                            
                            
                            
                            
                        } else {
                            int a= a06 ;
                            writer06.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer06.write(System.getProperty("line.separator"));
                            a= a06 + a07 ;
                            writer07.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer07.write(System.getProperty("line.separator"));
                            a= a06 + a07 +a08;
                            writer08.write("EDGE\t" + l + "\t" + m + "\t" + a+ "\t" + year + "\t" + auth.length);
                            writer08.write(System.getProperty("line.separator"));
                         
                        }
                    }
                }
            }
            writer06.close();
            ///////////////////////////////////////////////////////////2007
             rsola = ola.executeQuery("SELECT id,year,authors,`2007` as a07,`2008` as a08 from olabig1_2 where year=2007 and (authors like '%---%' or authors like '%.-%')");
            
            while (rsola.next()) {
                
                String[] auth = rsola.getString("authors").split(",");
                String id = rsola.getString("id");
                int year = rsola.getInt("year");
                
                
                
                int a07 = rsola.getInt("a07");
                int a08 = rsola.getInt("a08");
             
                
                
                
                
                System.out.println("2007 " + id);
                ArrayList<String> paper = new ArrayList();
                
                for (int i = 0; i < auth.length; i++) {
                    if (auth[i].contains("---") && auth[i].matches(".*\\d.*")) {
                        String au = auth[i].split("---")[1];
                        // 
                        
                        //  
                        au = au.replace("-", "");
                        if (isNumeric(au)) {
                            paper.add(au);
                            
                            if (!authors.contains(au)) {
                                miss.add(au);
                            }
                        }
                    } else if (auth[i].contains(".-") && auth[i].matches(".*\\d.*")) {
                        String au = auth[i].split("\\.-")[1];
                        // 
                        // 
                        //
                        au = au.replace("-", "");
                        if (isNumeric(au)) {
                            paper.add(au);
                            if (!authors.contains(au)) {
                                miss.add(au);
                            }
                        }
                    }
                }
                
                for (int i = 0; i < paper.size(); i++) {
                    long m = Long.parseLong(paper.get(i), 10);
                    for (int j = i + 1; j < paper.size(); j++) {
                        long l = Long.parseLong(paper.get(j), 10);
                         
                        
                         
                        if (m < l) {
                            
                            int a= a07 ;
                            writer07.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer07.write(System.getProperty("line.separator"));
                            a= a07 +a08;
                            writer08.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer08.write(System.getProperty("line.separator"));
                           
                            
                            
                            
                            
                            
                            
                            
                            
                        } else {
                            int a= a07 ;
                            writer07.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer07.write(System.getProperty("line.separator"));
                            a= a07 +a08;
                            writer08.write("EDGE\t" + l + "\t" + m + "\t" + a+ "\t" + year + "\t" + auth.length);
                            writer08.write(System.getProperty("line.separator"));
                        
                        }
                    }
                }
            }
            writer07.close();
            
            ////////////////////////////////////////////////////2008
             rsola = ola.executeQuery("SELECT id,year,authors,`2008` as a08 from olabig1_2 where year=2008 and (authors like '%---%' or authors like '%.-%')");
            
            while (rsola.next()) {
                
                String[] auth = rsola.getString("authors").split(",");
                String id = rsola.getString("id");
                int year = rsola.getInt("year");
                
                
                
                int a08 = rsola.getInt("a08");
             
                
                
                
                
                System.out.println("2008 " + id);
                ArrayList<String> paper = new ArrayList();
                
                for (int i = 0; i < auth.length; i++) {
                    if (auth[i].contains("---") && auth[i].matches(".*\\d.*")) {
                        String au = auth[i].split("---")[1];
                        // 
                        
                        //  
                        au = au.replace("-", "");
                        if (isNumeric(au)) {
                            paper.add(au);
                            
                            if (!authors.contains(au)) {
                                miss.add(au);
                            }
                        }
                    } else if (auth[i].contains(".-") && auth[i].matches(".*\\d.*")) {
                        String au = auth[i].split("\\.-")[1];
                        // 
                        // 
                        //
                        au = au.replace("-", "");
                        if (isNumeric(au)) {
                            paper.add(au);
                            if (!authors.contains(au)) {
                                miss.add(au);
                            }
                        }
                    }
                }
                
                for (int i = 0; i < paper.size(); i++) {
                    long m = Long.parseLong(paper.get(i), 10);
                    for (int j = i + 1; j < paper.size(); j++) {
                        long l = Long.parseLong(paper.get(j), 10);
                         
                        
                         
                        if (m < l) {
                            
                             int a=a08;
                            writer08.write("EDGE\t" + m + "\t" + l + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer08.write(System.getProperty("line.separator"));
                           
                            
                            
                            
                            
                            
                            
                            
                            
                            
                        } else {
                            int a=a08;
                            writer08.write("EDGE\t" + l + "\t" + m + "\t" + a + "\t" + year + "\t" + auth.length);
                            writer08.write(System.getProperty("line.separator"));
                         
                        }
                    }
                }
            }
            writer08.close();
            Writer MissedWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("\\data\\yearly graphs and datasets\\missed.txt"), "utf-8"));
            Iterator it = miss.iterator();
            while (it.hasNext()) {
                MissedWriter.write(it.next().toString());
                MissedWriter.write(System.getProperty("line.separator"));
            }
            MissedWriter.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}


