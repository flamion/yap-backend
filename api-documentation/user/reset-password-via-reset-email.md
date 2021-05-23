# Reset password of user

### Mappings:

* /security/resetPasswordRequest
* /security/resetPassword

### Description:

Resetting the password consists of 2 Steps.

#### Step 1

POST to /security/resetPasswordRequest

Body:

* emailAddress

You send the first POST to this endpoint. Contains only the email address of the user that should have his password
reset. The user will get an email containing a code needed to complete the password reset

#### Step 2

The user has to grab the code from the email and give it to you. You then send a POST to the second Mapping
/security/resetPassword;

POST to /security/resetPassword

Body:

* emailAddress
* newPassword
* resetCode