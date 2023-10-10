# Quality Assurance Guide for Ballerina Library Modules

- Authors: @NipunaRanasinghe
- Reviewers: @abeykoon @anupama-pathirage
- Created: 2023/05/26
- Updated: 2023/05/29

## Introduction

This document outlines a set of rules and guidelines to be followed during the development, testing, and documentation
stages of the Ballerina library development lifecycle.
By adhering to this checklist, Ballerina library developers can effectively evaluate and validate the
functionality, compatibility, security, performance, documentation, and code quality aspects of the modules.

## Library Structure

We have enforced a common structure for all Ballerina library modules to ensure consistency and
maintainability of the modules.

- Refer to the [Repository Structure](module-contribution-guide.md#repository-structure) section of the Ballerina
  library contribution guide for more information on the recommended structure.

## Code Quality

- The Ballerina coding standards and styles guide should always be followed within the Ballerina package.
    - The `Clean Code` Book can be considered as a good reference.
    - Also refer
      to [Ballerina anti-patterns](https://docs.google.com/document/d/1y6QVqaZzZt9jMpYV4jP5WRS_W_KoC4y40Uuoh1ALu8E/edit?usp=sharing)
      documentation.

- If the Ballerina library module contains a native (i.e. Java) implementation,
    - Java coding standards and best practices must be followed.
    - Java static analysis tools such as [Checkstyle](https://checkstyle.sourceforge.io/)
      and [SpotBugs](https://spotbugs.github.io/) should be configured to ensure code quality.
- Appropriate comments and documentation should be included in the code for maintainability.
- Necessary code reviews and peer reviews should be conducted whenever adding new features or making significant
  changes to the existing code.

## Functionality

- All the library APIs in the module should work as expected.
    - Edge cases and error scenarios should be appropriately handled within the functions.
    - Unit tests should be added to verify the functionality of each API.
- All required features and functionalities should be implemented.
    - Better to create a proposal document and get it approved before starting the implementation.
- Any known issues or limitations with the module should be proactively identified and addressed/documented properly.

## Compatibility

- The module should be tested with different versions of Ballerina to ensure compatibility. For handwritten ballerinax
  modules, There should be separate branches for each Swan Lake update version so that separate workflows can be
  configured for each version to ensure the compatibility.
- The `Package.md` file of the module should include an updated compatibility section with the following information:
    - minimum-compatible Ballerina version (e.g., Swan Lake 2201.4.1)
    - version of the external library or API that the module is compatible with. (e.g., Azure Service Bus SDK 7.13.1)
- The `Module.md` should contain clear instructions on how to set up compatible prerequisites (e.g., accounts, access
  tokens, etc.) for the module to work.

## Documentation

- Each module should have a `Package.md` file with the following information:
    - Brief overview of the module.
    - Compatibility section that contains the minimum-compatible Ballerina version and the version of the external
      library or API that the module is compatible with.
    - Community links such as the repository, issue tracker, and discussion channels.
    - The recommended template for the Package.md file can be found in [here](file-templates/Package.md)
- Each module should have a `Module.md` file with the following information:
    - Brief overview of the module.
    - List of prerequisites (e.g., accounts, access tokens, etc.) required to use the module.
    - Quick start guide that contains step-by-step instructions (along with code snippets) of a simple use case.
    - Link to the `samples`/`examples` directory that contains more real world examples.
    - The recommended template for the Module.md file can be found in [here](file-templates/Module.md)
- For handwritten connectors, the spec and the proposals should be added to `docs/spec` and `docs/proposals`directories
  respectively.
- Ballerina API documentation should follow the best practices.
  Refer to the https://ballerina.io/learn/generate-code-documentation/#write-ballerina-documentation for more details.
- All of the above documentation components should be kept updated along with any impacting changes or additions to the
  module.

## Testing

- Unit tests should be written to cover the functionality of the module.
- It is required to have 80% or more code coverage for the extended library modules.
- Test cases for edge cases, error handling, and boundary conditions should be included.
- It is strongly recommended to add new test cases for any bug fixes or enhancements introduced.

## Security

- The module should be reviewed for potential security vulnerabilities (i.e. input validation or injection attacks).
- Best practices for security, such as following secure coding standards and avoiding common pitfalls, should be
  followed.
- Each extended library module should ideally have security checks in place to ensure that the module does not contain
  any security vulnerabilities coming from its third-party dependencies. This can be done by using a security scanning
  tool such as Trivy, which is already integrated into the standard library build process.

### Trivy Scan Report

Following steps can be taken to identify the security vulnerabilities of the third-party dependencies using Trivy.

- Refer to [installation](https://aquasecurity.github.io/trivy/latest/getting-started/installation/) guide for
  installing Trivy based on your operating system.
- Navigate to the module directory and execute the `./gradlew build` command to build the module.
- Now, execute the `trivy fs ballerina/lib` command to start the Trivy scan. If there is any vulnerability there will be
  a similar report as below.
    ```shell
    2021-09-05T20:01:46.858+0530	INFO	Number of language-specific files: 189
    2021-09-05T20:01:46.858+0530	INFO	Detecting jar vulnerabilities...

    bcprov-jdk15on-1.61.jar (jar)
    ===========================================
    Total: 1 (UNKNOWN: 0, LOW: 0, MEDIUM: 1, HIGH: 0, CRITICAL: 0)

    +---------------------------------+------------------+----------+-------------------+---------------+---------------------------------------+
    |             LIBRARY             | VULNERABILITY ID | SEVERITY | INSTALLED VERSION | FIXED VERSION |                 TITLE                 |
    +---------------------------------+------------------+----------+-------------------+---------------+---------------------------------------+
    | org.bouncycastle:bcprov-jdk15on | CVE-2020-15522   | MEDIUM   |              1.61 |          1.66 | bouncycastle: Timing issue            |
    |                                 |                  |          |                   |               | within the EC math library            |
    |                                 |                  |          |                   |               | -->avd.aquasec.com/nvd/cve-2020-15522 |
    +---------------------------------+------------------+----------+-------------------+---------------+---------------------------------------+
    ```
- Find and update the installed vulnerable dependency for the vulnerability fixed version as in the report or the latest
  stable version (recommended).
- The results should be recorded in a GitHub issue in the form of a report. The issue must be tagged with the relevant
  milestone.

Additionally, a security vulnerability can be notified either from internal security tests and scans or
the <security@ballerina.io> mailing list. More on that is explained [here](https://ballerina.io/security/).

## Tooling Support

Library developers must verify that the tooling support is available and working as expected for a given library.
Even though there are many tools available, the primary focus should be on the Ballerina VSCode plugin.

- The following features are expected be tested and verified on each extended library module.
    - Syntax highlighting
    - Diagnostics
    - Code completions
    - Code actions
    - Formatting
    - Hover

## Community Feedback

- All the extended library related issues and discussions should be tracked in the [Ballerina Extended Library
  repository](https://github.com/ballerina-platform/ballerina-extended-library)
- Feedback and suggestions received from the community should be considered high-priority and addressed in a timely
  manner.
