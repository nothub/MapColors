#!/usr/bin/env sh

set -eu

cd "$(dirname "$(realpath "$0")")"

mkdir -p run
echo 'eula=true' >run/eula.txt

./gradlew runServer

mkdir -p build/dist
cp run/colors.csv build/dist/
