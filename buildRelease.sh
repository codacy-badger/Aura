#!/usr/bin/env bash

chmod +x gradlew
./gradlew clean :util:build :androidutil:assembleRelease

rm -rf release
mkdir release

for file in */build/outputs/aar/*-release.aar; do
  mv ${file} release
done

for file in */build/libs/*.jar; do
  mv ${file} release
done