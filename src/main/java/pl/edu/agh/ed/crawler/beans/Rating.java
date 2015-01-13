package pl.edu.agh.ed.crawler.beans;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by lpalonek on 27/10/14.
 */
public class Rating {

    @JsonProperty("ratingid")
    private Long ratingId;

    @JsonProperty("rating")
    private Long rating;

    @JsonProperty("talkid")
    private Long talkid;

    @JsonProperty("ratingwordid")
    private Long ratingwordid;

    @JsonProperty("name")
    private String name;

    public Long getRatingId() {
        return ratingId;
    }

    public void setRatingId(Long ratingId) {
        this.ratingId = ratingId;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    public Long getTalkid() {
        return talkid;
    }

    public void setTalkid(Long talkid) {
        this.talkid = talkid;
    }

    public Long getRatingwordid() {
        return ratingwordid;
    }

    public void setRatingwordid(Long ratingwordid) {
        this.ratingwordid = ratingwordid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rating rating = (Rating) o;

        if (ratingId != null ? !ratingId.equals(rating.ratingId) : rating.ratingId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return ratingId != null ? ratingId.hashCode() : 0;
    }
}
