# Create new Board

### Mapping /boards

* Method: POST

* Header: "Token $token"

* Requestbody:
    * Json:
        * [Board object](../objects/board.md) with properties:
            * name

* Response:
    * HttpStatus (401, 403, 400, 201)
    * ID of the newly created board

#### Description:

Takes in a Json formatted board and creates it.

Responds with the ID of the newly created board