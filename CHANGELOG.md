# Changelog v2.3.0

### Added

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
