# jtiki

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

jtiki is an interpreter for my own language, tiki. I'm mostly following along with the book [Crafting Interpreters](https://www.craftinginterpreters.com/) by Robert Nystrom. It's written in Java, as per part two of the book. However, not all features are identical. At my own discretion I have made changes or additions I found interesting.

## How to build

Install [Apache Ant](https://ant.apache.org/)

### Linux
On Ubuntu use the `apt-get install ant` command to install Ant. For other distributions please check the documentation of your vendor.

### Windows
Download Apache Ant from [http://ant.apache.org/](http://ant.apache.org/).

Extract the zip file into a directory structure of your choice. Set the `ANT_HOME` environment variable to this location and include the `ANT_HOME/bin` directory in your path.

Make also sure that the `JAVA_HOME` environment variable is set to the JDK. This is required for running Ant.

Check your installation by opening a command line and typing `ant -version` into the command line. The system should find the command `ant` and show the version number of your installed Ant version.

### Building

After installing ant, run the following commands.
```
git clone https://github.com/AtticusHelvig/jtiki.git
cd ./jtiki
ant
```

### Generating Expr and Stmt

jtiki has an automatic build option for the Stmt and Expr classes made by the GenerateAst class.

Assuming you are in the jtiki directory and have ant installed, run the following command to generate those classes.
```
ant gen
```

## How to run

If you built jtiki from source, you should have a bash script `jtiki` which you can run with the following command inside the jtiki directory.
```
./jtiki
```

If you downloaded the jar from the release tab, use the following command.
```
java -jar jtiki.jar
```
