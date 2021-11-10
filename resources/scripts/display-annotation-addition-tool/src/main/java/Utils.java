// Copyright (c) 2021 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

import org.apache.log4j.Logger;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.io.FileReader;
import java.util.Properties;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
    private static final Logger log = Logger.getLogger(Utils.class);
    /**
     * This is the method to fetch all available ballerina packages.
     * @param repoBasePath This is the base path where ballerina packages available.
     * @return List<String> This returns list of absolute paths to the packages available.
     */
    public static List<String> findBallerinaPackages(String repoBasePath) {
        List<String> fileList = new ArrayList<String>();
        try {
            File folder = new File(repoBasePath);
            File[] files = folder.listFiles();
            for (File file : files) {
                fileList.add(file.getAbsolutePath());
            }
        } catch (Exception e) {
            log.error(e);
        }
        return fileList;
    }

    /**
     * This is the method to check whether display annotation exists.
     * @param packagePath This is the path to ballerina package.
     */
    public static boolean isDisplayAnnotationExist(String packagePath) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(packagePath + "/client.bal"));
        for (String line : lines) {
            if (line.matches("(.*)@display(.*)label(.*)iconPath(.*)")) {
                return true;
            }
        }
        return false;
    }

    /**
     * This is the method to check whether display tag exists.
     * @param yamlPath This is the path to yaml file of ballerina package.
     */
    public static boolean isOpenApiDisplayTagExist(String yamlPath) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(yamlPath));
        for (String line : lines) {
            if (line.matches("(.*)x-display(.*)")) {
                return true;
            }
            if (line.matches("(.*)x-ballerina-display(.*)")) {
                return true;
            }
        }
        return false;
    }

    /**
     * This is the method to add display annotation.
     * @param packagePath This is the path to ballerina package.
     * @param moduleName This is the module name of ballerina package.
     * @param connectorName This is the connector name of ballerina package.
     */
    public static void addDisplayAnnotation(String packagePath, String moduleName, String connectorName) throws IOException {
        Path path = Paths.get(packagePath + "/client.bal");
        Stream<String> lines = Files.lines(path);
        String stringToReplace = "@display {label: \"" + connectorName + "\", iconPath: \"resources/" + moduleName + ".svg\"}\npublic isolated client class Client {";
        List <String> replaced = lines.map(line -> line.replaceAll("public isolated client class Client \\{", stringToReplace)).collect(Collectors.toList());
        Files.write(path, replaced);
        lines.close();
    }

    /**
     * This is the method to add display tag.
     * @param yamlPath This is the path to yaml file of ballerina package.
     * @param moduleName This is the module name of ballerina package.
     * @param connectorName This is the connector name of ballerina package.
     */
    public static void addDisplayTag(String yamlPath, String moduleName, String connectorName) throws IOException {
        Path path = Paths.get(yamlPath);
        Stream<String> lines = Files.lines(path);
        String stringToReplace = "info:\n  x-ballerina-display:\n    label: " + connectorName + "\n    iconPath: \"resources/" + moduleName + ".svg\"";
        List <String> replaced = lines.map(line -> line.replaceAll("info:", stringToReplace)).collect(Collectors.toList());
        Files.write(path, replaced);
        lines.close();
    }

    /**
     * This is the method to get value from config file.
     * @param config This is the config file key.
     * @return String This returns config file value.
     */
    public static String getConfigValue(String config) throws IOException {
        String configValue = new String();
        String currentPath = new java.io.File(".").getCanonicalPath();
        currentPath = currentPath + "/src/main/resources/config.properties";
        File configFile = new File(currentPath);
        try {
            FileReader reader = new FileReader(configFile);
            Properties prop = new Properties();
            prop.load(reader);
            configValue = prop.getProperty(config);
        } catch (IOException e) {
            log.error(e);
        }
        return configValue;
    }
}
