param(
    [switch]$CopyToPrism,
    [string]$PrismInstancePath = $env:FORGOTTEN_FEATURES_PRISM_INSTANCE
)

$ErrorActionPreference = "Stop"

$repoRoot = Resolve-Path (Join-Path $PSScriptRoot "..")
$gradle = Join-Path $repoRoot "gradlew.bat"

if (-not (Test-Path -LiteralPath $gradle)) {
    throw "Gradle wrapper not found at $gradle."
}

Push-Location $repoRoot
try {
    & $gradle build --no-daemon
    if ($LASTEXITCODE -ne 0) {
        throw "Gradle build failed with exit code $LASTEXITCODE."
    }

    $jar = Get-ChildItem -LiteralPath "build\libs" -Filter "forgotten-features-*.jar" |
        Where-Object { $_.Name -notlike "*-sources.jar" } |
        Sort-Object LastWriteTime -Descending |
        Select-Object -First 1

    if ($null -eq $jar) {
        throw "Build succeeded but no playable jar was found."
    }

    $expectedEntries = @(
        "fabric.mod.json",
        "forgottenfeatures.mixins.json",
        "assets/forgottenfeatures/lang/en_us.json",
        "assets/forgottenfeatures/items/ruby.json",
        "data/forgottenfeatures/worldgen/placed_feature/ore_ruby.json"
    )

    $jarEntries = & jar tf $jar.FullName
    foreach ($entry in $expectedEntries) {
        if ($jarEntries -notcontains $entry) {
            throw "Built jar is missing expected entry: $entry"
        }
    }

    Write-Output "Verified $($jar.Name)"

    if ($CopyToPrism) {
        & (Join-Path $PSScriptRoot "copy-to-prism.ps1") -InstancePath $PrismInstancePath -JarPath $jar.FullName
    }
} finally {
    Pop-Location
}

