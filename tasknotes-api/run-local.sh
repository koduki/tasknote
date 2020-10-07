#!/bin/bash

export TASKNOTES_GCP_PROJECT=toolbox-289508 
export TASKNOTES_GCP_BUCKET=ajfgeay8733
export GOOGLE_APPLICATION_CREDENTIALS=/mnt/c/Users/koduki/Downloads/toolbox-289508-ff88928d4ca7.json 
./mvnw clean quarkus:dev
