## Get the branch name
branch=$(git describe --contains --all HEAD)
echo "Branch name is $branch"


# Build the fatjar
echo "building the fatjar"
./gradlew shadowJar

tagName=yilmaznaslan/berlinterminfinder:$branch
echo "Build the image tag: $tagName"
docker build --tag tag --file Dockerfile .