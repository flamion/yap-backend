# GET user

### Mapping /user

* Header: "Token $token"

* Requestbody: none

* Response:
    * HttpStatus (401, 200)
    * Json:
        * User object

#### Description:

Returns user object corresponding to the provided token