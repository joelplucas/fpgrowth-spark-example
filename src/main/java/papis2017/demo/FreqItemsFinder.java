package papis2017.demo;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mongodb.MongoClient;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.fpm.FPGrowth;
import org.apache.spark.mllib.fpm.FPGrowth.FreqItemset;
import org.apache.spark.mllib.fpm.FPGrowthModel;
import org.apache.spark.rdd.RDD;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import papis2017.demo.sparkProcessors.FilterFreqItemByLength;
import papis2017.demo.sparkProcessors.LineProcessor;

/**
 * Created by joelpl on 15/06/17.
 */
public class FreqItemsFinder {

    private static double MIN_SUPPORT = 0.05;
    private static int MAX_RULES_COUNT = 100;
    private static int MIN_FREQ_PROPERTIES = 2;

    public static void main(String[] args) throws UnsupportedEncodingException {
        if (args.length != 2) {
            System.err.println("Please define an input file path and a valid MongoDB host!");
            System.exit(1);
        }

        FreqItemsFinder freqItemsFinder = new FreqItemsFinder();
        List<NavigationPattern> navigationPatterns = freqItemsFinder.findPatternsWithSpark(args[0]);
        freqItemsFinder.savePatterns(args[1], navigationPatterns);
    }

    public List<NavigationPattern> findPatternsWithSpark(String filePath)  throws UnsupportedEncodingException {
        JavaSparkContext sc = new JavaSparkContext();

        // read data from file
        JavaRDD<String> userEntries = sc.textFile(filePath);
        // count num entries
        long profilesCount = userEntries.count();
        // split every line
        JavaRDD<List<String>> userInterests =  userEntries.map(new LineProcessor());
        // filter entries by properties length
        userInterests = userInterests.filter(new FilterFreqItemByLength(MIN_FREQ_PROPERTIES));

        // run FPGrowth
        FPGrowth fpg = new FPGrowth().setMinSupport(MIN_SUPPORT);
        FPGrowthModel<String> model = fpg.run(userInterests);

        // convert to a readable format
        List<NavigationPattern> navigationPatterns = rddmodelToStrings(model, profilesCount);
        return navigationPatterns;
    }

    private List<NavigationPattern> rddmodelToStrings(FPGrowthModel<String> model, long profilesCount) throws UnsupportedEncodingException {
        RDD<FreqItemset<String>> fpgFreqItems = model.freqItemsets();
        List<NavigationPattern> navigationPatterns = new ArrayList<>();

        if(fpgFreqItems != null) {
            List<FPGrowth.FreqItemset<String>> freqItems = fpgFreqItems.toJavaRDD().take(MAX_RULES_COUNT);
            for(FPGrowth.FreqItemset<String> items : freqItems) {
                if(!isUsefulRules(items)) {
                    continue;
                }
                NavigationPattern navigationPattern = new NavigationPattern(items.javaItems(), items.freq(), profilesCount);
                navigationPatterns.add(navigationPattern);
            }
        }
        return navigationPatterns;
    }

    private boolean isUsefulRules(FreqItemset<String> items) {
        return (items.javaItems().size()>=MIN_FREQ_PROPERTIES);
    }

    public void savePatterns(String mongoHost, List<NavigationPattern> navigationPatterns) {
        MongoClient mongoClient = new MongoClient(mongoHost);
        Datastore ds = new Morphia().createDatastore(mongoClient,"dockerSpark");
        ds.save(navigationPatterns);
    }
}
