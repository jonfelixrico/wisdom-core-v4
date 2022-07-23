
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
