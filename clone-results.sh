#!/bin/bash
# Clone results repository if needed
if [ -d "viatra-cps-benchmark-results" ]; then
  # Repo exists, update
  rm -rf viatra-cps-benchmark-results
fi
  # Clone repo
  git clone git@github.com:viatra/viatra-cps-benchmark-results.git viatra-cps-benchmark-results
