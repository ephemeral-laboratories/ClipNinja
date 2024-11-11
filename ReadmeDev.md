# Developer Notes

## Building

To build the installer:

```shell
./gradlew packageDistributionForCurrentOS
```

This places an installer in `build\compose\binaries\main\...`.

To run the application without going through the full installer build:

```shell
./gradlew run
```

## Release Process

1. Ensure there are no unexpected local changes in the repository, and that
   the branch you have checked out is the main branch.
2. Perform a version bump:
    - Edit `version.txt` to increase the version.
    - Edit `Changes.md` to state that version for the top-most block.
3. Commit that
    ```shell
    git commit Changes.md version.txt
    ```
4. Tag that, and push the tag
    ```shell
    git tag NEW-VERSION
    git push --tags
    ```
5. Build the installers
    ```shell
    ./gradlew clean packageDistributionForCurrentOS
    ```
6. Over on GitHub, navigate to the tag and "Create release from tag".
    - Conventional title is the app name followed by the tag name
    - Conventional body is the entries from `Changes.md` for the new version
    - Publish the release
7. The installers should have finished being built by now, so add those
   to the release you just created.
