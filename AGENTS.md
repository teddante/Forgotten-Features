# Agent Notes

These notes are for Codex and other AI coding agents working in this repo.

## Project Shape

Forgotten Features is a Minecraft Java Edition mod project focused on faithful, toggleable recreations of removed, unused, shelved, and developer-mentioned features.

Keep the repo practical. Add files and process only when they help development, release, compatibility, or legal safety.

## Source Material

- `docs/allfeaturesfromwiki.md` is the raw wiki reference archive. Do not edit it.
- `docs/feature-candidates.md` is the tidy working list. Update this when ranking, grouping, or selecting features.
- Use `docs/features/` only for complex feature notes that need more detail than the candidate table.

## Legal Boundary

Old jars and decompiled old versions may be used locally as research references. Do not commit Minecraft jars, decompiled Minecraft source, copied Mojang code, or ripped vanilla assets.

When recreating old behavior, copy the observed behavior and important values into fresh code that fits the current loader APIs. Do not paste old Minecraft methods or classes into the mod.

## Development Defaults

- Prefer small, obvious changes.
- Keep features toggleable.
- Prefer vanilla data systems where they fit: recipes, loot tables, tags, data packs, worldgen JSON, and resource packs.
- Keep client-only code out of common/server paths.
- Keep loader-specific code thin.
- Do not add a dependency just because it is convenient once. Add it when it clearly reduces maintenance.

## What Agents Can Own

Codex can usually handle:

- Creating branches and keeping work scoped.
- Editing docs, Gradle files, Java code, resources, data files, and tests.
- Running command-line builds, tests, linters, and data generation.
- Reading CI output and fixing failures when GitHub access is available.
- Preparing commits, release notes, tags, and PR text when asked.
- Comparing implementation behavior against documented evidence.

Ask the user before actions that publish, push, create releases, upload to mod platforms, install new dependencies from the network, or require account credentials.

## Local Minecraft Research

Use ignored local folders for old-version research:

- `.research/`
- `local-research/`
- `decompiled-minecraft/`
- `minecraft-jars/`

These can contain downloaded jars, extracted resources, decompiled code, screenshots, notes, or measurements on the developer's machine. They must not be committed.

When a useful fact is found, write the fact into `docs/feature-candidates.md` or a file in `docs/features/`, then implement it in fresh mod code.

## Git Workflow

- Use short branches such as `feature/ruby`, `feature/giant`, `docs/candidates`, or `infra/mod-skeleton`.
- Make a commit at each useful checkpoint: repo docs, build skeleton, config system, one feature, one bug fix.
- Do not mix unrelated feature work in the same commit.
- Before finalizing, run the relevant Gradle build/test command once a build exists.

## First Technical Direction

Start modern-first:

- Fabric and NeoForge for the current unobfuscated Minecraft line.
- Forge only for a selected legacy line after the modern build works.
- Quilt as compatibility-tested through Fabric unless a real Quilt-specific need appears.

See `docs/project-workflow.md` for the full operating model.
