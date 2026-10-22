# CI/CD setup pointer

This project uses **Jenkins Pipeline** as its primary CI/CD implementation. Blue Ocean is an optional visual interface for viewing Jenkins stages. Rundeck is optional and can invoke the parameterized operations in `ops/smart-factory-runbook.sh`.

Start with:

- [`jenkins/README.md`](../jenkins/README.md) — Jenkins agent prerequisites, plugins, credentials, GHCR, and run instructions.
- [`CICD_JENKINS_BLUE_OCEAN_RUNDECK.md`](CICD_JENKINS_BLUE_OCEAN_RUNDECK.md) — architecture, Dev/QA workflows, operational jobs, and verification checklist.
- Root [`Jenkinsfile`](../Jenkinsfile) — CI, GHCR image publishing, and optional remote QA deployment.

No Git commit or push is performed while preparing this package. Jenkins cannot run the pipeline until the project is placed in a repository Jenkins can check out and a Jenkins agent is configured.
