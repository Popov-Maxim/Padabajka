#!/bin/bash

OLD_PACKAGE="com/fp/padabajka"
PACKAGE_RMDIR="com/fp"
NEW_PACKAGE="com/padabajka/dating"

find .. -type d -path "*/$OLD_PACKAGE" | while read -r DIR; do
    NEW_DIR=$(echo "$DIR" | sed "s|$OLD_PACKAGE|$NEW_PACKAGE|")

    mkdir -p "$NEW_DIR"
    git mv "$DIR"/* "$NEW_DIR/"

    echo "MOVE: $DIR → $NEW_DIR"
done

find .. -type d -path "*/$PACKAGE_RMDIR" | while read -r DIR; do
    echo "$DIR"
    rmdir "$DIR"
done

echo "✅ Done!"
