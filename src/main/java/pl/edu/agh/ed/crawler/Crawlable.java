package pl.edu.agh.ed.crawler;

import pl.edu.agh.ed.crawler.beans.Talk;
import pl.edu.agh.ed.crawler.dao.CrawlerDAO;
import pl.edu.agh.ed.crawler.rest.RestLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lpalonek on 27/10/14.
 */
public class Crawlable<T> implements Runnable{
    private String field;

    private String path;

    private Class<T> clazz;

    public Crawlable(String field, String path, Class<T> clazz){
        this.field = field;
        this.path = path;
        this.clazz = clazz;
    }

    @Override
    public void run() {
        if(!path.equals("talks")) {
            int offset = 0;
            RestLoader restLoader = new RestLoader();
            List<T> result = restLoader.loadData(50, offset, path, field, clazz);
            CrawlerDAO db = new CrawlerDAO();
            while (result.size() > 0) {
                System.out.println("Downloaded " + offset + " so far: " + field);
                offset += 50;
                db.insertData(result, clazz);
                result = restLoader.loadData(50, offset, path, field, clazz);
                if (result.size() > 50) {
                    break;
                }
            }
            db.terminate();
            System.out.println("No more " + field + " to download");
        }else{
            getTalks();
        }
    }

    private void getTalks(){
        RestLoader restLoader = new RestLoader();
        List<Talk> result = new ArrayList<>();
        Talk temp;

        for(int i = 1; i < 2117; ++i) {
            try {
                temp = restLoader.loadTalks("talks/" + i + ".json");
                if( temp != null && temp.getPublishedAt() != null) {
                    result.add(temp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        CrawlerDAO db = new CrawlerDAO();
        System.out.println("Downloaded " + 1857 + " so far: " + field);
        db.insertData(result, Talk.class);
        db.terminate();
        System.out.println("No more " + field + " to download");
    }

}
