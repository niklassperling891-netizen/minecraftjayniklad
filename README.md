# Breed Status — Fabric / Minecraft 1.21.11

Client-side Minecraft mod project. The marker key defaults to **B** and can be rebound in Options → Controls → Key Binds → Miscellaneous.

## Statuses

- **Green `G`** — adult `AnimalEntity`, no reported breeding cooldown, and an accepted breeding item is held in the main or off hand.
- **Orange `C`** — positive vanilla breeding-age value.
- **Red `—`** — baby animal or no matching breeding item held.
- **No marker** — unsupported breeding system.

Markers are drawn above supported animals within 24 blocks. The mixin uses the entity renderer, not a through-wall overlay.

## Important limits

This is a client-side best-effort status indicator, not a guarantee that a server will accept feeding. It cannot inspect every server-side condition, mod/plugin rule, or species-specific breeding requirement. Villagers are intentionally omitted because their breeding readiness depends on server-side willingness, inventory/food, beds and other conditions. Other mobs that do not use `AnimalEntity` are not marked. The displayed colors therefore describe the available client-side checks, not a universal prediction for every breedable entity.

## Build

Requirements: JDK 21, Gradle, and internet access for dependency resolution.

Generate wrapper scripts/JAR if not present:
```sh
gradle wrapper --gradle-version 8.14
```

Then run:
```sh
./gradlew build
```
Windows:
```bat
gradlew.bat build
```

The resulting JAR is expected under `build/libs/`. This archive does not bundle Gradle wrapper files. Project has not been compiled or tested in Minecraft in this environment.

## Install

1. Install Fabric Loader for Minecraft **1.21.11**.
2. Install the matching Fabric API for Minecraft **1.21.11**.
3. Copy `build/libs/breed-status-1.1.0.jar` to the instance's `mods` folder.
4. Launch the Fabric profile.

## Declared compatibility

- Minecraft 1.21.11
- Fabric Loader 0.18+
- Fabric API `0.141.4+1.21.11` (configured in `gradle.properties`)
- Java 21
- Client-side mod; server installation not required

## Verification

This project is a source archive, not a verified compiled release. Version coordinates and Minecraft API compatibility should be confirmed by running the Gradle build. If compilation reports changed API signatures, adjust the source to the exact mappings/API downloaded for 1.21.11.
