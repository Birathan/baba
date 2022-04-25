# Baba <img src="https://user-images.githubusercontent.com/34045539/161357485-cdb201e1-6d85-4b69-8700-33189a1ccea0.gif" width="30px" height="30px"/>

Baba is a discord bot that acts as your personal assistant with ~15 commands. You can use baba to set reminders, browse reddit, maintain a todo list, get random quotes for the day and much more!

# Setup
## Update environment variables
Update .env to include the API keys needed for the app to run.
```
DISCORD_BOT_TOKEN =

QUOTE_KEY =

PG_CONNECTION_URL = jdbc:postgresql://postgres:5432/
PG_USERNAME = user
PG_PASSWORD = password
TODO_TABLE = todolist
REMINDER_TABLE = reminders

REDDIT_USER = 
REDDIT_PW =
REDDIT_CLIENTID = 
REDDIT_SECRET =

GOOGLE_API_KEY =
GOOGLE_CX =

```

## Running
Simply run ```docker compose``` from the root directory!

# Command list
### MISC COMMMANDS
**!ping** - check if the bot is up and running
**!help** - get list of commands and how to use them

### TODO COMMAND
**!todo list**  - view task list (taskID, task, completion status)
**!todo add [string]**  - add task to list
**!todo complete [integer]**  - mark task as complete
**!todo remove [integer]**  - remove task from list

### CHANCE COMMAND
**!chance coin**  - simulate coin toss
**!chance dice**  - simulate dice roll
**!chance 8ball [string]** - simulate magic 8ball given statement
**!chance choose [choice1, choice2, etc...]** - choose randomly between list of choices

### QUOTE COMMAND
**!quote** - get a random quote



### REMINDER COMMAND
**!reminder add [HH:mm] [AM/PM] [string]** - set a timed reminder
**!reminder list** - view all reminders

### REDDIT SEARCH
**!reddit [string]** - get top 5 reddit posts for the week from given subreddit

### GOOGLE SEARCH
**!google [string]** - get top 5 google search results for given query

# Roadmap / Future implementations
- add more error handling
- make class for db connection
- add algo command
- add bot/server invites