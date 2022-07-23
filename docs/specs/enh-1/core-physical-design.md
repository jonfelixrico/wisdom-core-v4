# enh-1 physical design -- core

## Introduction
For saving the business data, we're going to be making use of event sourcing pattern. The reason why we're making use of the **event sourcing pattern** is so that we can have more freedom as to how we will implement the read side. The downside of this approach is that it's more complicated to implement because there's extra effort needed to create the read and the write side of the data.

### Aggregates

Aggregates in the code represents domain or business models. We want to follow the **rich domain model** pattern for this. Having a rich domain model means that we do our domain operations inside
domain classes (aggregates).

#### First layer -- contains domain-related code only
The first layer is the foundation of the aggregate object. This should be an abstract class and it should contain the necessary fields to represent a business object and the necessary
methods which represent business processes. The validations required for the business process should also be contained within the methods representing them.

By domain-related code only, we mean that there shouldn't be any database-specific fields present or anything not related to the domain.

When implementing it in the code, please make sure to observe the following:
* Follow the naming format of `{DomainModelName}Aggregate` when naming the class
  * E.g. `CatAggregate`, `BookAggregate`
* Make sure that it's an abstract class
* Do the validations inside of the methods

An example business object would be a Book. We are required to take note of the current page number in the book and the total number of pages in the book.
We can flip to the next or the previous page. We should take note that we can't flip to the next or prev page if there are no more pages available.

```ts
abstract class BookAggregate {
    pageCount: number // non-zero positive integer
    pageNo: number // 1 to pageCount

    flipToNextPage () {
        if (pageNo === pageCount) { // last page -- no more page available
            throw new NoNextPageException()
        } else {
            this.pageCount++;
        }
    }

    flipToPrevPage () {
        if (pageNo === 1) { // first page -- no prev page available
            throw new NoPrevPageException()
        } else {
            this.pageCount--
        }
    }
}
```
### Second layer -- the database mixed in


## Write side

This section is the documentation for the read side of the data. We'll be creating aggregates to represent our buisiness objects (BO).

### Aggregate overview
#### Server (`ServerAggregate`)

```ts
interface ServerAggregate {
    id: string
    // covers the quote creation business process
    submitQuote(submitInput: SubmitInput): QuoteAggregate;
}
```

Primarily, the purpose of this aggregate is to spawn child quote aggregates.

The aggregate id of this object should follow the format of: `server-{serverId}`.

#### Quote (`QuoteAggregate`)

```ts
interface QuoteAggregate {
    id: string
    content: string
    authorId: string
    submitterId: string

    createDt: string // yes, not `submitDt`

    serverId: string
    channelId: string

    // covers the quote-receiving business process
    receive(receiveData: ReceiveInput): Receive; // for now, receive will not be an aggregate
}
```

Aside from representing a quote, this aggregate covers quote-reciving business process by exposing a receive method.

The aggregate id of this object should follow the format of `quote-{quoteId}`.


#### Receive (`ReceiveAggregate`)
```ts
interface ReceiveAggregate {
    id: string // id of the receive
    quoteId: string
    userId: string // receiver of the quote
    createDt: string // time of receiving

    serverId: string
    channelId: string
}
```
This aggregate will contain no action as there are no business processes under the receive aggregate.

The format of the aggregate id will be `receive-{receiveId}`.

### Business processes covered
#### Quote submission
The quote-submission business process is done via the `ServerAggregate#submitQuote` method.

For `ServerAggregate`, it should append the `SEVER_QUOTE_SUBMITTED` event in the server instance. The payload should only contain the id of the quote created, e.g.:

```ts
interface ServerQuoteSubmittedEvent {
    aggregateId: string // aggregate id, in this case the id of the server
    eventType: 'SERVER_QUOTE_SUBMITTED'
    payload: {
        quoteId: string
    }
}
```

For `QuoteAggregate`, it should append the `QUOTE_CREATED` event in the quote instance. Unlike `SERVER_QUOTE_SUBMITTED` which contains only the quote id, the payload
should be similar to the body of `QuoteAggregate`.

```ts
interface QuoteCreatedEvent {
    aggregateId: string // aggregate id, in this case the id of the server
    eventType: 'QUOTE_CREATED'
    payload: ObjectContainingQuoteAggregateProperties
}
```

#### Receiving quotes 
The quote-receiving process is done via the `QuoteAggregate#receiveQuote` object.

This method being called will do two things:

The `QUOTE_RECEIVED` event will be appended to the quote aggregate. Please refer below for the schema of the payload:

```ts
interface QuoteReceivedEventPayload {
    receiveId: string
}
```

The `RECEIVE_CREATED` event will be appended to the receive aggregate. The schema of the payload should have the same attributes as the `ReceiveAggregate`.
