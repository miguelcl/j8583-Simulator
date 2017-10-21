pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        withMaven(maven: 'maven', jdk: 'jdk') {
          sh 'mvn clean install'
        }
        
      }
    }
  }
}