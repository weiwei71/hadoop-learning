package org.weiwei.hadoop.distcache;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.weiwei.hadoop.util.HadoopUtils;
import org.weiwei.hadoop.wordcount.WordCountMapper;
import org.weiwei.hadoop.wordcount.WordCountReducer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author weiwei
 */
public class DistributeCacheTester {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
        if(args.length!=3) {
            HadoopUtils.printUsage("DistributeCacheTester input output distcache");
            System.exit(-1);
        }

        Configuration conf = new Configuration();
        DistributedCache.addCacheFile(new URI("/user/weiwei/stopword"), conf);
        Job job = new Job(conf, "SelectedWordCount");

        job.setJarByClass(DistributeCacheTester.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        job.setMapperClass(DCWordCountMapper.class);
        job.setCombinerClass(WordCountReducer.class);
        job.setReducerClass(WordCountReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));


        System.exit(job.waitForCompletion(true)?0:1);
    }
}
