# baba <img src="https://user-images.githubusercontent.com/34045539/161357485-cdb201e1-6d85-4b69-8700-33189a1ccea0.gif" width="30px" height="30px"/>

Baba is a discord bot that acts as your personal assistant with ~10 commands. You can use baba to set reminders, browse reddit, maintain a todo list, get random quotes for the day and much more!

# Running Examples

# Documentation and Setup
## Docker

## Credentials/API keys
Update .env to include the API keys needed for the app to run. This includes Postgres, reddit and rapiD api credentials along with their respective API keys/secrets

# Command list
## **TODO COMMAND**
- !todo list  - view all tasks
- !todo add [string]  - add task
- !todo complete [integer]  - mark task as complete


## **CHANCE COMMAND**
- !chance coin  - simulate coin toss
- !chance dice  - simulate dice roll
- !chance 8ball [string] - simulate magic 8ball
- !chance choose [choice1, choice2, etc...] - choose randomly between list of choices

## **QUOTE COMMAND**
- !quote - this will generate a random quote

## **PING COMMAND**
- !ping - check if the bot is up and running

## **REMINDER COMMAND**
- !reminder - set a timed reminder

## **REDDIT SEARCH**
- !reddit [string] - Search top 5 posts from subreddit in the past week

# Roadmap / Future implementations
- add more error handling
- add error handling for reminder function (the time part)
- make class for db connection
- add algo command