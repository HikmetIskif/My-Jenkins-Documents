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
                    def dockerImageName = "hikmetiskifoglu/javaHelloWorldImage:latest"
                    
                    checkout([$class: 'GitSCM', branches: [[name: '*/main']], userRemoteConfigs: [[url: repositoryUrl]]])
                    
                    sh "javac HelloWorld.java"
                    sh "java HelloWorld"
                    
                    writeFile file: 'Dockerfile', text: '''
                    FROM openjdk:17-slim
                    COPY HelloWorld.class app.class
                    ENTRYPOINT ["java", "/app.class"]
                    '''
                    
                    sh "docker build -t ${dockerImageName} ."
                    
                    withCredentials([usernamePassword(credentialsId: 'docker_credentials', passwordVariable: 'DOCKERHUB_PASSWORD', usernameVariable: 'DOCKERHUB_USERNAME')]) {
                        sh "echo $DOCKERHUB_PASSWORD | docker login -u $DOCKERHUB_USERNAME --password-stdin"
                    }
                    
                    sh "docker push ${dockerImageName}"
                }
            }
        }
        stage('Second Machine Operations') {
            agent {
                label 'ubuntu_server_2'
            }
            steps {
                script {
                    def dockerImageName = "hikmetiskifoglu/javaHelloWorldImage:latest"
                    
                    sh "docker pull ${dockerImageName}"
                    
                    sh "docker run -d --name javaFileContainer ${dockerImageName}"
                }
            }
        }
    }
}
