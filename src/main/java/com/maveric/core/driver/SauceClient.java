package com.maveric.core.driver;

import com.maveric.core.config.ConfigProperties;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static com.maveric.core.driver.BrowserStackClient.isURL;

public class SauceClient {
    private static final Logger logger = LogManager.getLogger();
    private static final String app_path = ConfigProperties.APP_PATH.get();
    private static final boolean isSauce = ConfigProperties.SAUCE.getBoolean();
    private static final String baseURL = "https://%ssaucelabs.com/rest/v1/storage/%s/%s";
    private final static String username = ConfigProperties.SAUCE_USERNAME.get();
    private final static String accessKey = ConfigProperties.SAUCE_ACCESS_KEY.get();
    private final static String sauceHub = ConfigProperties.SAUCE_HUB.get();
    private final static boolean sauce_overwrite = ConfigProperties.SAUCE_OVERWRITE_FILE.getBoolean();

    public static void uploadFile() {
        Response response;
        if (isSauce && (!isURL(app_path)) && (!app_path.isEmpty()) && (!username.isEmpty()) && (!accessKey.isEmpty())) {
            File app_file = new File(app_path);
            if (app_file.exists()) {
                String dataCenter = "";
                if (sauceHub.contains("eu-central-1")) {
                    dataCenter = "eu-central-1.";
                } else if (sauceHub.contains("us-east-1")) {
                    dataCenter = "us-east-1.";
                }
                response = RestAssured.given().baseUri(String.format(baseURL, dataCenter, username, app_file.getName()))
                        .auth().preemptive().basic(username, accessKey)
                        .contentType(ContentType.BINARY).queryParam("overwrite", sauce_overwrite)
                        .body(app_file)
                        .post()
                        .then()
                        .statusCode(200).extract().response();
                String app_url = response.body().jsonPath().getString("filename");
                logger.info("uploaded app to sauce " + app_url);


            }
        }

    }


}

