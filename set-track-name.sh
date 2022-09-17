#!/bin/bash
BRANCH=$(git rev-parse --abbrev-ref HEAD)

if [ $BRANCH == "release" ]; then
	track_temp="production"
elif [ $BRANCH == "beta" ]; then
	track_temp="beta"
else
	track_temp="internal"
fi

echo "Will upload this build to the Google Play \"${track_temp}\" track."

echo "export TRACK=$track_temp" >> $BASH_ENV
