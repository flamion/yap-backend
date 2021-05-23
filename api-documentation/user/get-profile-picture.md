# Get a users profile picture

### Mapping /user/{userID}/profilePicture

* Method: GET

* Pathvariable: userID

* Requestbody: none

* Response:
    * HttpStatus (307, 204)

#### Description:

If the user has a profile picture, returns the CDN url where it can be accessed in the Response location header
and gives a 307 redirect Http code.

204 if the user has no profile picture 