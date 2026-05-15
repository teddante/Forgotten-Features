# Changelog

All notable changes to Forgotten Features will be documented here.

This project intends to use semantic versioning for the mod version, with Minecraft version and loader included in release files.

## Unreleased

- Bootstrapped a lightweight repository structure, starter documentation, and minimal GitHub hygiene.
- Started the first Fabric 26.1.2 feature slice with a config-backed Ruby item.
- Added Mod Menu integration for in-game Ruby toggles when Mod Menu is installed.
- Expanded Ruby into a survival-friendly item/block/ore set with recipes, loot tables, tags, and placeholder original textures.
- Added Ruby Ore generation in mountain biomes behind the Ruby ore-generation config toggle.
- Added `/forgottenfeatures findrubyore [radius]` for quick Ruby Ore generation checks.
- Tightened agent and project workflow docs around automated verification and launcher test builds.
- Added local verification and Prism-copy scripts, plus clearer issue/PR/version/release workflow guidance.
- Started restoring Void Fog as a toggleable client visual feature.
- Tuned Void Fog against the old renderer curve from local official-jar research.
- Smoothed Void Fog's modern renderer integration to avoid sudden transitions and sky/cloud fog artifacts.
- Added a custom suspended void-fog particle and `/ffvoidfog` client diagnostics for renderer compatibility checks.
- Stopped Void Fog from changing render-distance fog so optimization mods do not cull terrain and reveal sky.
- Moved Void Fog to a renderer-safe visual overlay and removed FogData distance edits entirely.
