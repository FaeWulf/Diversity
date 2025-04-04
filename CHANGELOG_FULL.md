# Changelog

# [2.3.4] - 2025-3-18

### Fixed

- Issue where Farmer trades replaced by leatherworker trades.
- `Faster minecart` now works properly on Forge/NeoForge.

## [2.3.2] - 2025-3-14

### Added

- Bundle now shows tooltip about place function switcher.
- Forge support, bugs expected.

### Changed

- Bundle enchantments can not apply to bundle.

## [2.3.1] - 2025-3-9

### Changed

- Require Faewulf's Lib version 1.2.2

### Fixed

- Game crash when load the mod server-side.

## [2.3.0] - 2025-3-7

### Added

- Now require to install `Faewulf's Lib` as a library dependency. Sorry for the inconvenient.
- `Beacon Extended` module extends the beacon's range and duration beyond level 4! Each layer past level 4 adds 20
  blocks in range, up to level 10.
- New `Day counter` option to change daytime tick (only change if you know what you are doing).
- Fox auto breed after eat berries.
- Two new enchantments for Bundle: `Vacuum` (sucks all newly picked-up item from inv) and `Selective Vacuum` (only sucks
  the same items exist in bundle).
- Extra config option for `Spy glass what is that?` (use range, show info, distance, name).
- `No saddled mobs wandering` (replaces `No tamed horse wandering`) now affect all mob that can wear saddle. (Modded
  mobs supported)

### Changed

- `No tamed horse wandering` renamed to `No saddled mobs wandering` in favor of its new behavior.
- `No saddled mobs wandering` affected mobs able to move when luring (example luring pig with carrot), avoiding water,
  panicking.
  They only stand still in idle mode.
- Bundle's enchantments are no longer obtained via enchanting.
  For rebalance purpose, all bundle enchantments will move to
  leather worker trade instead.

### Removed

- Obtain bundle's enchantments via enchanting table.

### Fixed

- Compatibility with `EasyShulkerBoxes`.
- Inconsistency behavior of `Shear prevents plants grow`.

## [2.2.2] - 2025-1-10

### Added

- support 1.21.4

### Changed

- Added swing animation for `mace rotates block`
- Now `Day counter` has 4 options: announce per `1 day`, `10 days`, `50 days`, `100 days`. Default is `1 day`.

### Fixed

- Day counter announce crash if daytime is negative.
- `Edible endstone` is not edible.

## [2.2.1] - 2024-12-9

### Changed

- `Hoe auto harvest` now has 4 options: "ALL", "HAND_ONLY", "HOE_ONLY", "DISABLE"
- Slightly reduce chance of capacity III appears in enchanting table.

### Fixed

- Compatibility issue with farmer's delight.

## [2.2.0] - 2024-11-21

### Added

- Right-click mature crop will harvest and auto replant for you.
- Hoe can harvest multiple crops. Default is 3x3, diamond and netherite gives 5x5. (Item tag:
  `diveristy:crop_harvester`, `diveristy:tier2_hoe`)
- Rail placed on `Gravel` will make Minecart and its variants travel faster. (Block tag: `diversity:rail_supporter`)
- Pickpocket villager using `Shear` while `sneaking`.
  Attempting to pickpocket from the front has a high chance of
  making them angry; reduce this risk by pickpocketing from behind, or completely nullify it with an invisibility
  potion.
  (Item tag: `diversity:pickpocket_tool`)
- Pickpocketing a villager has a chance to reset their profession.
  The higher the profession level, the lower the chance of it resetting.
- Snow Golem stands inside a Cauldron will produce Powder snow over time.
- Shears can be used on primed TNT to give a chance to defuse it.

### Changed

- Added enchantment description for custom enchantment.

## [2.1.3] - 2024-11-10

### Changed

- Crop drops XP now also drops from nature causes.
- `Renewable Coral` now accept biomes have tag `produces_corals_from_bonemeal`.
- Dispenser now can trigger `Bone meal coral` and `Bone meal small flower`.
- Swing animation when brushing `chicken` and `parrot` for feathers.

### Fixed

- Random game crash from placing down WetSponge

## [2.1.2] - 2024-10-30

### Fixed

- Game does not launch on neoforge version

## [2.1.1] - 2024-10-29

### Fixed

- Removed console spam messages.
- "Shear prevents plant grows" doesn't work on vine.

## [2.1.0] - 2024-10-9

### Added

- Goat drops wool and mutton.
- Shear can be used on `Saplings`, `Bamboo`, `Vine` and `Sugar cane` to prevent it from growing.
- Using the clock will show current time.
- Reverse phantom spawn condition.
  (Phantom will spawn if the player continuously sleeps skipping the night, default:
  false)
- End stone is cheese. (default: disable)
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

## [2.0.1] - 2024 -9-14

### Changed

- A little info screen
- Config file now is `diversity.toml` for easier reading and editing.

### Fixed

- Fixed issue with `Easy Shulkerboxes` compatibility.