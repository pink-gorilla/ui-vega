#!/bin/sh

clj -X:ci :profile '"npm-install"'
clj -X:ci 
npm test