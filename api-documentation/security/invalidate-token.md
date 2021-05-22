# Invalidate token

### Mapping /security/token

* Method: DELETE

* Header: "Token $token"

* Response:
    * HttpStatus (200)

#### Description:

Takes in a token from header and invalidates it. Does not check if the token is invalid, always responds with 200 OK