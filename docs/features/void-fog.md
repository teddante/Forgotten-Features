# Void Fog

## Summary

Void fog restores the old low-depth black fog and gray particle ambience that existed in Java Edition from Beta 1.8 Pre-release through 14w34b.

## Status

- Priority: high
- Category: Visuals
- Fidelity: close
- Initial implementation: Fabric 26.1.2
- Toggle: `features.voidFog.enabled`

## Evidence

`docs/allfeaturesfromwiki.md` notes that void fog and particles appeared from Java Edition Beta 1.8 Pre-release until removal in Java Edition 14w34c, affecting the lower part of the world.

Minecraft Wiki's Fog page describes the removed void fog as thick black fog that began appearing as the player descended below low Y levels, became stronger near bedrock, depended on lack of sky light, and included gray particles. The Void page also notes the old `depthsuspend` particle effect.

Local reference checks were made against official client jars in ignored `.research/` storage. Java Edition 1.3.2 contains the old renderer path and `depthsuspend` particle spawning code; Java Edition 14w34b still contains the old effect, while 14w34c is the removal comparison point. These jars and decompiled/bytecode notes are not committed.

## Current Scope

The current version adds:

- Low-depth black fog in the Overworld.
- Neutral black/gray fog color mapped to the old squared Y curve, translated onto the modern world bottom.
- Fog distance mapped toward the old sky-light plus `(Y + 4) / 32` curve, translated onto the modern world bottom and smoothed for modern high render distances.
- Sky-light escape behavior: old sky-light contribution reduces or removes the fog in open shafts.
- Lightweight gray ash particles using the old 16-block random sampling shape for the old depth-suspend feel, with a modern-height offset so they appear during the visible fog band.
- Mod Menu toggles for fog and particles.

## Implementation Notes

The fog uses a narrow client mixin into the modern 26.1 `FogRenderer` / `FogData` path instead of changing world state or server behavior. It only narrows environmental/render-distance fog and deliberately avoids touching sky/cloud fog distances, which keeps the effect from exposing sky rendering through cave scenes.

Particles use a client tick hook and are visual only. Minecraft's removed `depthsuspend` particle no longer exists in modern Java Edition, so the mod uses `ASH` as a clean modern substitute while preserving the old spawn shape.

## Later Scope

- Replace `ASH` with a custom clean-room depth-suspend-style particle if the built-in substitute feels too different.
- Add screenshots or comparison notes from old versions.
