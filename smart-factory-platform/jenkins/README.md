# Jenkins + Blue Ocean + Rundeck-style operations

Jenkins is the CI/CD orchestrator. Blue Ocean is its visual pipeline UI; it does not replace Jenkins agents or the pipeline. The scripts in `ops/` provide parameterized operations suitable for a Rundeck job's command step. Rundeck itself is optional.

## Jenkins agent requirements

Use a Linux Jenkins agent labeled `linux-java21-docker` with:

- JDK 21 and Maven 3.9+
- Git
- Docker CLI with access to a Docker Engine
- Docker Buildx capable of publishing `linux/amd64` and `linux/arm64` images
- `ssh`, `scp`, and Bash

Do not expose an unauthenticated Docker daemon. Prefer a dedicated, trusted build agent. For a Windows laptop, use `scripts/deploy-local.ps1` separately to pull published images into Docker Desktop; Jenkins' `local` Docker daemon means the daemon reachable from the Jenkins agent, not automatically the developer's laptop.

## Jenkins plugins

Install Pipeline, Git, Credentials Binding, JUnit, and ANSI Color. Install Blue Ocean if you want its visual pipeline view. Configure the pipeline as a Multibranch Pipeline or a Pipeline from SCM pointing at `Jenkinsfile`.

## Jenkins credentials

Create these credentials:

| ID | Jenkins credential type | Value |
|---|---|---|
| `smart-factory-ghcr-write` | Username with password | GitHub username; PAT with `write:packages` and required repository access |
| `smart-factory-qa-ssh` | SSH Username with private key | Deployment SSH user and private key |
| `smart-factory-qa-host` | Secret text | QA server hostname/IP |
| `smart-factory-qa-path` | Secret text | Absolute remote directory, e.g. `/opt/smart-factory` |

The `smart-factory-qa-ssh` credential must be an SSH Username with private key credential. Use a separate read-only GHCR token on the QA host and run `docker login ghcr.io` there once. Ensure the verified QA host key is already in the Jenkins agent user's `~/.ssh/known_hosts`; do not disable host-key checking.

The Jenkins pipeline uses `Dockerfile.ci` for the 10 non-AI services after Maven verification, avoiding duplicate Maven compilation inside Docker. The ordinary `Dockerfile` files remain available for local source builds.

## Run the pipeline

1. Put the project in a repository accessible to Jenkins. This ZIP has not been committed or pushed.
2. Create a Jenkins Multibranch Pipeline or Pipeline-from-SCM job using the `Jenkinsfile`.
3. Ensure the selected agent has label `linux-java21-docker`.
4. Run with `GHCR_OWNER` set to the lowercase GitHub username/org that owns the packages and `DEPLOY_TARGET=none` for the first run.
5. After CI and image publishing succeed, run again with `DEPLOY_TARGET=qa-remote` to publish and deploy the same build to QA.
6. To deploy to the Windows laptop's Docker Desktop, run PowerShell as the user with Docker access from the extracted project root:

```powershell
Copy-Item .env.example .env  # only if .env does not already exist; edit passwords first
.\scripts\deploy-local.ps1 -ImageNamespace "your-owner/smart-factory" -ImageTag "<7-char-git-sha>" -Profile develop
```

For private GHCR packages, run `docker login ghcr.io --username YOUR_GITHUB_USERNAME` first and enter a token with `read:packages` at the password prompt. Never commit `.env` or tokens.

## Rundeck-style jobs

Create jobs that call the following script on the target host. Parameterize `profile` (`develop`/`qa`), action, service name, and image tag. Suggested jobs:

- **Deploy**: `ops/smart-factory-runbook.sh deploy qa <image-tag>`
- **Status**: `ops/smart-factory-runbook.sh status qa`
- **Tail logs**: `ops/smart-factory-runbook.sh logs qa <service-name>`
- **Restart service**: `ops/smart-factory-runbook.sh restart qa <service-name>`
- **Rollback**: `ops/smart-factory-runbook.sh rollback qa <previous-good-image-tag>`

Set `IMAGE_PREFIX=ghcr.io/<owner>/smart-factory` in the job's protected environment. Limit deploy/rollback permissions to authorized operators. Keep DB volumes intact; these jobs never run `down -v`.

## Verification limitations

Pipeline syntax and shell scripts should be reviewed on the actual Jenkins agent. A successful Maven build and image push does not guarantee all runtime integrations are healthy; after deployment, inspect Compose health/status/logs and run the Postman API collection. No commit or push is performed by this package-preparation workflow.
