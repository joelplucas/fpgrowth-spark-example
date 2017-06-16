package papis2017.example;

import com.holdenkarau.spark.testing.SharedJavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import papis2017.demo.sparkProcessors.FilterFreqItemByLength;
import papis2017.demo.sparkProcessors.LineProcessor;

import java.util.List;

/**
 * Created by joelpl on 15/06/17.
 */
public class FilterFreqItemByLengthTest extends SharedJavaSparkContext {

    private JavaRDD<List<String>> population;

    @Before
    public void initialize() {
        JavaRDD<String> lines = jsc().textFile("src/test/resources/few_profiles.csv");
        this.population = lines.map(new LineProcessor());
    }

    @Test
    public void filterTwoPropertiesTest() {
        JavaRDD<List<String>> filteredEntries = population.filter(new FilterFreqItemByLength(2));
        Assert.assertEquals(3L, filteredEntries.count());
    }

    @Test
    public void filterThreePropertiesTest() {
        JavaRDD<List<String>> filteredEntries = population.filter(new FilterFreqItemByLength(3));
        Assert.assertEquals(1L, filteredEntries.count());
    }

}
