pipeline {
  agent any
  stages {
    stage('Build Component') {
      steps {
        withMaven(maven: 'maven', jdk: 'jdk') {
          sh 'mvn clean install'
        }
        
      }
    }
    stage('Deploy STG') {
      steps {
        echo 'Deploy on STG'
      }
    }
    stage('Testing') {
      parallel {
        stage('LB_STATUS') {
          steps {
            echo 'Validate LB_STATUS'
          }
        }
        stage('ITest') {
          steps {
            echo 'Checking LB_STATUS'
          }
        }
      }
    }
    stage('Send Email') {
      steps {
        echo 'Sending Email Report'
      }
    }
  }
}