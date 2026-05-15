# Project Workflow

This document ties together the lightweight process for Forgotten Features: how ideas become code, how GitHub is used, how releases are named, and how the mod should stay compatible and faithful without becoming over-engineered.

## North Star

Build the smallest well-made system that lets the project:

- Find and rank historical feature ideas.
- Recreate features as faithfully as practical.
- Let users and modpack authors toggle features cleanly.
- Support modern Fabric and NeoForge first.
- Stay legally safe and easy for humans or AI agents to work on.

## Source Pipeline

1. `docs/allfeaturesfromwiki.md` is the raw source archive. Do not edit it.
2. Interesting ideas are copied into `docs/feature-candidates.md`.
3. The candidate list ranks features by player interest, evidence quality, difficulty, and compatibility risk.
4. Complex features can get a dedicated note in `docs/features/`.
5. Code is written from the notes and observed behavior, not by pasting decompiled Minecraft source.

## Feature Selection

Use this rough scoring when deciding what to build next:

| Factor | Good early feature | Later feature |
| --- | --- | --- |
| Evidence | Implemented, removed, or clearly documented | Rumor, vague mention, or unclear behavior |
| Scope | Item, block, recipe, simple mob behavior | Dimension, worldgen overhaul, large AI system |
| Compatibility | Mostly isolated | Touches common hooks, worldgen, or registries heavily |
| Toggle safety | Easy to disable | Hard to remove from existing worlds |
| Player value | Recognizable and wanted | Niche or mostly archival |

Good first features are small, iconic, and easy to turn off.

## Fidelity Rules

Use a simple fidelity label for each feature:

- `strict`: match old implemented behavior as closely as modern Minecraft allows.
- `close`: match the feel and major behavior, but adjust where modern systems changed.
- `inspired`: for developer-mentioned, concept-only, or speculative ideas.

For old implemented features, research can include old jars, old resources, decompiled code viewed locally, screenshots, videos, and wiki notes. The committed mod code should still be fresh implementation.

Record useful facts such as sizes, speeds, drops, damage, spawn rules, recipes, loot, textures to recreate, sounds to recreate, and known quirks.

## Old Java Source Research

The efficient research path is local and temporary:

1. Use legitimate launcher, Fabric Loom, NeoForge tooling, or other accepted modding tools to download the target Minecraft version locally.
2. Inspect old classes/resources in an ignored local folder such as `.research/`, `minecraft-jars/`, or `decompiled-minecraft/`.
3. Extract behavior facts: values, formulas, registry names, AI goals, drops, NBT, block settings, recipes, models, sounds, and quirks.
4. Write those facts into `docs/feature-candidates.md` or `docs/features/<feature>.md`.
5. Rebuild the feature in original code against the current loader APIs.

Do not commit old jars, decompiled source, copied methods/classes, or ripped assets. The goal is faithful behavior, not copied source.

## Loader Strategy

Recommended order:

1. Fabric for the current unobfuscated Minecraft line.
2. NeoForge for the current unobfuscated Minecraft line.
3. Forge for one selected legacy line later, probably a modpack-heavy version.
4. Quilt as Fabric compatibility testing first.

Avoid promising every Minecraft version. Support fewer versions well.

## 2026 Technical Baseline

As of May 2026:

- Mojang has started shipping Java Edition without obfuscation for the new line, making mod creation, updates, and debugging easier.
- Fabric docs say Minecraft 26.1 is unobfuscated and includes parameter names, so mods targeting 26.1+ should use Mojang names instead of Yarn.
- Fabric docs say Minecraft 26.1 development needs JDK 25.
- Fabric Loom uses `net.fabricmc.fabric-loom` for non-obfuscated Minecraft 26.1+ and `net.fabricmc.fabric-loom-remap` for 1.21.11 or older.
- NeoForge docs recommend release filenames that include the Minecraft version and loader when a mod ships multiple loader files.

Practical result: start the modern codebase on 26.1+ if the ecosystem is ready enough for the chosen dependencies. Keep 1.20.1/1.21.x legacy work separate and later.

