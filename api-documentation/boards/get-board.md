# Get board

### Mapping /boards/{boardID}

* Method: GET

* Header: "Token $token"

* Pathvariable: boardID

* Requestbody: none

* Response:
    * HttpStatus (401, 403, 200)
    * Json:
        * [Board object](../objects/board.md)

#### Description:

Returns Json formatted board object of the requested ID