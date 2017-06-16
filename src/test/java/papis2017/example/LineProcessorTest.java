package papis2017.example;

import com.holdenkarau.spark.testing.SharedJavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;
import papis2017.demo.sparkProcessors.LineProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joelpl on 15/06/17.
 */
public class LineProcessorTest extends SharedJavaSparkContext {

    private List<String> inputEntries;

    @Before
    public void initialize() {
        inputEntries = new ArrayList<>();
    }

    @Test
    public void parseRegularLineTest() {
        inputEntries.add(";ADULT;GYM;LOWINCOME;");
        JavaRDD<String> rddEntries = jsc().parallelize(inputEntries);
        // simulating a Spark processor
        JavaRDD<List<String>> userCorrelations = rddEntries.map(new LineProcessor());

        List<String> userInterests = userCorrelations.first();

        Assert.assertEquals(3, userInterests.size());
        Assert.assertTrue(userInterests.get(0).equals("ADULT"));
        Assert.assertTrue(userInterests.get(1).equals("GYM"));
        Assert.assertTrue(userInterests.get(2).equals("LOWINCOME"));
    }

    @Test
    public void parseTwoPropertiesLineTest() {
        inputEntries.add("MALE;;GYM;;");
        JavaRDD<String> rddEntries = jsc().parallelize(inputEntries);
        // simulating a Spark processor
        JavaRDD<List<String>> userCorrelations = rddEntries.map(new LineProcessor());
        List<String> userInterests = userCorrelations.first();

        Assert.assertEquals(2, userInterests.size());
        Assert.assertTrue(userInterests.get(0).equals("MALE"));
        Assert.assertTrue(userInterests.get(1).equals("GYM"));
    }
}