## Build Tooling

Start simple:

- Gradle multi-project build.
- `common/` for shared code.
- `fabric/` and `neoforge/` for loader adapters.
- Add `forge/` only when there is a real legacy target.

Do not add Architectury API, extra abstraction layers, or a complex version matrix on day one. Reconsider them only if duplicate loader code becomes painful.

Once the Gradle skeleton exists, CI should run:

- Basic repository hygiene.
- Java setup for the selected Minecraft target.
- `./gradlew build`.
- Targeted tests for shared feature/config logic.
- Loader-specific build tasks for Fabric and NeoForge.

Local Codex work should run the same Gradle checks before a PR or release.

## Automated Verification

The default local verification loop is:

1. Compile while implementing.
2. Run `scripts/verify.ps1` or `./gradlew build --no-daemon` before commit.
3. Check the built jar contains expected assets/data when resources changed.
4. Copy the jar into the configured Prism Launcher instance for quick player testing.
5. Add a command, test, or data check when a feature would otherwise be awkward to verify.

Good automated checks include:

- Unit tests for config defaults, parsing, and feature toggles.
- Build-time compilation against current Minecraft/Fabric APIs.
- Jar content checks for expected models, textures, recipes, loot tables, tags, and worldgen files.
- Lightweight in-game commands for hard-to-see systems such as generation or spawning.
- Server/startup smoke tests once they are worth the maintenance cost.

Manual testing should focus on what automation cannot judge well: visual quality, game feel, sound, UI clarity, modpack behavior, and whether the feature is fun.

Local helper scripts:

- `scripts/verify.ps1`: runs the Gradle build and checks expected jar contents.
- `scripts/verify.ps1 -CopyToPrism`: also copies the playable jar to the configured Prism instance.
- `scripts/copy-to-prism.ps1`: copies the latest playable jar after a build.

Set `FORGOTTEN_FEATURES_PRISM_INSTANCE` locally, or pass `-PrismInstancePath` / `-InstancePath`.

## Config and Toggles

Every gameplay feature should have:

- A stable config key.
- A default state.
- A category.
- A note on client/server/worldgen impact.
- A safe behavior when disabled.

Likely categories:

- Blocks and items.
- Mobs and AI.
- World generation.
- Structures.
- Mechanics.
- Visuals and sounds.
- Experimental or speculative.

Use simple config files first. Add an in-game config UI when it improves usability. For 2026-era config UI, evaluate YACL and Cloth Config against the selected Minecraft version and loaders instead of committing too early.

For Fabric, Mod Menu is the expected access point for client-side config screens. Small early features can use a lightweight native screen exposed through Mod Menu. Larger future config screens can move to YACL or Cloth Config if the option count grows enough to justify the dependency.

## Compatibility Rules

- Prefer data-driven systems over code hooks when possible.
- Use tags for compatibility with other mods.
- Avoid replacing vanilla behavior globally unless the feature really requires it.
- Keep mixins small, targeted, and documented.
- Split client-only code from common/server code.
- Make worldgen features opt-in or clearly marked if they affect existing worlds.
- Test with a small compatibility pack once the mod has real features.

Automated tests cannot prove every in-game detail, but they can catch a lot:

- Config defaults and feature toggle behavior.
- Registry IDs and data generation output.
- Loot table, recipe, and tag JSON validity.
- Shared behavior helpers.
- Server startup smoke tests once the loader supports them in CI.
- GameTest-style checks for selected mechanics where practical.

Manual in-game testing should be saved for things automation cannot see well: feel, visuals, animation, sounds, worldgen aesthetics, and modpack compatibility.

## GitHub Workflow

See `docs/github-setup.md` for repository settings, branch protection, tags, and releases.

Issues are lightweight:

- Historical feature idea.
- Bug.
- Project improvement.
- Compatibility problem.

An issue should answer: what is wanted, what evidence exists, what "done" means, and whether it affects client, server, or worldgen.

Create an issue when:

