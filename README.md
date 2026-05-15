# Forgotten Features

Historically faithful, clean-room recreations of removed, unused, shelved, and developer-mentioned features for Minecraft Java Edition.

**Status:** first Fabric feature slice in progress. No public mod builds are published yet.

> NOT AN OFFICIAL MINECRAFT PRODUCT. NOT APPROVED BY OR ASSOCIATED WITH MOJANG OR MICROSOFT.

## Project Goals

- Rebuild old, unused, removed, and mentioned Minecraft features as faithfully as practical.
- Keep every feature toggleable for players, servers, and modpack authors.
- Use historical evidence to make features feel as close to the originals as possible.
- Support the most useful modern mod-loader paths without promising every historical Minecraft version.

## Reference Material

`docs/allfeaturesfromwiki.md` is the raw wiki reference dump. Treat it as source material and do not edit it directly.

`docs/feature-candidates.md` is the working list for tidied, ranked, and grouped feature ideas.

`docs/project-workflow.md` explains how issues, branches, commits, releases, loader choices, compatibility, and AI/Codex work should fit together.

`AGENTS.md` gives short working rules for Codex and other AI agents so automated development, testing, commits, and launcher test builds stay consistent.

`docs/github-setup.md` explains recommended branch protection, issues, PRs, tags, and releases.

## Planned Loader Targets

| Target | Role | Notes |
| --- | --- | --- |
| Fabric | Primary modern target | Fast iteration and broad modern ecosystem support. |
| NeoForge | Primary modern target | Important for modern content modpacks. |
| Forge | Legacy target | Intended for high-value legacy lines such as 1.20.1 once the architecture is stable. |
| Quilt | Compatibility-tested | Expected through the Fabric build first, unless Quilt-specific support becomes necessary. |

The repo starts with simple placeholder folders for `common/`, `fabric/`, `neoforge/`, and `forge/`. Actual build tooling will be added once the first Minecraft target version is chosen.

The first build target is Fabric for Minecraft 26.1.2. NeoForge is the next planned loader once the first feature/config pattern is proven.

## Feature Philosophy

Each feature should have:

- A source or evidence note.
- A simple priority: high, medium, low, or maybe-later.
- A toggle plan.
- A rough category, such as blocks/items, mobs, worldgen, mechanics, structures, UI, or cosmetic.
- Notes on whether it needs server support, client support, worldgen, or both.

For old implemented features, old jars and decompiled code can be used locally as research references. The repository should still contain our own implementation, not copied Minecraft source or redistributed assets.

## Repository Layout

```text
common/                 Future shared mod logic.
fabric/                 Future Fabric code.
neoforge/               Future NeoForge code.
forge/                  Future legacy Forge code.
docs/allfeaturesfromwiki.md
                        Raw wiki reference dump. Do not edit directly.
docs/feature-candidates.md
                        Tidied working list of possible features.
docs/project-workflow.md
                        Practical operating model for development.
docs/features/          Optional notes for individual complex features.
.github/                Minimal issue/PR templates and a basic safety CI check.
```

## Legal Boundaries

Contributors must not commit:

- Minecraft jars.
- Decompiled Minecraft source.
- Copied Mojang/Microsoft textures, sounds, models, or other assets.
- Claims that the project is official, endorsed, approved, or associated with Mojang or Microsoft.

See `docs/legal.md` before contributing feature research or assets.

## Getting Started

The first milestone is to tidy `docs/allfeaturesfromwiki.md` into `docs/feature-candidates.md`, pick a small first batch, then choose the modding template and Minecraft target version.

Good first implementation candidates:

- Giant behavior and spawning rules.
- Ruby item/block recreation.
- Quiver recreation.
- Old rose/cyan flower variants.
- A tiny removed recipe or loot-table experiment.

See `docs/roadmap.md` for the staged plan.
