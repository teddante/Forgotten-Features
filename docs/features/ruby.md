# Ruby

## Summary

Ruby is the first implementation slice for Forgotten Features: a faithful starter item for the removed ruby concept that predated emerald trading currency.

## Status

- Priority: high
- Category: Blocks and items
- Fidelity: close
- Initial implementation: Fabric 26.1.2
- Toggle: `features.ruby.enabled`

## Evidence

`docs/allfeaturesfromwiki.md` notes that rubies were intended as the original trading currency, appeared in Jeb's May 21, 2012 trading screenshot, and were replaced by emeralds because Dinnerbone is red-green colorblind. The raw archive also notes that a ruby texture remained in files after emeralds were implemented.

## Current Scope

The first version adds:

- `forgottenfeatures:ruby`
- English item name
- Item model and original placeholder texture
- Config-backed creative tab visibility

The item is always registered for world safety. Disabling the feature currently hides it from this mod's creative-tab injection rather than removing the registry ID.

## Later Scope

Possible next steps after the first playable slice:

- Recreate ruby ore or ruby block if evidence supports it.
- Add villager/trading behavior if a faithful design can be supported cleanly.
- Compare item texture more carefully against historical screenshots without copying Mojang assets.

