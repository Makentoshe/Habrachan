### :habrachan:network:user-me

This network manager should return the logged-in user instance by its credentials: session cookie or token header.

Some managers implemented for specified api doesn't provide a one-call request, so it requires several sequential
requests in a row.

For example, "mobile" api doesn't provide a method for requesting user data by credentials.
"me" request returns a user login with which help a user data retrieves.
