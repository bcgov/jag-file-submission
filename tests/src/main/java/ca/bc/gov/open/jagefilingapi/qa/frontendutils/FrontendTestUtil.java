package ca.bc.gov.open.jagefilingapi.qa.frontendutils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class FrontendTestUtil {

    public static void verifyLinkActive(String linkUrl) throws IOException {

        URL url = new URL(linkUrl);
        HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
        httpURLConnection.setConnectTimeout(3000);
        httpURLConnection.connect();

        if(httpURLConnection.getResponseCode() == 200) {
            System.out.println(linkUrl+" - "+httpURLConnection.getResponseMessage());
        }

        if(httpURLConnection.getResponseCode()==HttpURLConnection.HTTP_NOT_FOUND) {
            System.out.println(linkUrl+" - "+httpURLConnection.getResponseMessage() + " - "+ HttpURLConnection.HTTP_NOT_FOUND);
        }
    }
}
