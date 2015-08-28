# audiocues

[Setup]
- Checkout the project from github into a local directory
- In this directory, execute the following command to resolve dependencies, install them in your local maven repository, build the project and install the resulting artifact in your local repository as well: mvn clean install

[Eclipse import]
- Execute the following command in the project directory to generate eclipse metadata: mvn eclipse:eclipse
- Import this project into Eclipse by using the "Import Existing Project"-Wizard.
- Rightclick the project in project explorer and select: Configure -> Convert to Maven Project.


