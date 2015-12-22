/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crawling;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import DataProcessing.Author;
import Crawling.Paper;

/**
 *
 * @author Giorgos
 */
public class CrawlInstitutions {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, ParserConfigurationException, IOException, XPatherException {
        Class.forName("com.mysql.jdbc.Driver");
        String connectionURL = "";
        Connection connection = DriverManager.getConnection(connectionURL);
        //Statement papers = connection.createStatement();      
        connection.setAutoCommit(false);
        String query = "INSERT INTO `papersCrawled`(`title`, `authors`, `more`,`journal`) VALUES (?,?,?,?)";
        PreparedStatement papers = connection.prepareStatement(query);//oi prwtes seldies twn idrimatwn
        String scopus = "http://www.scopus.com/results/results.url?cc=10&sort=cp-f&src=s&nlo=&nlr=&nls=&sid=91FE933B41DB16EA8CBBD2FD3B32CA6D.WlW7NKKC52nnQNxjqAQrlA%3a140&sot=aff&sdt=a&sl=15&s=AF-ID%2860012296%29&ss=cp-f&ws=r-f&ps=r-f&cs=r-f&origin=resultslist&zone=resultslist";
        //String scopus = "http://www.scopus.com/results/results.url?cc=10&sort=cp-f&src=s&nlo=&nlr=&nls=&sid=389E09695483F6EE1392E1A8ECF8EF93.aXczxbyuHHiXgaIW6Ho7g%3a140&sot=aff&sdt=a&sl=15&s=AF-ID%2860028900%29&ss=cp-f&ws=r-f&ps=r-f&cs=r-f&origin=resultslist&zone=resultslist";
        //String scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&sid=7F9C11AF837AF6D93E11064458A20AFB.zQKnzAySRvJOZYcdfIziQ%3a290&sot=aff&sdt=a&sl=15&s=AF-ID%2860015331%29&origin=AffiliationProfile&txGid=7F9C11AF837AF6D93E11064458A20AFB.zQKnzAySRvJOZYcdfIziQ%3a";
        //String scopus="http://www.scopus.com/results/results.url?cc=10&sort=cp-f&src=s&nlo=&nlr=&nls=&sid=7F9C11AF837AF6D93E11064458A20AFB.zQKnzAySRvJOZYcdfIziQ%3a710&sot=aff&sdt=a&sl=15&s=AF-ID%2860002947%29&ss=cp-f&ws=r-f&ps=r-f&cs=r-f&origin=resultslist&zone=resultslist";
       // String scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&nlo=&nlr=&nls=&sid=6EDE5D4437DD10AEA6F7AB9B26C40315.N5T5nM1aaTEF8rE6yKCR3A%3a90&sot=aff&sdt=a&sl=15&s=AF-ID%2860031155%29&cl=t&offset=1&origin=resultslist&ss=cp-f&ws=r-f&ps=r-f&cs=r-f&cc=10&txGid=6EDE5D4437DD10AEA6F7AB9B26C40315.N5T5nM1aaTEF8rE6yKCR3A%3a";
        //String scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&sid=6EDE5D4437DD10AEA6F7AB9B26C40315.N5T5nM1aaTEF8rE6yKCR3A%3a300&sot=aff&sdt=a&sl=15&s=AF-ID%2860001524%29&origin=AffiliationProfile&txGid=6EDE5D4437DD10AEA6F7AB9B26C40315.N5T5nM1aaTEF8rE6yKCR3A%3a";
        // String scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&sid=6EDE5D4437DD10AEA6F7AB9B26C40315.N5T5nM1aaTEF8rE6yKCR3A%3a850&sot=aff&sdt=a&sl=15&s=AF-ID%2860004716%29&origin=AffiliationProfile&txGid=6EDE5D4437DD10AEA6F7AB9B26C40315.N5T5nM1aaTEF8rE6yKCR3A%3a";
       //  String scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&sid=6EDE5D4437DD10AEA6F7AB9B26C40315.N5T5nM1aaTEF8rE6yKCR3A%3a1020&sot=aff&sdt=a&sl=15&s=AF-ID%2860030988%29&origin=AffiliationProfile&txGid=6EDE5D4437DD10AEA6F7AB9B26C40315.N5T5nM1aaTEF8rE6yKCR3A%3a";
       // String scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&sid=D88570EEA9658A40E1F70032EAE105D4.f594dyPDCy4K3aQHRor6A%3a80&sot=aff&sdt=a&sl=15&s=AF-ID%2860025812%29&origin=AffiliationProfile&txGid=D88570EEA9658A40E1F70032EAE105D4.f594dyPDCy4K3aQHRor6A%3a";
       //String scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&sid=D88570EEA9658A40E1F70032EAE105D4.f594dyPDCy4K3aQHRor6A%3a240&sot=aff&sdt=a&sl=15&s=AF-ID%2860017404%29&origin=AffiliationProfile&txGid=D88570EEA9658A40E1F70032EAE105D4.f594dyPDCy4K3aQHRor6A%3a";
       // String scopus="http://www.scopus.com/results/results.url?sort=plf-f&src=s&sid=D88570EEA9658A40E1F70032EAE105D4.f594dyPDCy4K3aQHRor6A%3a520&sot=aff&sdt=a&sl=15&s=AF-ID%2860018592%29&origin=AffiliationProfile&txGid=D88570EEA9658A40E1F70032EAE105D4.f594dyPDCy4K3aQHRor6A%3a";
       // String scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&sid=D88570EEA9658A40E1F70032EAE105D4.f594dyPDCy4K3aQHRor6A%3a520&sot=aff&sdt=a&sl=15&s=AF-ID%2860018592%29&origin=AffiliationProfile&txGid=D88570EEA9658A40E1F70032EAE105D4.f594dyPDCy4K3aQHRor6A%3a";
       // String scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&sid=D88570EEA9658A40E1F70032EAE105D4.f594dyPDCy4K3aQHRor6A%3a730&sot=aff&sdt=a&sl=15&s=AF-ID%2860022461%29&origin=AffiliationProfile&txGid=D88570EEA9658A40E1F70032EAE105D4.f594dyPDCy4K3aQHRor6A%3a";
       // String scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&sid=D88570EEA9658A40E1F70032EAE105D4.f594dyPDCy4K3aQHRor6A%3a890&sot=aff&sdt=a&sl=15&s=AF-ID%2860010667%29&origin=AffiliationProfile&txGid=D88570EEA9658A40E1F70032EAE105D4.f594dyPDCy4K3aQHRor6A%3a";
       // String scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&sid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a390&sot=aff&sdt=a&sl=15&s=AF-ID%2860019507%29&origin=AffiliationProfile&txGid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a";
       // String scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&sid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a620&sot=aff&sdt=a&sl=15&s=AF-ID%2860001086%29&origin=AffiliationProfile&txGid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a";
       //  String scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&sid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a810&sot=aff&sdt=a&sl=15&s=AF-ID%2860003684%29&origin=AffiliationProfile&txGid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a";      
       // String scopus="http://www.scopus.com/results/results.url?sort=plf-f&src=s&sid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a1140&sot=aff&sdt=a&sl=15&s=AF-ID%2860013925%29&origin=AffiliationProfile&txGid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a";
       // String scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&sid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a1330&sot=aff&sdt=a&sl=15&s=AF-ID%2860010108%29&origin=AffiliationProfile&txGid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a";
       //String scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&sid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a1520&sot=aff&sdt=a&sl=15&s=AF-ID%2860104004%29&origin=AffiliationProfile&txGid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a";
       // String scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&sid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a1770&sot=aff&sdt=a&sl=15&s=AF-ID%2860067711%29&origin=AffiliationProfile&txGid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a";
        int cou = 0;
        for (cou = 0; cou < 100; cou++) {
            if (cou > 0) {
                scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&nlo=&nlr=&nls=&sid=91FE933B41DB16EA8CBBD2FD3B32CA6D.WlW7NKKC52nnQNxjqAQrlA%3a330&sot=aff&sdt=a&sl=15&s=AF-ID%2860067083%29&cl=t&offset="+ Integer.toString(cou * 2) +"1&origin=resultslist&ss=plf-f&ws=r-f&ps=r-f&cs=r-f&cc=10&txGid=91FE933B41DB16EA8CBBD2FD3B32CA6D.WlW7NKKC52nnQNxjqAQrlA%3a";
              //  scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&nlo=&nlr=&nls=&sid=389E09695483F6EE1392E1A8ECF8EF93.aXczxbyuHHiXgaIW6Ho7g%3a140&sot=aff&sdt=a&sl=15&s=AF-ID%2860028900%29&cl=t&offset="+ Integer.toString(cou * 2) +"1&origin=resultslist&ss=cp-f&ws=r-f&ps=r-f&cs=r-f&cc=10&txGid=389E09695483F6EE1392E1A8ECF8EF93.aXczxbyuHHiXgaIW6Ho7g%3a";
             //scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&nlo=&nlr=&nls=&sid=7F9C11AF837AF6D93E11064458A20AFB.zQKnzAySRvJOZYcdfIziQ%3a290&sot=aff&sdt=a&sl=15&s=AF-ID%2860015331%29&cl=t&offset="+ Integer.toString(cou * 2) +"1&origin=resultslist&ss=cp-f&ws=r-f&ps=r-f&cs=r-f&cc=10&txGid=7F9C11AF837AF6D93E11064458A20AFB.zQKnzAySRvJOZYcdfIziQ%3a";
            //scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&nlo=&nlr=&nls=&sid=7F9C11AF837AF6D93E11064458A20AFB.zQKnzAySRvJOZYcdfIziQ%3a710&sot=aff&sdt=a&sl=15&s=AF-ID%2860002947%29&cl=t&offset="+ Integer.toString(cou * 2) +"1&origin=resultslist&ss=cp-f&ws=r-f&ps=r-f&cs=r-f&cc=10&txGid=7F9C11AF837AF6D93E11064458A20AFB.zQKnzAySRvJOZYcdfIziQ%3a";
            //scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&nlo=&nlr=&nls=&sid=7F9C11AF837AF6D93E11064458A20AFB.zQKnzAySRvJOZYcdfIziQ%3a1300&sot=aff&sdt=a&sl=15&s=AF-ID%2860031155%29&cl=t&offset="+Integer.toString(cou * 2)+"01&origin=resultslist&ss=plf-f&ws=r-f&ps=r-f&cs=r-f&cc=10&txGid=7F9C11AF837AF6D93E11064458A20AFB.zQKnzAySRvJOZYcdfIziQ%3a";
            //scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&nlo=&nlr=&nls=&sid=6EDE5D4437DD10AEA6F7AB9B26C40315.N5T5nM1aaTEF8rE6yKCR3A%3a90&sot=aff&sdt=a&sl=15&s=AF-ID%2860031155%29&cl=t&offset="+ Integer.toString(cou * 2) +"1&origin=resultslist&ss=plf-f&ws=r-f&ps=r-f&cs=r-f&cc=10&txGid=6EDE5D4437DD10AEA6F7AB9B26C40315.N5T5nM1aaTEF8rE6yKCR3A%3a";
            // scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&nlo=&nlr=&nls=&sid=6EDE5D4437DD10AEA6F7AB9B26C40315.N5T5nM1aaTEF8rE6yKCR3A%3a300&sot=aff&sdt=a&sl=15&s=AF-ID%2860001524%29&cl=t&offset="+ Integer.toString(cou * 2) +"1&origin=resultslist&ss=plf-f&ws=r-f&ps=r-f&cs=r-f&cc=10&txGid=6EDE5D4437DD10AEA6F7AB9B26C40315.N5T5nM1aaTEF8rE6yKCR3A%3a";
             // scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&nlo=&nlr=&nls=&sid=6EDE5D4437DD10AEA6F7AB9B26C40315.N5T5nM1aaTEF8rE6yKCR3A%3a850&sot=aff&sdt=a&sl=15&s=AF-ID%2860004716%29&cl=t&offset="+ Integer.toString(cou * 2) +"1&origin=resultslist&ss=plf-f&ws=r-f&ps=r-f&cs=r-f&cc=10&txGid=6EDE5D4437DD10AEA6F7AB9B26C40315.N5T5nM1aaTEF8rE6yKCR3A%3a";
            //  scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&nlo=&nlr=&nls=&sid=6EDE5D4437DD10AEA6F7AB9B26C40315.N5T5nM1aaTEF8rE6yKCR3A%3a1020&sot=aff&sdt=a&sl=15&s=AF-ID%2860030988%29&cl=t&offset="+ Integer.toString(cou * 2) +"1&origin=resultslist&ss=plf-f&ws=r-f&ps=r-f&cs=r-f&cc=10&txGid=6EDE5D4437DD10AEA6F7AB9B26C40315.N5T5nM1aaTEF8rE6yKCR3A%3a";
           // scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&nlo=&nlr=&nls=&sid=D88570EEA9658A40E1F70032EAE105D4.f594dyPDCy4K3aQHRor6A%3a80&sot=aff&sdt=a&sl=15&s=AF-ID%2860025812%29&cl=t&offset="+ Integer.toString(cou * 2) +"1&origin=resultslist&ss=plf-f&ws=r-f&ps=r-f&cs=r-f&cc=10&txGid=D88570EEA9658A40E1F70032EAE105D4.f594dyPDCy4K3aQHRor6A%3a";
            //scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&nlo=&nlr=&nls=&sid=D88570EEA9658A40E1F70032EAE105D4.f594dyPDCy4K3aQHRor6A%3a240&sot=aff&sdt=a&sl=15&s=AF-ID%2860017404%29&cl=t&offset="+ Integer.toString(cou * 2) +"1&origin=resultslist&ss=plf-f&ws=r-f&ps=r-f&cs=r-f&cc=10&txGid=D88570EEA9658A40E1F70032EAE105D4.f594dyPDCy4K3aQHRor6A%3a";
            //scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&nlo=&nlr=&nls=&sid=D88570EEA9658A40E1F70032EAE105D4.f594dyPDCy4K3aQHRor6A%3a520&sot=aff&sdt=a&sl=15&s=AF-ID%2860018592%29&cl=t&offset="+ Integer.toString(cou * 2) +"1&origin=resultslist&ss=plf-f&ws=r-f&ps=r-f&cs=r-f&cc=10&txGid=D88570EEA9658A40E1F70032EAE105D4.f594dyPDCy4K3aQHRor6A%3a";
            //scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&nlo=&nlr=&nls=&sid=D88570EEA9658A40E1F70032EAE105D4.f594dyPDCy4K3aQHRor6A%3a730&sot=aff&sdt=a&sl=15&s=AF-ID%2860022461%29&cl=t&offset="+ Integer.toString(cou * 2) +"1&origin=resultslist&ss=plf-f&ws=r-f&ps=r-f&cs=r-f&cc=10&txGid=D88570EEA9658A40E1F70032EAE105D4.f594dyPDCy4K3aQHRor6A%3a";
            //scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&nlo=&nlr=&nls=&sid=D88570EEA9658A40E1F70032EAE105D4.f594dyPDCy4K3aQHRor6A%3a890&sot=aff&sdt=a&sl=15&s=AF-ID%2860010667%29&cl=t&offset="+ Integer.toString(cou * 2) +"1&origin=resultslist&ss=plf-f&ws=r-f&ps=r-f&cs=r-f&cc=10&txGid=D88570EEA9658A40E1F70032EAE105D4.f594dyPDCy4K3aQHRor6A%3a";
           // scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&nlo=&nlr=&nls=&sid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a390&sot=aff&sdt=a&sl=15&s=AF-ID%2860019507%29&cl=t&offset="+ Integer.toString(cou * 2) +"1&origin=resultslist&ss=plf-f&ws=r-f&ps=r-f&cs=r-f&cc=10&txGid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a";
           // scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&nlo=&nlr=&nls=&sid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a620&sot=aff&sdt=a&sl=15&s=AF-ID%2860001086%29&cl=t&offset="+ Integer.toString(cou * 2) +"1&origin=resultslist&ss=plf-f&ws=r-f&ps=r-f&cs=r-f&cc=10&txGid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a";
           // scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&nlo=&nlr=&nls=&sid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a810&sot=aff&sdt=a&sl=15&s=AF-ID%2860003684%29&cl=t&offset="+ Integer.toString(cou * 2) +"1&origin=resultslist&ss=plf-f&ws=r-f&ps=r-f&cs=r-f&cc=10&txGid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a";
           // scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&nlo=&nlr=&nls=&sid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a1140&sot=aff&sdt=a&sl=15&s=AF-ID%2860013925%29&cl=t&offset="+ Integer.toString(cou * 2) +"1&origin=resultslist&ss=plf-f&ws=r-f&ps=r-f&cs=r-f&cc=10&txGid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a";
           // scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&nlo=&nlr=&nls=&sid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a1330&sot=aff&sdt=a&sl=15&s=AF-ID%2860010108%29&cl=t&offset="+ Integer.toString(cou * 2) +"1&origin=resultslist&ss=plf-f&ws=r-f&ps=r-f&cs=r-f&cc=10&txGid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a";
           // scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&nlo=&nlr=&nls=&sid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a1520&sot=aff&sdt=a&sl=15&s=AF-ID%2860104004%29&cl=t&offset="+ Integer.toString(cou * 2) +"1&origin=resultslist&ss=plf-f&ws=r-f&ps=r-f&cs=r-f&cc=10&txGid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a156";
           // scopus="http://www.scopus.com/results/results.url?sort=cp-f&src=s&nlo=&nlr=&nls=&sid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a1770&sot=aff&sdt=a&sl=15&s=AF-ID%2860067711%29&cl=t&offset="+ Integer.toString(cou * 2) +"1&origin=resultslist&ss=plf-f&ws=r-f&ps=r-f&cs=r-f&cc=10&txGid=73F7D44EEEAD770DEBDD0ABA511632E3.f594dyPDCy4K3aQHRor6A%3a";
            }
             try {
                DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
                domFactory.setNamespaceAware(true);
                DocumentBuilder builder = domFactory.newDocumentBuilder();
                InputStream inputStream;
                HtmlCleaner cleaner = new HtmlCleaner();
                CleanerProperties props = cleaner.getProperties();
                List<Cookie> cookies;
                HttpClient httpClient = new DefaultHttpClient();
                httpClient.getParams().setParameter("http.useragent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36");

                try {
                    BasicCookieStore cookieStore = new BasicCookieStore();
                    HttpContext localContext = new BasicHttpContext();
                    localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
                    HttpGet httpget = new HttpGet(scopus);
                    HttpResponse response = httpClient.execute(httpget, localContext);
                    HttpEntity entity = response.getEntity();
                    String responseTxt = EntityUtils.toString(entity);
                    TagNode node = cleaner.clean(new ByteArrayInputStream(responseTxt.getBytes("UTF-8")));

                    Object[] Titles = node.evaluateXPath("//./li[@class=\"dataCol2\"]/div/span/a");
                    Object[] papers_authors = node.evaluateXPath("//./li[@class=\"dataCol3\"]/div");
                    Object[] Authors_id = node.evaluateXPath("//./li[@class=\"dataCol3\"]/div/a");
                    Object[] journal = node.evaluateXPath("//./li[@class=\"dataCol5\"]/div/i/a/@href");
                    int i = 0;
                    boolean g = false;
                    ArrayList<Paper> pap = new ArrayList();
                    ArrayList<Author> auth = new ArrayList();
                
                    for (Object o : Titles) {
                        TagNode aNode = (TagNode) o;
                        String title = aNode.getText().toString();
                        pap.add(new Paper(null, title, false, null, null));
                    }
                   
                    for (Object o : Authors_id) {
                        TagNode aNode = (TagNode) o;
                        String author = aNode.getText().toString();
                        String url = aNode.getAttributeByName("href");
                        int deikths = url.indexOf("authorId=");
                        String id = url.substring(deikths + 9,  url.indexOf(";"));
                        auth.add(new Author(author, id, null, null, null, null, null));
                    }
                    int m = 0;
                     for (Object o : journal) {
                        
                        pap.get(m).setJournal("http://www.scopus.com" + o.toString());
                        m++;
                    }
                    m = 0;
                     boolean more = false;
                    int counter = 0;
                    m = 0;
                    for (Object o : papers_authors) {
                        TagNode aNode = (TagNode) o;
                        String test = aNode.getText().toString();
                        if (test.contains("...")) {
                            more = true;
                            pap.get(m).setMore(more);
                        } else {
                            more = false;
                        }
                        String[] authors = test.split(",");
                        int a = 0;
                        if (more) {
                            a = (int) Math.ceil((authors.length - 1) / 2);
                        } else {
                            a = (int) Math.ceil((authors.length) / 2);
                        }

                        ArrayList<Author> paper_auth = new ArrayList();
                        for (i = counter; i < counter + a; i++) {
                            try {
                                paper_auth.add(auth.get(i));
                            } catch (Exception er) {
                                System.out.println(er.getMessage());
                            }
                        }
                        counter = counter + a;
                        pap.get(m).setAuthors(paper_auth);
                        m++;
                    }
                   for (i = 0; i < pap.size(); i++) {
                        String a = "";
                        for (int j = 0; j < pap.get(i).getAuthors().size(); j++) {
                            a = a + pap.get(i).getAuthors().get(j).getName() + "-" + pap.get(i).getAuthors().get(j).getId() + ",";
                        }
                        if (pap.get(i).getJournal() == null) {
                            pap.get(i).setJournal("");
                          
                        }
                       papers.setString(1, pap.get(i).getTitle());
                        papers.setString(2, a);
                        papers.setBoolean(3, pap.get(i).isMore());
                        papers.setString(4, pap.get(i).getJournal());
                        papers.addBatch();
                    }
                    papers.executeBatch();
        
                    connection.commit();
                } finally {
                    httpClient.getConnectionManager().shutdown();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        connection.commit();
    }
}
