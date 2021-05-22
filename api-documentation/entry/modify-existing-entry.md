# Modify existing entry

### Mapping /entry

* Method: PUT

* Header: "Token $token"

* Requestbody:
    * Json:
        * [Entry Object](../objects/entry.md)

* Response:
    * HttpStatus (403, 401, 400, 204, 200)

#### Description:

Takes an entry object and updates the corresponding object in the database