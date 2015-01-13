package pl.edu.agh.ed.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.edu.agh.ed.crawler.dao.CrawlerDAO;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.RunnableFuture;

/**
 * Created by lpalonek on 27/10/14.
 */
public class UserCrawler implements Runnable{
    List<Integer> userIds;

    public UserCrawler(List<Integer> userIds) {
        this.userIds = userIds;
    }

    @Override
    public void run() {
        crawlTedUsers();
    }


    public void crawlTedUsers(){
        for( Integer id : userIds){
            System.out.println(id);
            parseHTML(getUrlSource(id), id);
        }
    }


    private String getUrlSource(Integer id){
        URL url = null;
        StringBuilder builder = new StringBuilder();
        try {
            url = new URL("http://www.ted.com/profiles/"+id);
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result;
            while((result = in.readLine()) != null) {
                builder.append(result);
            }
            in.close();
        } catch ( IOException e) {
            System.out.print(e.getMessage());
            return "";
        }
        return builder.toString();
    }

    public void parseHTML(String html, int id){

        Document doc = Jsoup.parse(html);
//        Document doc = null;
//        try {
//            doc = Jsoup.parse(new File("/tmp/test.html"), String.valueOf(Charset.defaultCharset()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        String country = getPlace(doc, true);
        String city = getPlace(doc, false);

        String workTitle = getWorkTitle(doc, 0);
        String workPlace = getWorkTitle(doc, 1);

        String website = getWebsites(doc);

        String userName = getUserName(doc);

        String languages = getLanguages(doc);

        String areas = getExpertiseAreas(doc);

        String universities = getUniversities(doc);
        CrawlerDAO db = new CrawlerDAO();
        db.insertUsers((long)id, userName,
                country, city,
                workTitle, workPlace,
                website, languages,
                areas, universities);
        db.terminate();
    }


    private String getWorkTitle( Document doc, int field){
        Elements elements = doc.getElementsByClass("profile-header__summary");
        if( elements.isEmpty() || elements.size() == 0 || elements.get(0).childNodes().isEmpty()
                || (elements.get(0).childNode(0).toString().split(", ").length == 1 && field == 1)){
            return "";
        }else{
            String text = elements.get(0).childNode(0).toString();
            if(text.contains("<div")){
                return "";
            }else {
                String result = text.split(", ")[field];
                if(field == 0) {
                    return result.substring(1);
                }else{
                    return result;
                }
            }
        }
    }

    private String getPlace(Document doc, boolean country){
        Elements elements = doc.getElementsByClass("profile-header__summary");
        if(elements.isEmpty()){
            return "";
        }else{
            Elements childElements = elements.get(0).getAllElements();
            if(childElements.isEmpty() || childElements.size() < 2){
                return "";
            }else{
                Element place = childElements.get(1);
                if(country){
                    return place.select("a").text();
                }else{
                    String[] cityList = place.select("div").text().split(", ");
                    String city = "";
                    if( !cityList[0].equals(country)){
                        city = cityList[0];
                    }
                    return city;
                }
            }
        }
    }

    private String getUserName(Document doc){
        Elements elements = doc.select("title");
        if(elements.isEmpty()){
            return "";
        }else{
            return elements.get(0).text().split(" \\|")[0];
        }
    }

    private String getLanguages(Document doc){
        Elements elements = doc.select("a[href~=/people\\?languages]");
        if( elements.isEmpty()){
            return "";
        }else{
            StringBuilder builder = new StringBuilder();
            for(Element element : elements){
                builder.append(element.text()+":");
            }
            return builder.toString();
        }
    }

    private String getExpertiseAreas( Document doc){
        Elements elements = doc.select("h3:contains(Areas of Expertise)");
        if(elements.isEmpty()){
            return "";
        }else {
            Element element = elements.parents().get(0);
            return element.select("p").text();
        }
    }

    private String getUniversities(Document doc){
        Elements elements = doc.select("a[href~=/people\\?universities]");
        if( elements.isEmpty()){
            return "";
        }else{
            StringBuilder builder = new StringBuilder();
            for(Element element : elements){
                builder.append(element.text()+":");
            }
            return builder.toString();
        }    }

    private String getWebsites(Document doc){
        Elements elements = doc.getElementsByClass("profile-header__link__text");
        if(elements.isEmpty()){
            return "";
        }else{
            return elements.get(0).text();
        }
    }
}
