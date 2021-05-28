# Get a board members user Object

### Mapping /boards/{boardID}/member/{userID}

* Method: GET

* Header: "Token $token"

* Pathvariable: boardID, userID

* Requestbody: none

* Response:
    * HttpStatus (401, 403, 204, 200)
    * Json:
        * [User object](../objects/user.md)

#### Description:

Returns Json formatted user object of the requested userID.

The user requesting must be at least a member of the board.