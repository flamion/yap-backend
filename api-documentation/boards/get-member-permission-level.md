# Get user permission level

### Mapping /boards/{boardID}/member/{userID}/permissionLevel

* Method: GET

* Header: "Token $token"

* Pathvariable: boardID, userID

* Requestbody: none

* Response:
    * HttpStatus (403, 401, 204, 200)
    * 32-Bit Integer 

#### Description:

Returns the permission level of the requested board member