pipeline{
    environment {
        imagename = "dqf/dqf-analyser-completeness"
        dockerImage = ''
        dockercontainername = 'analysercompleteness'
    }


    agent any
    stages{
        stage('Build'){
            steps{
                sh "mvn -version"
                sh "mvn clean install"
            }
        }
        stage('Build docker Image'){
            steps{
                script{
                    dockerImage = docker.build imagename
                }
            }
        }
        stage('remove existing image'){
            steps{
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    sh "docker stop ${dockercontainername}"
                    sh "docker rm -f ${dockercontainername}"
                }
            }
        }
        stage('Run Docker contaiener'){
            steps{
                sh "docker run -d --name ${dockercontainername} --network dqf-net ${imagename}"
            }
        }
    }
}