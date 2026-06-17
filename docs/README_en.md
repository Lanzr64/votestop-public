# Vote Stop Server
English | [中文](../README.md)
A Minecraft MOD that allows players to shut down the server through voting.

## Introduction

Vote Stop Server is a **Minecraft Mod** that allows players to decide whether to shut down the server through voting. When the server needs to restart for maintenance or players want to end the current game session, a vote can be initiated for all online players to decide together.

**Core Mechanism**: Requires unanimous consent from all active (non-AFK) players to shut down the server, ensuring everyone's wishes are respected.

## Features

- **Unanimous Consent** — The server will only shut down when all active players vote in favor
- **AFK Detection** — Low-overhead AFK monitoring that only tracks interaction events and chat events, determining AFK status solely based on tick timestamps
- **Server Startup Protection** — Voting is disabled for a period of time after server startup
- **Extremely Low Performance Impact** — Almost no performance cost
- **Almost Fully Configurable** — All in-game messages can be customized through configuration files, AFK check intervals and broadcast intervals are configurable

## Commands

| Command | Description |
|---------|-------------|
| `/tyj votestop yes` | Vote in favor of shutting down the server |
| `/tyj votestop no` | Vote against shutting down the server (will immediately cancel the current vote) |
| `/tyj votestop` | Same as `/tyj votestop yes` |

> **Note**: Any player voting against will immediately cancel the entire vote

## How It Works

1. **Vote Initiation**: A player types `/tyj votestop` to start a vote
2. **Condition Check**: The system checks whether the server startup protection period has passed and whether there is already an ongoing vote
3. **Voting in Progress**: Once the vote starts, the system periodically broadcasts the current number of yes votes and remaining time
4. **AFK Exclusion**: The system automatically detects AFK players, counting only active players
5. **Result Determination**:
   - ✅ **Unanimous Approval** → Server shuts down
   - ❌ **Someone Votes Against** → Vote is cancelled
   - ⏰ **Timeout Without Approval** → Vote is cancelled