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
import java.io.IOException;
import java.util.List;

public class IconTool {
    static String iconsSourcePath;
    static String openApiConnectorsPath;
    public static void main(String[] args) throws IOException {
        iconsSourcePath = Utils.getConfigValue("iconsSourcePath");
        openApiConnectorsPath = Utils.getConfigValue("openApiConnectorsPath");
        List<String> ballerinaPackages = Utils.findBallerinaPackages(openApiConnectorsPath);
        for (String packagePath : ballerinaPackages) {
            String[] packageNameSplit = packagePath.split("/");
            String moduleName = packageNameSplit[packageNameSplit.length - 1];
            boolean resourcesFolderExist = Utils.isResourcesFolderExist(packagePath);
            if (resourcesFolderExist) {
                String iconPath = Utils.findConnectorIcon(moduleName, iconsSourcePath);
                Utils.addConnectorIcon(packagePath, iconPath, moduleName);
            } else {
                File folderToCreate = new File(packagePath + "/resources");
                folderToCreate.mkdir();
                String iconPath = Utils.findConnectorIcon(moduleName, iconsSourcePath);
                Utils.addConnectorIcon(packagePath, iconPath, moduleName);
            }
        }
    }
}
