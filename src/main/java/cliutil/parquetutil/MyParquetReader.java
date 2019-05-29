package cliutil.parquetutil;

import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.SimpleGroup;
import org.apache.parquet.example.data.simple.SimpleGroupFactory;
import org.apache.parquet.hadoop.Footer;
import org.apache.parquet.hadoop.ParquetFileReader;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.ParquetRecordWriter;
import org.apache.parquet.hadoop.example.GroupReadSupport;
import org.apache.parquet.hadoop.example.GroupWriteSupport;
import org.apache.parquet.hadoop.metadata.BlockMetaData;
import org.apache.parquet.hadoop.util.HiddenFileFilter;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.MessageTypeParser;
import org.apache.parquet.schema.Types;

import javax.annotation.processing.SupportedSourceVersion;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhou1 on 2019/5/28.
 */
public class MyParquetReader {
    public static void main(String[] args) throws Exception{
//        GroupReadSupport readSupport = new GroupReadSupport();
//        ParquetReader.Builder<Group> reader=
//                ParquetReader.builder(readSupport, new Path(new URI("hdfs://192.168.13.128:9000/par/000000_0")));
//        ParquetReader<Group> reader1=reader.build();
//
//
//        //获取到parquet的元数据信息
//        Configuration config = new Configuration();
//        config.set("fs.defaultFS","hdfs://192.168.13.128:9000");
//        FileSystem fs = FileSystem.get(config);
//
////        List<FileStatus> statuses = Arrays.asList(fs.listStatus(new Path(new URI("hdfs://192.168.13.128:9000/par/000000_0")),
////                HiddenFileFilter.INSTANCE));
////        List<Footer> footers = ParquetFileReader.readAllFootersInParallelUsingSummaryFiles(config, statuses, false);
////
////        Footer footer = (Footer)footers.get(footers.size()-1);
////        List<BlockMetaData> blocks = footer.getParquetMetadata().getBlocks();
////        MessageType fileSchema = footer.getParquetMetadata().getFileMetaData().getSchema();
////        System.err.println(fileSchema);
////        Types.MessageTypeBuilder builder = Types.buildMessage();
//
//
//        Group line = reader1.read();
//        SimpleGroup customLine = (SimpleGroup)line;
////        System.err.println(customLine.getType());
//
//        MessageType schema = MessageTypeParser.parseMessageType(customLine.getType().toString());
////        System.err.println(schema);


        AvroParquetReader.Builder<GenericRecord> avroParquetReaderBuilder = AvroParquetReader.builder(
                new Path(new URI("hdfs://192.168.13.128:9000/par/000000_0")));
        ParquetReader<GenericRecord> avroParquetReader = avroParquetReaderBuilder.build();

        GenericRecord record;
        while ( (record = avroParquetReader.read()) !=null){
            Injection<GenericRecord, byte[]> recordInjection = GenericAvroCodecs.toBinary(record.getSchema());
            byte[] recordBinary = recordInjection.apply(record);
            System.err.println(record.getClass());
//            System.err.println(new String(recordBinary));
//            System.err.println(recordInjection.invert(recordBinary).get());
        }

        avroParquetReader.close();
    }

    private ParquetReader<GenericRecord> avroParquetReader;
    public MyParquetReader(URI uri) throws IOException {
        AvroParquetReader.Builder<GenericRecord> avroParquetReaderBuilder =
                AvroParquetReader.builder(new Path(uri));
        avroParquetReader = avroParquetReaderBuilder.build();
    }

    public GenericRecord readRecord() throws IOException{
        return avroParquetReader.read();
    }

    public void close() throws IOException{
        avroParquetReader.close();
    }
}
