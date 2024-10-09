# Changelog v2.1.0

### Added

- Goat drops wool and mutton.
- Shear can be used on `Saplings`, `Bamboo`, `Vine` and `Sugar cane` to prevent it from growing.
- Using the clock will show current time.
- Reverse phantom spawn condition.
  (Phantom will spawn if the player continuously sleeps skipping the night, default:
  false)
- Slime chunk detector: Holding a `Slime Ball`, `Slime Block`, or items with the `diversity:slime_detector` tag will
  emit
  particles and sound when in a slime chunk, with a 10% chance while walking/running and 70% chance when sneaking.

### Changed

- **Config file now is `diversity.toml` for easier reading and editing.**
- A little info screen.
- `Bonemeal small flower` now exclude `small_flower` has the tag `diversity:bonemeal_blacklist`, default list contains
  `wither_rose` and `torchflower`.
- `Bonemeal small flower` now requires dirt with tag `diversity:rich_soil`to be bonemealed. Default contains `Mycelium`.
- `Torch lights target on fire` now use the tag `diversity:flame_weapon`, default list contains `torch` and
  `soul_torch`.
- Added swing animation for some feature uses the right-click event.
- Rework Bone meal coral, small flower to prevent hard conflict.
- `Fox buries Item` now buries item from its mouth, if not then use loot table (bury random items).
- `Bigger bookshelf radius for enchanting table` feature now toggleable.
- `Egg auto hatch` now only hatch on blocks have the tag `diversity:egg_hatchable`, default list contains `hay_block`.

### Removed

- Check slime chunk command (replaced with `slime chunk detector` feature).

### Fixed

- Fixed issue with `Easy Shulkerboxes` compatibility.
- Duplicate Text Display on named Shulker.
- `No anvil xp limit` crash.
- Missing command in forge/neoforge.