
https://e.printstacktrace.blog/using-sdkman-as-a-docker-image-for-jenkins-pipeline-a-step-by-step-guide/

docker build -t sdkman:local .

docker run --rm -u sdkman:local java -version

docker run --rm -u $(id -u) sdkman:local java -version

---

docker build \
    --build-arg JAVA_VERSION=15.0.2.j9-adpt \
    --build-arg MAVEN_VERSION=3.6.3 \
    -t sdkman:mvn-3.6.3-jdk-15.0.2.j9-adpt .

---

docker run -it --rm \
	-v gradle-cache:/root/.gradle \
	-v $(pwd):/root/project \
	-w /root/project \
	demo-app-builder \
	gradle build
