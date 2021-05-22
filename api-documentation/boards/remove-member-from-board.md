# Remove member from a board

### Mapping /boards/{boardID}/member/{userID}

* Method: POST

* Pathvariable: boardID, userID

* Header: "Token $token"

* Response:
    * HttpStatus (403, 401, 200)

#### Description:

Removes a member from a board. userID is the ID of the member to be removed. The removing member must be an admin
or the user that is to be removed (for leaving a board).
