package org.weiwei.hadoop.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author weiwei
 */
public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] elems = line.split("\t");
        if (elems != null && elems.length == 2) {
            context.write(new Text(elems[0]), new IntWritable(Integer.parseInt(elems[1])));
        }
    }
}
