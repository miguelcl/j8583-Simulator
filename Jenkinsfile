pipeline {
  agent any
  stages {
    stage('Compile And Build') {
      steps {
        withMaven(maven: 'maven', jdk: 'jdk') {
          sh 'mvn clean install'
        }
        
      }
    }
    stage('Deploy US3-STG') {
      steps {
        echo 'Deploy on STG'
      }
    }
    stage('IT') {
      steps {
        echo 'Execute IT Jobs'
        build(job: 'integration-test-gmp-stg', wait: true)
      }
    }
    stage('Send Email') {
      steps {
        echo 'Sending Email Report'
      }
    }
  }
}