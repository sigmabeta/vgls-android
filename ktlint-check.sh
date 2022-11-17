#!/bin/bash

# Get a specific version of ktlint
curl -sSLO https://github.com/pinterest/ktlint/releases/download/0.47.1/ktlint
chmod a+x ktlint

# Use it to fix as many problems as it can
./ktlint --android --reporter=html,output=ktlint.html