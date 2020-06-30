package com.maveric.core.testng.reporter;

import com.maveric.core.config.ConfigProperties;
import com.maveric.core.testng.listeners.ReportListener;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.commons.io.FileUtils.readFileToString;


public class CustomReporter {
    private final static AtomicInteger counter = new AtomicInteger();
    protected static final Logger log = LogManager.getLogger();

    private static int passedTCsOrFeatures = 0;
    private static String fileName;

    private static final String projectName = ConfigProperties.APPLICATION_NAME.get();
    private static final String retryCount = String.valueOf(ConfigProperties.MAX_RETRY_COUNT.get());

    public static StringBuilder createCustomReport(String fileName) {

        String featuresOrTestCases = ReportListener.isCucumber ? "Features" : "Test Cases";

        String keywords = "";
        StringBuilder htmlStringBuilder = new StringBuilder();

        htmlStringBuilder.append("<html><head>"
                + "<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">"
                + "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">"
                + "<script	src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script>"
                + "<style type=\"text/css\">.row{margin-right:0px; margin-left:0px;} .skills{text-align:center;padding-right:20px;line-height:40px;color:white;margin-bottom:2px}.passed{background-color:green}.failed{background-color:#f44336}.skipped{background-color:#808080}.testName{background-color:#406084}.total{background-color:#a7856b}.executionTime{background-color:#e59866}.padding-logs{padding:1cm}body{padding:0;font:14px \"Calibri\",Lucida Grande;line-height:1.5}.checkbox{padding-left:0;margin-top:-5px}.checkbox label{display:inline-block;position:relative;padding-left:5px}.checkbox label::before{content:\"\";display:inline-block;position:absolute;width:17px;height:17px;left:0;margin-left:-20px;border:1px solid #7f7fff;border-radius:3px;background-color:#fff;-webkit-transition:border .15s ease-in-out,color .15s ease-in-out;-o-transition:border .15s ease-in-out,color .15s ease-in-out;transition:border .15s ease-in-out,color .15s ease-in-out}.checkbox label::after{display:inline-block;position:absolute;width:16px;height:16px;left:0;top:0;margin-left:-10px;padding-left:3px;padding-top:1px;font-size:11px;color:#555}.checkbox input[type=\"checkbox\"]{opacity:0}.checkbox input[type=\"checkbox\"]:focus+label::before{outline:thin dotted;outline:5px auto -webkit-focus-ring-color;outline-offset:-2px}.checkbox input[type=\"checkbox\"]:checked+label::after{font-family:\"FontAwesome\";content:\"\\f00c\";margin-left:-20px}.checkbox input[type=\"checkbox\"]:disabled+label{opacity:.65}.checkbox input[type=\"checkbox\"]:disabled+label::before{background-color:#eee;cursor:not-allowed}.checkbox.checkbox-circle+label::before{border-radius:50%}.checkbox.checkbox-inline{margin-top:0}.checkbox-primary input[type=\"checkbox\"]:checked+label::before{background-color:#428bca;border-color:#428bca}.checkbox-primary input[type=\"checkbox\"]:checked+label::after{color:#fff}.checkbox-danger input[type=\"checkbox\"]:checked+label::before{background-color:#d9534f;border-color:#d9534f}.checkbox-danger input[type=\"checkbox\"]:checked+label::after{color:#fff}.checkbox-info input[type=\"checkbox\"]:checked+label::before{background-color:#5bc0de;border-color:#5bc0de}.checkbox-info input[type=\"checkbox\"]:checked+label::after{color:#fff}.checkbox-warning input[type=\"checkbox\"]:checked+label::before{background-color:#f0ad4e;border-color:#f0ad4e}.checkbox-warning input[type=\"checkbox\"]:checked+label::after{color:#fff}.checkbox-success input[type=\"checkbox\"]:checked+label::before{background-color:#5cb85c;border-color:#5cb85c}.checkbox-success input[type=\"checkbox\"]:checked+label::after{color:#fff}.box{color:black;padding:4px;margin-top:4px;border-bottom:1px double;border-top:1px double;border-left: 1px double;border-right: 1px double;border-color:gray}a{font-color:blue}.red{background:#ffb699}.green{background:#90ee90}.yellow{background:#ff6}.blue{background:#f0e68c}#mainTable{border-collapse:collapse}*{font-family:Tahoma,Calibri Light!important}</style>\r\n"
                + "<script src=\"https://code.jquery.com/jquery-1.12.4.min.js\"></script></head>")
                .append("<body background=\"screenshots/Background.png\">")
                .append("<script language=\"javascript\"> ")
                .append("function toggle(id,id2) {")
                .append("      var ele = document.getElementById(id);")
                .append("      var text = document.getElementById(id2);")
                .append("      if(ele.style.display == \"block\") {")
                .append("            ele.style.display = \"none\";")
                .append("            text.innerHTML = \"[+]\";")
                .append("      }")
                .append("      else {")
                .append("            ele.style.display = \"block\";")
                .append("            text.innerHTML = \"[-]\";")
                .append("      }")
                .append("}")
                .append("</script>")
                .append("<script type=\"text/javascript\">")
                .append("$('#fails').show();")
                .append("$('#pass').show();")
                .append("$('#skip').show();")
                .append("$(document).ready(function(){")
                .append("$(\"input:checkbox\").change(function() {")
                .append("$.each($('input[type=\"checkbox\"]'), function (key, value) {")
                .append("var inputValue = $(this).attr(\"value\");")
                .append("var ischecked= $(this).is(':checked');")
                .append("if(ischecked)")
                .append("{")
                .append("$(\".\" + inputValue).show();")
                .append("}")
                .append("else")
                .append("{")
                .append("$(\".\" + inputValue).hide();")
                .append("}")
                .append("});")
                .append("});")
                .append("});")
                .append("</script>")
                .append("")
                .append("")
                .append("<meta charset=\"utf-8\"><title>Automation Report</title><style type=\"text/css\">body{margin:5px;} .box{color: black; padding: 4px; margin-top: 4px;  border-bottom: 1px double;  border-top: 1px double; border-color:gray;}.red{ background: #ffb699; }.green{ background: #90EE90; }.yellow{ background: #FFFF66; }.blue{ background: #F0E68C; }#mainTable {border-collapse: collapse;}* {font-family: Tahoma, Calibri Light !important;}</style></head>")
                .append("<div class=\\\"row\\\"><table border=\"0\" style=\"width:100%\"><tbody><tr><td><table><tbody><tr><td align=\"left\" width=\"10%\"><img style=\"margin:5px;\"  src=\"screenshots/MavericLogo.png\" border=\"2\">&nbsp;</td></tr></tbody></table></td><td align=\"center\"><b><left><font  style=\"font-size: 25px;\"> " + projectName + " Automation Results" + " </font></left></b></td><td><div name=\"jenkin_compiler_name\" align=\"right\"><table style=\"background-color:rgba(0, 0, 0, 0);\"><tbody><tr><td><b><div id='runCompletedOn' class=\"label label-danger\" >00:00:00</div><b></td></tr></tbody></table></div></td></tr></tbody></table></div>")
                .append("<div class=\"row\">")
                .append("<div class=\"col-xs-3 skills testName \">Environment : <span id=\"environment\"></span></div>")
                .append("<div class=\"col-xs-3 skills total \">Total " + featuresOrTestCases + " : <span id=\"totalTestCases\"></div>")
                .append("<div class=\"col-xs-3 skills passed\">Execution Time :")
                .append("<span id=\"executionTime\"></span></div>")
                .append("<div class=\"col-xs-3 skills executionTime\">Browser : <span id=\"browser\"></span></div>")
                .append("<div class=\"col-xs-3 skills passed\">Passed " + featuresOrTestCases + " : <span id=passedCount></span></div>")
                .append("<div class=\"col-xs-3 skills failed\">Failed " + featuresOrTestCases + " :<span id=failedCount></span></div>")
                .append("<div class=\"col-xs-3 skills testName\">Retry Count : " + retryCount + "<span id=retryCount></span></div>")
                .append("<div class=\"col-xs-3 skills total\">Groups : " + keywords + "<span id=keywords></span></div>")
                .append("</div><br />")
                .append("<div class=\"row\">")
                .append("<h4 class=\"col-xs-12 col-sm-6 pull-left\"><b><font color=\"#24468D\">Report Details</font></b></h4>")
                .append("<div class=\"col-xs-12 col-sm-6\"><div style=\"margin-left: 50%;\"><div class=\"col-xs-4 col-sm-offset-3 col-sm-3 checkbox checkbox-success\"><input type=\"checkbox\"id=\"pass\"value=\"green\" name=\"checkbox\" checked><label for=\"pass\">Passed</label></div>")
                .append("<div class=\"col-xs-4 col-sm-3 checkbox checkbox-danger\"><input type=\"checkbox\"id=\"fails\"value=\"red\" name=\"checkbox\" checked><label for=\"fails\">Failed</label></div></div></div></div>");

        htmlStringBuilder.append("</table></body></html>");
        //write html string content to a file
        CustomReporter.fileName = fileName;
        appendToFile(htmlStringBuilder.toString(), fileName);


        return htmlStringBuilder;
    }


