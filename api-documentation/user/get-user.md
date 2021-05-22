# Get user

### Mapping /user

* Header: "Token $token"

* Requestbody: none

* Response:
    * HttpStatus (401, 200)
    * Json:
        * [User object](../objects/user.md)

#### Description:

Returns user object corresponding to the provided token