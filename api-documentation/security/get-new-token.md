# Get new token for user

* Method: POST

### Mapping /security/token

* Requestbody:
    * Json:
        * field: "emailAddress: $emailAddress"
        * field: "password: $password"

* Response:
    * HttpStatus (400, 403, 200)
    * Plaintext response with token

#### Description

Returns a plaintext response containing the new user token.