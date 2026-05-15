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

## Current Scope

The current version adds:

- Low-depth black fog in the Overworld.
- Fog strength mapped to the modern world bottom instead of hardcoded old Y=0 assumptions.
- Sky-light escape behavior: if the camera can see sky, void fog does not apply.
- Lightweight gray ash particles for the old depth-suspend feel.
- Mod Menu toggles for fog and particles.

## Implementation Notes

The fog uses a narrow client mixin into the modern 26.1 `FogRenderer` / `FogData` path instead of changing world state or server behavior.

Particles use a client tick hook and are visual only.

## Later Scope

- Tune the exact start/full-strength curve after playtesting.
- Replace placeholder particles with a closer custom depth-suspend particle if needed.
- Add screenshots or comparison notes from old versions.

