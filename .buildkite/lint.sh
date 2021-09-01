#!/bin/bash -eu

echo "--- :rubygems: Setting up Gems"
bundle install

echo "--- :terminal: Linting JSON"
bundle exec rake lint
