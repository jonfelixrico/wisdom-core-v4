# enh-1 physical design -- core

## Introduction
For saving the business data, we will be using the **event sourcing** pattern.

We chose this pattern over a straightforward save-to-db approach is so that we can easily scale the bot out once we start getting more load. With this, we can deploy multiple read instances while deploying only a few write instances.

## Write side
This section will describe the aggregates that we'll be using for the read side.

### The server aggregate
```ts
interface ServerAggregate {
    id: string
    // covers the quote creation business process
    submitQuote(input: InputQuote): SavedQuote;
}
```

Not much can be said about the server aggregate. Since we don't really need much validation from it, we only need to know its id. Its aggregate id should follow this format: `server-{serverId}`.

#### submitQuote
This method is tied to the business process where the user can submit a quote.

```ts
interface ServerSubmitQuoteEvent {
    aggregateId: string // aggregate id of the server
    eventType: 'SERVER_QUOTE_SUBMITTED'
    payload: {
        quoteId: string
    }
}
```

```ts
interface QuoteCreatedEvent {
    aggregateId: string // aggregate id of the quote
    eventType: 'QUOTE_CREATED'
    payload: {
        serverId: string
        channelId: string

        id: string
        content: string
        authorId: string
        submitterId: string
        createDt: Date
    }
}
```

### The quote aggregate
The only purpose right now of the quote aggregate is to allow us to receive quotes. And thus, this is its structure:

```ts
interface QuoteAggregate {
    id: string

    // covers the quote-receiving business process
    logReceive(receiveData: ReceiveInput): Receive; // for now, receive will not be an aggregate
}
```

The aggregate id for quotes should follow the format of `quote-{quoteId}`.

#### logReceive
This method is mapped to the business process where a user can receive a quote.

When called, it should emit the following events:

```ts
interface QuoteReceivedEvent {
    aggreagateId: string // the agg id of the quote
    eventType: 'QUOTE_RECEIVED'
    payload: {
        receiveId: string
    }
}
```

```ts
interface ReceiveCreatedEvent {
    aggregateId: string // the agg id of the receive
    eventType: 'RECEIVE_CREATED',
    payload: {
        id: string
        quoteId: string

        userId: string // the id of the user who received the quote
        createDt: string // the timestamp that the quote was received

        channelId: string
        serverId: string
    }
}
```

The created receive object should have an aggregate id that follows this format: `receive-{receiveId}`.