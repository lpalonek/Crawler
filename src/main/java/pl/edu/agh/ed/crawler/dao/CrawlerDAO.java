package pl.edu.agh.ed.crawler.dao;

import pl.edu.agh.ed.crawler.beans.Event;
import pl.edu.agh.ed.crawler.beans.Rating;
import pl.edu.agh.ed.crawler.beans.Talk;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by lpalonek on 26/10/14.
 */
public class CrawlerDAO {

    private Connection conn;

    public CrawlerDAO(){
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", "admin", "password");
//            conn = DriverManager.getConnection("postgresql://admin:password@localhost/test");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void terminate(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initDB() {
//        initializeTalks();
//        initializeEvents();
//        initializeRatings();
        initializeUsers();
    }

    public List<Integer> getUsers(){
        List<Integer> result = new LinkedList<>();
        Statement stmnt = null;
        try {
            stmnt = conn.createStatement();
            String sql = "select distinct \"profileId\" from comments order by \"profileId\";";
            ResultSet rs = stmnt.executeQuery(sql);
            while( rs.next()){
                result.add(rs.getInt("profileId"));
            }
            stmnt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void initializeTalks(){
        deleteTable("talks");
        createTalks();
    }

    private void initializeEvents(){
        deleteTable("events");
        createEvents();
    }

    private void initializeRatings(){
        deleteTable("ratings");
        createRatings();
    }

    private void initializeUsers(){
        deleteTable("users");
        createUsers();
    }


    private void createTalks(){
        String sql = "CREATE TABLE talks" +
                "(id INT PRIMARY KEY NOT NULL," +
                "event_id INT," +
                "name TEXT," +
                "description TEXT," +
                "slug TEXT," +
                "native_language_code VARCHAR(10)," +
                "published_at TIMESTAMP," +
                "recorded_at TIMESTAMP," +
                "updated_at TIMESTAMP," +
                "released_at TIMESTAMP," +
                "viewed_count INT);";
        createTable(sql);
    }

    private void createEvents(){
        String sql = "CREATE TABLE events" +
                "(id INT PRIMARY KEY NOT NULL," +
                "name TEXT," +
                "description TEXT," +
                "header_text TEXT," +
                "slug TEXT," +
                "url TEXT," +
                "starts_at TIMESTAMP);";
        createTable(sql);
    }

    private void createRatings(){
        String sql = "CREATE TABLE ratings" +
                "(ratingid INT PRIMARY KEY NOT NULL," +
                "rating INT," +
                "talkId INT," +
                "ratingWordId INT," +
                "name TEXT);";
        createTable(sql);
    }

    private void createUsers(){
        String sql = "CREATE TABLE users" +
                "(id INT PRIMARY KEY NOT NULL," +
                "user_name varchar(50)," +
                "country varchar(50)," +
                "city VARCHAR(50)," +
                "work_title TEXT," +
                "work_place TEXT," +
                "website TEXT," +
                "languages TEXT," +
                "areas TEXT," +
                "universities TEXT)";
        createTable(sql);
    }

    private void createTable(String sql){
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        }catch(SQLException s){
            s.printStackTrace();
        }
    }

    private void deleteTable(String table){
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = "DROP TABLE " +table;
            stmt.executeUpdate(sql);
            stmt.close();
        }catch(SQLException s){
            s.printStackTrace();
        }
    }

    public <T> void insertData(List<T> list, Class<T> clazz){
        try {
            T t = clazz.newInstance();

//            System.out.println(list.get(0).getClass());
            if( t instanceof Talk){
                insertTalk((List<Talk>) list);
            }
            if( t instanceof Event){
                insertEvents((List<Event>)list);
            }
            if( t instanceof Rating){
                insertRatings(list);
            }
        }
        catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void insertTalk(List<Talk> talks){
        for(Talk talk : talks){
//            Talk talk = (Talk)((Map) entry).get("talk");
            String sql = "INSERT INTO TALKS "+
                    "(id, event_id," +
                    " name, description," +
                    " slug, native_language_code," +
                    " published_at, recorded_at," +
                    " updated_at, viewed_count) VALUES" +
                    "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = null;
            try {
                statement = conn.prepareStatement(sql);
                statement.setLong(1, talk.getId());
                statement.setLong(2, talk.getEventId());
                statement.setString(3, talk.getName());
                statement.setString(4, talk.getDescritpion());
                statement.setString(5, talk.getSlug());
                statement.setString(6, talk.getNativeLanguageCode());
                statement.setTimestamp(7, new Timestamp(talk.getPublishedAt().getTime()));
                statement.setTimestamp(8, new Timestamp(talk.getRecorderAt().getTime()));
                statement.setTimestamp(9, new Timestamp(talk.getUpdatedAt().getTime()));
//                statement.setTimestamp(10, new Timestamp(talk.getReleasedAt().getTime()));
                statement.setLong(10, talk.getVieCount());
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Inserted "+talks.size()+" new rows");
    }

    private <T>void insertEvents(List<T> events){
        for(T entry: events){
            Event event = (Event)((Map) entry).get("event");
            String sql = "INSERT INTO events "+
                    "(id, name, description, header_text, slug, url, starts_at)"+
                    "values(?, ?, ?, ?, ?, ? ,?)";

            PreparedStatement statement = null;
            try {
                statement = conn.prepareStatement(sql);
                statement.setLong(1, event.getId());
                statement.setString(2, event.getName());
                statement.setString(3, event.getDescription());
                statement.setString(4, event.getHeaderText());
                statement.setString(5, event.getSlug());
                statement.setString(6, event.getUrl());
                statement.setTimestamp(7, new Timestamp(event.getStartsAt().getTime()));
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Inserted "+events.size()+" new rows");
    }

    private <T>void insertRatings(List<T> ratings){
        for(T entry: ratings){
            Rating rating = (Rating)entry;
            String sql = "INSERT INTO ratings "+
                    "(ratingid, rating, talkId, ratingWordId, name)"+
                    "values(?, ?, ?, ?, ?)";

            PreparedStatement statement = null;
            try {
                statement = conn.prepareStatement(sql);
                statement.setLong(1, rating.getRatingId());
                statement.setLong(2, rating.getRating());
                statement.setLong(3, rating.getTalkid());
                statement.setLong(4, rating.getRatingwordid());
                statement.setString(5, rating.getName());
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Inserted "+ratings.size()+" new rows");
    }

    public void insertUsers( Long id, String userName,
                             String country, String city,
                             String workTitle, String workPlace,
                             String website, String languages,
                             String areas, String universities){
        String sql = "INSERT INTO users" +
                "(id, user_name," +
                " country, city," +
                " work_title, work_place," +
                " website, languages," +
                " areas, universities) VALUES" +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = null;
        try{
            statement = conn.prepareStatement(sql);
            statement.setLong(1, id);
            statement.setString(2, userName);
            statement.setString(3, country);
            statement.setString(4, city);
            statement.setString(5, workTitle);
            statement.setString(6, workPlace);
            statement.setString(7, website);
            statement.setString(8, languages);
            statement.setString(9, areas);
            statement.setString(10, universities);
            statement.executeUpdate();
            statement.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}

