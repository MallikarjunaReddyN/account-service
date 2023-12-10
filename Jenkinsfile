pipeline {
    agent any

    environment {
        ENV_NAME = getEnvName(env.BRANCH_NAME)
    }

    stages {
        stage('Checkout') {
            steps {
                echo "pulling .." + env.BRANCH_NAME
                echo "Environment" +$ENV_NAME
                git "https://github.com/MallikarjunaReddyN/account-service.git"
            }
        }
        stage('Build') {
            steps {
                sh "./gradlew clean build"
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
    if(branchName.equals("dev")) {
        return "dev";
    } else if (branchName.equals("main")) {
        return "qa";
    } else if (branchName.contains("releases/release")) {
        return "prod";
    } else {
        return null;
    }
}