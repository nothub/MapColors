#!/usr/bin/env sh

set -eu

cd "$(dirname "$(realpath "$0")")/.."

tag="$(date +%s)"

git tag "${tag}"
git push origin "refs/tags/${tag}"
