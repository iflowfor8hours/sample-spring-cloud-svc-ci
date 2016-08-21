def user = "iflowfor8hours"
def cfUser = "${cf_username}"
def cfPassword = "${cf_password}"
def cfOrg = "${cf_org}"
def sonarUrl = "${sonar_url}"
def flow

stage 'compile'
node {
	git([url: "https://github.com/${user}/sample-spring-cloud-svc-ci.git", branch: 'jenkins'])
	flow = load 'ci/pipeline.groovy'
	flow.build()
}

stage 'test'
node {
	git([url: "https://github.com/${user}/sample-spring-cloud-svc-ci.git", branch: 'jenkins'])
	flow = load 'ci/pipeline.groovy'
	flow.clean_test()
}

stage 'publish-sonar-results'
node {
	flow.sonar(sonarUrl)
}

stage 'deploy-to-development'
node {
	git([url: "https://github.com/${user}/sample-spring-cloud-svc-ci.git", branch: 'jenkins'])
	flow = load 'ci/pipeline.groovy'
	flow.push('api.run.pivotal.io', "${cfUser}", "${cfPassword}", "${cfOrg}", 'development', 'cfapps.io', 'sample-spring-cloud-svc-ci-dev')
}

stage 'run-tests-on-dev'
parallel(
	smokeTests: {
		node {
			git([url: "https://github.com/${user}/sample-spring-cloud-svc-ci.git", branch: 'jenkins'])
			flow = load 'ci/pipeline.groovy'
			flow.runSmokeTests('api.run.pivotal.io', "${cfUser}", "${cfPassword}", "${cfOrg}", 'development', 'cfapps.io', 'sample-spring-cloud-svc-ci-dev')
		}
	},
	acceptanceTests: {
		node {
			git([url: "https://github.com/${user}/sample-spring-cloud-svc-ci.git", branch: 'jenkins'])
			flow = load 'ci/pipeline.groovy'
			flow.runAcceptanceTests('api.run.pivotal.io', "${cfUser}", "${cfPassword}", "${cfOrg}", 'development', 'cfapps.io', 'sample-spring-cloud-svc-ci-dev')
		}
	}
)

stage name: 'deploy-to-test', concurrency: 1
node {
	git([url: "https://github.com/${user}/sample-spring-cloud-svc-ci.git", branch: 'jenkins'])
	flow = load 'ci/pipeline.groovy'
	flow.pushIf('api.run.pivotal.io', "${cfUser}", "${cfPassword}", "${cfOrg}", 'test', 'cfapps.io', 'sample-spring-cloud-svc-ci-test')
}

stage 'run-tests-on-test'
parallel(
	smokeTests: {
		node {
			git([url: "https://github.com/${user}/sample-spring-cloud-svc-ci.git", branch: 'jenkins'])
			flow = load 'ci/pipeline.groovy'
			flow.runSmokeTests('api.run.pivotal.io', "${cfUser}", "${cfPassword}", "${cfOrg}", 'test', 'cfapps.io', 'sample-spring-cloud-svc-ci-test')
		}
	},
	acceptanceTests: {
		node {
			git([url: "https://github.com/${user}/sample-spring-cloud-svc-ci.git", branch: 'jenkins'])
			flow = load 'ci/pipeline.groovy'
			flow.runAcceptanceTests('api.run.pivotal.io', "${cfUser}", "${cfPassword}", "${cfOrg}", 'test', 'cfapps.io', 'sample-spring-cloud-svc-ci-test')
		}
	}
)

input message: "Does https://sample-spring-cloud-svc-ci-prod.cfapps.io look good?"
try {
    checkpoint('Before production')
} catch (NoSuchMethodError _) {
    echo 'Checkpoint feature available in CloudBees Jenkins Enterprise.'
}

stage name: 'deploy-to-prod', concurrency: 1
node {
	git([url: "https://github.com/${user}/sample-spring-cloud-svc-ci.git", branch: 'jenkins'])
	flow = load 'ci/pipeline.groovy'
	flow.pushIf('api.run.pivotal.io', "${cfUser}", "${cfPassword}", "${cfOrg}", 'production', 'cfapps.io', 'sample-spring-cloud-svc-ci-prod')
}
