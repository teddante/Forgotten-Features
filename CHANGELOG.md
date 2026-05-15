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
