# Contributing

Thanks for helping make Forgotten Features accurate, useful, and legally safe.

## Simple Rules

- Use `docs/allfeaturesfromwiki.md` as raw reference material, but do not edit it directly.
- Put cleaned-up, ranked feature ideas in `docs/feature-candidates.md`.
- Do not commit Minecraft jars, decompiled source, copied vanilla assets, or copied Mojang code.
- Keep features independently toggleable.
- Keep changes small and understandable.
- Follow `docs/project-workflow.md` for issues, branches, commits, releases, and loader decisions.

## Feature Notes

For each candidate feature, try to capture:

- Name.
- Priority.
- Evidence/source.
- Original version or era, if known.
- What it should do.
- Whether it affects client, server, worldgen, or data packs.
- Toggle/category idea.

## Branch Names

Recommended branch names:

- `feature/ruby`
- `feature/giant`
- `docs/sky-dimension-research`
- `infra/multiloader-bootstrap`
- `fix/config-sync`

Long-running Minecraft version branches should use:

- `mc/latest`
- `mc/1.20.1`

## Commits and Pull Requests

Commit when a useful checkpoint is complete: one feature, one bug fix, one docs cleanup, or one build/config step.

Pull requests should stay focused, link the issue if there is one, and include a short test or manual-check note.
