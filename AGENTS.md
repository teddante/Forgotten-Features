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
- Add debug or verification helpers when a feature is hard to test manually, but keep them scoped and useful.

## What Agents Can Own

Codex can usually handle:

- Creating branches and keeping work scoped.
- Editing docs, Gradle files, Java code, resources, data files, and tests.
- Running command-line builds, tests, linters, and data generation.
- Reading CI output and fixing failures when GitHub access is available.
- Preparing commits, release notes, tags, and PR text when asked.
- Comparing implementation behavior against documented evidence.

Ask the user before actions that publish, push, create releases, upload to mod platforms, install new dependencies from the network, or require account credentials. Build artifacts can be copied to local test launchers when useful, but jar files are not committed.

## Local Environment

Use local environment variables instead of hardcoding machine-specific paths:

- `FORGOTTEN_FEATURES_PRISM_INSTANCE`: optional Prism Launcher instance path for local jar copy/testing.

The repo includes `.env.example`, but real `.env` files stay ignored.

## Verification Loop

Before handing work back:

1. Run the smallest useful check while developing.
2. Run the full Gradle build when Java/resources/data changed.
3. Inspect the built jar when adding resources, data files, or generated assets.
4. Copy the latest playable jar to the user's configured launcher instance when available.
5. Commit only after the branch is coherent and checks pass.

Prefer automation over asking the user to test. Use manual in-game testing for visuals, feel, compatibility, and things a command-line check cannot prove.

Use `scripts/verify.ps1` for the normal local build/check loop. Use `scripts/verify.ps1 -CopyToPrism` when a configured Prism instance should receive the latest playable jar.

## Local Minecraft Research

Use ignored local folders for old-version research:

- `.research/`
- `local-research/`
- `decompiled-minecraft/`
- `minecraft-jars/`

These can contain downloaded jars, extracted resources, decompiled code, screenshots, notes, or measurements on the developer's machine. They must not be committed.

When a useful fact is found, write the fact into `docs/feature-candidates.md` or a file in `docs/features/`, then implement it in fresh mod code.

## Git Workflow

- Use short branches such as `feature/<name>`, `docs/<topic>`, `infra/<topic>`, or `fix/<bug>`.
- Make a commit at each useful checkpoint: repo docs, build skeleton, config system, one feature, one bug fix.
- Do not mix unrelated feature work in the same commit.
- Before finalizing, run the relevant Gradle build/test command once a build exists.
- Open or draft a pull request when a branch is coherent enough to review. Do not create releases from feature branches.
- Follow `docs/github-setup.md` for branch protection, tags, and releases.

## Issues, PRs, and Releases

- Create an issue when work needs discussion, historical evidence, compatibility reports, or user-facing scope decisions.
- Skip the issue for tiny obvious fixes, docs cleanup, or follow-up changes already covered by the current branch.
- Open a PR when a branch has a focused set of commits, passing checks, and a clear summary.
- Bump versions only for release preparation, not every feature commit.
- Update `CHANGELOG.md` with user-visible changes as they happen, then tidy it during release prep.
- Create releases from `main` after merge, build verification, changelog review, and explicit user approval.

## Research and Web Sources

Browse when facts may have changed, when official loader/API guidance matters, or when historical claims need better sourcing. Prefer official docs, Minecraft Wiki pages, mod loader docs, and local Minecraft jars/caches over unsourced memory.

## First Technical Direction

Start modern-first:

- Fabric and NeoForge for the current unobfuscated Minecraft line.
- Forge only for a selected legacy line after the modern build works.
- Quilt as compatibility-tested through Fabric unless a real Quilt-specific need appears.

See `docs/project-workflow.md` for the full operating model.
