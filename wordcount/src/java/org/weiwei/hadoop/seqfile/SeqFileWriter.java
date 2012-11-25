package org.weiwei.hadoop.seqfile;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.SnappyCodec;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author weiwei
 */
public class SeqFileWriter {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            printUsage();
            System.exit(-1);
        }
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        SequenceFile.Writer writer = SequenceFile.createWriter(fs, conf, new Path(args[1]), Text.class, NullWritable.class, SequenceFile.CompressionType.BLOCK, new SnappyCodec());
        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        String line = br.readLine();
        while (line != null) {
            String[] elems = line.split("\t");
            writer.append(new Text(elems[0]), NullWritable.get());
            line = br.readLine();
        }
        writer.close();
        br.close();
    }

    private static void printUsage() {
        System.out.println("SeqFileWriter input output");
    }
}
