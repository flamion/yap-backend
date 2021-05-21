# Get all boards of a user

### Mapping /boards/user

* Method: GET

* Header: "Token $token"

* Requestbody: none

* Response:
    * HttpStatus (401, 403, 200)
    * Json:
        * Array with Board IDs

#### Description:

Returns a Json formatted Array containing the board IDs of all boards the user is a member of