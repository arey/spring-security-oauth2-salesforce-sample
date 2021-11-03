# Spring Security OAuth 2.0 client configuration for Salesforce

This sample demonstrates how to integrate the `spring-boot-starter-oauth2-client` to invoke the Salesforce REST API
that is secured by the OAuth 2.0 **username-password flow**.

## Request an Access Token

To request an access token, the connected app sends the user’s username and password as an out-of-band POST 
to the Salesforce token endpoint, such as the following POST:
```
grant_type=password&
client_id=3MVG9lKcPoNINVBIPJjdw1J9LLM82HnFVVX19KY1uA5mu0QqEWhqKpoW3svG3XHrXDiCQjK1mdgAvhCscA9GE&
client_secret=1955279925675241571&
username=testuser@salesforce.com&
password=mypassword
```

## Salesforce Grants an Access Token

After the request is verified, Salesforce sends a response to the client.
Example:
```
{
    "id":"https://login.salesforce.com/id/00Dx0000000BV7z/005x00000012Q9P",
    "issued_at":"1278448832702",
    "instance_url":"https://yourInstance.salesforce.com/",
    "signature":"0CmxinZir53Yex7nE0TD+zMpvIWYGb/bdJh6XfOH6EQ=",
    "access_token":"00Dx0000000BV7z!AR8AQAxo9UfVkh8AlV0Gomt9Czx9LjHnSSpwBMmbRcgKFmxOtvxjTrKW19ye6PE3Ds1eQz3z8jr3W7_VbWmEu4Q8TVGSTHxs",
    "token_type":"Bearer"
}
```

## Resources

* [OAuth 2.0 Username-Password Flow for Special Scenarios from the Salesforce documentation](https://help.salesforce.com/s/articleView?id=sf.remoteaccess_oauth_username_password_flow.htm&type=5)
* [Spring Security OAuth 5.2 Migration Sample](https://github.com/jgrandja/spring-security-oauth-5-2-migrate/)
