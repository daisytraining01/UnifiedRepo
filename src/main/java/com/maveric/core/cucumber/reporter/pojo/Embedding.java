
package com.maveric.core.cucumber.reporter.pojo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonAutoDetect
@Getter
@Setter
public class Embedding {

    @JsonProperty("data")
    public String data;
    @JsonProperty("mime_type")
    public String mimeType;
    @JsonProperty("name")
    public String name;

}
