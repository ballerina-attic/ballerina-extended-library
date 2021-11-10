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

import java.io.IOException;
import java.util.List;

public class DisplayAnnotationTool {
    public static void main(String[] args) throws IOException {
        String openApiPath = Utils.getConfigValue("openApiConnectorsPath");
        List<String> ballerinaPackages = Utils.findBallerinaPackages(openApiPath);
        for (String packagePath : ballerinaPackages) {
            System.out.println(packagePath);
            String[] packageNameSplit = packagePath.split("/");
            String moduleName = packageNameSplit[packageNameSplit.length - 1];
            String[] splitNames = moduleName.split(".");
            String connectorName = null;
            for (String splitName : splitNames) {
                String splitCapitalizedName = splitName.substring(0, 1).toUpperCase();
                connectorName = splitCapitalizedName + " ";
            }
            boolean isDisplayAnnotationAvailable = Utils.isDisplayAnnotationExist(packagePath);
            if (!isDisplayAnnotationAvailable) {
                Utils.addDisplayAnnotation(packagePath, moduleName, connectorName);
            }
            String yamlPath = packagePath + "/openapi.yaml";
            boolean isOpenApiDisplayTagAvailable = Utils.isOpenApiDisplayTagExist(yamlPath);
            if (!isOpenApiDisplayTagAvailable) {
                Utils.addDisplayTag(yamlPath, moduleName, connectorName);
            }
        }
    }
}
