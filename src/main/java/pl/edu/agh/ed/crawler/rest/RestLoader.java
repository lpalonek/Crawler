package pl.edu.agh.ed.crawler.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import pl.edu.agh.ed.crawler.beans.Event;
import pl.edu.agh.ed.crawler.beans.Rating;
import pl.edu.agh.ed.crawler.beans.Talk;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lpalonek on 26/10/14.
 */
public class RestLoader {

    private final String apiKey = "cypfqs5rytc3pk9kb28crvwa";
//    private final String apiKey = "b6azk6recjaxp59x4vxqw97n";

    public static final String PATH_TALKS = "talks.json";
    public static final String PATH_EVENTS = "events.json";
    public static final String PATH_RATINGS = "ratings.json";

    public Talk loadTalks(String path) throws IOException {
        String json = getResponse(path);
//        String json = "{\"talk\":{\"id\":1,\"name\":\"Al Gore: Averting the climate crisis\",\"description\":\"With the same humor and humanity he exuded in <em>An Inconvenient Truth,<\\/em> Al Gore spells out 15 ways that individuals can address climate change immediately, from buying a hybrid to inventing a new, hotter \\\"brand name\\\" for global warming.\",\"slug\":\"al_gore_on_averting_climate_crisis\",\"recorded_at\":\"2006-02-25 00:00:00\",\"published_at\":\"2006-06-27 00:11:00\",\"updated_at\":\"2014-07-03 20:42:24\",\"viewed_count\":2611710,\"emailed_count\":720,\"commented_count\":241,\"event\":{\"id\":3,\"name\":\"TED2006\"},\"images\":[{\"image\":{\"size\":\"113x85\",\"url\":\"http:\\/\\/images.ted.com\\/images\\/ted\\/205_113x85.jpg\"}},{\"image\":{\"size\":\"240x180\",\"url\":\"http:\\/\\/images.ted.com\\/images\\/ted\\/205_240x180.jpg\"}},{\"image\":{\"size\":\"480x360\",\"url\":\"http:\\/\\/images.ted.com\\/images\\/ted\\/205_480x360.jpg\"}}],\"image_16x9\":false,\"media\":{\"internal\":{\"64k\":{\"uri\":\"http:\\/\\/download.ted.com\\/talks\\/AlGore_2006-64k.mp4?apikey=bebe814011ab2366a1a8a57a1876e3abc04ddcb2\"},\"180k\":{\"uri\":\"http:\\/\\/download.ted.com\\/talks\\/AlGore_2006-180k.mp4?apikey=bebe814011ab2366a1a8a57a1876e3abc04ddcb2\"},\"320k\":{\"uri\":\"http:\\/\\/download.ted.com\\/talks\\/AlGore_2006-320k.mp4?apikey=bebe814011ab2366a1a8a57a1876e3abc04ddcb2\"},\"450k\":{\"uri\":\"http:\\/\\/download.ted.com\\/talks\\/AlGore_2006-450k.mp4?apikey=bebe814011ab2366a1a8a57a1876e3abc04ddcb2\"},\"600k\":{\"uri\":\"http:\\/\\/download.ted.com\\/talks\\/AlGore_2006-600k.mp4?apikey=bebe814011ab2366a1a8a57a1876e3abc04ddcb2\"},\"950k\":{\"uri\":\"http:\\/\\/download.ted.com\\/talks\\/AlGore_2006-950k.mp4?apikey=bebe814011ab2366a1a8a57a1876e3abc04ddcb2\"},\"podcast-light\":{\"uri\":\"http:\\/\\/download.ted.com\\/talks\\/AlGore_2006-light.mp4?apikey=bebe814011ab2366a1a8a57a1876e3abc04ddcb2\"},\"podcast-regular\":{\"uri\":\"http:\\/\\/download.ted.com\\/talks\\/AlGore_2006.mp4?apikey=bebe814011ab2366a1a8a57a1876e3abc04ddcb2\"},\"podcast-high\":{\"uri\":\"http:\\/\\/download.ted.com\\/talks\\/AlGore_2006-480p.mp4?apikey=bebe814011ab2366a1a8a57a1876e3abc04ddcb2\"},\"audio-podcast\":{\"uri\":\"http:\\/\\/download.ted.com\\/talks\\/AlGore_2006.mp3?apikey=bebe814011ab2366a1a8a57a1876e3abc04ddcb2\"},\"podcast-low-en\":{\"uri\":\"http:\\/\\/download.ted.com\\/talks\\/AlGore_2006-low-en.mp4?apikey=bebe814011ab2366a1a8a57a1876e3abc04ddcb2\"},\"podcast-high-en\":{\"uri\":\"http:\\/\\/download.ted.com\\/talks\\/AlGore_2006-480p-en.mp4?apikey=bebe814011ab2366a1a8a57a1876e3abc04ddcb2\"}}},\"languages\":{\"sq\":{\"name\":\"Albanian\"},\"ar\":{\"name\":\"Arabic\"},\"bg\":{\"name\":\"Bulgarian\"},\"zh-cn\":{\"name\":\"Chinese, Simplified\"},\"zh-tw\":{\"name\":\"Chinese, Traditional\"},\"hr\":{\"name\":\"Croatian\"},\"nl\":{\"name\":\"Dutch\"},\"en\":{\"name\":\"English\",\"native\":true},\"fr\":{\"name\":\"French\"},\"fr-ca\":{\"name\":\"French (Canada)\"},\"gl\":{\"name\":\"Galician\"},\"de\":{\"name\":\"German\"},\"el\":{\"name\":\"Greek\"},\"gu\":{\"name\":\"Gujarati\"},\"he\":{\"name\":\"Hebrew\"},\"hi\":{\"name\":\"Hindi\"},\"hu\":{\"name\":\"Hungarian\"},\"id\":{\"name\":\"Indonesian\"},\"it\":{\"name\":\"Italian\"},\"ja\":{\"name\":\"Japanese\"},\"ko\":{\"name\":\"Korean\"},\"lv\":{\"name\":\"Latvian\"},\"lt\":{\"name\":\"Lithuanian\"},\"rup\":{\"name\":\"Macedo\"},\"mk\":{\"name\":\"Macedonian\"},\"fa\":{\"name\":\"Persian\"},\"pl\":{\"name\":\"Polish\"},\"pt\":{\"name\":\"Portuguese\"},\"pt-br\":{\"name\":\"Portuguese, Brazilian\"},\"ro\":{\"name\":\"Romanian\"},\"ru\":{\"name\":\"Russian\"},\"sr\":{\"name\":\"Serbian\"},\"sk\":{\"name\":\"Slovak\"},\"sl\":{\"name\":\"Slovenian\"},\"es\":{\"name\":\"Spanish\"},\"sw\":{\"name\":\"Swahili\"},\"sv\":{\"name\":\"Swedish\"},\"tl\":{\"name\":\"Tagalog\"},\"th\":{\"name\":\"Thai\"},\"tr\":{\"name\":\"Turkish\"},\"uk\":{\"name\":\"Ukrainian\"},\"ur\":{\"name\":\"Urdu\"}},\"tags\":[{\"tag\":\"TED Conference\"},{\"tag\":\"alternative energy\"},{\"tag\":\"cars\"},{\"tag\":\"climate change\"},{\"tag\":\"culture\"},{\"tag\":\"environment\"},{\"tag\":\"global issues\"},{\"tag\":\"politics\"},{\"tag\":\"science\"},{\"tag\":\"sustainability\"},{\"tag\":\"technology\"}],\"themes\":[{\"theme\":{\"id\":7,\"name\":\"Presentation Innovation\"}},{\"theme\":{\"id\":15,\"name\":\"A Greener Future?\"}},{\"theme\":{\"id\":23,\"name\":\"Bold Predictions, Stern Warnings\"}},{\"theme\":{\"id\":25,\"name\":\"Design That Matters\"}},{\"theme\":{\"id\":28,\"name\":\"Not Business as Usual\"}},{\"theme\":{\"id\":30,\"name\":\"Technology, History and Destiny\"}}],\"speakers\":[{\"speaker\":{\"id\":2,\"name\":\"Al Gore\"}}],\"ratings\":[{\"rating\":{\"id\":7,\"name\":\"Funny\",\"count\":443}},{\"rating\":{\"id\":3,\"name\":\"Courageous\",\"count\":125}},{\"rating\":{\"id\":2,\"name\":\"Confusing\",\"count\":46}},{\"rating\":{\"id\":1,\"name\":\"Beautiful\",\"count\":50}},{\"rating\":{\"id\":21,\"name\":\"Unconvincing\",\"count\":209}},{\"rating\":{\"id\":11,\"name\":\"Longwinded\",\"count\":90}},{\"rating\":{\"id\":8,\"name\":\"Informative\",\"count\":368}},{\"rating\":{\"id\":10,\"name\":\"Inspiring\",\"count\":341}},{\"rating\":{\"id\":22,\"name\":\"Fascinating\",\"count\":95}},{\"rating\":{\"id\":9,\"name\":\"Ingenious\",\"count\":50}},{\"rating\":{\"id\":24,\"name\":\"Persuasive\",\"count\":230}},{\"rating\":{\"id\":23,\"name\":\"Jaw-dropping\",\"count\":84}},{\"rating\":{\"id\":26,\"name\":\"Obnoxious\",\"count\":97}},{\"rating\":{\"id\":25,\"name\":\"OK\",\"count\":143}}]}}";
        ObjectMapper mapper = getMapper();
        if(json != null) {
            String temp = mapper.readTree(json).get("talk").toString();
            Talk result = mapper.readValue(temp, Talk.class);

            return result;
        }else{
            return null;
        }
    }

