# Create new Entry

### Mapping /boards/{entryID}/entry

* Header: "Token $token"

* Requestbody:
    * Json:
        * Entry object with properties:
            * dueDate (64 bit Integer unix timestamp) [optional]
            * title
            * description

* Response:
    * HttpStatus (401, 403, 201)

#### Description:

Takes in a Json formatted entry and creates it on the board.