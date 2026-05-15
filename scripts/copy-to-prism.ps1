param(
    [string]$InstancePath = $env:FORGOTTEN_FEATURES_PRISM_INSTANCE,
    [string]$JarPath
)

$ErrorActionPreference = "Stop"

if ([string]::IsNullOrWhiteSpace($InstancePath)) {
    throw "Provide -InstancePath or set FORGOTTEN_FEATURES_PRISM_INSTANCE."
}

$repoRoot = Resolve-Path (Join-Path $PSScriptRoot "..")

if ([string]::IsNullOrWhiteSpace($JarPath)) {
    $libs = Join-Path $repoRoot "build\libs"
    $jar = Get-ChildItem -LiteralPath $libs -Filter "forgotten-features-*.jar" |
        Where-Object { $_.Name -notlike "*-sources.jar" } |
        Sort-Object LastWriteTime -Descending |
        Select-Object -First 1

    if ($null -eq $jar) {
        throw "No playable Forgotten Features jar found in $libs. Run ./gradlew.bat build first."
    }

    $JarPath = $jar.FullName
}

$resolvedJar = Resolve-Path -LiteralPath $JarPath
$resolvedInstance = Resolve-Path -LiteralPath $InstancePath
$modsDir = Join-Path $resolvedInstance "minecraft\mods"

if (-not (Test-Path -LiteralPath $modsDir)) {
    New-Item -ItemType Directory -Path $modsDir | Out-Null
}

$resolvedModsDir = Resolve-Path -LiteralPath $modsDir
$destination = Join-Path $resolvedModsDir (Split-Path -Leaf $resolvedJar)

Get-ChildItem -LiteralPath $resolvedModsDir -Filter "forgotten-features-*.jar" |
    Where-Object { $_.FullName -ne $destination } |
    ForEach-Object {
        try {
            Remove-Item -LiteralPath $_.FullName -Force
        } catch {
            Write-Warning "Could not remove old jar $($_.Name). Close the launcher/game if it is loaded."
        }
    }

try {
    Copy-Item -LiteralPath $resolvedJar -Destination $destination -Force
} catch {
    if (Test-Path -LiteralPath $destination) {
        $sourceLength = (Get-Item -LiteralPath $resolvedJar).Length
        $destinationLength = (Get-Item -LiteralPath $destination).Length
        if ($sourceLength -eq $destinationLength) {
            Write-Warning "The Prism jar appears to already match the current build, but it could not be overwritten because it is in use."
            return
        }
    }

    throw
}

Write-Output "Copied $(Split-Path -Leaf $resolvedJar) to $resolvedModsDir"
