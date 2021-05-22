# Delete board

### Mapping /board/{boardID}

* Method: DELETE

* Header: "Token $token"

Pathvariable: boardID

* Response:
    * HttpStatus (401, 403, 200)

#### Description:

Deletes a board. The user has to be a board admin to delete the board