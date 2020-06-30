
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
public class Before {

    @JsonProperty("result")
    public Result result;
    @JsonProperty("match")
    public Match match;
    @JsonProperty("embeddings")
    public List<Embedding> embeddings = new ArrayList<Embedding>();
    @JsonProperty("output")
    public List<String> output = new ArrayList<String>();
}