    public <T> List<T> loadData(Integer limit, Integer offset, String field, String path, Class<T> clazz){
        String json = getResponse(path, "limit", limit, "offset", offset);
//        String json = FileUtils.readFileToString(new File("/tmp/test.json"));
        JSONObject talks = (JSONObject) JSONValue.parse(json);
        JSONArray talk;
        if(talks.containsKey(field)){
            talk = (JSONArray) talks.get(field);
        }else{
            return new ArrayList<>();
        }
        List<T> result = null;
        try {
            result = getMapper().readValue(talk.toString(), getTypeReference(clazz));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getResponse( String path, Object... objects){
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("https://api.ted.com/v1/")
                .register(String.class)
                .path(path)
                .queryParam("api-key", apiKey);
        target = appendParam(target, objects);
        String result = null;
        try {
            result = target.request(MediaType.APPLICATION_JSON_TYPE)
                    .get(String.class);
        }catch( ForbiddenException | ServerErrorException e){
            System.out.println(path+" "+e.getMessage()+" trying again...");
            return getResponse(path, objects);
        }catch(NotFoundException e){
            System.out.println(path+" "+e.getMessage()+" skipping...");
        }
        return result;
    }

    private WebTarget appendParam(WebTarget target, Object... objects){
        for( int i = 0; i < objects.length; i+=2){
            target = target.queryParam((String) objects[i], objects[i+1]);
        }
        return target;
    }

    private ObjectMapper getMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        return mapper;
    }

    private <T> TypeReference getTypeReference(Class<T> clazz){
        try {
            T t = clazz.newInstance();
            if( t instanceof Talk){
                return new TypeReference<List<Map<String, Talk>>>() {};
            }
            if( t instanceof Event){
                return new TypeReference<List<Map<String, Event>>>() {};
            }
            if( t instanceof Rating){
                return new TypeReference<List< Rating>>() {};
            }

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
