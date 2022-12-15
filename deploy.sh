#!/bin/bash
export NAMESPACE=lianan
export API_VERSION=v1
export APPNAME=bookinfo
export NAME=$APPNAME-$API_VERSION

export IMAGE_SERVER=quay.redhat.com/lianan/$APPNAME

export VERSION=1.0-B
export IMAGE=$IMAGE_SERVER:$VERSION


envsubst < template.yaml > deploy.yaml