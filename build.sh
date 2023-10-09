## Get the branch name
branch=$(git branch --show-current)
echo "Branch name is $branch"

# Build the fatjar
echo "building the fatjar"
./gradlew shadowJar

tagName=yilmaznaslan/berlin-auslaenderbehorde-termin-bot:$branch
echo "Build the image tag: $tagName"
docker build --tag $tagName --file Dockerfile .


# - Push the image to a docker registery `
echo "Pushing image to container registery"
docker push $tagName