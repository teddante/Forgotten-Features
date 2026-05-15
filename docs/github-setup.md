# GitHub Setup

This project should use GitHub lightly but intentionally: protect important history, let feature branches move quickly, and keep releases understandable.

## Recommended Branch Protection

Protect `main`, but keep the rule simple.

Recommended settings:

- Require a pull request before merging.
- Require status checks to pass before merging.
- Select the repository CI check once it has run at least once.
- Block force pushes.
- Block branch deletion.
- Do not require approving reviews yet while the project is mostly solo/agent-driven.
- Do not require signed commits yet.
- Let administrators bypass rules for now, if GitHub offers that option.

Why this is useful:

- Feature branches still work normally.
- Codex can still push branches and open PRs.
- `main` cannot be accidentally overwritten.
- CI becomes the gate for merges.

Why not make it stricter yet:

- Required reviews and signed commits can slow early iteration.
- The project is still finding its shape.
- Stricter rules are easy to add later.

## Issues

Create an issue when the work needs discussion or historical evidence.

Good issue candidates:

- A new feature with uncertain behavior.
- A bug or crash report.
- A compatibility problem.
- A config/design decision.
- A feature that likely needs multiple commits.

Skip an issue for:

- Tiny docs fixes.
- Obvious build fixes.
- Follow-up work already covered by an active branch or PR.

## Pull Requests

Use PRs for feature branches and anything that should be reviewed before it becomes part of `main`.

Default PR style:

- Draft PR while the feature is still being tested.
- Ready PR once it builds, docs are updated, and manual checks are noted.
- Squash merge is fine for small branches if the PR history is noisy.
- Merge commit is fine when the individual commits are useful checkpoints.

## Tags and Releases

Git tags mark exact commits. GitHub releases are user-facing release pages that can attach jar files.

Use tags only for actual release points:

```text
v0.1.0
v0.2.0
v0.2.1
```

Do not tag every feature branch.

Release flow:

1. Merge finished feature PRs into `main`.
2. Review `CHANGELOG.md`.
3. Bump `mod_version` in `gradle.properties`.
4. Run `scripts/verify.ps1`.
5. Commit release prep on `main`.
6. Create and push a tag, such as `v0.1.0`.
7. Create a GitHub release from that tag.
8. Attach the built jar files.
9. Publish to Modrinth/CurseForge only when ready.

Codex can create commits, tags, pushes, PR text, and release notes. Creating GitHub releases or uploading mod-platform files may need GitHub CLI, a connector/tool, or user approval depending on the available environment.

## Repository Topics

Optional GitHub repository topics:

```text
minecraft
minecraft-mod
fabric
neoforge
forge
java
forgotten-features
modding
clean-room
historical-recreation
```

These help discovery but are not urgent.

## Historical Research

For historical features, use the best evidence available:

- `docs/allfeaturesfromwiki.md` as the raw local archive.
- Current Minecraft Wiki pages for readable summaries and citations.
- Official Mojang/Minecraft posts when available.
- Local old-version jars/resources for behavior research.

Do not commit old jars, decompiled Minecraft source, copied Mojang code, or ripped assets. Commit only the observed facts and the clean reimplementation.

