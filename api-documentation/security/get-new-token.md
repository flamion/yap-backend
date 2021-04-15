# Get new token for user

### Mapping /security/token

* Requestbody:
    * Json:
        * field: "emailAddress: $emailAddress"
        * field: "password: $password"

* Response:
    * HttpStatus (400, 401, 200)
    * Plaintext response with token

#### Description

Returns a plaintext response containing the new user token.