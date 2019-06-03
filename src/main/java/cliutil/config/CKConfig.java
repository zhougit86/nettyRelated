package cliutil.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CKConfig {

    public static  Properties getProp (String FilePath) throws IOException {
        Properties prop = new Properties();
        FileInputStream in = new FileInputStream(FilePath);
        prop.load(in);
        in.close();
        return prop;
    }

}
