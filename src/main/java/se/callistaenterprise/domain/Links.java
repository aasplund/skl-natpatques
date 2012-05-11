package se.callistaenterprise.domain;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Links {
    @XmlElement private List<URI> links;
    
    public Links() {
        links = new ArrayList<URI>();
    }
    
    public Links(int size) {
        links = new ArrayList<URI>(size);
    }
    
    public List<URI> getLinks() {
        return links;
    }
    
    public void add(URI link) {
        links.add(link);
    }
    
    public Links sort() {
        Collections.sort(links);
        return this;
    }
}
