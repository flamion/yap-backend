# Add member to a board

### Mapping /boards/{boardID}/member

* Method: POST

* Pathvariable: boardID

* Header: "Token $token"

* Requestbody:
    * emailAddress

* Response:
    * HttpStatus (409, 403, 401, 400, 200)

#### Description:

Accepts an email Address and adds the user behind it to a board 
