protrade-data
=========

This is the main data fetching API for the protrade tennis trading application. It encapsulates the fetching of statistics and scores as well as Betfair market data, providing a unified Match model which clients can use in their applications.

# Installation instructions

The build process uses GNU make(http://www.gnu.org/software/make/) and ant(http://ant.apache.org/).

To install the project:

## Checkout the code

```
cd workspace
mkdir protrade-data
cd protrade-data
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

## Finally

### Create config.local

To enable automatic login and to allow running the tests which require a Betfair account, a config.local file must be created under the project root. This file must contain the Betfair user account and the encrypted password.

```
username:=yourusername
password:=yourencryptedpassword
```

To help encrypt the password, a utility class is provided under:  org.ic.protrade.authentication.Encrypt.

# Usage

`ant jar` packages the project.

You can also run the recording demo: `java -cp bin/:lib/*:lib/static/* org/ic/protrade/data/demo/RecordingDemo`

###Note!
The protrade-data.jar created with `ant jar` will not include any dependencies from lib/.
