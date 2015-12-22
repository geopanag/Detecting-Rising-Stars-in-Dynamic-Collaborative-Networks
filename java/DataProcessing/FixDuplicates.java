/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataProcessing;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Administrator
 */
public class FixDuplicates {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, SQLException, InterruptedException, IOException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String connectionURL = "";
        Connection connection = DriverManager.getConnection(connectionURL);
        Statement papers = connection.createStatement();
        Statement papersDel=connection.createStatement();
        ResultSet rsbig = null;
        ResultSet rsbig2 = null;
        rsbig=papers.executeQuery("SELECT title,count(*) as c FROM `papers` group by title having c>1");
        while(rsbig.next()){
            //keep the double with most authors
            String title = rsbig.getString("title");
            System.out.println(title);
            rsbig2=papersDel.executeQuery("SELECT authors,id FROM papers where title like '%"+title+"%'");
            int max=0;
            String id = null;
            while(rsbig2.next()){
                String authors=rsbig2.getString("authors");
                authors = authors.replaceAll(", ", " ");
                String[] auth=authors.split(",");
                if(auth.length>max){
                    id=rsbig2.getString("id");
                    max=auth.length;
                }
                System.out.println(id);
            }
            //diegrapse ta alla
            papersDel.executeUpdate("DELETE FROM papers where title like'%"+title+"%' and id!='"+id+"'");
        }
    }
}
