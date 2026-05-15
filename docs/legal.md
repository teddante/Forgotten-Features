# Legal Notes

This is not legal advice. It is the working safety policy for the project.

Forgotten Features should be a clean-room recreation mod. The project must not ship Minecraft itself, old Minecraft jars, decompiled Minecraft source, copied Mojang code, or removed vanilla assets.

## Required Disclaimer

Use this disclaimer in public project pages:

```text
NOT AN OFFICIAL MINECRAFT PRODUCT. NOT APPROVED BY OR ASSOCIATED WITH MOJANG OR MICROSOFT.
```

## Allowed Research Workflow

Contributors may:

- Use legitimate Minecraft installations and tooling to inspect old versions locally.
- Decompile old versions locally for research and comparison.
- Record behavior notes, screenshots, measurements, constants, and videos.
- Write clean feature specifications from observations.
- Reimplement behavior in original code.
- Create original assets inspired by historical behavior.

Contributors must not:

- Commit Minecraft jars.
- Commit decompiled Minecraft source.
- Copy code from decompiled Minecraft classes.
- Commit ripped textures, sounds, models, or other assets.
- Make the project appear official or endorsed.

## Faithful Reimplementation

The goal is to match old behavior as closely as practical. For features that existed in old versions, old code can help answer questions like "what was the speed, range, drop table, hitbox, or AI rule?"

When implementing, translate that research into fresh code that fits the modern Minecraft and loader APIs. Do not paste old Minecraft methods or classes into the mod.

## Asset Policy

Assets should be original. If a current vanilla asset can be referenced by Minecraft's normal resource system without redistribution, prefer referencing it. If an asset was removed, recreate it from scratch.
