package com.maveric.core.driver;

import com.browserstack.local.Local;
import com.maveric.core.config.ConfigProperties;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class BrowserStackClient {
    private static final Logger logger = LogManager.getLogger();
    private static final String app_path = ConfigProperties.APP_PATH.get();
    private static final boolean isBrowserStack = ConfigProperties.BROWSER_STACK.getBoolean();
    private static final String baseURL = "https://api-cloud.browserstack.com/app-automate";
    private final static String username = ConfigProperties.BROWSER_STACK_USERNAME.get();
    private final static String accessKey = ConfigProperties.BROWSER_STACK_ACCESS_KEY.get();
    private final static String APP_NAME = ConfigProperties.APP_NAME.get();
    public static String app_id;
    private static Local bsLocal = null;

    public static void uploadFile() {
        Response response;

        if (isBrowserStack && (!app_path.isEmpty()) && (!username.isEmpty()) && (!accessKey.isEmpty())) {
            deleteFiles();
            if (isURL(app_path)) {
                String app = "{\"custom_id\": \"" + APP_NAME + "\",\"url\": \"" + app_path + "\"}";
                response = RestAssured.given().baseUri(baseURL)
                        .auth().preemptive().basic(username, accessKey)
                        .contentType("application/json")
                        .body(app)
                        .post("/upload")
                        .then()
                        .statusCode(200).extract().response();
                response.prettyPrint();
                String app_url = response.body().jsonPath().getString("app_url");
                logger.info("uploaded app to browserstack " + app_url);
                app_id = app_url;
            } else {
                String app = "{\"custom_id\": \"" + APP_NAME + "\"}";
                File app_file = new File(app_path);
                if (app_file.exists()) {
                    response = RestAssured.given().baseUri(baseURL)
                            .auth().preemptive().basic(username, accessKey)
                            .multiPart("file", app_file)
                            .multiPart("data", app)
                            .post("/upload")
                            .then()
                            .statusCode(200).extract().response();
                    response.prettyPrint();
                    String app_url = response.body().jsonPath().getString("app_url");
                    logger.info("uploaded app to browserstack " + app_url);
                    app_id = app_url;
                }
            }
        }

    }

    private static Response getRecentFileUploads() {
        Response response =
                RestAssured.given().baseUri(baseURL)
                        .auth().basic(username, accessKey)
                        .contentType("application/json")
                        .get("recent_apps/" + APP_NAME)
                        .then()
                        .statusCode(200)
                        .extract().response();
        return response;
    }

    public static void deleteFiles() {
        if (isBrowserStack && (!app_path.isEmpty()) && (!username.isEmpty()) && (!accessKey.isEmpty())) {

            List<Object> apps_ = getRecentFileUploads().body().jsonPath().getList("app_id");
            if (apps_ != null) {
                apps_.forEach(app_id ->
                {
                    RestAssured.given().baseUri(baseURL)
                            .auth().basic(username, accessKey)
                            .contentType("application/json")
                            .delete("app/delete/" + app_id)
                            .then()
                            .statusCode(200);
                    logger.info("deleted app from browserstack " + app_id);
                });
            }
        }
    }

    public static void startBrowserStackLocal() {
        try {
            if (ConfigProperties.BROWSER_STACK.getBoolean() && ConfigProperties.BROWSER_STACK_LOCAL.getBoolean()) {
                if (ConfigProperties.BROWSER_STACK_ACCESS_KEY.get().isEmpty()) {
                    throw new RuntimeException("Access key not available for Browser Stack");
                }
                bsLocal = new Local();
                HashMap<String, String> bsLocalArgs = new HashMap<>();
                bsLocalArgs.put("key", ConfigProperties.BROWSER_STACK_ACCESS_KEY.get());
                bsLocal.start(bsLocalArgs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopBrowserStackLocal() {
        try {
            if (ConfigProperties.BROWSER_STACK.getBoolean() && ConfigProperties.BROWSER_STACK_LOCAL.getBoolean()) {
                if (bsLocal != null && bsLocal.isRunning()) {
                    bsLocal.stop();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isURL(String url) {
        try {
            URL obj = new URL(url);
            obj.toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }

}

