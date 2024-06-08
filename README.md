**Usage**:

```/resourcepack reload``` - Applies the RP currently in server.properties (even if the server hasnt restarted yet)
```/resourcepack custom <url> <sha1>``` - Applies *any* resourcepack to the player
```/resourcepack sendNewRPMessage``` - sends a message to the player telling them a new resourcepack is available


**Subcommands**:

- /resourcepack reload
- /resourcepack reload <player/selector>
- /resourcepack custom <url> <sha1>
- /resourcepack custom <url> <sha1> <player/selector>
- /resourcepack sendNewRPMessage
- /resourcepack sendNewRPMessage <player/selector>


**Notes**:

- only the */resourcepack reload* command (without the target specifier) is available without any perms
- the <url> field has to be incased in "", because minecraft is stupid. ex. ```/resourcepack custom "https://github.com/Mat0u5/DO2-Resources/releases/download/release-5c41e631bc0f09e98351c998675314ef49941e13/RP.zip" 1039ab27e38f70391aa884bbc7563481eee9048b```
