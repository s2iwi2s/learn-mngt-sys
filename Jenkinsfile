def img
pipeline {
    environment {
        registry = "s2iwi2s/lms" //To push an image to Docker Hub, you must first name your local image using your Docker Hub username and the repository name that you created through Docker Hub on the web.
        registryUrl = "https://registry.hub.docker.com "
        registryCredential = 'docker-hub-login'
        dockerImage = ''
        dockerImageArgs = "--build-arg=BASE_IMAGE=openjdk:11 --build-arg=JAVA_OPTS='-Xmx512m -Xms256m' --build-arg=JHIPSTER_SLEEP=3 --build-arg=SPRING_PROFILES_ACTIVE=prod --build-arg=SPRING_DATASOURCE_URL=jdbc:postgresql://192.168.110.136:5432/LearnMngtSys --build-arg=SPRING_LIQUIBASE_URL=jdbc:postgresql://192.168.110.136:5432/LearnMngtSys ."
    }
    agent any
    stages {
        stage('checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/s2iwi2s/learn-mngt-sys.git'
            }
        }

        stage('clean') {
            steps {
                sh "chmod +x mvnw"
                sh "./mvnw -version"
                sh "./mvnw -ntp clean"
            }
        }

        stage ('Stop previous running container'){
            steps{
                sh returnStatus: true, script: 'docker stop $(docker ps -a | grep ${JOB_NAME} | awk \'{print $1}\')'
                sh returnStatus: true, script: 'docker rmi $(docker images | grep ${registry} | awk \'{print $3}\') --force' //this will delete all images
                sh returnStatus: true, script: 'docker rm ${JOB_NAME}'
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
                script {
                    sshagent(['docker-host-key']) {
                        img = registry + ":${env.BUILD_ID}"
                        println ("${img}")
                        dockerImage = docker.build("${img}", "${dockerImageArgs}")
                    }
                }
            }
        }

        stage('Test - Run Docker Container on Jenkins node') {
           steps {
                sh label: '', script: "docker run -d --name ${JOB_NAME} -p 8181:8181 ${img}"
			}
        }
    }
}
