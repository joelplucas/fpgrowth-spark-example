package papis2017.demo.sparkProcessors;

import java.util.List;
import org.apache.spark.api.java.function.Function;

/**
 * Created by joelpl on 15/06/17.
 */

public class FilterFreqItemByLength implements Function<List<String>, Boolean> {

    private int minProps;

    public FilterFreqItemByLength(int minProps) {
        this.minProps = minProps;
    }

    @Override
    public Boolean call(List<String> freqItems) {
        if(freqItems != null && freqItems.size()>= this.minProps) {
            return true;
        } else {
            return false;
        }
    }
}