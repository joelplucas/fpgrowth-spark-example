package papis2017.demo;

import com.google.common.base.CharMatcher;
import org.mongodb.morphia.annotations.Entity;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joelpl on 15/06/17.
 */
@Entity(noClassnameStored = true)
public class NavigationPattern implements Comparable<NavigationPattern> {

    private List<String> freqItems;
    private float relevance;

    public NavigationPattern() {
    }

    public NavigationPattern(List<String> freqItemsPattern, long frequency, long totalUsers) throws UnsupportedEncodingException {
        this.freqItems = removeUnicode(freqItemsPattern);
        this.relevance = (int)frequency;
        this.relevance = (100*(float)frequency)/(float)totalUsers;
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

    public void setRelevance(float relevance) {
        this.relevance = relevance;
    }

    // remove extra chars added wrongly by MLlib
    private List<String> removeUnicode(List<String> freqItemsPattern) {
        List<String> freqItems = new ArrayList<>();
        for(String item : freqItemsPattern) {
            freqItems.add(CharMatcher.INVISIBLE.removeFrom(item));
        }
        return freqItems;
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