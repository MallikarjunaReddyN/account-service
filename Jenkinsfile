pipeline {
    agent any

    environment {
        ENV_NAME = getEnvName(env.BRANCH_NAME)
    }

    stages {
        stage('Checkout') {
            steps {
                echo "pulling .. " + env.BRANCH_NAME
                echo "Environment is : ${ENV_NAME}"
                echo "Custom Environment variable: " + env.VARIABLE1
            }
        }
        stage('Test & Build') {
            steps {
                sh "./gradlew clean build"
            }
        }

        stage("SonarQube analysis") {
            steps {
                withSonarQubeEnv('sonarcloud') {
                    sh './gradlew sonar'
                }
            }
        }
        stage("Quality Gate") {
            steps {
                timeout(time: 300, unit: 'SECONDS') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Docker Build & Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'registry-credentials',
                    usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    script {
                        sh "./gradlew jib -Djib.to.auth.username=$USERNAME -Djib.to.auth.password=$PASSWORD -Djib.to.tags=$BUILD_NUMBER"
                    }
                }
            }
        }

        //   stage('K8S Deploy') {
        //    steps {
        //     script {
        //      withKubeConfig(caCertificate: '', clusterName: '', contextName: '', credentialsId: 'K8S', namespace: '', restrictKubeConfigAccess: false, serverUrl: '') {
        //       sh('kubectl apply -f jenkins-aks-deploy-from-acr.yaml')
        //      }
        //     }
        //    }
        //   }
    }
}

def getEnvName(branchName) {
    if (branchName.equals("dev")) {
        return "dev";
    } else if (branchName.equals("main")) {
        return "qa";
    } else if (branchName.contains("releases/release")) {
        return "prod";
    } else {
        return null;
    }
}