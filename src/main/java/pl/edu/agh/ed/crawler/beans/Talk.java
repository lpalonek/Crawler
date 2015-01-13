package pl.edu.agh.ed.crawler.beans;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Map;

/**
 * Created by lpalonek on 26/10/14.
 */
public class Talk {

    @JsonProperty("id")
    private Long id;


    private Long eventId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String descritpion;

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("native_language_code")
    private String nativeLanguageCode;

    @JsonProperty("published_at")
    private Date publishedAt;

    @JsonProperty("recorded_at")
    private Date recorderAt;

    @JsonProperty("updated_at")
    private Date updatedAt;

    @JsonProperty("released_at")
    private Date releasedAt;

    @JsonProperty("viewed_count")
    private Integer vieCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("event")
    public void setEventId(Map<String,Object> event) {
        Integer temp = (Integer) event.get("id");
        eventId = temp.longValue();
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescritpion() {
        return descritpion;
    }

    public void setDescritpion(String descritpion) {
        this.descritpion = descritpion;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getNativeLanguageCode() {
        return nativeLanguageCode;
    }

    public void setNativeLanguageCode(String nativeLanguageCode) {
        this.nativeLanguageCode = nativeLanguageCode;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Date getRecorderAt() {
        return recorderAt;
    }

    public void setRecorderAt(Date recorderAt) {
        this.recorderAt = recorderAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getReleasedAt() {
        return releasedAt;
    }

    public void setReleasedAt(Date releasedAt) {
        this.releasedAt = releasedAt;
    }

    public Integer getVieCount() {
        return vieCount;
    }

    public void setVieCount(Integer vieCount) {
        this.vieCount = vieCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Talk talk = (Talk) o;

        if (id != null ? !id.equals(talk.id) : talk.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
