# Changelog v2.4.0

To see full change log, see `CHANGELOG_FULL.md` in mod's jar.</br>
Or alternatively, visit [mod's github](https://github.com/FaeWulf/Diversity/tree/sub-mod-1.21.3/CHANGELOG_FULL.md)

### Added

- Support 1.21.5
- Sugarcane can grows/can be placed on block has tag `diversity:strong_support_sugarcane` now doesn't need water
  nearby anymore. Default block: Mud,Muddy Mangrove Roots.
- Extra setting for `Day counter`: message speed.
- Compatibility with `Enchancement` mod.

### Changed

- `Day counter`'s setting now using number, default 1 for everyday announcement. Disable the feature with value 0.

### Fixed

- Blocks placed via bundle now award stats like normal block place behavior.
- Critical issue where bundle gets deleted if placing bucket of powder snow. To prevent the issue or prevent an item can
  be placed by bundle, use the tag
  `diversity:bundle_place_mode_blacklist`.
- Correct behavior for `Gold trimmed armor also gold` when interacting with the Piglin.