# Mialee Misc

## Features
- Custom Item Group system, allows item groups with cycling display icons.
- StatusEffect and DamageModifierStatusEffect classes with public constructors.
- Allows items to send a packet on left click instead of attacking.
- Allows items to decide if they should use the crossbow hold/charge poses.
- Allows items to display the cooldown progress of other items instead of their own.
- Allows items to be eligible for any enchantment target they decide.
- Allows item entities to be immune to all damage if their item is on a tag.
- Adds some helper methods for getting enchanted stacks.
- Adds an item which pastes an entire namespace's item identifiers to the console (for easy tag creation).
- Adds useful math methods:
  - ClampLoop (clamps a value between two values, but loops around if the value is outside the range).
- Adds a tracker for the last targeted entity by a player.
- [WIP] Allows for custom superflat presets to be added with a config file.