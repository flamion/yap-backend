# Create new user

### Mapping /user

* Requestbody:
    * Json:
        * [User Object](../objects/user.md)

* Response:
    * HttpStatus (400, 409, 201)
    * ID of the newly created user

#### Description:

Accepts a Json formatted user object and creates the user in the database, if the provided object is valid
(For example, if the user does not exist yet and the password is valid)

[Password requirements](../user/password-requirements.md)