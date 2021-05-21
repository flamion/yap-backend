# Get entry
### Mapping /entry/{entryID}

* Method: GET

* Header: "Token $token"

* Pathvariable: entryID

* Requestbody: none

* Response:
    * HttpStatus (401, 403, 204, 200)
    * Json:
        * Content: Entry Object

#### Description:

Takes an entry id in the path and returns a Json formatted entry object