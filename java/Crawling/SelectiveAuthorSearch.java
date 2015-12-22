/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Crawling;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import DataProcessing.Author;

/**
 *
 * @author Administrator
 */
public class SelectiveAuthorSearch {
     private static String base64Encode(String stringToEncode) {
        return DatatypeConverter.printBase64Binary(stringToEncode.getBytes());
    }

    public static ArrayList<Author> fetch(String Author, String Initial) {
        try {
            ArrayList<Author> upopsifioi = new ArrayList();
            Map<Integer, Author> map = new HashMap<Integer, Author>();
            String scopus = "http://www.scopus.com/results/authorNamesList.url?sort=count-f&src=al&sid=BC646E4994C0A013845A83E47A054EA4.fM4vPBipdL1BpirDq5Cw%3a80&sot=al&sdt=al&sl=46&s=AUTH--LAST--NAME%28" + Author + "%29+AND+AUTH--FIRST%28I.%29&st1=" + Author + "&st2=" + Initial + "&selectionPageSearch=anl&reselectAuthor=false&activeFlag=false&showDocument=false&resultsPerPage=20&offset=1&jtp=false&currentPage=1&previousSelectionCount=0&tooManySelections=false&previousResultCount=0&authSubject=LFSC&authSubject=HLSC&authSubject=PHSC&authSubject=SOSC&exactAuthorSearch=false&showFullList=false&authorPreferredName=&origin=AuthorNamesList";
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true);

            HtmlCleaner cleaner = new HtmlCleaner();
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

                Object[] idsNode = node.evaluateXPath("//div[@id=\"srchResultsList\"]//div[@class=\"dataCol2\"]//div[@class=\"txtSmaller\"][1]");
          
                int i = 0;
                boolean g = false;
                //keep the first five athors from scopus search with similar name
                for (Object o : idsNode) {
                    i++;
                    TagNode aNode = (TagNode) o;
                    String test = aNode.getText().toString();
                    if (test.contains(Author + ", " + Initial.substring(0, 1))) {
                        
                        map.put(i, new Author(test, null, null, null, "", "", false));
                       
                    }
                    if(i==5){
                        break;
                    }
                }
                for (int key : map.keySet()) {
                 //gather scopus data for the author
                    Object[] hrefNode = node.evaluateXPath("(//div[@id=\"srchResultsList\"]//div[@class=\"dataCol2\"])[" + key + "]//a[@href]");
                    for (Object h : hrefNode) {
                        TagNode aNode = (TagNode) h;
                        String href = aNode.getAttributeByName("href").toString();
                        String part = href.substring(href.indexOf("authorId=") + 9);
                        String id = part.substring(0, part.indexOf("&"));
                        map.get(key).setId(id);
                        break;
                    }
                    Object[] FieldNode = node.evaluateXPath("(//div[@id=\"srchResultsList\"]//div[@class=\"dataCol4\"])[" + key + "]");
                    for (Object o : FieldNode) {
                        TagNode aNode = (TagNode) o;
                        map.get(key).setField(aNode.getText().toString());
                        break;
                    }
                    Object[] AffNode = node.evaluateXPath("(//div[@id=\"srchResultsList\"]//div[@class=\"dataCol5\"])[" + key + "]");
                    for (Object o : AffNode) {
                        TagNode aNode = (TagNode) o;
                        map.get(key).setAffiliation(aNode.getText().toString());
                        break;
                    }
                    Object[] CityNode = node.evaluateXPath("(//div[@id=\"srchResultsList\"]//div[@class=\"dataCol6\"])[" + key + "]");
                    for (Object o : CityNode) {
                        TagNode aNode = (TagNode) o;
                        map.get(key).setCity(aNode.getText().toString());
                        break;
                    }
                    Object[] CountryNode = node.evaluateXPath("//div[@id=\"srchResultsList\"]//div[@class=\"dataCol7\"][" + key + "]");
                    for (Object o : CountryNode) {
                        TagNode aNode = (TagNode) o;
                        map.get(key).setCity(map.get(key).getCity().concat(" "+aNode.getText().toString()));
                        break;
                    }
                }
                for (Author value : map.values()) {
                    upopsifioi.add(value);
                }
                return upopsifioi;

            } finally {
                httpClient.getConnectionManager().shutdown();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
