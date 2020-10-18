# task.Notes

Make easy `"Note -> Task"`, more and more!

## Build & Deploy via monolith

```bash
$ export BUCKET_DATA=""
$ export BUCKET_IMAGE=""
$ gcloud builds submit --config tasknotes-mono/cloudbuild.yaml \
    --substitutions=_BUCKET_IMAGE=${BUCKET_IMAGE},_BUCKET_DATA=${BUCKET_DATA}  
    --project toolbox-28950
```