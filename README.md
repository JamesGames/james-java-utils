James Java Utils
===============

James Java Utils is a library that contains various classes that I found to be useful between multiple projects of mine. I plan on slowly adding classes here as projects that use new classes appear on Github.

## Requirements

* Java 1.8

## Installation

To use James Java Utils in your Maven managed project use the
following dependency definition:

    <dependency>
        <groupId>com.github.JamesJamesJames</groupId>
        <artifactId>james-java-utils</artifactId>
    </dependency>

The source code isn't hosted any maven repo, so in the meantime you'll have to download this project from here on Github and build it with maven to have it exist on your machine's local maven repository (easy to do if you build it via your projects maven build where this project is a git submodule of your own project).

## Building

To manually build the project, download Maven and change the current
working directory to the directory containing pom.xml and run the
command "mvn install".
The default location for the Maven local repository is

* "~/.m2" on Linux and OSX

or

* "Documents and Settings\{your-username}\.m2" on Windows

## Credits

* James Murphy - JamesGames.Org(at)gmail.com
