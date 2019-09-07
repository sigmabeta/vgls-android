#!/bin/bash

# Dear user: please replace the following variable with the root package of your project.
ROOT_PACKAGE=com.vgleadsheets

# Dear user: please replace the following with a path to a stub build.gradle you want 
# to use for this new module.
STUB_GRADLE=./stub.gradle

if [ -z ${ROOT_PACKAGE+x} ]; then 
	echo "Plese modify this script to set ROOT_PACKAGE."; 
	exit 1
fi

if [ -z ${STUB_GRADLE+x} ]; then 
	echo "Plese modify this script to set STUB_GRADLE."; 
	exit 1
fi

# Check that settings.gradle is in this directory.
SETTTINGS_FILE=settings.gradle
if test -f "$SETTTINGS_FILE"; then
    echo "Found $SETTTINGS_FILE."
else 
	echo "This script should be run from your project root directory."
	echo "Run it from wherever your settings.gradle file is located."	
	exit 1
fi

echo "Please enter the relative path to your new module. (e.g. features/some/module)"
read MODULE_PATH

MODULE_ID=:${MODULE_PATH//\//\:}
MODULE_PACKAGE=$ROOT_PACKAGE.${MODULE_PATH//\//\.}
SRC_PATH=$MODULE_PATH/src/main/java/$MODULE_PACKAGE
MANIFEST_PATH=$MODULE_PATH/src/main/AndroidManifest.xml
BUILD_GRADLE_PATH=$MODULE_PATH/build.gradle

echo "Creating directory: $MODULE_PATH"
mkdir -p $SRC_PATH

echo "Module id: $MODULE_ID"
echo "Module package name: $MODULE_PACKAGE"
echo "Manifest location: $MANIFEST_PATH"
echo "Source location: $SRC_PATH"
echo "build.gradle location: $BUILD_GRADLE_PATH"

echo -e ",\n        '$MODULE_ID'" >> settings.gradle
echo -e "<manifest package=\"$MODULE_PACKAGE\" />\n" > $MANIFEST_PATH

cp $STUB_GRADLE $BUILD_GRADLE_PATH