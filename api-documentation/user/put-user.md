# PUT user

### Mapping /user

* Header: "Token $token"

* Requestbody:
    * Json:
        * User Object

* Response:
    * HttpStatus (400, 401, 200)

#### Description:

Accepts a user object formatted as Json and updates the user in the Database