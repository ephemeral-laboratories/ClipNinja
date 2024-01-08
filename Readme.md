# Clipboard Ninja

Are any of these stories relatable?

- You often paste URLs to things, but don't want to include all the tracking information
- You pasted an <abbr title="Twitter">X</abbr> or Tiktok link to Discord, but it didn't embed properly,
  so now you have to change the domain, so it can embed

Various workflows can be automated by watching the clipboard and replacing its contents,
and thus the clipboard ninja was born.

## Installing

Grab an installer from [Releases](https://github.com/ephemeral-laboratories/ClipboardNinja/releases).

Currently, we only have installers for Windows. If you want support for another
platform, [file an issue](https://github.com/ephemeral-laboratories/ClipboardNinja/issues).
It might be easier to implement than you think!

This app is free software, but if you find it useful and have the means,
you might consider [sponsoring the project](https://github.com/sponsors/ephemeral-laboratories).

## Current Automations

All automations are separately toggleable.

Replacing URL domain to fix embeds:

| Changes URL   | To URL          |
|---------------|-----------------|
| `twitter.com` | `vxtwitter.com` |
| `x.com`       | `fixupx.com`    |
| `tiktok.com`  | `fxtiktok.com`  |

Removing tracking tokens from URL:

| Parameter | For URL domains           |
|-----------|---------------------------|
| `utm_*`   | (all)                     |
| `si`      | `youtube.com`, `youtu.be` |
