This piece of code is dependant on non-free IntelliJIDEA 9.0.x libraries:
IntelliJIdea9Directory/plugins/tasks/lib/tasks-core.jar
IntelliJIdea9Directory/plugins/tasks/lib/jira-connector.jar

Please add then to your maven repository.

Example poms:


.m2/repository/com/intellij/idea/jira-connector/9.0.2

<?xml version="1.0" encoding="UTF-8"?><project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.intellij.idea</groupId>
  <artifactId>jira-connector</artifactId>
  <version>9.0.2</version>
</project>

 mvn -o install:install-file -Dfile=jira-connector.jar -DgroupId=com.intellij.idea -DartifactId=jira-connector -Dversion=9.0.2  -Dpackaging=jar

.m2/repository/com/intellij/idea/tasks-core/9.0.2

<?xml version="1.0" encoding="UTF-8"?><project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.intellij.idea</groupId>
  <artifactId>tasks-core</artifactId>
  <version>9.0.2</version>
</project>

mvn -o install:install-file -Dfile=tasks-core.jar -DgroupId=com.intellij.idea -DartifactId=tasks-core -Dversion=9.0.2 -Dpackaging=jar
mvn -o install:install-file -Dfile=jira-connector.jar -DgroupId=com.intellij.idea -DartifactId=jira-connector -Dversion=9.0.2  -Dpackaging=jar

