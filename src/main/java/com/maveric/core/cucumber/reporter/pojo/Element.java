
package com.maveric.core.cucumber.reporter.pojo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect
@Getter
@Setter
public class Element {

    @JsonProperty("line")
    public Integer line;
    @JsonProperty("name")
    public String name;
    @JsonProperty("description")
    public String description;
    @JsonProperty("type")
    public String type;
    @JsonProperty("keyword")
    public String keyword;
    @JsonProperty("steps")
    public List<Step> steps = new ArrayList<Step>();
    @JsonProperty("start_timestamp")
    public String startTimestamp;
    @JsonProperty("before")
    public List<Before> before = new ArrayList<Before>();
    public List<After> after = new ArrayList<After>();
    @JsonProperty("id")
    public String id;
    @JsonProperty("tags")
    public List<Tag> tags = new ArrayList<Tag>();

}
