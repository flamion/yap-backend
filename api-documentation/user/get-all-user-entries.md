# Get all user entries

### Mapping /user/entries

* Header: "Token $token"

* Requestbody: none

* Response:
    * HttpStatus (401, 200)
    * Json:
        * Array with IDs of the entries belonging to the user

#### Description:

Returns an Array with IDs of the entries belonging to the user.

64-Bit data type is recommended.