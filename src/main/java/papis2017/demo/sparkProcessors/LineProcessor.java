package papis2017.demo.sparkProcessors;

import org.apache.spark.api.java.function.Function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by joelpl on 15/06/17.
 */
public class LineProcessor implements Function<String, List<String>> {

    @Override
    public List<String> call(String line) {
        String[] navigationInterests = line.split(";");
        List<String> validWords = getValidWords(navigationInterests);
        //List<String> validWords = Arrays.asList(navigationInterests);
        return validWords;
    }

    private List<String> getValidWords(String[] navigationInterests) {
        List<String> validWords = new ArrayList<>();
        for(String navigationInterest : navigationInterests) {
            // removing ";" and blank spaces
            if(navigationInterest.length() > 1) {
                validWords.add(navigationInterest);
            }
        }
        return validWords;
    }
}
