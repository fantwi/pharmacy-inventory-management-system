#!/usr/bin/env bash

set -e

mkdir -p out

javac -d out src/com/pharmacy/Main.java src/com/pharmacy/model/*.java src/com/pharmacy/service/*.java src/com/pharmacy/exception/*.java src/com/pharmacy/ui/*.java

java -cp out com.pharmacy.Main