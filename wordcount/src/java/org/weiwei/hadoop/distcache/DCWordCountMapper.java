package org.weiwei.hadoop.distcache;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author weiwei
 */
public class DCWordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    private Path[] paths;

    protected void setup(Context context) throws IOException {
        paths = DistributedCache.getLocalCacheFiles(context.getConfiguration());
    }
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] elems = line.split("\t");
        Path file  = paths[0];
        BufferedReader bf = new BufferedReader(new FileReader(file.toString()));
        String word = bf.readLine();
        if (elems != null && elems.length == 2) {
            if(!elems[0].contentEquals(word)) {
                context.write(new Text(elems[0]), new IntWritable(Integer.parseInt(elems[1])));
            }
        }
        bf.close();
    }
}
