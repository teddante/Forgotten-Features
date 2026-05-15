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
- Fog distance mapped toward the old sky-light plus `(Y + 4) / 32` curve, translated onto the modern world bottom, with a short fade-in band before it reaches the old target distance.
- Sky-light escape behavior: old sky-light contribution reduces or removes the fog in open shafts.
- Custom gray suspended particles using the old 16-block random sampling shape for the old depth-suspend feel, with a modern-height offset so they appear during the visible fog band.
- Mod Menu toggles for fog and particles.
- Client diagnostic command: `/ffvoidfog`.

## Implementation Notes

The fog uses a narrow client mixin into the modern 26.1 `FogRenderer` / `FogData` path instead of changing world state or server behavior. It only narrows environmental/render-distance fog and deliberately avoids touching sky/cloud fog distances, which keeps the effect from exposing sky rendering through cave scenes. The mixins use lower priority so they apply after common fog-setting mixins such as Sodium Extra's fog multipliers.

Particles use a client tick hook and are visual only. Minecraft's removed `depthsuspend` particle no longer exists in modern Java Edition, so the mod registers a tiny clean-room suspended particle rather than relying on `ASH`, which drifts downward.

## Later Scope

- Use `/ffvoidfog` results from Sodium/Iris test instances to confirm whether any renderer-specific compatibility path is still needed.
- Add screenshots or comparison notes from old versions.
