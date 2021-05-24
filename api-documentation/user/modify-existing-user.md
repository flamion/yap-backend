# Modify existing user

### Mapping /user

* Method: PUT

* Header: "Token $token"

* Requestbody:
    * Json:
        * [User Object](../objects/user.md)

* Response:
    * HttpStatus (400, 401, 403, 200)

#### Description:

Accepts a user object formatted as Json and updates the user in the Database