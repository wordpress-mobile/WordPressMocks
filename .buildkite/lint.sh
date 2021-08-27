#!/bin/bash -eu

echo "--- :rubygems: Setting up Gems"
install_gems

echo "--- :terminal: Linting JSON"
bundle exec rake lint
