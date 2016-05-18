# AudioCues
AudioCues is a research prototype demonstrating how gradually changing dissonances can be injected into an audio stream of (MIDI-based) ambient music to reflect changes in cloud monitoring data. Users then unconsciously become aware of these changes while being concentrated on another task.

A short introductory video explaining the concepts can be found here: https://www.youtube.com/watch?v=gWJtGZOp3K0

## Getting started

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

## Publications
If you use AudioCues for research purposes with a resulting publication, we would appreciate if you cite our paper:
```TeX
@inproceedings{bermbach_audiocues,
 author = {David Bermbach and Jacob Eberhardt},
 title = {Towards Audio-Visual Cues for Cloud Infrastructure Monitoring},
 booktitle = {Proceedings of the 4th IEEE International Conference on Cloud Engineering (IC2E2016)},
 series = {IC2E'16},
 year = {2016},
 publisher = {IEEE}
}
```

