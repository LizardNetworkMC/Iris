#!/bin/bash

# Changes (YYYY-MM-DD):
#   - 2024-01-04 @xIRoXaSx: Added script file.

dockerBuild() {
    mkdir -p "${ROOT}/build"

    local workdir="/home/gradle/iris"
    docker run \
        -u="gradle" \
        -v="$PWD":"${workdir}" \
        -w="${workdir}" \
        --tmpfs="${workdir}/.gradle/:uid=1000,gid=1000" \
        --rm \
        "${DOCKER_IMAGE_GRADLE}" \
            ./gradlew \
            iris
}
