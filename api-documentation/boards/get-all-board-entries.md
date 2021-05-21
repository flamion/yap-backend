# Get all entries on a board

### Mapping /boards/{boardID}/entries

* Method: GET

* Header: "Token $token"

* Pathvariable: boardID

* Requestbody: none

* Response:
    * HttpStatus (401, 403, 200)
    * Json:
        * Array with Entry object IDs

#### Description:

Returns a Json formatted Array containing the entry IDs of all entries on the board