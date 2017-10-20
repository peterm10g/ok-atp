package com.lsh.atp.api.model.baseVo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jingyuan on 16/10/18.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Salerule implements Serializable {

    @Deprecated
    @JsonProperty("rule_code")
    private long ruleCode;

    @JsonProperty("rule_name")
    private String ruleName;

    private List<String> dc;

    public Salerule(long ruleCode,String ruleName){
        this.ruleCode=ruleCode;
        this.ruleName=ruleName;
    }
}
