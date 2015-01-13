package pl.edu.agh.ed.crawler;

import pl.edu.agh.ed.crawler.beans.Event;
import pl.edu.agh.ed.crawler.beans.Rating;
import pl.edu.agh.ed.crawler.beans.Talk;
import pl.edu.agh.ed.crawler.dao.CrawlerDAO;
import pl.edu.agh.ed.crawler.rest.RestLoader;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lpalonek on 26/10/14.
 */
public class Crawler {
    public static void main(String [] args) throws ClassNotFoundException {
        ExecutorService executor = Executors.newFixedThreadPool(40) ;
        CrawlerDAO crawlerDAO = new CrawlerDAO();
//        RestLoader rest = new RestLoader();
//        try {
//            rest.loadTalks("z");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        crawlerDAO.initDB();
//        executor.execute(new Crawlable<Talk>(RestLoader.PATH_TALKS, "talks", Talk.class));
//        executor.execute(new Crawlable<Event>(RestLoader.PATH_EVENTS, "events", Event.class));
//        executor.execute(new Crawlable<Rating>(RestLoader.PATH_RATINGS, "ratings", Rating.class));
//
        Map<Integer, List<Integer>> data = generateUserIds(40);
        for(int i = 0; i < 40; ++i){
            executor.execute(new UserCrawler(data.get(i)));
        }
        UserCrawler crawler = new UserCrawler(new ArrayList<Integer>());
//        crawler.parseHTML("z",2);
        executor.shutdown();

    }

    public static Map<Integer, List<Integer>> generateUserIds(Integer numberOfThreads){
        CrawlerDAO db = new CrawlerDAO();
        Map<Integer, List<Integer>> result = new LinkedHashMap<>();
        List<Integer> users = db.getUsers();
        int usersPerThread = users.size()/numberOfThreads;
        for(int i = 0; i < numberOfThreads; ++i){
            List<Integer> temp = new LinkedList<>();
            for( int j = 0; j < usersPerThread; ++j){
                temp.add(users.get(0));
                users.remove(0);
            }
            result.put(i, temp);
        }
        List<Integer> tmp = result.get(0);
        tmp.addAll(users);
        result.put(0,tmp);
        db.terminate();
        return result;
    }

}
