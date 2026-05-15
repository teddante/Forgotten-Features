# Roadmap

## 1. Tidy the Feature List

- Keep `docs/allfeaturesfromwiki.md` as the raw reference dump.
- Build `docs/feature-candidates.md` into the readable working list.
- Rank candidates by player interest, evidence quality, difficulty, and toggle safety.

## 2. Pick the First Build Target

- Choose the first modern Minecraft version.
- Start with Fabric and NeoForge if the template makes that manageable.
- Delay legacy Forge until there is a working mod and some real features.
- Follow `docs/project-workflow.md` for the loader/version decision.

## 3. First Playable Alpha

- Add config infrastructure.
- Add a tiny feature registry.
- Add a small first batch of low-risk features.
- Make every feature toggleable.
- Add a simple in-game config screen if it is not too much overhead.

## 4. Modpack Readiness

- Add server config examples.
- Add compatibility notes.
- Publish on Modrinth and CurseForge.

## 5. Legacy Support

- Add selected Forge support after the modern architecture proves stable.
- Prefer high-value modpack versions over trying to support everything.
