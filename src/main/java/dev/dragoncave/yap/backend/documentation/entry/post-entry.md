#POST entry

###Mapping /entry
* Header: "Token $token"

* Requestbody:
  * Json:
    * Entry object
    

* Response:
  * HttpStatus
  * Entry Id
    
####Description:
Takes in a Json formatted entry object and returns the id of the newly created entry object, if successful