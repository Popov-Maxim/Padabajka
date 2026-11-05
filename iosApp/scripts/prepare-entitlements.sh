#!/bin/sh
set -eu

project_id=$(/usr/libexec/PlistBuddy -c "Print :PROJECT_ID" "$FIREBASE_CONFIG")
firebase_auth_host="$project_id.firebaseapp.com"

mkdir -p "$DERIVED_FILE_DIR"
cp "$SRCROOT/iosApp/iosApp.entitlements" "$CODE_SIGN_ENTITLEMENTS"
/usr/libexec/PlistBuddy -c "Add :com.apple.developer.associated-domains:0 string applinks:$firebase_auth_host" "$CODE_SIGN_ENTITLEMENTS"
/usr/libexec/PlistBuddy -c "Add :com.apple.developer.associated-domains:1 string applinks:www.$firebase_auth_host" "$CODE_SIGN_ENTITLEMENTS"
