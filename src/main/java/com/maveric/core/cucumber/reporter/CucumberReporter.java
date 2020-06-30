package com.maveric.core.cucumber.reporter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maveric.core.cucumber.reporter.pojo.*;
import com.maveric.core.testng.reporter.CustomReporter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CucumberReporter {

    private static final String SEPARATOR = System.getProperty("file.separator");
    public static String testCaseName = "";
    public static String lineSeparator = System.getProperty("line.separator");
    public static int totalFeatures = 0;
    public static int passedFeatures = 0;
    public static String featureTags = "";

    public static void generateCucumberReport() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            String userDir = System.getProperty("user.dir");
            String cucumberResultsDir = userDir + "/target";

            CucumberResults[] results = objectMapper.readValue(
                    new File(cucumberResultsDir + SEPARATOR + "cucumber.json"),
                    CucumberResults[].class);

            for (CucumberResults result : results) {

                if (result.getKeyword().equalsIgnoreCase("feature")) {

                    totalFeatures++;

                    StringBuilder builder = new StringBuilder();
                    String featureName = result.getName();
                    // String featureId = result.getId();
                    // String featureDesc = result.getDescription();
                    // String featureUri = result.getUri();
                    boolean featureStatus = true;
                    List<Tag> tags = result.getTags();
                    featureTags = tags.get(0).getName();

                    List<Element> elements = result.getElements();
                    int scenarioCount = 0;
                    int passedScenarioCount = 0;
                    boolean bgFlag = false;
                    List<Step> bgSteps = new ArrayList<>();
                    for (Element element : elements) {
                        if (element.getType().equalsIgnoreCase("background")) {
                            bgSteps.addAll(element.getSteps());
                        }
                        if (element.getType().equalsIgnoreCase("scenario")) {
                            scenarioCount++;
                            boolean testStatus = true;
                            String scenarioName = element.getName();
                            // String scenarioDesc = element.getDescription();
                            // String scenarioID = element.getId();
                            // String scenarionKeyword = element.getKeyword();
                            // int ScenarioLine = element.getLine();
                            List<After> after = element.getAfter();
                            List<String> afterOutput = new ArrayList<>();
                            after.forEach(after1 -> afterOutput.addAll(after1.getOutput()));


//                                String duration = afterOutput.getDuration();

//                            List<Before> before = element.getBefore();
//                            Result beforeOutput = before.get(0).result;
                            List<Step> steps = new ArrayList<>();
                            steps.addAll(bgSteps);
                            steps.addAll(element.getSteps());

                            testCaseName = scenarioName;

                            ArrayList<String> scenarioLogs = new ArrayList<String>();
                            int stepCount = 0;
                            boolean skipNextSteps = false;

                            for (Step step : steps) {

                                // String stepLine = step.getLine();

                                String stepName = step.getName();
                                String stepKeyword = step.getKeyword();
                                String stepResult = step.getResult().getStatus();
                                List<String> output = step.getOutput().stream().map(s -> s + "<br>").collect(Collectors.toList());


                                stepCount++;
                                ArrayList<String> stepLogs = new ArrayList<>(output);
                                int count = Integer.parseInt(scenarioCount + "" + stepCount);
                                String name = stepKeyword + " " + stepName;

                                if (stepResult.equalsIgnoreCase("passed")) {
                                    testStatus = true;

                                } else if (stepResult.equalsIgnoreCase("failed") && !skipNextSteps) {

                                    testStatus = false;
                                    featureStatus = false;
                                    String error = step.getResult().getErrorMessage();
                                    stepLogs.add(error);
                                    if (afterOutput.size() > 0) {
                                        stepLogs.add(String.join("", afterOutput));
                                    }
                                }

                                String logs = String.join("", stepLogs);

                                if (testStatus) {
                                    scenarioLogs.add(CustomReporter.appendStepPass(logs, name, count).toString());
                                } else if (skipNextSteps) {
                                    scenarioLogs.add(CustomReporter.appendStepSkip(logs, name, count).toString());
                                } else {
                                    scenarioLogs.add(CustomReporter.appendStepFail(logs, name, count, scenarioName)
                                            .toString());
                                    skipNextSteps = true;
                                }
                            }

                            String logs = String.join("", scenarioLogs);

                            if (testStatus) {
                                passedScenarioCount++;
                                builder.append(CustomReporter.appendScenarioPass(logs, testCaseName));
                            } else {
                                builder.append(CustomReporter.appendScenarioFail(logs, testCaseName));

                            }
                            bgSteps.clear();
                        }
                    }

                    if (featureStatus)
                        passedFeatures++;
                    CustomReporter.appendFeature(featureName, builder, scenarioCount, passedScenarioCount);


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

