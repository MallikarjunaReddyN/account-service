pipeline {
    agent any

    environment {
        ENV_NAME = getEnvName(env.BRANCH_NAME)
        GIT_SHA_SHORT=`git rev-parse --short=8 ${GIT_COMMIT}`
        scannerHome = tool 'Sonar Scanner'
    }

    stages {
        stage('Branch & Environment Info') {
            steps {
                echo "pulling .. " + env.BRANCH_NAME
                echo "Environment is : "+ env.ENV_NAME
                echo "Commit id : " + env.GIT_COMMIT
                echo "Short commit id: " + env.GIT_SHA_SHORT
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
                expression {
                    return env.ENV_NAME != 'null';
                }
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
                expression {
                    return env.ENV_NAME != 'null';
                }
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
                expression {
                    return env.ENV_NAME != 'null';
                }
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