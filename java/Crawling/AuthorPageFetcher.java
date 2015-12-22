/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crawling;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
import org.jsoup.Jsoup;

/**
 *
 * @author Giorgos
 */
public class AuthorPageFetcher {

    private static String base64Encode(String stringToEncode) {
        return DatatypeConverter.printBase64Binary(stringToEncode.getBytes());
    }

    public static ArrayList<String> fetch(String id) {
        try {

            ArrayList<String> stoixeia = new ArrayList();
            String scopus = "http://www.scopus.com/authid/detail.url?authorId=" + id;
            System.out.println(scopus);
            
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
                Object[] NameNode = node.evaluateXPath("//tr[td=\"Name\"][1]/td[@class=\"tableBorderGrey-T\"][3]");
                Object[] AffNode = node.evaluateXPath("//tr[td=\"Affiliation\"][1]/td[@class=\"tableBorderLight-T\"][3]");
                Object[] FieldNode = node.evaluateXPath("//tr[td=\"Subject area\"][1]/td[@class=\"tableMbWh-T\"][3]");
                String Name = null;
                String geo = null;
                String fie = null;
                String Field = null;
                String Aff = null;
                String City = null;
                boolean g = false;
                for (Object o : NameNode) {
                    TagNode aNode = (TagNode) o;
                    Name = aNode.getText().toString();
                }
                for (Object o : AffNode) {
                    TagNode aNode = (TagNode) o;
                    geo = Jsoup.parse(aNode.getText().toString()).toString();
                    
                }
                for (Object o : FieldNode) {
                    TagNode aNode = (TagNode) o;
                    fie = Jsoup.parse(aNode.getText().toString()).toString();
                }
                String[] token;
                if (geo != null) {
                    geo=geo.replaceAll("<html>|<head>|</head>|<body>|&amp|</body>|</html>", "");
                    token = geo.split(",");
                    Aff = token[0];
                    if(token.length>2){
                        City = token[2];
                    }else{
                        City=token[1];
                    }

                }
                if (fie != null) {
                    token = fie.split(",");
                    Field = token[0].replaceAll("<html>|<head>|</head>|<body>|&amp|</body>|</html>", "");
                    
                }
                stoixeia.add(Name);
                stoixeia.add(Aff);
                stoixeia.add(City);
                stoixeia.add(Field);
                return stoixeia;
            } finally {
                httpClient.getConnectionManager().shutdown();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
