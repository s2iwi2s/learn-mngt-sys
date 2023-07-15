#!/usr/bin/env groovy

node {
    stage('checkout') {
        checkout scm
    }

    docker.image('jhipster/jhipster:v8.0.0-beta.1').inside('-u jhipster -e MAVEN_OPTS="-Duser.home=./"') {
        stage('check java') {
            sh "java -version"
        }

        stage('clean') {
            sh "chmod +x mvnw"
            sh "./mvnw -ntp clean -P-webapp"
        }
        stage('nohttp') {
            sh "./mvnw -ntp checkstyle:check"
        }

        stage('install tools') {
            sh "./mvnw -ntp com.github.eirslett:frontend-maven-plugin:install-node-and-npm@install-node-and-npm"
        }

        stage('npm install') {
            sh "./mvnw -ntp com.github.eirslett:frontend-maven-plugin:npm"
        }
        stage('backend tests') {
            try {
                sh "./mvnw -ntp verify -P-webapp"
            } catch(err) {
                throw err
            } finally {
                junit '**/target/surefire-reports/TEST-*.xml,**/target/failsafe-reports/TEST-*.xml'
            }
        }

        stage('frontend tests') {
            try {
               sh "npm install"
               sh "npm test"
            } catch(err) {
                throw err
            } finally {
                junit '**/target/test-results/TESTS-results-jest.xml'
            }
        }

        stage('packaging') {
            sh "./mvnw -ntp verify -P-webapp -Pprod -DskipTests"
            archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
        }
    }

    def dockerImage
    stage('publish docker') {
        // A pre-requisite to this step is to setup authentication to the docker registry
        // https://github.com/GoogleContainerTools/jib/tree/master/jib-maven-plugin#authentication-methods
        sh "./mvnw -ntp -Pprod verify jib:build"
    }
}
