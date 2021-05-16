# Check if token is still valid

* Method: GET

### Mapping /security/token/checkValid

* Header: "Token $token"

* RequestBody: none

* Response:
    * HttpStatus (200)
    * boolean

#### Description:

Returns true if the token is (still) valid and false if the token is invalid.