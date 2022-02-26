# VanillaCore
Example Chat Plugin created for VanillaRealms :)

## Commands & Permissions
#### Commands
- /message (default)
- /reply (default)
- /broadcast (vanillacore.broadcast)
- /commandspy (vanillacore.commandspy)
- /socialspy (vanillacore.socialspy)

#### Permissions (From plugin.yml):
permissions:
- vanillacore.commandspy.list:
  - description: See the list of active command spies
  - default: op
- vanillacore.bypass.commandspy:
  - description: Bypasses this players commands being sent to command spies
- vanillacore.bypass.move:
  - description: Bypass having to move to execute commands & chat
  - default: op
- vanillacore.socialspy.list:
  - description: See the list of active social spies
  - default: op
- vanillacore.bypass.socialspy:
  - description: Bypass messages showing up in social spy
  - default: op
- vanillacore.bypass.ascii:
  - description: Bypass ascii characters being blocked in your chat messages
  - default: op
- vanillacore.bypass.antiad:
  - description: Bypass the block on sending links and IPs in your chat messages
  - default: op
- vanillacore.staff:
  - description: Counted as a staff member
  - default: op
- vanillacore.chat.format:
  - description: Allows the user to use colors and formats in chat
  - default: op
