# Create new Entry

### Mapping /boards/{entryID}/entry

* Method: POST

* Header: "Token $token"

* Requestbody:
    * Json:
        * Entry object with properties:
            * dueDate (64 bit Integer unix timestamp) [optional]
            * title
            * description

* Response:
    * HttpStatus (401, 403, 201)
    * Newly created Entry ID

#### Description:

Takes in a Json formatted entry and creates it on the board. Returns the ID of the newly created entry.