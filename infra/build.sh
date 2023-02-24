## Get the branch name
branch=$(git describe --contains --all HEAD)
echo "Branch name is $branch"


# Build the fatjar
echo "building the fatjar"
./gradlew shadowJar

tagName=yilmaznaslan/berlinterminfinder:$branch
echo "Build the image tag: $tagName"
docker build --tag $tagName --file Dockerfile .


# - Push the image to a docker registery `
echo "Pushing image to container registery"
docker push $tagName