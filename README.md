protrade-data
=========

This is the main data fetching API for the protrade tennis trading application. It encapsulates the fetching of statistics and scores as well as Betfair market data, providing a unified Match model which clients can use in their applications.

# Installation instructions

The build process uses make and ant.
The Makefile only provides a wrapper for the ant script, so it is not really required, but provides a few simple tasks to pass some runtime parameters to the ant runtime.

To install ant, follow the instructions here: http://ant.apache.org/.

Use ant -p to list all available tasks.

To install the project:

1. get the code from Github (i.e. here)
2. run make -> this task will: 1. fetch ivy (required for managing dependencies) 2. resolve dependencies and 3. run a standard build
3. done :p