# Create new Entry

### Mapping /boards/{boardID}/entry

* Method: POST

* Pathvariable: boardID

* Header: "Token $token"

* Requestbody:
    * Json:
        * [Entry object](../objects/entry.md) with properties:
            * dueDate (64 bit Integer unix timestamp) [optional]
            * title
            * description

* Response:
    * HttpStatus (403, 401, 201)
    * Newly created Entry ID

#### Description:

Takes in a Json formatted entry and creates it on the board. Returns the ID of the newly created entry.