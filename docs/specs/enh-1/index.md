# enh-1: quote submitting and receiving

## User stories

### User stories to support
* As a server member, I want to be able to submit quotes made by another server member (can also be by the user themselves)
* As a server member, I want to be able to receive a random quote from the server pool

### User stories not supported
* As an admin, I want a quote to have a certain number of votes before it gets accepted
  * Only accepted votes will be qualified to be received

## Logical design

### Submitting quotes
Quotes will be submitted by entering the `/submit` command. The command has the following arguments:
* **content** - the actual quote
* **user** - the user who spoke the quotes

Argument validation will be done by Discord's _slash commands_ feature so we don't need to worry about that on the Discord-side of things.  
Once a quote has been submitted, _the bot will announce it to the same channel that it was sent in_.

The announcement will contain the following bits of information:
* the actual quote
* the author where the quote came from
* the submitter -- the one who called the `/submit` command in the first place


#### Scenario: an error was encountered during submitting
If an error was encountered, then the bot's response will be sent in the same channel but visible only to the user. It should contain a generic message to let the user know about a server error.

### Receiving quotes
Quotes will be received by entering the `/receive` command.

A random quote will be retrieved from the server, and that quote will be displayed in the server in the bot's response. The bot's response is to be
sent in the same channel, for everyone to see.

The response will contain the following bits of information:
* the quote content
* the author
* the year the quote was submitted in
* how many times was it received (including the current `/receive` call)

#### Scenario: there are no quotes in the server to be received
If this is the case, then the bot's response will be sent in the same channel, but only visible to the user. It should contain a message to let the user know that there
are no quotes in the server which can be received.

#### Scenario: an error was encountered during receiving
Same action as `/submit`.

## Physical design