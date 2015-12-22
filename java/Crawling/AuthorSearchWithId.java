/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crawling;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
 * @author Giorgos
 */
public class AuthorSearchWithId {

    private static String base64Encode(String stringToEncode) {
        return DatatypeConverter.printBase64Binary(stringToEncode.getBytes());
    }

    public static Author fetch(Author author) {
        String[] mal = author.getName().split(" ");
        String name;
        String initial="";
        ArrayList<String> temp = new ArrayList();
        if (mal.length >= 3) {
            String s = "";
            int i;
            for (i = 0; i < mal.length - 1; i++) {
                s = s + "+" + mal[i];
            }
            name=s;
            initial=mal[i];
        } else {
            if(mal.length>1){
                name=mal[0];
                initial=mal[1];
            }else{
                name=mal[0];
                initial="";
            }
            
        }
        try {
            String scopus = "http://www.scopus.com/results/authorNamesList.url?sort=count-f&src=al&sid=BC646E4994C0A013845A83E47A054EA4.fM4vPBipdL1BpirDq5Cw%3a80&sot=al&sdt=al&sl=46&s=AUTH--LAST--NAME%28" + name + "%29+AND+AUTH--FIRST%28I.%29&st1=" + name + "&st2=" + initial + "&selectionPageSearch=anl&reselectAuthor=false&activeFlag=false&showDocument=false&resultsPerPage=20&offset=1&jtp=false&currentPage=1&previousSelectionCount=0&tooManySelections=false&previousResultCount=0&authSubject=LFSC&authSubject=HLSC&authSubject=PHSC&authSubject=SOSC&exactAuthorSearch=false&showFullList=false&authorPreferredName=&origin=AuthorNamesList";
            System.out.println(scopus);
            DocumentBuilderFactory domFactory2 = DocumentBuilderFactory.newInstance();
            domFactory2.setNamespaceAware(true);

            HtmlCleaner cleaner2 = new HtmlCleaner();
            HttpClient httpClient2 = new DefaultHttpClient();
            httpClient2.getParams().setParameter("http.useragent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36");
            try {
                
                BasicCookieStore cookieStore2 = new BasicCookieStore();
                HttpContext localContext2 = new BasicHttpContext();
                localContext2.setAttribute(ClientContext.COOKIE_STORE, cookieStore2);
                HttpGet httpget2 = new HttpGet(scopus);
                HttpResponse response = httpClient2.execute(httpget2, localContext2);
                HttpEntity entity = response.getEntity();
                String responseTxt = EntityUtils.toString(entity);
                TagNode node2 = cleaner2.clean(new ByteArrayInputStream(responseTxt.getBytes("UTF-8")));

                Object[] hrefNode = node2.evaluateXPath("(//div[@id=\"srchResultsList\"]//li[@class=\"dataCol2\"])//a[@href]");
                int i=0;
                for (Object h : hrefNode) {
                    i++;
                    TagNode bNode = (TagNode) h;
                    String href = bNode.getAttributeByName("href").toString();
                    String part = href.substring(href.indexOf("authorId=") + 9);
                    String id = part.substring(0, part.indexOf("&"));
                    if(id.equalsIgnoreCase(author.getId())){
                        author.setId(id);
                        break;
                    }
                }
                Object[] FieldNode = node2.evaluateXPath("(//div[@id=\"srchResultsList\"]//li[@class=\"dataCol4\"])[" + i + "]/div");
                for (Object o : FieldNode) {
                    TagNode aNode = (TagNode) o;
                    author.setField(aNode.getText().toString());
                    break;
                }
                Object[] AffNode = node2.evaluateXPath("(//div[@id=\"srchResultsList\"]//li[@class=\"dataCol5\"])[" + i + "]/div");
                for (Object o : AffNode) {
                    TagNode aNode = (TagNode) o;
                    author.setAffiliation(aNode.getText().toString());
                    break;
                }
                Object[] CityNode = node2.evaluateXPath("(//div[@id=\"srchResultsList\"]//li[@class=\"dataCol6\"])[" + i + "]/div");
                for (Object o : CityNode) {
                    TagNode aNode = (TagNode) o;
                    author.setCity(aNode.getText().toString());
                    break;
                }
                Object[] CountryNode = node2.evaluateXPath("//div[@id=\"srchResultsList\"]//li[@class=\"dataCol7\"][" + i + "]/div");
                for (Object o : CountryNode) {
                    TagNode aNode = (TagNode) o;
                    author.setCountry(aNode.getText().toString());
                    break;
                }
                return author;
            } finally {
                httpClient2.getConnectionManager().shutdown();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
