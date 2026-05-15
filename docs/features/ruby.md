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

The current version adds:

- `forgottenfeatures:ruby`
- `forgottenfeatures:ruby_block`
- `forgottenfeatures:ruby_ore`
- `forgottenfeatures:deepslate_ruby_ore`
- English item name
- Block/item models and original placeholder textures
- Ruby Block crafting and uncrafting recipes
- Ruby Ore and Deepslate Ruby Ore smelting/blasting recipes
- Ore loot tables that mirror Emerald Ore behavior for Silk Touch, Fortune, and explosions
- Pickaxe and iron-tool tags
- Ruby as a beacon payment item
- Ruby Ore world generation in mountain biomes, mirroring Emerald Ore placement
- Common item tags for Ruby gems, ores, and storage blocks
- Config-backed creative tab visibility
- Mod Menu config screen toggles when Mod Menu is installed
- Admin verification command: `/forgottenfeatures findrubyore [radius]`

Ruby content is always registered for world safety. Disabling the feature hides it from this mod's creative-tab injection and prevents new Ruby Ore generation on the next startup, rather than removing registry IDs.

## Later Scope

Possible next steps after the first playable slice:

- Add villager/trading behavior if a faithful design can be supported cleanly.
- Compare item texture more carefully against historical screenshots without copying Mojang assets.
