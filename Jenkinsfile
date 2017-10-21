pipeline {
  agent any
  stages {
    stage('error') {
      steps {
        withMaven(maven: 'maven', jdk: 'jdk')
      }
    }
  }
}