# OrangeCustomItems

A Paper plugin for OrangeMC, targeting Paper 1.21.5 and Java 21.

## Included items

- **Triple Totem**: vanilla totem behaviour, with three tracked activations. It can be held in either hand. Remaining uses are stored on the item and survive restarts.
- **Golden Apple+**: a renamed vanilla golden apple. It keeps the normal golden apple food and regeneration effects, but grants Absorption II (4 extra hearts) for two minutes.

## Commands

- `/oci give <player> triple_totem`
- `/oci give <player> golden_apple_plus [amount]`
- `/oci list`
- `/oci reload`

Permission: `orangecustomitems.admin` (operators by default).

## Build with GitHub Actions

1. Create a GitHub repository named `OrangeCustomItems`.
2. Upload the contents of this project so `.github/workflows/build.yml` is at that exact path in the repository.
3. Push/commit the files.
4. Open **Actions** and wait for **Build OrangeCustomItems** to finish successfully.
5. Open the completed workflow run and download the `OrangeCustomItems-1.0.0` artifact. The JAR is inside the downloaded artifact.
6. Put the JAR in the server's `plugins` folder and restart the server.

## Build locally

Requires Java 21 and Maven:

```bash
mvn clean package
```

The JAR will be at `target/OrangeCustomItems-1.0.0.jar`.

## Notes

- This first version does not add crafting recipes yet; the items are issued with `/oci give` while we decide balanced recipes.
- The Triple Totem is a unique item and is intentionally not stackable through the give command.
- This project targets Paper 1.21.5. Older Paper versions may require API and plugin metadata adjustments.
