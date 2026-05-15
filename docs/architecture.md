# Architecture

Forgotten Features should stay simple at the start: one shared core idea, thin loader-specific code, and every feature behind a toggle.

## Starting Shape

```text
common/
  shared feature logic once the mod skeleton exists

fabric/
  Fabric entrypoint and adapters

neoforge/
  NeoForge entrypoint and adapters

forge/
  selected legacy Forge support later
```

## Design Principles

- Keep loader-specific code thin.
- Use existing Minecraft data systems where they fit: recipes, loot tables, tags, data packs, worldgen JSON, and resource packs.
- Use normal config systems that players and modpacks already understand.
- Organize options by feature category rather than by internal code structure.
- Be careful with worldgen and dimensions, because they affect existing worlds.

## Config Categories

Likely in-game/config groups:

- Blocks and items.
- Mobs and AI.
- World generation.
- Structures.
- Mechanics.
- Visuals and sounds.
- Experimental or speculative.

## Versioning

Release artifacts should include:

- Mod name.
- Loader.
- Minecraft version.
- Mod version.

Example:

```text
forgotten-features-fabric-1.21.x-0.1.0.jar
forgotten-features-neoforge-1.21.x-0.1.0.jar
forgotten-features-forge-1.20.1-0.1.0.jar
```

GitHub releases can group the loader-specific files for the same mod version.
