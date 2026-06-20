# Sidebet — Backend

A REST API for a private, friends-only prediction market — think Kalshi, but the
currency is bragging rights instead of money. Users create yes/no markets about
real life ("Will Marco be on time Friday?"), stake virtual points on an outcome,
and the pot is split among whoever called it right.

This repository is the **backend**: a Java / Spring Boot service backed by a SQL
database. The React frontend lives in a separate repo:
[sidebet-frontend](https://github.com/jsanchez04/sidebet-frontend).

> **Live API:** https://sidebet.onrender.com (try https://sidebet.onrender.com/users)
> **Live app:** https://sidebet-frontend.vercel.app
> (Hosted on a free tier, so the first request after a period of inactivity may
> take ~30–50 seconds to wake up.)

## Features

- **Users & balances** — every user starts with a pool of virtual points.
- **Markets** — any user can open a yes/no market with a question.
- **Wagers** — users stake points on YES or NO; the stake is held until the market resolves.
- **Pooled payouts** — when a market resolves, the entire pot is split among the
  winning side in proportion to each winner's stake. If nobody picked the winning
  side, all stakes are refunded.
- **Transactional integrity** — placing and resolving wagers run as atomic
  transactions, so points can never be lost or duplicated mid-operation.
- **Leaderboard** — users ranked by current point balance.

## Tech stack

| Layer        | Choice                                  |
|--------------|-----------------------------------------|
| Language     | Java 21                                  |
| Framework    | Spring Boot (Spring Web, Spring Data JPA)|
| Database     | H2 (local dev) / PostgreSQL (production) |
| Build        | Maven                                    |
| Deployment   | Docker on Render                         |

The app uses Spring **profiles** to switch databases automatically: an in-memory
H2 database for fast local development, and PostgreSQL when deployed — no code
changes required between the two.

## API endpoints

| Method | Path                              | Description                          |
|--------|-----------------------------------|--------------------------------------|
| GET    | `/users`                          | List all users (the leaderboard data)|
| POST   | `/users`                          | Create a user                        |
| GET    | `/markets`                        | List all markets                     |
| POST   | `/markets`                        | Create a market                      |
| GET    | `/wagers`                         | List all wagers                      |
| POST   | `/wagers`                         | Place a wager (YES/NO + stake)       |
| POST   | `/markets/{id}/resolve?outcome=`  | Resolve a market and pay out         |

## Running locally

**Prerequisites:** Java 21 and Maven (the included `./mvnw` wrapper handles Maven for you).

```bash
git clone https://github.com/jsanchez04/Sidebet.git
cd Sidebet
./mvnw spring-boot:run
```

The API starts on `http://localhost:8080` using the local H2 database, seeded with
a few sample users and a market. Try it:

```bash
# List the seeded users
curl http://localhost:8080/users

# Create a market
curl -X POST http://localhost:8080/markets \
  -H "Content-Type: application/json" \
  -d '{"question":"Will it rain at the cookout?","creatorId":1}'

# Place a wager (user 2 stakes 300 points on YES)
curl -X POST http://localhost:8080/wagers \
  -H "Content-Type: application/json" \
  -d '{"marketId":1,"userId":2,"side":"YES","stake":300}'

# Resolve the market (YES wins)
curl -X POST "http://localhost:8080/markets/1/resolve?outcome=YES"
```

## How the payout works

When a market resolves, the service totals every stake into a single pool. The
losing side forfeits its stakes; the winning side splits the entire pool in
proportion to how much each winner risked. For example, if the pool is 600 points
and the only winning wager was 300, that user collects all 600 — netting +300,
funded by the players who bet wrong.

## Project status & roadmap

The core market lifecycle (create → wager → resolve → pay out) is complete and
deployed, with a `/markets/{id}/stats` endpoint powering a live YES/NO chance
percentage. Planned next:

- [ ] Multiple outcomes per market (early / on time / 5 min late / …), not just YES/NO
- [ ] Automatic closing-time enforcement on markets
- [ ] User authentication

## License

This project is for portfolio and educational purposes.
