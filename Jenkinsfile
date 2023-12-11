pipeline {
    agent any

    environment {
        ENV_NAME = getEnvName(env.BRANCH_NAME)
        scannerHome = tool 'Sonar Scanner'
    }

    stages {
        stage('Branch & Environment Info') {
            steps {
                echo "pulling .. " + env.BRANCH_NAME
                echo "Environment is : ${ENV_NAME}"
                echo "Commit id : " + env.GIT_COMMIT
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
                    sh '${scannerHome}/bin/sonar-scanner'
                }
            }
        }
        stage("Quality Gate") {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Docker Build & Push') {
            when {
                branch 'dev' || 'main' || 'releases/release-*'
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'registry-credentials',
                    usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    script {
                        sh "./gradlew jib -Djib.to.auth.username=$USERNAME -Djib.to.auth.password=$PASSWORD -Djib.to.tags=$BUILD_NUMBER"
                    }
                }
            }
        }

        stage('Update Image tag') {
            when {
                branch 'dev' || 'main' || 'releases/release-*'
            }
            steps {
                dir("${env.WORKSPACE}/k8s") {
                    sh "sed -ie 's/##TAG##/$BUILD_NUMBER/g' deployment.yaml"
                    sh "cat deployment.yaml"
                }

            }
        }

        stage('K8S Deploy') {
            when {
                branch 'dev' || 'main' || 'releases/release-*'
            }
            steps {
                script {
                    dir("${env.WORKSPACE}/k8s") {
                        sh "/Users/mdireddy/.jenkins/kubectl config use-context minikube"
                        sh "/Users/mdireddy/.jenkins/kubectl apply -f . -n default"
                        //kubernetesDeploy(configs: "deployment.yaml", "service.yaml")
                    }
                }
            }
        }
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