- The scope needs discussion.
- Historical evidence needs review.
- Compatibility behavior is unclear.
- A user reports a bug or crash.
- The feature spans multiple commits or likely needs review from others.

Skip the issue when:

- The work is a tiny obvious fix.
- The change is documentation cleanup.
- The work is already part of the active feature branch.

Pull requests are also lightweight:

- One feature, bug, or infrastructure change per PR.
- Link the issue if there is one.
- Include a short test/manual-check note.
- Update `docs/feature-candidates.md` or `docs/features/` if historical behavior changed.
- Mention where the playable jar was copied when using a local launcher test instance.

Codex can manage this workflow end-to-end when the user asks: create or switch branches, edit files, run checks, stage changes, write commits, inspect CI, fix failures, and draft PR descriptions. Publishing or pushing should still be an explicit user decision.

## Branches and Commits

Use branches for work that might take more than one sitting:

- `feature/ruby`
- `feature/giant`
- `docs/candidates`
- `infra/mod-skeleton`
- `fix/config-sync`

Commit when a meaningful checkpoint is complete:

- Repo setup.
- Build skeleton.
- Config system.
- One feature.
- One bug fix.
- One docs cleanup.
- One verification/tooling improvement.

Avoid huge mixed commits. A future AI agent should be able to read a commit and understand exactly why it exists.

Do not commit:

- Built mod jars.
- Old Minecraft jars.
- Decompiled Minecraft source.
- Local launcher instances.
- `.env` or machine-specific config.

## Versioning and Releases

See `docs/github-setup.md` for the GitHub-side tag and release setup.

Use simple semantic mod versions:

- `0.1.0` first playable alpha.
- `0.2.0` new feature batch.
- `0.2.1` bug fix.
- `1.0.0` stable enough for normal players and modpacks.

Version bumps happen during release prep, not during every feature commit. Feature branches update `CHANGELOG.md`; release prep updates `mod_version`, finalizes the changelog entry, and tags the commit.

Release files should include mod ID, loader, Minecraft version, and mod version:

```text
forgotten-features-fabric-26.1.2-0.1.0.jar
forgotten-features-neoforge-26.1.2-0.1.0.jar
forgotten-features-forge-1.20.1-0.1.0.jar
```

Release flow:

1. Merge feature branches to `main`.
2. Review `CHANGELOG.md`.
3. Bump `mod_version` and release file naming if needed.
4. Run `scripts/verify.ps1`.
5. Commit release prep.
6. Tag the release, such as `v0.1.0`.
7. Create one GitHub release containing all loader jars for that mod version.
8. Publish matching files to Modrinth and CurseForge once the project is ready.
9. Keep release notes short and user-facing.

Create a release only with explicit user approval.

## Codex and AI Workflow

AI agents should work like careful contributors:

- Read `AGENTS.md`, `README.md`, this file, and the relevant docs before changing code.
- Use a feature branch or worktree for larger tasks.
- Keep edits scoped.
- Preserve `docs/allfeaturesfromwiki.md`.
- Update the candidate list or feature docs when behavior decisions are made.
- Run relevant checks before finishing.
- Summarize changed files and remaining risks.

Codex worktrees map naturally to Git branches: one branch per feature or infrastructure task, then a PR when the work is reviewable. Keep the branch small enough that a human can review it without needing archaeology of the archaeology project.

Recommended Codex loop:

1. Read `AGENTS.md` and the relevant workflow/docs.
2. Create or use a focused branch.
3. Implement one scoped task.
4. Run the smallest meaningful check while iterating.
5. Run `scripts/verify.ps1` or the full Gradle build before commit.
6. Copy the playable jar to the configured launcher instance when useful.
7. Update docs/candidates if behavior decisions changed.
8. Commit only when the checkpoint is coherent.
9. Open or update a PR when asked.

Browse the web when official/current facts matter. For historical features, combine the raw wiki archive, current Minecraft Wiki pages, official posts where available, and local old-version research. Record useful evidence in the feature docs rather than relying on chat memory.
