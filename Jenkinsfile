pipeline {
  agent any
  stages {
    stage('test-g1') {
      parallel {
        stage('build') {
          steps {
            withMaven(maven: 'maven', jdk: 'jdk') {
              sh 'mvn clean install'
            }
            
          }
        }
        stage('build-g1') {
          steps {
            withMaven(jdk: 'jdk', maven: 'maven') {
              echo 'aca'
            }
            
          }
        }
      }
    }
  }
}