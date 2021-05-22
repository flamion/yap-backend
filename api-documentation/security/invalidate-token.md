# Invalidate token

### Mapping /security/token

* Method: DELETE

* Header: "Token $token"

* Requestbody:
    * Json:
        * password

* Response:
    * HttpStatus (200)

#### Description:

Takes in a token and invalidates it. Does not check if the token is invalid, always responds with 200 OK