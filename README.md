# audiocues

[Setup]
- Checkout the project from github into a local directory
- In this directory, execute the following command to resolve dependencies, install them in your local maven repository, build the project and install the resulting artifact in your local repository as well: mvn clean install

[Eclipse import]
- Execute the following command in the project directory to generate eclipse metadata: mvn eclipse:eclipse
- Import this project into Eclipse by using the "Import Existing Project"-Wizard.
- Rightclick the project in project explorer and select: Configure -> Convert to Maven Project.

[Execution]

There are two convenient options for execution

1. Directly from Eclipse:
Execute any main method by using the Eclipse UI. Recommended entry point is the class "de.tuberlin.ise.dbe.audiocues.AudioCueStarter".

2. Executing a Java Archive:
Maven is configured to directly generate an executable jar with the AudioCueStarter as its entry point.
This jar file is located in the target directory.
Execute it by invoking the following command in that directory: "java -jar AudioCues-0.0.1-SNAPSHOT.jar"



