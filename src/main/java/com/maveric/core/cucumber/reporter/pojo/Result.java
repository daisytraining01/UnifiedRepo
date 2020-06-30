
package com.maveric.core.cucumber.reporter.pojo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonAutoDetect
@Getter
@Setter
public class Result {

    @JsonProperty("duration")
    public Long duration;
    @JsonProperty("status")
    public String status;
    @JsonProperty("error_message")
    public String errorMessage;

}
