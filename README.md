## CSE210 CW1

#### Prerequisites

* Java 8
* Maven
* Apache POI
* Dataset (included at `src/main/java/cse210/Dataset_RG.xlsx`)

#### How to run the code?

* First install dependencies `mvn install` or `mvn compile`
* Run App by `mvn exec:java -Dexec.mainClass="cse210.App" -X`

Optionally,

* Generate javadoc `mvn javadoc:javadoc` or `mvn javadoc:javadoc -Dshow=private` for generating private docs as well (which should be at `target/site/apidocs`)
* `mvn test` for simple testing