    public static void appendPass(String logs, String testName, String totalTime) {

        SimpleDateFormat sdfDate = new SimpleDateFormat("HHmmssSSS");
        Date now = new Date();
        String randNo = sdfDate.format(now) + getId();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("<div id=\"pass\" class=\"green box padding-logs\">" + testName
                        + "&nbsp;<a style=\"font-size:13px;vertical-align:2px;\" id=\"" + testName
                        + "_passed_0" + randNo + "\" href=\"javascript:toggle('" + testName + "_passed_" + randNo + "','" + testName
                        + "_passed_0" + randNo + "');\"> [+]</a><a style=\"position: absolute;right: 9;color:black;\"> Execution Time : " + totalTime + "</a><div id=\"" + testName
                        + "_passed_" + randNo + "\" style=\"display: none; background-color: #bdf5bd; margin-left: 0.5cm; \">")
                .append("<div class=\"messages\">" + logs + "</div></div></div>");
        appendToFile(stringBuilder.toString(), fileName);

    }

    public static void appendFail(String logs, String testName, String totalTime) {

        StringBuilder stringBuilder = new StringBuilder();
        SimpleDateFormat sdfDate = new SimpleDateFormat("HHmmssSSS");
        Date now = new Date();
        String randNo = sdfDate.format(now) + getId();
        stringBuilder.append("<div id=\"fail\" class=\"red box padding-logs\">" + testName
                + "&nbsp;<a style=\"font-size:13px;vertical-align:2px;\" id=\"" + testName
                + "_failed_1" + randNo + "\" href=\"javascript:toggle('" + testName + "_failed_" + randNo + "','" + testName
                + "_failed_1" + randNo + "');\"> [+]</a><a style=\"position: absolute;right: 9;color:black;\"> Execution Time : " + totalTime + "</a><div id=\"" + testName
                + "_failed_" + randNo + "\" style=\"display: none; background-color: rgb(255, 218, 204); margin-left: 0.5cm;\">")
                .append("<div class=\"messages\">" + logs + "</div><br></div></div>");
        appendToFile(stringBuilder.toString(), fileName);


    }


