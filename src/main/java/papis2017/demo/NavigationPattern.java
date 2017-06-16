package papis2017.demo;

import org.mongodb.morphia.annotations.Entity;

import java.util.List;

/**
 * Created by joelpl on 15/06/17.
 */
@Entity(noClassnameStored = true)
public class NavigationPattern implements Comparable<NavigationPattern> {

    private List<String> freqItems;
    private double relevance;

    public NavigationPattern() {
    }

    public NavigationPattern(List<String> freqItems, long frequency, long totalUsers) {
        this.freqItems = freqItems;
        this.relevance = (int)frequency;
        this.relevance = (100*frequency)/totalUsers;
    }


    public List<String> getFreqItems() {
        return freqItems;
    }

    public void setFreqItems(List<String> freqItems) {
        this.freqItems = freqItems;
    }

    public double getRelevance() {
        return relevance;
    }

    public void setRelevance(double relevance) {
        this.relevance = relevance;
    }


    @Override
    public int compareTo(NavigationPattern o) {
        if(o.getRelevance() > this.getRelevance()) {
            return 1;
        } else if (o.getRelevance() < this.getRelevance()) {
            return -1;
        } else { //equals
            return 0;
        }
    }

}