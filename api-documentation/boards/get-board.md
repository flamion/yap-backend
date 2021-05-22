# Get board

### Mapping /boards/{boardID}

* Method: GET

* Header: "Token $token"

* Pathvariable: boardID

* Requestbody: none

* Response:
    * HttpStatus (401, 403, 204, 200)
    * Json:
        * Board object

#### Description:

Returns Json formatted board object of the requested ID