    public static void appendToFile(String fileContent, String fileName) {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            String data = fileContent;
            File file = new File(fileName);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // true = append file
            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);
            bw.write(data);

        } catch (IOException e) {
            log.error("", e);
        } finally {
            try {
                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {
                log.error("", ex);
            }
        }
    }

    public static void updateValues(int passedCount, int failedCount, String endDate, String execTime, String groups) {
        String str;
        try {
            str = readFileToString(new File(fileName), Charset.defaultCharset());


            Document doc = Jsoup.parse(str);

            Elements passed = doc.select("span#passedCount");
            Elements failed = doc.select("span#failedCount");

            Elements executionTime = doc.select("span#executionTime");
            Elements runCompletedOn = doc.select("div#runCompletedOn");
            Elements environment = doc.select("span#environment");
            Elements totalTestCases = doc.select("span#totalTestCases");
            Elements browser = doc.select("span#browser");
            Elements keywords = doc.select("span#keywords");

            String env = ConfigProperties.BUILD_NAME.get();
            String browserName = "";
            environment.get(0).text(env);
            totalTestCases.get(0).text(String.valueOf(passedCount + failedCount));
            passed.get(0).text(String.valueOf(passedCount));
            failed.get(0).text(String.valueOf(failedCount));
            runCompletedOn.get(0).text(endDate);
            executionTime.get(0).text(execTime);
            keywords.get(0).text(groups);
            browserName = ConfigProperties.BROWSER.get();
            browser.get(0).text(browserName);
            FileUtils.writeStringToFile(new File(fileName), doc.toString(), Charset.defaultCharset());

        } catch (Exception e) {
            log.error("", e);
        }

    }


    public static int getId() {
        return counter.incrementAndGet();
    }


    public static StringBuilder appendStepPass(String logs, String testName, int stepNo) {

        StringBuilder stringBuilder = new StringBuilder();
        SimpleDateFormat sdfDate = new SimpleDateFormat("HHmmssSSS");
        Date now = new Date();
        String randNo = sdfDate.format(now) + getId();
        ;
        stringBuilder
                .append("<div id=\"pass\" class=\"green box padding-logs\">" + testName
                        + "<a style=\"font-size:13px;vertical-align:2px;\" id=\"" + "step" + stepNo
                        + "_passed_0" + randNo + "\" href=\"javascript:toggle('" + "step" + stepNo + "_passed" + randNo + "','" + "step" + stepNo
                        + "_passed_0" + randNo + "');\"> [+]</a><div id=\"" + "step" + stepNo
                        + "_passed" + randNo + "\" style=\"display: none; background-color: #bdf5bd; margin-left: 0.5cm; \">")
                .append("<div class=\"messages\">" + logs + "</div></div></div>");

        return stringBuilder;
    }

    public static StringBuilder appendStepSkip(String logs, String testName, int stepNo) {

        StringBuilder stringBuilder = new StringBuilder();
        SimpleDateFormat sdfDate = new SimpleDateFormat("HHmmssSSS");
        Date now = new Date();
        String randNo = sdfDate.format(now) + getId();
        ;
        stringBuilder
                .append("<div id=\"skip\" class=\"orangered box padding-logs\">" + testName
                        + "<a style=\"font-size:13px;vertical-align:2px;\" id=\"" + "step" + stepNo
                        + "_skipped_0" + randNo + "\" href=\"javascript:toggle('" + "step" + stepNo + "_skipped" + randNo + "','" + "step" + stepNo
                        + "_skipped_0" + randNo + "');\"> [+]</a><div id=\"" + "step" + stepNo
                        + "_skipped" + randNo + "\" style=\"display: none; background-color: #FF4500; margin-left: 0.5cm; \">")
                .append("<div class=\"messages\">" + logs + "</div></div></div>");

        return stringBuilder;
    }

    public static StringBuilder appendStepFail(String logs, String testName, int stepNo, String scenarioName) {

        StringBuilder stringBuilder = new StringBuilder();
        SimpleDateFormat sdfDate = new SimpleDateFormat("HHmmssSSS");
        Date now = new Date();
        String randNo = sdfDate.format(now) + getId();
        ;
        stringBuilder
                .append("<div id=\"fail\" class=\"red box padding-logs\">" + testName
                        + "<a style=\"font-size:13px;vertical-align:2px;\" id=\"" + "step" + stepNo
                        + "_failed_0" + randNo + "\" href=\"javascript:toggle('" + "step" + stepNo + "_failed" + randNo + "','" + "step" + stepNo
                        + "_failed_0" + randNo + "');\"> [+]</a><div id=\"" + "step" + stepNo
                        + "_failed" + randNo + "\" style=\"display: none; background-color: rgb(255, 218, 204);; margin-left: 0.5cm; \">")
                .append("<div class=\"messages\">" + logs + "</div><br></div></div>");


        return stringBuilder;
    }

    public static StringBuilder appendScenarioPass(String logs, String testName) {

        StringBuilder stringBuilder = new StringBuilder();
        SimpleDateFormat sdfDate = new SimpleDateFormat("HHmmssSSS");
        Date now = new Date();
        String randNo = sdfDate.format(now) + getId();
        ;
        stringBuilder
                .append("<div id=\"pass\" class=\"green box padding-logs\">" + "Scenario : " + testName
                        + "&nbsp;<a style=\"font-size:13px;vertical-align:2px;\" id=\"" + testName
                        + "_passed_0" + randNo + "\" href=\"javascript:toggle('" + testName + "_passed_" + randNo + "','" + testName
                        + "_passed_0" + randNo + "');\"> [+]</a><div id=\"" + testName
                        + "_passed_" + randNo + "\" style=\"display: none; background-color: #bdf5bd; margin-left: 0.5cm; \">")
                .append("<div class=\"messages\">" + logs + "</div></div></div>");
        return stringBuilder;
    }

    public static StringBuilder appendScenarioFail(String logs, String testName) {
        StringBuilder stringBuilder = new StringBuilder();
        SimpleDateFormat sdfDate = new SimpleDateFormat("HHmmssSSS");
        Date now = new Date();
        String randNo = sdfDate.format(now) + getId();
        ;
        stringBuilder.append("<div id=\"fail\" class=\"red box padding-logs\">" + "Scenario : " + testName
                + "&nbsp;<a style=\"font-size:13px;vertical-align:2px;\" id=\"" + testName
                + "_failed_1" + randNo + "\" href=\"javascript:toggle('" + testName + "_failed_" + randNo + "','" + testName
                + "_failed_1" + randNo + "');\"> [+]</a><div id=\"" + testName
                + "_failed_" + randNo + "\" style=\"display: none; background-color: rgb(255, 218, 204); margin-left: 0.5cm;\">")
                .append("<div class=\"messages\">" + logs + "</div></div></div>");

        return stringBuilder;

    }

    public static StringBuilder appendScenarioSkip(String logs, String testName) {

        StringBuilder stringBuilder = new StringBuilder();
        SimpleDateFormat sdfDate = new SimpleDateFormat("HHmmssSSS");
        Date now = new Date();
        String randNo = sdfDate.format(now);

        stringBuilder
                .append("<div id=\"pass\" class=\"yellow box padding-logs\">" + "Scenario : " + testName
                        + "&nbsp;<a style=\"font-size:13px;vertical-align:2px;\" id=\"" + testName
                        + "_passed_0" + randNo + "\" href=\"javascript:toggle('" + testName + "_passed_" + randNo + "','" + testName
                        + "_passed_0" + randNo + "');\"> [+]</a><div id=\"" + testName
                        + "_passed_" + randNo + "\" style=\"display: none; background-color: #f7ed20; margin-left: 0.5cm; \">")
                .append("<div class=\"messages\">" + logs + "</div></div></div>");
        return stringBuilder;
    }

    public static void appendFeature(String featureName, StringBuilder builder, int totalScenarios, int passedScenarios) {
        int failedScenarios = totalScenarios - passedScenarios;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("<div id=\"pass\" class=\"blue box padding-logs\">" + "Feature : " + featureName
                        + "&nbsp;<a style=\"font-size:13px;vertical-align:2px;\" id=\"" + featureName
                        + "_passed_0\" href=\"javascript:toggle('" + featureName + "_passed','" + featureName
                        + "_passed_0');\"> [+]</a><a style=\"position: absolute;right: 9;color:black;\"> Total Scenarios : " + totalScenarios + ", Passed Scenarios : " + passedScenarios + ", Failed Scenarios : " + failedScenarios + "</a><div id=\"" + featureName
                        + "_passed\" style=\"display: none; background-color: #F0E68C; margin-left: 0.5cm; \">")
                .append("<div class=\"messages\">" + builder + "</div></div></div>");

        appendToFile(stringBuilder.toString(), fileName);

    }

}
