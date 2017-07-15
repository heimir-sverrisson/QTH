# QTH
This is a collection of utility functions for Amateur radio QTH (location) calculations.
Including Maidenhead square/subsquare calculations and distance and bearing between
two locations.
Conversion between lat/long coordinates and Maidenhead squares in both directions.
This library only supports squares that are 4 our 6 characters long, i.e. `DN70` or `DN70ja`.

## Setup
You need to install **gradle 4.0** and **JDK 1.7.0** or later

### Clone the repo
```
git clone https://github.com/heimir-sverrisson/QTH.git
cd QTH
```
### Run Gradle
```
gradle build
```
The Jar file `QTH-{version}.jar` should be in the `build/libs` directory.

### Optional tasks
The `javadoc` documentation can be built with:
```
gradle javadoc
``` 
The documentation is then found in `build/docs/javadoc/index.html`.

Tests can be run with: 
```
gradle test
``` 
and HTML test reports found in `build/reports/tests/index.html`.
