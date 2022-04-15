#!/bin/bash

#Variables
IP_ADDR=""
CONFIGMAP_NAME="kubectl-config"
NAME_SPACE=""

mkdir created

sed -e "s/SERVER_IP/${IP_ADDR}/g" < ./deployment-template.yaml > created/deployment.yaml
sed -i "s/CONFIGMAP_NAME/${CONFIGMAP_NAME}/g" created/deployment.yaml

sed -e "s/NAME_SPACE/${NAME_SPACE}/g" < ./serviceAccount-template.yaml > created/serviceAccount.yaml


#Apply
kubectl apply -f created/*
kubectl apply -f service.yaml
echo "All Done."