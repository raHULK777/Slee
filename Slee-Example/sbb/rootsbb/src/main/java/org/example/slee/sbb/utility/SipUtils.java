package org.example.slee.sbb.utility;

import javax.sip.address.SipURI;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Properties;

public class SipUtils {
    public static Properties getProperties() {
        Properties prop = new Properties();
        try {
            InputStream inputStream = new FileInputStream("META-INF\\application.properties");
            prop.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }

    public static SipURI getSipURIFromAOR(SipURI incomingRequestURI) throws ParseException {
//        String aor=incomingRequestURI.getUser();
//        Properties prop=getProperties();
//        String ipPort=prop.getProperty(aor.trim());
//        String[] arr=ipPort.split(":");
        String ip="192.168.0.107";
        int port=5063;
        SipURI outURI= (SipURI) incomingRequestURI.clone();
        outURI.setHost(ip);
        outURI.setPort(port);

    return outURI;
    }
}
