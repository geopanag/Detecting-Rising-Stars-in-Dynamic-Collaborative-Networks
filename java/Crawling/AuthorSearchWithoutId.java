package Crawling;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author herc
 */
public class AuthorSearchWithoutId {

    private static String base64Encode(String stringToEncode) {
        return DatatypeConverter.printBase64Binary(stringToEncode.getBytes());
    }

    public static ArrayList<String> fetch(String Author, String Initial) {
        try {
            ArrayList<String> stoixeia = new ArrayList();
            String scopus = "http://www.scopus.com/results/authorNamesList.url?sort=count-f&src=al&sid=BC646E4994C0A013845A83E47A054EA4.fM4vPBipdL1BpirDq5Cw%3a80&sot=al&sdt=al&sl=46&s=AUTH--LAST--NAME%28" + Author + "%29+AND+AUTH--FIRST%28I.%29&st1=" + Author + "&st2=" + Initial + "&selectionPageSearch=anl&reselectAuthor=false&activeFlag=false&showDocument=false&resultsPerPage=20&offset=1&jtp=false&currentPage=1&previousSelectionCount=0&tooManySelections=false&previousResultCount=0&authSubject=LFSC&authSubject=HLSC&authSubject=PHSC&authSubject=SOSC&exactAuthorSearch=false&showFullList=false&authorPreferredName=&origin=AuthorNamesList";
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

                Object[] idsNode = node.evaluateXPath("//div[@id=\"srchResultsList\"]/*/li[@class=\"dataCol2\"]//div[@class=\"txtSmaller\"][1]");
                int i = 0;
                boolean g = false;
                if (idsNode.length > 1) {
                   return null;
                } else{
                    for (Object o : idsNode) {
                        i++;
                        TagNode aNode = (TagNode) o;
                        String test = aNode.getText().toString();
                        if (test.contains(Author + ", " + Initial.substring(0, 1))) {
                            break;
                        }
                    }
                    Object[] hrefNode = node.evaluateXPath("(//div[@id=\"srchResultsList\"]//li[@class=\"dataCol2\"])[" + i + "]//a[@href]");
                    for (Object h : hrefNode) {
                        TagNode bNode = (TagNode) h;
                        String href = bNode.getAttributeByName("href").toString();
                        String part = href.substring(href.indexOf("authorId=") + 9);
                        String id = part.substring(0, part.indexOf("&"));
                        stoixeia.add(id);
                        break;
                    }
                    Object[] FieldNode = node.evaluateXPath("(//div[@id=\"srchResultsList\"]//li[@class=\"dataCol4\"])[" + i + "]/div");
                    for (Object o : FieldNode) {
                        TagNode aNode = (TagNode) o;
                        stoixeia.add(aNode.getText().toString());
                        break;
                    }
                    Object[] AffNode = node.evaluateXPath("(//div[@id=\"srchResultsList\"]//li[@class=\"dataCol5\"])[" + i + "]/div");
                    for (Object o : AffNode) {
                        TagNode aNode = (TagNode) o;
                        stoixeia.add(aNode.getText().toString());
                        break;
                    }
                    Object[] CityNode = node.evaluateXPath("(//div[@id=\"srchResultsList\"]//li[@class=\"dataCol6\"])[" + i + "]/div");
                    for (Object o : CityNode) {
                        TagNode aNode = (TagNode) o;
                        stoixeia.add(aNode.getText().toString());
                        break;
                    }
                    Object[] CountryNode = node.evaluateXPath("//div[@id=\"srchResultsList\"]//li[@class=\"dataCol7\"][" + i + "]/div");
                    for (Object o : CountryNode) {
                        TagNode aNode = (TagNode) o;
                        stoixeia.add(aNode.getText().toString());
                        break;
                    }
                    if (stoixeia.size() > 3) {
                        return stoixeia;
                    }
                }
            } finally {
                httpClient.getConnectionManager().shutdown();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
