#!/bin/bash

#
# Load config
#
[[ -z "${BUCKET_IMAGE}" ]] && BUCKET_IMAGE=$(cat ${HOME}/.tasknotes_deploy|grep BUCKET_IMAGE:|awk '{print $2}')
[[ -z "${BUCKET_DATA}" ]] && BUCKET_DATA=$(cat ${HOME}/.tasknotes_deploy|grep BUCKET_DATA:|awk '{print $2}')
[[ -z "${SRV_ACCOUNT}" ]] && SRV_ACCOUNT=$(cat ${HOME}/.tasknotes_deploy|grep SRV_ACCOUNT:|awk '{print $2}')
[[ -z "${GCP_JWT_ISSUER}" ]] && GCP_JWT_ISSUER=$(cat ${HOME}/.tasknotes_deploy|grep GCP_JWT_ISSUER:|awk '{print $2}')
[[ -z "${GCP_PRJ}" ]] && GCP_PRJ=$(cat ${HOME}/.tasknotes_deploy|grep GCP_PRJ:|awk '{print $2}')

cd `dirname $0`/../
gcloud builds submit --config tasknotes-mono/cloudbuild.yaml --substitutions=_BUCKET_IMAGE=${BUCKET_IMAGE},_BUCKET_DATA=${BUCKET_DATA},_SRV_ACCOUNT=${SRV_ACCOUNT},_GCP_JWT_ISSUER=https://securetoken.google.com/${GCP_JWT_ISSUER} --project ${GCP_PRJ}