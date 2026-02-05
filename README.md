[![LOGO](https://i.imgur.com/48V7w2w.png)](LOGO)

# OldLegacyGMSwitcher

**Backported game mode switcher from 1.16+**

https://www.curseforge.com/minecraft/mc-mods/legacy-gamemode-switcher

**Credits:** beecupbe and GTNH Team

---

A compact Forge mod (Minecraft 1.7.10) that restores the modern gamemode switching UX for older clients.

This project is a minimal and compatibility-focused backport of the Legacy Game Mode Switcher, adapted to the GTNewHorizons build setup. It:

- Adds a convenient in-game UI for switching game modes (creative/survival/adventure).
- Keeps compatibility with 1.7.10 Forge and GTNH tooling.
- Is small, well-tested and intended for server operators or players that want the newer UX on older Minecraft versions.

## Features ✅

- Backported UI for game mode switching
- Syncs permissions and state across client/server
- Uses GTNH example build system for reproducible builds and CI

## Quick start 🔧

1. Edit `gradle.properties` to set `modName`, `modId`, and optionally `version`.
2. Build: `./gradlew build` — the mod JAR will be in `build/libs/`.
3. Test locally: `./gradlew runClient` (client) or `./gradlew runServer` (server).

## Versioning & Releases 🔖

- Set `version=1.0.0` in `gradle.properties` or create a Git tag (e.g., `git tag 1.0.0 && git push --tags`) to trigger CI releases.
- `mcmod.info` already includes `${modVersion}` which is substituted from the build setup.

## Contributing ✨

Contributions are welcome! Please fork the repo, make changes, and open a pull request. Follow the code style and run `./gradlew build` to ensure everything compiles.

Consider adding tests or a small changelog entry for notable fixes and features.

## License 📄

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

[![License: MIT](https://img.shields.io/badge/license-MIT-yellow.svg)](LICENSE)

---

For more details about the build setup and advanced topics, see `gradle.properties` and the GTNewHorizons ExampleMod documentation.
