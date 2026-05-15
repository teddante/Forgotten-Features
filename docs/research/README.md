# Research Workflow

Forgotten Features should treat old Minecraft features like historical artifacts.

## Raw Reference

`docs/allfeaturesfromwiki.md` is the raw wiki dump. Do not edit it directly.

Use `docs/feature-candidates.md` for the cleaned-up working list.

## Process

1. Find a feature in the raw dump or another source.
2. Add it to the candidate list with a priority and evidence note.
3. If it was actually implemented before, inspect the old version locally and record the important behavior.
4. Rebuild it in fresh code for the current mod target.
5. Keep the feature toggleable and easy to disable.
