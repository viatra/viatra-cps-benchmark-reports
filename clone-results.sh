#!/bin/bash

cd "$( cd "$( dirname "$0" )" && pwd )/../.."

# Clone results repository if needed
if [ -d "viatra-cps-benchmark-results" ]; then
  # Repo exists, update
  cd viatra-cps-benchmark-results
  git fetch
  git reset origin/master --hard
  cd ..
else
  # Clone repo
  git clone git@github.com:viatra/viatra-cps-benchmark-results.git viatra-cps-benchmark-results
fi