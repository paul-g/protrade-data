protrade-data
=========

This is the main data fetching API for the protrade tennis trading application. It encapsulates the fetching of statistics and scores as well as Betfair market data, providing a unified Match model which clients can use in their applications.

# Installation instructions

The build process uses make and ant.
The Makefile only provides a wrapper for the ant script, so it is not really required, but provides a few simple tasks to pass some parameters to the ant runtime.

To install ant, follow the instructions here: http://ant.apache.org/.

To list all available tasks:
`ant -p` 

To install the project:

## Get the code from Github (i.e. here)

```
cd workspace
mkdir protrade-data
git init
git remote add origin git@github.com:paul-g/protrade-data.git
git pull -u origin master
```

## Build the project

First option (using make):

```
cd wokspace/protrade-data
make
```

Second option:

```
ant init-ivy  (fetch ivy  - required for managing dependencies)
ant resolve   (resolve dependencies from maven central, using ivy)
ant all       (run a standard build)
```

## Done

If everything went well, you should be done at this stage :p

# Usage

After you have fetched the project, made the desired modification etc. , you can use the provided `ant jar` task to create a jar which you can use in your other projects. 

###Note!
The protrade-data.jar created with `ant jar` will not include the dependencies (anything from the lib/ folder), but only the class hierarchy.