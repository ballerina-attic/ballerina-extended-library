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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * This class contains functions which are used for identifying ballerina packages
 * within a repo, add resources folder for each ballerina packages and add relevant
 * icons inside those resources folders.
 */
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
                fileList.add(file.toString());
            }
        } catch (Exception e) {
            log.error(e);
        }
        return fileList;
    }

    /**
     * This is the method to check availability of resources folder.
     * @param ballerinaPackagePath This is the base path where ballerina packages available.
     * @return boolean This returns whether resource folder available or not.
     */
    public static boolean isResourcesFolderExist(String ballerinaPackagePath) {
        File folder = new File(ballerinaPackagePath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().equals("resources")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This is the method to obtain icon's absolute path.
     * @param packageName  This is the path to a specific ballerina package.
     * @param iconFolderPath This is the base path where all icons available.
     * @return String This returns a specific ballerina icon's absolute path if icon available
     */
    public static String findConnectorIcon(String packageName, String iconFolderPath) {
        File folder = new File(iconFolderPath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().equals(packageName + ".svg")) {
                    return file.getAbsolutePath();
                }
            }
        }
        return null;
    }

    /**
     * This is the method to add icons.
     * @param ballerinaPackagePath  This is the path to a specific ballerina package.
     * @param iconPath This is the icon path to copy.
     * @param moduleName This is the module name of ballerina package where icon being added.
     * @return Nothing.
     */
    public static void addReplaceIcon(String ballerinaPackagePath, String iconPath, String moduleName) throws IOException {
        String destination = ballerinaPackagePath + "/resources/" + moduleName + ".svg";
        Path sourcePath = Paths.get(iconPath);
        Path destinationPath = Paths.get(destination);
        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
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

    /**
     * This is the method to add icons.
     * @param packagePath This is the package path.
     * @param iconPath This returns path to icon.
     * @param moduleName This returns path to module.
     */
    public static void addConnectorIcon(String packagePath, String iconPath, String moduleName) throws IOException {
        if (iconPath != null) {
            addReplaceIcon(packagePath, iconPath, moduleName);
        } else {
            log.error("No icon available for " + packagePath);
        }
    }
}