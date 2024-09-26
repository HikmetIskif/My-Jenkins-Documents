pipeline {
    agent none
    triggers {
        pollSCM('* * * * *')
    }
    stages {
        stage('First Machine Operations') {
            agent {
                label 'ubuntu_server_1'
            }
            steps {
                script {
                    def repositoryUrl = "https://github.com/HikmetIskif/My-Jenkins-Documents"
                    def dockerImageName = "hikmetiskifoglu/javahelloworldimage:latest"
                    
                    checkout([$class: 'GitSCM', branches: [[name: '*/main']], userRemoteConfigs: [[url: repositoryUrl]]])
                    
                    sh "javac HelloWorld.java"
                    sh "java HelloWorld"
                    
                    writeFile file: 'Dockerfile', text: '''
                    FROM openjdk:17-slim
                    COPY HelloWorld.java HelloWorld.java
                    CMD ["sh", "-c", "javac HelloWorld.java && java HelloWorld && tail -f /dev/null"]
                    '''
                    
                    sh "sudo docker build -t ${dockerImageName} ."
                    
                    withCredentials([usernamePassword(credentialsId: 'docker_credentials', passwordVariable: 'DOCKERHUB_PASSWORD', usernameVariable: 'DOCKERHUB_USERNAME')]) {
                        sh "echo $DOCKERHUB_PASSWORD | sudo docker login -u $DOCKERHUB_USERNAME --password-stdin"
                    }
                    
                    sh "sudo docker push ${dockerImageName}"
                }
            }
        }
        stage('Second Machine Operations') {
            agent {
                label 'ubuntu_server_2'
            }
            steps {
                script {
                    def dockerImageName = "hikmetiskifoglu/javahelloworldimage:latest"
                    
                    sh "sudo docker pull ${dockerImageName}"
                    
                    sh "sudo docker run -d --name javaFileContainer ${dockerImageName}"
                }
            }
        }
    }
}
