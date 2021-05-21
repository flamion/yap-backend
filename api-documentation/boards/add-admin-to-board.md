# Add Admin to a board

### Mapping /boards/{boardID}/member

* Method: POST

* Pathvariable: boardID

* Header: "Token $token"

* Requestbody:
    * emailAddress

* Response:
    * HttpStatus (401, 403, 400, 200)

#### Description:

Accepts an email Address and makes the user behind it admin
