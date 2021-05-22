# Get entry object

### Mapping /entry/{entryID}

* Method: GET

* Header: "Token $token"

* Pathvariable: entryID

* Requestbody: none

* Response:
    * HttpStatus (401, 403, 200, 204)
    * Json:
        * [Entry Object](../objects/entry.md)

#### Description:

Takes an entry id in the path and returns a Json formatted entry object, if the user has access to the entry.