#!/bin/bash

# Dear user: please replace the following variable with the root package of your project.
ROOT_PACKAGE=com.vgleadsheets

# Dear user: please replace the following with a path to a stub build.gradle you want 
# to use for this new module.
STUB_GRADLE=./stub.gradle

# Dear user: please replace the following with a path to a directory full of KT files
# you want to prefill your new module with.
STUB_KT=./stubs

if [ -z ${ROOT_PACKAGE+x} ]; then 
	echo "Please modify this script to set ROOT_PACKAGE."; 
	exit 1
fi

if [ -z ${STUB_GRADLE+x} ]; then 
	echo "Please modify this script to set STUB_GRADLE."; 
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
echo ""
echo ""
read MODULE_PATH

MODULE_ID=:${MODULE_PATH//\//\:}
MODULE_PACKAGE=$ROOT_PACKAGE.${MODULE_PATH//\//\.}
ROOT_PATH=${ROOT_PACKAGE//./\/}
KT_SRC_PATH=$MODULE_PATH/src/main/java/$ROOT_PATH/$MODULE_PATH
LAYOUT_SRC_PATH=$MODULE_PATH/src/main/res/layout
MANIFEST_PATH=$MODULE_PATH/src/main/AndroidManifest.xml
BUILD_GRADLE_PATH=$MODULE_PATH/build.gradle

# echo ""
# echo "Creating directory: $MODULE_PATH"
mkdir -p $KT_SRC_PATH
mkdir -p $LAYOUT_SRC_PATH

# echo ""
# echo "Module id: $MODULE_ID"
# echo "Module package name: $MODULE_PACKAGE"
# echo "Manifest location: $MANIFEST_PATH"
# echo "Source location: $KT_SRC_PATH"
# echo "build.gradle location: $BUILD_GRADLE_PATH"
# echo ""
# echo ""

echo -e ",\n        '$MODULE_ID'" >> settings.gradle
echo -e "<manifest package=\"$MODULE_PACKAGE\" />\n" > $MANIFEST_PATH

cp $STUB_GRADLE $BUILD_GRADLE_PATH

if [ -z ${STUB_KT+x} ]; then 
	echo "All done!"
	echo -e "\n"
	echo "If you modify this script to set STUB_KT, I can add all the KT files";
	echo "in that location to your new module. Optionally, place an XML file in"
	echo "there and I'll copy it to /res/layout in your new module."
	exit 0
fi


echo "Please enter the name of your feature's fragment. So if your fragment will be"
echo "called 'ThingFragment', enter 'Thing'."
echo ""
echo ""
read NAME_PASCAL

# # sed arguments
CAMEL_TO_SNAKE="s/\([a-z0-9]\)\([A-Z]\)/\1_\L\2/g"
FIRST_TO_LOWER="s/./\L&/"

NAME_SNAKE=$(echo $NAME_PASCAL | sed $CAMEL_TO_SNAKE | sed $FIRST_TO_LOWER)
NAME_CAMEL=$(echo $NAME_PASCAL | sed $FIRST_TO_LOWER)

# Copy all the stub files to the source directory for the new module.
for FILE_NAME in $STUB_KT/*
do
    if [ ${FILE_NAME: -3} == ".kt" ]; then
        # echo "Copying $FILE_NAME to $KT_SRC_PATH..."
	    cp $FILE_NAME $KT_SRC_PATH
    fi

	if [ ${FILE_NAME: -4} == ".xml" ]; then
        # echo "Copying $FILE_NAME to $LAYOUT_SRC_PATH..."
	    cp $FILE_NAME $LAYOUT_SRC_PATH/fragment_$NAME_SNAKE.xml
    fi
done

# echo ""

for OLD_FILE_NAME in $KT_SRC_PATH/*
do
	NEW_FILE_NAME="$(echo ${OLD_FILE_NAME} |sed -e 's/FEATURE_NAME_/'$NAME_PASCAL'/g')"

	# echo "Renaming $OLD_FILE_NAME to $NEW_FILE_NAME..."
	mv $OLD_FILE_NAME $NEW_FILE_NAME
done

findreplace () {
	# First argument is the string to find
	# Second argument is the string to replace with

	# echo "Replacing $1 with $2..."

	for FILE_NAME in $KT_SRC_PATH/*
	do
		# echo "In $FILE_NAME..."
		sed -i 's/'$1'/'$2'/g' $FILE_NAME
	done

	# echo "Done!"
	# echo ""
}

findreplace "feature_name_" $NAME_SNAKE
findreplace "feature_name" $NAME_CAMEL
findreplace "Feature_Name_" $NAME_PASCAL

echo ""
echo "All done! Happy developing."
echo ""