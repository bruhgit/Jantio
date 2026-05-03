#!/bin/bash

# Jantio Build Script
echo "Jantio - Building..."

# Temizle
rm -rf out/production/jantio
mkdir -p out/production/jantio

# Derle
find src -name "*.java" | xargs javac -d out/production/jantio -encoding UTF-8

# JAR oluştur
jar cfe jantio.jar com.jantio.JantioApp -C out/production/jantio .

echo "Build completed! Run with: java -jar jantio.jar"
