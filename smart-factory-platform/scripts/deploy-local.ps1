param(
  [Parameter(Mandatory = $true)]
  [ValidatePattern('^[a-z0-9][a-z0-9-]*/smart-factory$')]
  [string]$ImageNamespace,
  [Parameter(Mandatory = $true)]
  [ValidatePattern('^[a-f0-9]{7,40}$|^latest$')]
  [string]$ImageTag,
  [ValidateSet('develop','qa')]
  [string]$Profile = 'develop'
)
$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $PSScriptRoot
Set-Location $root
if (-not (Test-Path '.env')) {
  throw 'Missing .env. Copy .env.example to .env and set strong local-only database passwords first.'
}
$override = if ($Profile -eq 'qa') { 'docker-compose.qa.yml' } else { 'docker-compose.develop.yml' }
if (-not (Test-Path $override)) { throw "Missing Compose override: $override" }
Write-Host "Pulling images from ghcr.io/$ImageNamespace with tag $ImageTag ..."
$env:IMAGE_PREFIX = "ghcr.io/$ImageNamespace"
$env:IMAGE_TAG = $ImageTag
docker compose -f docker-compose.yml -f $override pull
if ($LASTEXITCODE -ne 0) { throw 'Image pull failed. Check docker login ghcr.io and package read permissions.' }
docker compose -f docker-compose.yml -f $override up --no-build -d
if ($LASTEXITCODE -ne 0) { throw 'Compose startup failed. Inspect docker compose logs.' }
docker compose -f docker-compose.yml -f $override ps
