pipeline {
    agent any

    environment {
        JDK_VERSION = "JDK-11"
        REGISTRY_NAME = "s2iwi2s/lms"
        IMG_NAME = "${REGISTRY_NAME}:${env.BUILD_ID}"

        REGISTRY_URL = 'https://registry.hub.docker.com'
        REGISTRY_CREDENTIAL = 'docker-hub-login'

        GIT_REPO = 'https://github.com/s2iwi2s/learn-mngt-sys.git'
        GIT_BRANCH = 'main'

        DHOST = 'stoi@192.168.44.129'
        DOCKER_HOST = "tcp://localhost:2375"

        DB_URL = "jdbc:postgresql://postgres-db:5432/LearnMngtSys"
        JAVA_OPTIONS = "-Xmx512m -Xms256m"

        IMAGE_ARGS = "--build-arg=BASE_IMAGE=openjdk:11 --build-arg=JAVA_OPTS='${JAVA_OPTIONS}' --build-arg=JHIPSTER_SLEEP=3 --build-arg=SPRING_PROFILES_ACTIVE=prod,api-docs --build-arg=SPRING_DATASOURCE_URL=${DB_URL} --build-arg=SPRING_LIQUIBASE_URL=${DB_URL} ."

        dockerImage = ''
    }

    tools {
        jdk "${JDK_VERSION}"
    }

    stages {

        /*stage('Show Env') {
            steps {
                sh 'printenv|sort'
            }
        }*/

        stage('checkout') {
            steps {
                git branch: GIT_BRANCH, url: GIT_REPO
            }
        }

        stage('clean') {
            steps {
                sh "java -version"
                sh "chmod +x mvnw"
                sh "./mvnw -version"
                sh "./mvnw -ntp clean"
            }
        }

        stage ('Stop previous running container'){
            steps{
                sh returnStatus: true, script: 'docker stop $(docker ps -a | grep ${JOB_NAME} | awk \'{print $1}\')'
                sh returnStatus: true, script: 'docker rmi $(docker images | grep ${REGISTRY_NAME} | awk \'{print $3}\') --force' //this will delete all images
                sh returnStatus: true, script: 'docker rm $JOB_NAME'
            }
        }

        stage('Packaging as jar') {
            steps {
                sh "./mvnw -ntp -Pprod,no-liquibase verify -DskipTests"
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            }
        }

        stage('Build Image') {
            steps {
                echo "********************************************************"
                echo "Building our image: ${IMG_NAME}"
                script {
                    docker.withServer(DOCKER_HOST) {
                        dockerImage = docker.build(IMG_NAME, IMAGE_ARGS)
                    }
                }
            }
        }

        stage('Deploy to docker') {
            steps {
                script {
                    dockerImage.run("--name ${JOB_NAME} -p 8181:8181 --network pgnetwork")
                }
            }
        }

        stage('Push To DockerHub') {
            steps {
                echo "Push To DockerHub $REGISTRY_URL with $REGISTRY_CREDENTIAL"
                script {
                    docker.withRegistry(REGISTRY_URL, REGISTRY_CREDENTIAL) {
                        dockerImage.push()
                    }
                }
            }
        }
    }
}
