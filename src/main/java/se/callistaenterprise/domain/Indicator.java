package se.callistaenterprise.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
@XmlRootElement
public class Indicator {
    @XmlAttribute private String name;
    @XmlAttribute private Integer value;
    
    @SuppressWarnings("unused")
    private Indicator() {
    }
    
    public Indicator(String name, Integer value) {
        this.name = name;
        this.value = value;
    }
    
    public String getName() {
        return name;
    }
    
    public Integer getValue() {
        return value;
    }
}
