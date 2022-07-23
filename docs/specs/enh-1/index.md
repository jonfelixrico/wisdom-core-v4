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

### Core

Under the hood, we'll be using event sourcing to store our data. In this document, we'll be defining the read and write side of the data separately.
#### Write side

##### `ServerAggregate`
This describes the server entitiy. For now, all the server entity does is to create the quote aggregate.
```ts
interface ServerAggregate {
    id: string
    submitQuote(quoteToCreate: QuoteToCreate): QuoteAggregate;
}
```

##### `QuoteAggregate`
Currently, the quote aggregate does nothing but contain the necessary data for a quote.
```ts
interface QuoteAggregate {
    id: string
    content: string
    authorId: string
    submitterId: string

    createDt: string // yes, not `submitDt`

    serverId: string
    channelId: string
}
```


##### `ServerAggregate#submitQuote`
Using the `submitQuote` method should produce the following events in the aggreagates:

For `ServerAggregate`, it should append the `SEVER_QUOTE_SUBMITTED` event in the instance. The payload should only contain the id of the quote created, e.g.:
```ts
{
    quoteId: string
}
``` 

For `QuoteAggregate`, it should append the `QUOTE_CREATED` event in the instance as well. Unlike `SERVER_QUOTE_SUBMITTED` which contains only the quote id, the payload
should be similar to the body of `QuoteAggregate`.

### Bot

We'll be relying on Discord's slash commands API for the responses. We don't really need to send a message directly into the channel.
#### Submitting a quote
##### On success
![Success](enh-1/submit-success.png)

##### On error
Note: this should only be visible to the user. Please refer to [Discord's ephemeral messages](https://support.discord.com/hc/en-us/articles/1500000580222-Ephemeral-Messages-FAQ).  
![Error](enh-1/quote-submit-error.png)

#### Receiving a quote

##### On success
![Success](enh-1/quote-receive-success.png)

##### On error
Note: this should only be visible to the user.  
![Error](enh-1/quote-receive-error.png)

##### No quote found
Note: this should only be visible to the user.  
![No quote](enh-1/quote-receive-no-quote.png)

### Webservices
We'll be introducing the following webservices:
* `POST /quote` - submit quote
* `GET /quote/random` - get a random quote
* `POST /quote/{quoteId}/receive` - record the receive of a quote

#### `POST /quote`
##### Request body
```ts
{
    content: string
    authorId: string
    submitterId: string // the user who submitted the quote

    serverId: string
    channelId: string
}
```

##### Response body
```ts
{
    id: string
    
    content: string
    authorId: string
    submitDt: Date
}
```

#### `GET /quote/random`

##### Request body
None.

##### Response body
Same response body as `POST /quote`.

#### `POST /quote/{quoteId}/receive`
##### Request body
```ts
{
    quoteId: string
    receiverId: string

    serverId: string
    channelId: string
}
```

##### Response body
```ts
{
    id: string

    quoteId: string
    receiveDt: string
    receiverId: string
}
```