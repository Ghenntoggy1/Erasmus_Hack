# PBL Security Project

## Quick Start
We mainly use the JetBrains Java IDE, IntelliJ Idea, which simplifies launching the project a lot, but we can show you how to work with the commands if you are on Linux or just having troubles with the interface:

1. Launch docker container (for Postgres), you should be in the root of the project:
```bash
docker compose up -d
```
This will pull the image of PostgreSQL and launch the container.
2. Launch the Java project.
The primary approach would be to navigate to the `Main.java` file and just press the `run` button, but you can use also the CLI:
```bash
javac  .\src\main\java\Main.java 
```
NOTE: You have to install Java locally for this one.

## Configuration Files Overview:

For dependencies we use `pom.xml` or Maven. For avoiding any bugs and brain fucks when first launching the project just do:
```bash
mvn clean install
```
Or search for the Maven button in IntelliJ and click `clean`. This will clean previous runs of the project and update dependencies.

For credentials, we use `application.properties` located in `src/main/resources`. Here all the necessary credentials you need are located.
