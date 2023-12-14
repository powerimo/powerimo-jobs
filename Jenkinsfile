pipeline {

    environment {
        FULL_PATH_BRANCH = "${env.BRANCH_NAME}"
        RELEASE_BRANCH = FULL_PATH_BRANCH.substring(FULL_PATH_BRANCH.lastIndexOf('/') + 1, FULL_PATH_BRANCH.length()).trim()
        NSS_API_KEY = credentials('powerimo-nss-api-key')
    }

    tools{
        maven 'maven3'
        jdk 'OpenJDK17'
    }

    agent any

    stages {
        stage('Initialization') {
            steps {
                sh 'java -version'
                sh 'mvn --version'
                sh 'echo HOME=${HOME}'
                sh 'echo PATH=${PATH}'
                sh 'echo M2_HOME=${M2_HOME}'
                sh 'echo FULL_PATH_BRANCH=${FULL_PATH_BRANCH}'
                sh 'echo RELEASE_BRANCH=${RELEASE_BRANCH}'
            }
        }

        stage('Prepare snapshot') {
            when {
                expression { BRANCH_NAME == 'main' }
            }
            steps {
                script {
                    sh "mvn -B -DskipTests=true clean -f pom.xml"
                }
            }
        }

        stage('Prepare release') {
            when {
                expression { BRANCH_NAME ==~ /release\/[0-9]+\.[0-9]+/ }
            }
            steps {
                script {
                    def buildVersion = "${RELEASE_BRANCH}.${BUILD_NUMBER}"
                    echo "buildVersion=${buildVersion}"
                    sh "mvn -B -DskipTests=true clean versions:set -DnewVersion=${buildVersion} -f pom.xml"
                    sh "mvn -B versions:commit"
                }
            }
        }

        stage("Deploy artifacts to Nexus") {
            steps {
                script {
                    def buildVersion = "${RELEASE_BRANCH}.${BUILD_NUMBER}"
                    sh "mvn deploy -Drevision=${buildVersion}"
                }
            }
        }

    } // stages

    post {
        always {
            nssSendJobResult(recipients: "AndewilEventsChannel")
        }
    }

}
