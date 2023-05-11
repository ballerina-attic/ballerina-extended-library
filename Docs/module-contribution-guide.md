# Adding a new Ballerina Extended Library Module

Authors: @ThisaruGuruge @NipunaRanasinghe
Reviewers: @abeykoon
Created: 2023/05/11
Updated: 2023/05/11

## Table of Contents

1. [Introduction](#introduction)
2. [Prerequisites](#prerequisites)
3. [Repository creation](#repository-creation)
    * 3.1 [Repository naming](#repository-naming)
4. [Repository initialization](#repository-initialization)
5. [Repository structure](#repository-structure)
    * 5.1 [The `.github/` directory](#the-github-directory-required)
        * 5.1.1 [The `workflows/` directory](#the-workflows-directory-required)
    * 5.2 [The `ballerina/` directory](#the-ballerina-directory-gradle-submodulerequired)
    * 5.3 [The `build-config/` directory](#the-build-config-directory-required)
    * 5.4 [The `ballerina-tests/` directory](#the-ballerina-tests-directory-gradle-submoduleoptional)
    * 5.5 [The `compiler-plugin/` directory](#the-compiler-plugin-directory-gradle-submoduleoptional)
    * 5.6 [The `compiler-plugin-tests/` directory](#the-compiler-plugin-tests-directory-gradle-submoduleoptional)
    * 5.7 [The `docs/` directory](#the-docs-directory-optional)
    * 5.8 [The `examples/` directory](#the-examples-directory-gradle-submoduleoptional)
    * 5.9 [The `native/` directory](#the-native-directory-gradle-submoduleoptional)
    * 5.10 [Other Build Files](#other-build-files)
        * 5.10.1 [The `LICENSE` file](#the-license-file-required)
        * 5.10.2 [The `README.md` file](#the-readmemd-file-required)
        * 5.10.3 [The `build.gradle` file](#the-buildgradle-file-required)
        * 5.10.4 [The `gradle.properties` file](#the-gradleproperties-file-required)
        * 5.10.5 [The `gradlew` and `gradlew.bat` files](#the-gradlew-and-gradlewbat-files-required)
        * 5.10.6 [The `settings.gradle` file](#the-settingsgradle-file-required)
        * 5.10.7 [The `spotbugs-exclude.xml` file](#the-spotbugs-excludexml-file-optional)
6. [Ballerina Extended Library Dashboard](#ballerina-extended-library-dashboard)

## Introduction

This guide provides instructions on how to create a new Ballerina Extended Library module repository. 
It explains the directory structure and CI/CD configuration files that are required for a Ballerina Extended Library module repository.

Examples of the directory structure and common files can be found in existing Ballerina Extended Library module
repositories. The links to these repositories are available on the [Ballerina Extended Library Dashboard](https://github.com/ballerina-platform/ballerina-extended-library#status-dashboard).

The [Ballerina Azure Service Bus Module](https://github.com/ballerina-platform/module-ballerinax-azure-service-bus) would be a reference to follow along with this guide.

## Prerequisites

- Install [Ballerina](https://ballerina.io/downloads)
- Install [Gradle](https://gradle.org/releases/) (7.1 or above is recommended)
- Create a [GitHub access token](https://docs.github.com/en/enterprise-server@3.4/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token)
- Set up the following environment variables in your local environment.
    - `packageUser` - Your GitHub username
    - `packagePAT` - Your GitHub access token

## Repository Creation

- The new repository should be created under
  the [Ballerina Platform Organization](https://github.com/ballerina-platform).
- The repository request can be created by filling out
  the [GITHUB Repo Creation Request Form](https://identity-internal.cloud.wso2.com/user-portal/t/wso2internal928/assets/webapp/8f912d33-ec75-4d5f-8fb3-ac5e6f96ddf2)
  in WSO2 internal apps.

### Repository naming convention

The repository name should start with the `module-ballerinax-` prefix and then the module name.

> Note: If the module is related to a known vendor (e.g. Google, AWS, etc.), it is recommended use the vendor name as a prefix, separated by a full stop.

Example Names:

- module-ballerinax-github
- module-ballerinax-aws.s3
- module-ballerinax-googleapis.gmail

## Repository Initialization

1. Clone the repository and navigate to the repository directory.
2. Run the following command to configure Gradle in your project.
   ```shell
   gradle init
   ```

   This will open an interactive Gradle CLI to initialize the repository. Use the *default options* to generate the Gradle scripts.

   Example output of the `gradle init` command:

   ```
   Select type of project to generate:
     1: basic
     2: application
     3: library
     4: Gradle plugin
   Enter selection (default: basic) [1..4] # Select Option 1 (Hit Enter)
   
   Select build script DSL:
     1: Groovy
     2: Kotlin
   Enter selection (default: Groovy) [1..2] # Select Option 1 (Hit Enter)
   
   Project name (default: module-ballerina-sample): # Select the repository name (Hit Enter)
   
   Generate build using new APIs and behavior (some features may change in the next minor release)? (default: no) [yes, no] # Select no (Hit Enter)
   ```

3. Create a new directory named `ballerina` inside the repository. This directory will contain the Ballerina module source code.

```shell
mkdir ballerina
```

Move to the `ballerina` directory and initialize it as a Ballerina module.

```shell
cd ballerina
bal init 
```

Then follow the instructions in the following [Directory Structure](#directory-structure) section to add/update the files.

## Repository structure

Ballerina extended library modules should be structured to be aligned with the following directory structure.

```shell
.
├── .github/
├── ballerina/
├── ballerina-tests/
├── build-config/
├── compiler-plugin/
├── compiler-plugin-tests/
├── docs/
├── examples/
└── native/
```

### The `.github` directory [Required]

This directory contains the GitHub workflow scripts and other configurations required for the module. The following structure should be maintained in this directory.

```shell
.github
├── CODEOWNERS
├── pull_request_template.md
└── workflows/
    ├── build-timestamped-master.yml
    ├── build-with-bal-test-native.yml
    ├── central-publish.yml
    ├── process-load-test-result.yml
    ├── publish-release.yml
    ├── pull-request.yml
    ├── stale_check.yml
    ├── trigger-load-tests.yml
    ├── trivy-scan.yml
    └── update-spec.yml
```

- `CODEOWNERS`: This file contains the GitHub usernames of the module owners. This is used to notify the module owners on pull requests.
- `pull_request_template.md`: This file contains the template for the pull request description.

#### The `workflows` directory [Required]

This directory contains the GitHub workflow scripts. The following workflow scripts are required for a Ballerina module.

##### The `central-publish.yml` workflow script [Required]

This workflow script is used to publish the module to the [Ballerina Central](https://central.ballerina.io/) PROD environment. This workflow can be triggered manually on demand.

##### The `central-publish-dev-stage.yml` workflow script [Required]

This workflow script is used to publish the module to the [Ballerina Central](https://central.ballerina.io/) DEV or STAGE environments. This workflow can be triggered manually on demand.

##### The `pull-request.yml` workflow script [Required]

This workflow script is used to build and run tests on pull requests. It has two main jobs, `Build on Ubuntu`and `Build on Windows`, which are required checks for a pull request.
This workflow script is triggered automatically when a pull request is created or updated.

> **Note:** The above-mentioned checks (`Build on Ubuntu` and `Build on Windows`) should be marked as `required` in repo settings.

### The `ballerina` directory [Gradle Submodule][Required]

This directory contains the Ballerina module source code including the `Ballerina.toml`, `Module.md`, and `Package.md` files and the tests.

It also includes a `build.gradle` file, which is used to build the Ballerina submodule. You can use the [Ballerina Gradle plugin](https://github.com/ballerina-platform/plugin-gradle) to build the Ballerina module. 
The Ballerina Gradle plugin to add automated commits during the build to update the `Ballerina.toml`, `Dependencies.toml`, and `CompilerPlugin.toml` files.

### The `build-config` directory [Required]

This directory contains the build configurations for the module. The following structure should be maintained in this directory.

```shell
build-config
├── checkstyle
└── resources
```

#### The `checkstyle` directory [Optional]

This directory contains the checkstyle configurations for the module. This is required only if a module has Java (native) code. It includes a `build.gradle` file.

### The `ballerina-tests` directory [Gradle Submodule][Optional]

This directory contains the Ballerina tests that cannot be included in the `ballerina` directory (e.g. the tests that are required to be run with the compiler plugin). It also includes a `build.gradle` file, which is used to build and run the `ballerina-tests` submodule.

### The `compiler-plugin` directory [Gradle Submodule][Optional]

This directory contains the compiler plugin source code. It also includes a `build.gradle` file, which is used to build the `compiler-plugin` submodule.

### The `compiler-plugin-tests` directory [Gradle Submodule][Optional]

This directory contains the compiler plugin tests written using [TestNG](https://testng.org/doc/). It also includes a `build.gradle` file, which is used to build and run the compiler-plugin-tests submodule. This is required only if the module has a compiler plugin.

### The `docs` directory [Optional]

This directory contains the module specifications. It may include a `proposals` directory to add the implemented proposals.

### The `examples` directory [Gradle Submodule][Optional]

This directory contains the examples for the module. It includes a `build.gradle` file, which is used to build the `examples` submodule.

### The `native` directory [Gradle Submodule][Optional]

This directory contains the Java native code of the module. This is required only if the module has native code. 
It also includes a `build.gradle` file, which is used to build the `native` submodule.

### Other build files

Apart from the above-mentioned directories, there are other files that are required for the build. These files are
required to be added to the root directory of the module.

Following are the files that are required for the build.
```
.
├── LICENSE
├── README.md
├── build.gradle
├── changelog.md
├── codecov.yml
├── gradle.properties
├── gradlew
├── gradlew.bat
├── settings.gradle
├── spotbugs-exclude.xml
```

### The `LICENSE` file [Required]

This file contains the license of the module. All the Ballerina Standard Library modules use the `Apache-2` license.

### The `README.md` file [Required]

This file contains the description and the build steps of the module. Usually the content of this file is similar to the `Module.md` and `Package.md` files of the Ballerina module.

It also includes the status badges for the module including the `Build`, `CodeCoverage`, `Trivy`, `GraalVM`, `last commit`, and the `Open Issues` badges.

### The `build.gradle` file [Required]

This file contains the build configurations of the module. It includes the configurations for adding other Ballerina module dependencies.

### The `gradle.properties` file [Required]

This file contains the dependency versions of the module. 

### The `gradlew` and `gradlew.bat` files [Required]

These files are auto-generated using the `gradle wrapper` command. These files are required to build the module using the Gradle wrapper.

### The `settings.gradle` file [Required]

This file contains the Gradle settings of the module. It includes the Gradle submodules of the module. The above-mentioned directories related to `Gradle Submodule` should be added to this file using the following convention.

```
ballerina -> <module_name>-ballerina
ballerina-tests -> <module_name>-ballerina-tests
compiler-plugin -> <module_name>-compiler-plugin
compiler-plugin-tests -> <module_name>-compiler-plugin-tests
examples -> <module_name>-examples
native -> <module_name>-native
```

This file also includes the Gradle plugins that are used to build the module.

### The `spotbugs-exclude.xml` file [Optional]

This file is used to skip specific spotbugs warnings/errors. This is required only if the module has Java (native) code.

## Ballerina Extended Library Dashboard

Once your Ballerina extended library is implemented and published to the Ballerina Central, you can add it to
the [Ballerina Extended Library Dashboard](https://github.com/ballerina-platform/ballerina-extended-library#status-dashboard).
This dashboard is used to track the build status of all the Ballerina extended libraries.
Edit the [README.md](https://github.com/ballerina-platform/ballerina-extended-library/blob/main/README.md) file of the
Ballerina Extended Library Dashboard and, add your library status badges to the table
under [Connectors](https://github.com/ballerina-platform/ballerina-extended-library/blob/main/README.md#connectors-1)
section.
