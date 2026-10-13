# Changelog v2.5.5

To see full change log, see `CHANGELOG_FULL.md` in mod's jar.</br>
Or alternatively, visit [mod's github](https://github.com/FaeWulf/Diversity/tree/sub-mod-1.21.3/CHANGELOG_FULL.md)

### Changed

- Ported to Minecraft 26.3 for Fabric and NeoForge
- Gradle 9.8.0, Fabric Loom 1.18.2, ModDevGradle 2.0.148, Fabric API 0.161.0+26.3, NeoForge 26.3.0.51-beta,
  ModMenu 21.0.0

### Fixed

- Coral bonemealing: the removed vanilla coral worldgen features are now reimplemented internally
- Bundle stacking: oversized stacks are split correctly again
- `deepslate_generator` now works on both Fabric and NeoForge
- Farmland trample prevention (`prevent_farmland_trampling`) still cancels the farmland reset
- Rabbit carrot-crop breeding feature is applied again