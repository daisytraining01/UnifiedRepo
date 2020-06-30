package com.maveric.core.testng.listeners;

import com.maveric.core.utils.reporter.Report;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.UUID;

import static com.maveric.core.testng.listeners.ReportListener.reportFolder;

public class RestLogFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec, FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);
        StringBuilder requestBuilder = new StringBuilder();
        requestBuilder.append("Request : ").append(requestSpec.getMethod()).append("\n");
        requestBuilder.append("URI : ").append(requestSpec.getURI()).append("\n");
        requestBuilder.append("Headers : ").append("\n");
        requestSpec.getHeaders().asList().forEach(header -> requestBuilder.append(header).append("\n"));
        requestBuilder.append("Params : ").append("\n");
        requestSpec.getPathParams().forEach((key, value) -> requestBuilder.append(key).append(" : ").append(value).append("\n"));
        requestSpec.getRequestParams().forEach((key, value) -> requestBuilder.append(key).append(" : ").append(value).append("\n"));

        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("Response : ").append("\n").append(response.getStatusLine()).append("\n");
        responseBuilder.append("Headers : ").append("\n");
        response.getHeaders().asList().forEach(header -> responseBuilder.append(header).append("\n"));
        responseBuilder.append("Body : ").append("\n").append(response.getBody().asString()).append("\n");
        String path = writeResponse(requestBuilder.append(responseBuilder));
        String html = "<a target=_blank href=" + path.replaceAll(" ", "%20") + ">" + "API Call" + "</a>";
        Report.log(html);
        return response;
    }


    public String writeResponse(StringBuilder data) {
        String random = UUID.randomUUID().toString();
        String filePath = "/api/" + random;
        File file = new File(
                reportFolder + filePath);
        file.mkdirs();
        writeFile(file + "/Output.txt", data.toString());
        return "./" + filePath + "/Output.txt";
    }


    public void writeFile(String fileLocation, String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileLocation))) {
            writer.write(text);
        } catch (Exception e) {
            logger.error("", e);
        }
    }
}
